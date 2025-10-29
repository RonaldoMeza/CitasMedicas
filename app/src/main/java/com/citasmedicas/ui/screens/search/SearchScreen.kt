package com.citasmedicas.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.citasmedicas.data.repository.DoctorRepository
import com.citasmedicas.model.Doctor
import com.citasmedicas.ui.theme.*
import com.citasmedicas.util.isCompactWidth
import com.citasmedicas.util.getColumnCount

/**
 * Pantalla de búsqueda de médicos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDoctorDetail: (String) -> Unit,
    initialSearchQuery: String? = null,
    onNavigateToNotifications: () -> Unit = {}
) {
    val repository = remember { DoctorRepository() }
    var searchQuery by remember { mutableStateOf(initialSearchQuery ?: "") }
    var doctors by remember { mutableStateOf(repository.getAllDoctors()) }
    
    // Estados de filtros
    var selectedSpecialty by remember { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var showTelemedicineOnly by remember { mutableStateOf(false) }
    
    // Listas de opciones para filtros
    val specialties = listOf(
        "Cardiología", "Neurología", "Traumatología", 
        "Oftalmología", "Pediatría", "Medicina General", "Dermatología"
    )
    val cities = listOf(
        "Lima", "Arequipa", "Cusco", "Trujillo"
    )

    // Filtrar médicos según búsqueda y filtros
    LaunchedEffect(searchQuery, selectedSpecialty, selectedCity, showTelemedicineOnly) {
        var filteredDoctors = repository.getAllDoctors()
        
        // Filtro por búsqueda general
        if (searchQuery.isNotBlank()) {
            filteredDoctors = filteredDoctors.filter { doctor ->
                doctor.name.contains(searchQuery, ignoreCase = true) ||
                doctor.specialty.contains(searchQuery, ignoreCase = true) ||
                doctor.location.contains(searchQuery, ignoreCase = true)
            }
        }
        
        // Filtro por especialidad
        if (selectedSpecialty != null) {
            filteredDoctors = filteredDoctors.filter { doctor ->
                doctor.specialty.equals(selectedSpecialty, ignoreCase = true)
            }
        }
        
        // Filtro por ciudad/ubicación
        if (selectedCity != null) {
            filteredDoctors = filteredDoctors.filter { doctor ->
                doctor.location.contains(selectedCity!!, ignoreCase = true)
            }
        }
        
        // Filtro por teleconsulta
        if (showTelemedicineOnly) {
            filteredDoctors = filteredDoctors.filter { it.supportsTelemedicine }
        }
        
        doctors = filteredDoctors
    }

    val colorScheme = MaterialTheme.colorScheme
    
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
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }

                Text(
                    text = "Buscar Médicos",
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
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por especialidad o nombre...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros por especialidad
            Text(
                text = "Especialidad",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedSpecialty == null,
                        onClick = { selectedSpecialty = null },
                        label = { Text("Todas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MediTurnBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(specialties) { specialty ->
                    FilterChip(
                        selected = selectedSpecialty == specialty,
                        onClick = { selectedSpecialty = if (selectedSpecialty == specialty) null else specialty },
                        label = { Text(specialty) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MediTurnBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros por ciudad
            Text(
                text = "Ciudad",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCity == null,
                        onClick = { selectedCity = null },
                        label = { Text("Todas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MediTurnBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(cities) { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = { selectedCity = if (selectedCity == city) null else city },
                        label = { Text(city, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MediTurnBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtro de teleconsulta
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Solo teleconsulta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = showTelemedicineOnly,
                    onCheckedChange = { showTelemedicineOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MediTurnBlue
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contador de resultados
            Text(
                text = "${doctors.size} médicos encontrados",
                fontSize = 14.sp,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de médicos con layout responsivo
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorSearchCard(
                        doctor = doctor,
                        onClick = {
                            Log.d("SearchScreen", "Navegando a doctor: ${doctor.id}")
                            onNavigateToDoctorDetail(doctor.id)
                        }
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
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
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
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doctor.specialty,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
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
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${doctor.experience} años",
                    fontSize = 12.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doctor.location,
                    fontSize = 12.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Estado de disponibilidad
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (doctor.isAvailable) MediTurnBlue else colorScheme.onSurface.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (doctor.isAvailable) "Disponible" else "Ocupado",
                        color = if (doctor.isAvailable) Color.White else colorScheme.onSurface,
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

@Composable
fun FilterChipsRow(
    showTelemedicine: Boolean,
    onTelemedicineFilterChanged: (Boolean) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = showTelemedicine,
                onClick = { onTelemedicineFilterChanged(!showTelemedicine) },
                leadingIcon = if (showTelemedicine) {
                    { Icon(Icons.Default.Phone, "") }
                } else null,
                label = { Text("Teleconsulta") }
            )
        }
    }
}