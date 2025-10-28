package com.citasmedicas.ui.screens.profile

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.ui.theme.*

/**
 * Pantalla de perfil del usuario
 */
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        item {
            ProfileHeaderSection(onNavigateToNotifications = onNavigateToNotifications)
        }
        
        // Información básica
        item {
            ProfileMainCard(
                onNavigateToEditProfile = onNavigateToEditProfile
            )
        }
        
        // Información detallada
        item {
            ProfileDetailsSection()
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ProfileHeaderSection(onNavigateToNotifications: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediTurnBlue)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Notificaciones con badge dinámico
            val unreadCount = com.citasmedicas.ui.components.getUnreadNotificationCount()
            com.citasmedicas.ui.components.NotificationIcon(
                onClick = onNavigateToNotifications,
                badgeCount = unreadCount,
                tint = Color.White
            )
        }
    }
}

@Composable
fun ProfileMainCard(
    onNavigateToEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .offset(y = (-20).dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto de perfil
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Gray, CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "Yordy Pillaca",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Yordy.Pillaca@tecsup.edu.pe",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Editar información
            Button(
                onClick = onNavigateToEditProfile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Información")
            }
        }
    }
}

@Composable
fun ProfileDetailsSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val profileDetails = listOf(
            ProfileDetail(
                icon = Icons.Default.Person,
                iconColor = MediTurnLightBlue,
                label = "Nombre completo",
                value = "Yordy Pillaca Ramos"
            ),
            ProfileDetail(
                icon = Icons.Default.Email,
                iconColor = MediTurnPurple,
                label = "Correo electrónico",
                value = "yordy.pillaca@tecsup.edu.pe"
            ),
            ProfileDetail(
                icon = Icons.Default.Phone,
                iconColor = MediTurnLightGreen,
                label = "Teléfono",
                value = "+51 985624565"
            ),
            ProfileDetail(
                icon = Icons.Default.LocationOn,
                iconColor = MediTurnOrange,
                label = "Dirección",
                value = "Av. Huarochiri 1687, Santa Anita, Lima"
            ),
            ProfileDetail(
                icon = Icons.Default.DateRange,
                iconColor = MediTurnPink,
                label = "Fecha de nacimiento",
                value = "08/08/2003"
            )
        )
        
        profileDetails.forEach { detail ->
            ProfileDetailCard(detail = detail)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfileDetailCard(
    detail: ProfileDetail
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(detail.iconColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    detail.icon,
                    contentDescription = detail.label,
                    tint = detail.iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = detail.label,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = detail.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}

data class ProfileDetail(
    val icon: ImageVector,
    val iconColor: Color,
    val label: String,
    val value: String
)