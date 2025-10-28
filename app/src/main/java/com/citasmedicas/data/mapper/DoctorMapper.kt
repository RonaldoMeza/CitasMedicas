package com.citasmedicas.data.mapper

import com.citasmedicas.data.local.entity.DoctorEntity
import com.citasmedicas.model.Doctor

fun DoctorEntity.toModel() = Doctor(
    id = id,
    name = name,
    specialty = specialty,
    rating = rating,
    reviews = reviews,
    experience = experience,
    location = location,
    isAvailable = isAvailable,
    price = price,
    schedule = schedule,
    imageUrl = imageUrl,
    description = description,
    phoneNumber = phoneNumber,
    supportsTelemedicine = supportsTelemedicine
)

fun Doctor.toEntity() = DoctorEntity(
    id = id,
    name = name,
    specialty = specialty,
    rating = rating,
    reviews = reviews,
    experience = experience,
    location = location,
    isAvailable = isAvailable,
    price = price,
    schedule = schedule,
    imageUrl = imageUrl,
    description = description,
    phoneNumber = phoneNumber,
    supportsTelemedicine = supportsTelemedicine
)
