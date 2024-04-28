# 프로젝트 설명
- 헥사고날을 간단하게 적용해보려고 했으나 lock 관련 실험 프로젝트로 변질되었음.
- 관련 테스트 내용을 간단하게 요약하려고 함.
- StorageService 에 Aop로 구현한 lock 과 함수형으로 구현한 lock 기반 매서드가 있음.
- Redisson 분산락(pub/sub) 으로 구현
- StorageLockServiceTest 의 매서드로 확인

# 공부하면서 느낀점
- Lettuce는 분산락 구현 시 setnx, setex과 같은 명령어를 이용해 지속적으로 Redis에게 락이 해제되었는지 요청을 보내는 스핀락 방식
- Redisson은 Pub/Sub 방식을 이용하기에 락이 해제되면 락을 subscribe 하는 클라이언트는 락이 해제되었다는 신호를 받고 락 획득을 시도
- lock 은 transactional 과 함께 사용하려면 주의해야 함. 
    - transaction 보다 lock 이 더 먼저 적용되어야 함.
    - get lock > tx begin > tx commit > unlock 순으로 진행되어야 문제가 없음.
    - lock 획득 후 실행되는 로직을 신규 트랜잭션으로 실행(REQUIRES_NEW)
    - lock 매서드가 REQUIRES_NEW 로 신규 트랜잭션을 생성하더라도, 상위 트랜잭션으로부터 시작할 경우 connection 점유 이슈가 생긴다.
      - spring.datasource.hikari.maximum-pool-size 를 잘 설정해야 함.
      - tx A begin > lock 획득 > txB begin > tx B commit > unlock > tx A commit 순으로 실행됨.
      - 만일 maximum-pool-size이 10이고 한번에 10개의 요청이들어올 경우 tx A 가 connection을 모두 가져가게 되므로 tx B 는 connection이 반납될 때까지 대기함.
        이런경우 connection점유 이슈가 생길 수 있어 근본적인 해결은 lock획득 전에 tx를 생성하지 않는게 좋을듯.
        무조건 transactional 밖에서 lock을 획득하자!! 
- Lock 경과시간 만료후 Lock 에 접근하게 될 수도 있습니다.
     만약 A 프로세스가 Lock 을 취득한 후 leaseTime 을 1초로 설정했다고 해봅시다.
     근데 A 프로세스의 작업이 2초가 걸리는 작업이였다면 이미 Lock 은 leaseTime 이 경과하여 도중에 해제가 되었을 테고, A 프로세스는 Lock 에 대해서 Monitor 상태가 아닌데 Lock 을 해제하려고 할 것 입니다.
     따라서 IllegalMonitorStateException 이 발생하게 됩니다.

# 테스트 실행 방법
```
docker compose up -d
./gradlew test -i
```
# 참고자료
- https://helloworld.kurly.com/blog/distributed-redisson-lock/
- https://jongmin4943.tistory.com/entry/Spring-Redisson-%EB%B6%84%EC%82%B0%EB%9D%BDDistribute-Lock-%EC%A2%80-%EB%8D%94-%EC%9E%98-%EC%8D%A8%EB%B3%B4%EA%B8%B0-33-%ED%95%9C%EA%B3%84%EC%99%80-%EA%B7%B9%EB%B3%B5?category=1041393
- https://devroach.tistory.com/83
정
