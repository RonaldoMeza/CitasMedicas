package com.citasmedicas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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

/**
 * Pantalla principal de MediTurn - Diseño Figma
 */
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToMyAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        item {
            HeaderSection(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToSearch = onNavigateToSearch
            )
        }
        
        // Especialidades
        item {
            SpecialtiesSection(
                onNavigateToSearch = onNavigateToSearch
            )
        }
        
        // Médicos destacados
        item {
            FeaturedDoctorsSection(
                onNavigateToSearch = onNavigateToSearch
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HeaderSection(
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
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
            
            // Barra de búsqueda
            Card(
                onClick = onNavigateToSearch,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Buscar médicos, especialidades...",
                        color = Color.Gray
                    )
                }
            }
        }
        
        // Notificaciones y perfil
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono de notificaciones
            Box {
                IconButton(onClick = {
                    // Acción de notificaciones por implementar
                }) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = Color.White
                    )
                }
                // Indicador de notificaciones no leídas
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "2",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
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
    onNavigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Especialidades",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de especialidades
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getSpecialties()) { specialty ->
                SpecialtyCard(
                    specialty = specialty,
                    onClick = onNavigateToSearch
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
                color = Color.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun FeaturedDoctorsSection(
    onNavigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Médicos Destacados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            TextButton(onClick = onNavigateToSearch) {
                Text(
                    text = "Ver todos",
                    color = MediTurnBlue,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de médicos destacados
        DoctorCard(
            name = "Dra. María González",
            specialty = "Cardiología",
            onClick = onNavigateToSearch
        )
    }
}

@Composable
fun DoctorCard(
    name: String,
    specialty: String,
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
            
            Column {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
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