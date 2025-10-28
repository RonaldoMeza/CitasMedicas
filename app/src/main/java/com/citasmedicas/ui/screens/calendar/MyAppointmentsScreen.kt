package com.citasmedicas.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.data.repository.AppointmentRepository
import com.citasmedicas.model.Appointment
import com.citasmedicas.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla de mis citas médicas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDoctorDetail: (String) -> Unit,
    onNavigateToAppointmentDetail: (Appointment) -> Unit = {},
    onNavigateToAppointment: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val appointmentRepository = remember { AppointmentRepository() }
    var upcomingAppointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    var pastAppointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }
    var selectedAppointmentDetail by remember { mutableStateOf<Appointment?>(null) }

    // Recargar citas cuando cambie la pestaña
    LaunchedEffect(selectedTab) {
        upcomingAppointments = appointmentRepository.getUpcomingAppointments()
        pastAppointments = appointmentRepository.getPastAppointments()
    }

    // Confirmar cancelación de cita
    showDeleteDialog?.let { appointmentId ->
        CancelAppointmentDialog(
            onConfirm = {
                appointmentRepository.cancelAppointment(appointmentId)
                showDeleteDialog = null
                upcomingAppointments = appointmentRepository.getUpcomingAppointments()
                pastAppointments = appointmentRepository.getPastAppointments()
            },
            onDismiss = { showDeleteDialog = null }
        )
    }

    // Dialog de detalles de la cita
    selectedAppointmentDetail?.let { appointment ->
        AppointmentDetailDialog(
            appointment = appointment,
            onDismiss = { selectedAppointmentDetail = null },
            onCancel = {
                showDeleteDialog = appointment.id
                selectedAppointmentDetail = null
            },
            onEdit = {
                selectedAppointmentDetail = null
                onNavigateToAppointment(appointment.id)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar personalizada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis Citas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )

                Row {
                    // Notificaciones con badge dinámico
                    val unreadCount = com.citasmedicas.ui.components.getUnreadNotificationCount()
                    com.citasmedicas.ui.components.NotificationIconSmall(
                        onClick = onNavigateToNotifications,
                        badgeCount = unreadCount,
                        tint = colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón agregar
                    IconButton(
                        onClick = { onNavigateToSearch() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Black, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Tabs para próximas y pasadas
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Próximas (${upcomingAppointments.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pasadas (${pastAppointments.size})") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según la pestaña seleccionada
            when (selectedTab) {
                0 -> UpcomingAppointmentsContent(
                    appointments = upcomingAppointments,
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail,
                    onNavigateToAppointmentDetail = { selectedAppointmentDetail = it },
                    onCancelAppointment = { showDeleteDialog = it }
                )
                1 -> PastAppointmentsContent(
                    appointments = pastAppointments,
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail,
                    onNavigateToAppointmentDetail = { selectedAppointmentDetail = it }
                )
            }
        }
    }
}

@Composable
fun UpcomingAppointmentsContent(
    appointments: List<Appointment>,
    onNavigateToDoctorDetail: (String) -> Unit,
    onNavigateToAppointmentDetail: (Appointment) -> Unit,
    onCancelAppointment: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val upcomingAppointments = appointments.map { appointment ->
        AppointmentData(
            id = appointment.id,
            doctorId = appointment.doctorId,
            doctorName = appointment.doctorName,
            specialty = appointment.specialty,
            date = appointment.date,
            time = appointment.time,
            location = "Ubicación: S/ ${appointment.price}",
            type = "Presencial"
        )
    }

    if (upcomingAppointments.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Sin citas",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No tienes citas próximas",
                    fontSize = 16.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(upcomingAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    appointmentData = appointments.firstOrNull { it.id == appointment.id },
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail,
                    onNavigateToAppointmentDetail = onNavigateToAppointmentDetail,
                    onCancelAppointment = onCancelAppointment
                )
            }
        }
    }
}

@Composable
fun PastAppointmentsContent(
    appointments: List<Appointment>,
    onNavigateToDoctorDetail: (String) -> Unit,
    onNavigateToAppointmentDetail: (Appointment) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    // Convertir citas pasadas a AppointmentData
    val pastAppointmentsData = appointments.map { appointment ->
        AppointmentData(
            id = appointment.id,
            doctorId = appointment.doctorId,
            doctorName = appointment.doctorName,
            specialty = appointment.specialty,
            date = appointment.date,
            time = appointment.time,
            location = "S/ ${appointment.price}",
            type = "Pasada"
        )
    }

    if (pastAppointmentsData.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Sin citas",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No tienes citas pasadas",
                    fontSize = 16.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pastAppointmentsData) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    appointmentData = appointments.firstOrNull { it.id == appointment.id },
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail,
                    onNavigateToAppointmentDetail = onNavigateToAppointmentDetail
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentData,
    appointmentData: Appointment?,
    onNavigateToDoctorDetail: (String) -> Unit,
    onNavigateToAppointmentDetail: ((Appointment) -> Unit)? = null,
    onCancelAppointment: ((String) -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Foto del médico
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Gray, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Foto del médico",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = appointment.doctorName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )

                        Text(
                            text = appointment.specialty,
                            fontSize = 14.sp,
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                // Tipo de cita
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.onSurface.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = appointment.type,
                        color = colorScheme.onSurface,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Detalles de la cita
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Fecha",
                    tint = colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.date,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Hora",
                    tint = colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.time,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.location,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Llamar */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Llamar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Llamar")
                }

                Button(
                    onClick = {
                        appointmentData?.let { onNavigateToAppointmentDetail?.invoke(it) }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text("Ver detalles")
                }
            }
        }
    }
}

data class AppointmentData(
    val id: String,
    val doctorId: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val location: String,
    val type: String
)

@Composable
fun CancelAppointmentDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text("Cancelar Cita")
        },
        text = {
            Text("¿Estás seguro de que deseas cancelar esta cita?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cancelar Cita")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
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
                val dialogColorScheme = MaterialTheme.colorScheme

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = dialogColorScheme.surfaceVariant
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
                                color = dialogColorScheme.onSurface
                            )
                            Text(
                                text = appointment.specialty,
                                fontSize = 14.sp,
                                color = dialogColorScheme.onSurface.copy(alpha = 0.6f)
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
                        tint = dialogColorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fecha: ${appointment.date}",
                        fontSize = 14.sp,
                        color = dialogColorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Hora",
                        tint = dialogColorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hora: ${appointment.time}",
                        fontSize = 14.sp,
                        color = dialogColorScheme.onSurface
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
                            color = dialogColorScheme.onSurface
                        )
                        Text(
                            text = appointment.reason,
                            fontSize = 14.sp,
                            color = dialogColorScheme.onSurface.copy(alpha = 0.6f)
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
                        tint = dialogColorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Precio: S/ ${appointment.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MediTurnBlue
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

                // Botón Cancelar Cita
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Cancelar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cancelar Cita")
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