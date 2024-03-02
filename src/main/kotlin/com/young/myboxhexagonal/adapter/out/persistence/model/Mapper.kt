package com.young.myboxhexagonal.adapter.out.persistence.model

import com.young.myboxhexagonal.domain.Storage


internal fun Storage.toEntity() = StorageEntity(
    id = id,
    parentStorageId = parentStorageId,
    storageName = storageName,
    storageFileSize = storageFileSize,
    extType = extType
)

internal fun StorageEntity.toDomain() =
    Storage(
        id = this.id,
        parentStorageId = parentStorageId,
        storageName = storageName,
        storageFileSize = storageFileSize,
        extType = extType
    )
