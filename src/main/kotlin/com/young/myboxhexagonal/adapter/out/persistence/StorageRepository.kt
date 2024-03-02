package com.young.myboxhexagonal.adapter.out.persistence

import com.young.myboxhexagonal.adapter.out.persistence.model.StorageEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface StorageRepository : JpaRepository<StorageEntity, Long> {

    fun findStorageById(id: Long): StorageEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StorageEntity s where s.id = :id")
    fun findByIdForUpdate(id: Long): StorageEntity?
}
