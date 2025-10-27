package com.citasmedicas.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.data.datasource.AppointmentDataSource
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
    onNavigateToDoctorDetail: (String) -> Unit
) {
    var appointments by remember { mutableStateOf(AppointmentDataSource.getAllAppointments()) }
    var selectedTab by remember { mutableStateOf(0) }

    // Recargar citas cuando cambie la pestaña
    LaunchedEffect(selectedTab) {
        appointments = AppointmentDataSource.getAllAppointments()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar personalizada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
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
                    color = Color.Black
                )

                Row {
                    // Notificaciones
                    Box {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Notificaciones",
                                tint = Color.Gray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.Red, CircleShape)
                                .align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = "2",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón agregar
                    IconButton(
                        onClick = { /* Agregar cita */ },
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
                    text = { Text("Próximas (${appointments.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pasadas (0)") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según la pestaña seleccionada
            when (selectedTab) {
                0 -> UpcomingAppointmentsContent(
                    appointments = appointments,
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
                1 -> PastAppointmentsContent(
                    appointments = appointments,
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
            }
        }
    }
}

@Composable
fun UpcomingAppointmentsContent(
    appointments: List<Appointment>,
    onNavigateToDoctorDetail: (String) -> Unit
) {
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
                    color = Color.Gray
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
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
            }
        }
    }
}

@Composable
fun PastAppointmentsContent(
    appointments: List<Appointment>,
    onNavigateToDoctorDetail: (String) -> Unit
) {
    // Por ahora todas las citas son consideradas próximas
    // En una implementación real, se filtrarían por fecha
    val pastAppointments = emptyList<AppointmentData>()

    if (pastAppointments.isEmpty()) {
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
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pastAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentData,
    onNavigateToDoctorDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            color = Color.Black
                        )

                        Text(
                            text = appointment.specialty,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Tipo de cita
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = appointment.type,
                        color = Color.Black,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Menú de opciones
                IconButton(onClick = { /* Opciones */ }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = Color.Gray
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
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Hora",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.location,
                    fontSize = 14.sp,
                    color = Color.Gray
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
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
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
                    onClick = { onNavigateToDoctorDetail(appointment.doctorId) },
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