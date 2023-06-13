package com.young.myboxhexagonal.application.port.out

import com.young.myboxhexagonal.domain.Storage

interface StoragePersistencePort {
    fun findById(id: Long): Storage?
    fun save(storage: Storage)
}
