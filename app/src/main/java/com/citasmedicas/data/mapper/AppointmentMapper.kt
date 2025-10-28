package com.citasmedicas.data.mapper

import com.citasmedicas.data.local.entity.AppointmentEntity
import com.citasmedicas.model.Appointment
import com.citasmedicas.util.DateUtils

fun AppointmentEntity.toModel(): Appointment = Appointment(
    id = id,
    doctorId = doctorId,
    doctorName = doctorName,
    specialty = specialty,
    date = DateUtils.isoToUiDate(dateIso),
    time = DateUtils.time24ToUi(time24),
    reason = reason,
    price = price
)

fun Appointment.toEntity(userId: String): AppointmentEntity = AppointmentEntity(
    id = id,
    userId = userId,
    doctorId = doctorId,
    doctorName = doctorName,
    specialty = specialty,
    dateIso = DateUtils.uiDateToIso(date),
    time24 = DateUtils.uiTimeTo24(time),
    reason = reason,
    price = price
)
