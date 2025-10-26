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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.ui.theme.*

/**
 * Pantalla de mis citas médicas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDoctorDetail: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

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
                    text = { Text("Próximas (2)") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pasadas (2)") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según la pestaña seleccionada
            when (selectedTab) {
                0 -> UpcomingAppointmentsContent(
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
                1 -> PastAppointmentsContent(
                    onNavigateToDoctorDetail = onNavigateToDoctorDetail
                )
            }
        }
    }
}

@Composable
fun UpcomingAppointmentsContent(
    onNavigateToDoctorDetail: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val upcomingAppointments = listOf(
            AppointmentData(
                id = "appointment_1",
                doctorName = "Dra. María González",
                specialty = "Cardiología",
                date = "25 Oct 2025",
                time = "10:00 AM",
                location = "Hospital Central",
                type = "Presencial",
                isVirtual = false
            ),
            AppointmentData(
                id = "appointment_2",
                doctorName = "Dr. Carlos Ramírez",
                specialty = "Neurología",
                date = "28 Oct 2025",
                time = "2:30 PM",
                location = "Teleconsulta",
                type = "Virtual",
                isVirtual = true
            )
        )

        items(upcomingAppointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                onNavigateToDoctorDetail = onNavigateToDoctorDetail
            )
        }
    }
}

@Composable
fun PastAppointmentsContent(
    onNavigateToDoctorDetail: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val pastAppointments = listOf(
            AppointmentData(
                id = "appointment_3",
                doctorName = "Dr. Juan Pérez",
                specialty = "Pediatría",
                date = "20 Oct 2025",
                time = "09:00 AM",
                location = "Clínica Infantil",
                type = "Presencial",
                isVirtual = false
            )
        )

        items(pastAppointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                onNavigateToDoctorDetail = onNavigateToDoctorDetail
            )
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
                        containerColor = if (appointment.isVirtual) Color.Black else Color.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = appointment.type,
                        color = if (appointment.isVirtual) Color.White else Color.Black,
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
                    if (appointment.isVirtual) Icons.Default.Call else Icons.Default.LocationOn,
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
                    onClick = { onNavigateToDoctorDetail(appointment.id) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    if (appointment.isVirtual) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = "Unirse",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Unirse")
                    } else {
                        Text("Ver detalles")
                    }
                }
            }
        }
    }
}

data class AppointmentData(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val location: String,
    val type: String,
    val isVirtual: Boolean
)