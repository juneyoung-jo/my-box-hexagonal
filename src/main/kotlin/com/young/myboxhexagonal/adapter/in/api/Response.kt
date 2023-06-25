package com.young.myboxhexagonal.adapter.`in`.api

import org.springframework.http.ResponseEntity

fun <T> ofSuccess(t: T? = null) =
    ResponseEntity.ok(t)
