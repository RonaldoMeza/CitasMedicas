package com.citasmedicas.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
 * Pantalla de búsqueda de médicos
 * Permite buscar por especialidad, nombre o ubicación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDoctorDetail: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

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
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }

                Text(
                    text = "Buscar Médicos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row {
                    // Notificaciones
                    Box {
                        IconButton(onClick = { /* Notificaciones */ }) {
                            Icon(
                                Icons.Default.Notifications,
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
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra de búsqueda con filtro
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Especialidad, nombre...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { /* Filtros avanzados */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MediTurnBlue, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Filtros",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros rápidos
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("Todos", "Disponible hoy", "Mejor valorados", "Cerca de mí")

                items(filters) { filter ->
                    FilterChip(
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        selected = selectedFilter == filter,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MediTurnBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contador de resultados
            Text(
                text = "5 médicos encontrados",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de médicos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(getDoctors()) { doctor ->
                    DoctorSearchCard(
                        doctor = doctor,
                        onClick = { onNavigateToDoctorDetail(doctor.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorSearchCard(
    doctor: Doctor,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto del médico
            Box(
                modifier = Modifier
                    .size(60.dp)
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

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = doctor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doctor.specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${doctor.rating} (${doctor.reviews})",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${doctor.experience} años",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doctor.location,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Estado de disponibilidad
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (doctor.isAvailable) Color.Black else Color.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (doctor.isAvailable) "Disponible" else "Ocupado",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Precio
                if (doctor.isAvailable) {
                    Text(
                        text = "S/ ${doctor.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MediTurnBlue
                    )
                }
            }
        }
    }
}

// Data class para los médicos
data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int,
    val experience: Int,
    val location: String,
    val isAvailable: Boolean,
    val price: Int
)

fun getDoctors(): List<Doctor> {
    return listOf(
        Doctor(
            id = "doctor_1",
            name = "Dra. María González",
            specialty = "Cardiología",
            rating = 4.9,
            reviews = 127,
            experience = 15,
            location = "Hospital Central",
            isAvailable = true,
            price = 50
        ),
        Doctor(
            id = "doctor_2",
            name = "Dr. Carlos Ramírez",
            specialty = "Neurología",
            rating = 4.8,
            reviews = 98,
            experience = 12,
            location = "Clínica San Rafael",
            isAvailable = true,
            price = 60
        ),
        Doctor(
            id = "doctor_3",
            name = "Dra. Ana Martínez",
            specialty = "Pediatría",
            rating = 5.0,
            reviews = 203,
            experience = 20,
            location = "Hospital Infantil",
            isAvailable = false,
            price = 45
        )
    )
}