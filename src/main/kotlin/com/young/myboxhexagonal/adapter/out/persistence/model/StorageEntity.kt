package com.young.myboxhexagonal.adapter.out.persistence.model

import com.young.myboxhexagonal.common.type.StorageExtType
import com.young.myboxhexagonal.domain.Storage
import jakarta.persistence.*

//@DynamicUpdate
@Entity
@Table(name = "storages")
class StorageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val parentStorageId: Long? = null,

    @Column(nullable = false)
    val storageName: String,

    @Column(nullable = false)
    val storageFileSize: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val extType: StorageExtType

) : BaseEntity()

fun StorageEntity.toDomain() =
    Storage(
        id = this.id,
        parentStorageId = parentStorageId,
        storageName = storageName,
        storageFileSize = storageFileSize,
        extType = extType
    )
