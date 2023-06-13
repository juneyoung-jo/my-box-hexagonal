package com.young.myboxhexagonal.adapter.out.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

//@DynamicUpdate
@Entity
@Table(name = "users")
class UserEntity(
    @Id
    var id: Long? = null,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val rootStorageId: Long
) : BaseEntity()
