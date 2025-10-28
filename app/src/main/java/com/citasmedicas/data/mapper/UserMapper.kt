package com.citasmedicas.data.mapper

import com.citasmedicas.data.local.entity.UserEntity
import com.citasmedicas.model.User

fun UserEntity.toModel() = User(
    id = id,
    email = email,
    name = name
)
