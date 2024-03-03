package com.young.myboxhexagonal.application.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Aspect
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction
) {

    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
        private val log = LoggerFactory.getLogger(DistributedLockAop::class.java)
    }

    @Around("@annotation(com.young.myboxhexagonal.application.aop.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)

        val key = "$REDISSON_LOCK_PREFIX${
            CustomSpringELParser.getDynamicValue(
                signature.parameterNames,
                joinPoint.args,
                distributedLock.key
            )
        }"
        val rLock: RLock = redissonClient.getLock(key)

        return try {
            val available = rLock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)
            if (!available) {
                return false
            }
            println("get Lock aop = ${Thread.currentThread()}")
            aopForTransaction.proceed(joinPoint)
        } finally {
            try {
                println("unLock aop = ${Thread.currentThread()}")
                rLock.unlock()
            } catch (e: IllegalMonitorStateException) {
                log.info("Redisson Lock Already UnLock serviceName: ${method.name}, key: $key")
            }
        }
    }
}


/**
 * Spring Expression Language Parser
 */
class CustomSpringELParser private constructor() {
    companion object {
        fun getDynamicValue(parameterNames: Array<String>, args: Array<Any>, key: String): Any? {
            val parser = SpelExpressionParser()
            val context = StandardEvaluationContext()

            for (i in parameterNames.indices) {
                context.setVariable(parameterNames[i], args[i])
            }

            return parser.parseExpression(key).getValue(context, Object::class.java)
        }
    }
}

/**
 * AOP에서 트랜잭션 분리를 위한 클래스
 */
@Component
class AopForTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun proceed(joinPoint: ProceedingJoinPoint): Any {
        return joinPoint.proceed()
    }
}
