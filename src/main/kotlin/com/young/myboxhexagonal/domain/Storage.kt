package com.young.myboxhexagonal.domain

import com.young.myboxhexagonal.common.type.StorageExtType

data class Storage(
    var id: Long? = null,
    val parentStorageId: Long? = null,
    val storageName: String,
    val storageFileSize: Long,
    val extType: StorageExtType
)
