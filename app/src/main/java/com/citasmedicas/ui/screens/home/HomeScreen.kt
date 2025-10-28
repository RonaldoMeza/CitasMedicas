package com.citasmedicas.ui.screens.home

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.ui.theme.*
import com.citasmedicas.ui.components.NotificationIcon
import com.citasmedicas.ui.components.getUnreadNotificationCount
import com.citasmedicas.data.repository.DoctorRepository
import com.citasmedicas.model.Doctor
import androidx.compose.foundation.text.BasicTextField

/**
 * Pantalla principal de MediTurn - Diseño Figma
 */
@Composable
fun HomeScreen(
    onNavigateToSearch: (String?) -> Unit,
    onNavigateToMyAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAppointment: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {}
) {
    val doctorRepository = remember { DoctorRepository() }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Doctor>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    // Realizar búsqueda cuando cambia el texto
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isSearching = true
            searchResults = doctorRepository.searchDoctorsByName(searchQuery)
        } else {
            isSearching = false
            searchResults = emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header con búsqueda
        item {
            HeaderSection(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToNotifications = onNavigateToNotifications,
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it }
            )
        }
        
        // Si hay búsqueda, mostrar resultados
        if (isSearching) {
            item {
                SearchResultsSection(
                    results = searchResults,
                    searchQuery = searchQuery,
                    onNavigateToAppointment = onNavigateToAppointment
                )
            }
        } else {
            // Especialidades
            item {
                SpecialtiesSection(
                    onSpecialtyClick = { specialtyName ->
                        onNavigateToSearch(specialtyName)
                    }
                )
            }
            
            // Médicos destacados
            item {
                FeaturedDoctorsSection(
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToAppointment = onNavigateToAppointment
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HeaderSection(
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: (String?) -> Unit,
    onNavigateToNotifications: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediTurnBlue)
            .padding(16.dp)
    ) {
        Column {
            // Bienvenida
            Text(
                text = "Hola, Yordy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "¿Cómo te sientes hoy?",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Barra de búsqueda editable
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            color = colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Buscar médicos, especialidades...",
                                        color = colorScheme.onSurface.copy(alpha = 0.6f),
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }
        
        // Notificaciones y perfil
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono de notificaciones con badge dinámico
            val unreadCount = getUnreadNotificationCount()
            NotificationIcon(
                onClick = onNavigateToNotifications,
                badgeCount = unreadCount,
                tint = Color.White
            )
            
            // Foto de perfil
            IconButton(onClick = onNavigateToProfile) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray, CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun SpecialtiesSection(
    onSpecialtyClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Especialidades",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de especialidades
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getSpecialties()) { specialty ->
                SpecialtyCard(
                    specialty = specialty,
                    onClick = { onSpecialtyClick(specialty.name) }
                )
            }
        }
    }
}

@Composable
fun SpecialtyCard(
    specialty: Specialty,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de la especialidad
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(specialty.backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    specialty.icon,
                    contentDescription = specialty.name,
                    tint = specialty.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = specialty.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun FeaturedDoctorsSection(
    onNavigateToSearch: (String?) -> Unit,
    onNavigateToAppointment: (String) -> Unit = {}
) {
    val doctorRepository = remember { DoctorRepository() }
    val colorScheme = MaterialTheme.colorScheme
    
    // Obtener médicos disponibles
    var availableDoctors by remember { mutableStateOf<List<Doctor>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        availableDoctors = doctorRepository.getAvailableDoctors()
    }
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Médicos Disponibles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            
            TextButton(onClick = { onNavigateToSearch(null) }) {
                Text(
                    text = "Ver todos",
                    color = MediTurnBlue,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de médicos disponibles
        if (availableDoctors.isNotEmpty()) {
            availableDoctors.forEach { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = {
                        onNavigateToAppointment(doctor.id)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cargando...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun SearchResultsSection(
    results: List<Doctor>,
    searchQuery: String,
    onNavigateToAppointment: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Resultados de búsqueda: \"$searchQuery\"",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "${results.size} médicos encontrados",
            fontSize = 14.sp,
            color = colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Sin resultados",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No se encontraron médicos",
                        fontSize = 16.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Intenta con otros términos de búsqueda",
                        fontSize = 14.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            results.forEach { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = {
                        onNavigateToAppointment(doctor.id)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DoctorCard(
    doctor: Doctor? = null,
    name: String = "",
    specialty: String = "",
    onClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val displayName = doctor?.name ?: name
    val displaySpecialty = doctor?.specialty ?: specialty
    
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
                    text = displayName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = displaySpecialty,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                // Mostrar información adicional si hay doctor completo
                doctor?.let {
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
                            text = "${it.rating} (${it.reviews})",
                            fontSize = 12.sp,
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "S/ ${it.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MediTurnBlue
                    )
                }
            }
            
            // Indicador de disponibilidad
            doctor?.let {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (it.isAvailable) MediTurnBlue else Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (it.isAvailable) "Disponible" else "Ocupado",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Modelo de datos para una especialidad médica
data class Specialty(
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color
)

fun getSpecialties(): List<Specialty> {
    return listOf(
        Specialty("Cardiología", Icons.Default.Person, MediTurnRed.copy(alpha = 0.2f), MediTurnRed),
        Specialty("Neurología", Icons.Default.Person, MediTurnPurple.copy(alpha = 0.2f), MediTurnPurple),
        Specialty("Traumatología", Icons.Default.Person, MediTurnLightBlue.copy(alpha = 0.2f), MediTurnLightBlue),
        Specialty("Oftalmología", Icons.Default.Person, MediTurnLightGreen.copy(alpha = 0.2f), MediTurnLightGreen),
        Specialty("Pediatría", Icons.Default.Person, MediTurnPink.copy(alpha = 0.2f), MediTurnPink),
        Specialty("Medicina General", Icons.Default.Person, MediTurnOrange.copy(alpha = 0.2f), MediTurnOrange)
    )
}