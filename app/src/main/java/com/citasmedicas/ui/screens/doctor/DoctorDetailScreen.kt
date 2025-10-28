package com.citasmedicas.ui.screens.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.citasmedicas.data.datasource.DoctorDataSource
import com.citasmedicas.model.Doctor
import com.citasmedicas.ui.theme.*

/**
 * Pantalla de detalle del médico
 * información completa, horarios y permite agendar cita
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    onNavigateToAppointment: (String) -> Unit
) {
    val dataSource = remember { DoctorDataSource() }


    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(doctorId) {
        try {
            Log.d("DoctorDetail", "Buscando médico con ID: '$doctorId'")
            isLoading = true
            errorMessage = null
            doctor = dataSource.getDoctorById(doctorId)
            if (doctor == null) {
                Log.e("DoctorDetail", "Médico no encontrado con ID: '$doctorId'")
                errorMessage = "Médico con ID: $doctorId no encontrado"
            } else {
                Log.d("DoctorDetail", "Médico encontrado: ${doctor?.name}")
            }
        } catch (e: Exception) {
            Log.e("DoctorDetail", "Error al cargar médico", e)
            errorMessage = "Error al cargar médico: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        // Estado de carga
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        doctor?.let { doc ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header con foto del médico
                item {
                    DoctorHeaderSection(
                        doctor = doc,
                        onNavigateBack = onNavigateBack
                    )
                }

                // Información principal
                item {
                    DoctorInfoSection(doctor = doc)
                }

                // Horarios disponibles
                item {
                    AvailableTimesSection(
                        schedule = doc.schedule,
                        onTimeSelected = { /* TODO: Manejar selección */ }
                    )
                }

                // Descripción
                item {
                    DoctorDescriptionSection(description = doc.description)
                }

                // Botón agendar
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = { onNavigateToAppointment(doc.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MediTurnBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Agendar",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Agendar Cita - S/ ${doc.price}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } ?: run {
            // Error: médico no encontrado
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = errorMessage ?: "Médico no encontrado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    if (errorMessage?.contains("ID:") == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ID recibido: $doctorId",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorHeaderSection(
    doctor: Doctor,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediTurnBlue)
            .padding(16.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto del médico
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Foto",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = doctor.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = doctor.specialty,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun DoctorInfoSection(
    doctor: Doctor
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Calificación
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Calificación",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${doctor.rating}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = " (${doctor.reviews} reseñas)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Experiencia
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Experiencia",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${doctor.experience} años de experiencia",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ubicación
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = doctor.location,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teléfono
            if (doctor.phoneNumber.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Teléfono",
                        tint = MediTurnBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = doctor.phoneNumber,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AvailableTimesSection(
    schedule: List<String>,
    onTimeSelected: (String) -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Horarios",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Horarios Disponibles",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar horarios en filas horizontales (3 por fila)
            schedule.chunked(3).forEachIndexed { index, timeRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeRow.forEach { time ->
                        FilterChip(
                            onClick = {
                                selectedTime = time
                                onTimeSelected(time)
                            },
                            label = { Text(time) },
                            selected = selectedTime == time,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MediTurnBlue,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (index < schedule.chunked(3).size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DoctorDescriptionSection(
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Acerca del Doctor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}