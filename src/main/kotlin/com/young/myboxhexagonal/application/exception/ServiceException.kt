package com.young.myboxhexagonal.application.exception

class ServiceException(
    private val storageErrorCode: StorageErrorCode,
    customMessage: String? = storageErrorCode.message
) : RuntimeException(customMessage)
