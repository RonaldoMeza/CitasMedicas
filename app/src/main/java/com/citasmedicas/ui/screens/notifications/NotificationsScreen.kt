package com.citasmedicas.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.data.NotificationState
import com.citasmedicas.data.repository.AppointmentRepository
import com.citasmedicas.model.Appointment
import com.citasmedicas.ui.theme.MediTurnBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Pantalla de notificaciones de recordatorios de citas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAppointmentDetail: (Appointment) -> Unit = {}
) {
    val appointmentRepository = remember { AppointmentRepository() }
    var appointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }
    
    LaunchedEffect(Unit) {
        appointments = appointmentRepository.getAllAppointments()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = "Notificaciones",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MediTurnBlue,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        // Contenido
        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Sin notificaciones",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes notificaciones",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments.sortedByDescending { 
                    // Ordenar por fecha, más reciente primero
                    it.date
                }) { appointment ->
                    NotificationCard(
                        appointment = appointment,
                        isRead = NotificationState.isRead(appointment.id),
                        onMarkAsRead = {
                            NotificationState.markAsRead(appointment.id)
                        },
                        onClick = {
                            NotificationState.markAsRead(appointment.id)
                            selectedAppointment = appointment
                        }
                    )
                }
            }
        }
    }
    
    // Mostrar diálogo de detalle si hay una cita seleccionada
    selectedAppointment?.let { appointment ->
        AppointmentDetailDialog(
            appointment = appointment,
            onDismiss = { selectedAppointment = null },
            onCancel = { 
                selectedAppointment = null
            },
            onEdit = {
                selectedAppointment = null
                onNavigateToAppointmentDetail(appointment)
            }
        )
    }
}

@Composable
fun NotificationCard(
    appointment: Appointment,
    isRead: Boolean,
    onMarkAsRead: () -> Unit,
    onClick: () -> Unit
) {
    val (relativeTime, title, message) = getNotificationInfo(appointment)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMarkAsRead()
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) Color.White else Color(0xFFE3F2FD)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isRead) Color.LightGray else MediTurnBlue,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = if (isRead) Color.Gray else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = relativeTime,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Indicador de no leída
            if (!isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MediTurnBlue, RoundedCornerShape(6.dp))
                )
            }
        }
    }
}

fun getNotificationInfo(appointment: Appointment): Triple<String, String, String> {
    val title = "Recordatorio de Cita"
    val message = "Tienes una cita con ${appointment.doctorName} el ${appointment.date} a las ${appointment.time}"
    
    // Calcular tiempo relativo
    val timeString = calculateRelativeTime(appointment.date, appointment.time)
    
    return Triple(timeString, title, message)
}

fun calculateRelativeTime(date: String, time: String): String {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val appointmentDate = dateFormat.parse(date)
        val now = Calendar.getInstance().time
        
        appointmentDate?.let {
            val diff = it.time - now.time // Diferencia hacia el futuro
            
            val days = diff / (1000 * 60 * 60 * 24)
            val hours = diff / (1000 * 60 * 60)
            val minutes = diff / (1000 * 60)
            
            when {
                days < 0 -> "Hace ${-days} días"
                days == 0L -> "Hoy a las $time"
                days == 1L -> "Mañana a las $time"
                days < 7 -> "En $days días"
                days < 30 -> "En ${days / 7} semanas"
                else -> "En ${days / 30} meses"
            }
        } ?: date
    } catch (e: Exception) {
        date
    }
}

@Composable
fun AppointmentDetailDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onEdit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Cita médica",
                tint = Color(0xFF2196F3)
            )
        },
        title = {
            Text(
                text = "Detalle de la Cita",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Información del médico
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Médico",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = appointment.doctorName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = appointment.specialty,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Fecha y hora
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fecha: ${appointment.date}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Hora",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hora: ${appointment.time}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Motivo de consulta
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Motivo",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Motivo:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Text(
                            text = appointment.reason,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Precio
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Precio",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Precio: S/ ${appointment.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }
            }
        },
        confirmButton = {
            Column {
                // Botón Reprogramar
                Button(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Reprogramar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reprogramar Cita")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botón Cerrar
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        }
    )
}

