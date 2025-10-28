package com.citasmedicas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.citasmedicas.data.NotificationState
import com.citasmedicas.data.repository.AppointmentRepository
import com.citasmedicas.model.Appointment

@Composable
fun NotificationIcon(
    onClick: () -> Unit,
    badgeCount: Int,
    tint: Color = Color.Gray
) {
    Box {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = tint
            )
        }
        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun NotificationIconSmall(
    onClick: () -> Unit,
    badgeCount: Int,
    tint: Color = Color.Gray
) {
    Box {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = tint
            )
        }
        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * Hook para obtener el conteo de notificaciones no leídas
 */
@Composable
fun getUnreadNotificationCount(): Int {
    val appointmentRepository = remember { AppointmentRepository() }
    var appointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        appointments = appointmentRepository.getAllAppointments()
    }
    
    // Re-validar cuando cambie el estado de notificaciones
    LaunchedEffect(appointments) {
        appointments = appointmentRepository.getAllAppointments()
    }
    
    return NotificationState.getUnreadCount(appointments)
}

