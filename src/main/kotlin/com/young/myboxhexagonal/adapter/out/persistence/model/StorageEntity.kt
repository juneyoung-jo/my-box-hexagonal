package com.young.myboxhexagonal.adapter.out.persistence.model

import com.young.myboxhexagonal.common.type.StorageExtType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

//@DynamicUpdate
@Entity
@Table(name = "storages")
class StorageEntity(
    @Id
    var id: Long? = null,

    @Column(nullable = false)
    val userId: Long,

    val parentStorageId: Long? = null,

    @Column(nullable = false)
    val storageName: String,

    @Column(nullable = false)
    val storageFileSize: Long,

    @Column(nullable = false)
    val extType: StorageExtType

) : BaseEntity()
