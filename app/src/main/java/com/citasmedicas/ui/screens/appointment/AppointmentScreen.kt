package com.citasmedicas.ui.screens.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.citasmedicas.data.datasource.DoctorDataSource
import com.citasmedicas.model.Doctor
import com.citasmedicas.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla para agendar una cita médica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    doctorId: String,
    onNavigateBack: () -> Unit,
    onAppointmentScheduled: () -> Unit
) {
    val dataSource = remember { DoctorDataSource() }

    // Estado para el doctor
    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(doctorId) {
        isLoading = true
        doctor = dataSource.getDoctorById(doctorId)
        isLoading = false
    }

    // Estado para los campos del formulario
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        selectedDate = formatter.format(calendar.time)
    }

    if (isLoading) {
        // Mostrar un indicador de carga
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            AppointmentHeaderSection(
                doctor = doctor,
                onNavigateBack = onNavigateBack
            )

            // Formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Información del médico
                doctor?.let {
                    DoctorInfoCard(doctor = it)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Selección de fecha
                    DatePickerSection(
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selección de hora
                    TimePickerSection(
                        selectedTime = selectedTime,
                        availableTimes = it.schedule ?: emptyList(),
                        onTimeSelected = { selectedTime = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Motivo de consulta
                    ReasonSection(
                        reason = reason,
                        onReasonChanged = { reason = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Resumen y botón confirmar
                    SummaryCard(
                        doctor = doctor,
                        date = selectedDate,
                        time = selectedTime,
                        reason = reason,
                        onConfirm = {
                            showDialog = true
                        }
                    )
                } ?: run {
                    // Error: médico no encontrado
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Médico no encontrado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }
            }

            // Espacio para bottom navigation
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Dialog de confirmación
        if (showDialog) {
            ConfirmAppointmentDialog(
                onConfirm = {
                    // NOTA: En DÍA 4 solo simulamos el proceso.
                    // La funcionalidad de guardar realmente la cita es para DÍA 5.
                    // Por ahora solo mostramos el diálogo de confirmación.
                    showDialog = false
                    onAppointmentScheduled()
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun AppointmentHeaderSection(
    doctor: Doctor?,
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
            Text(
                text = "Agendar Cita",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun DoctorInfoCard(
    doctor: Doctor
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
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Gray, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Foto",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = doctor.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = doctor.specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "S/ ${doctor.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MediTurnBlue
                )
            }
        }
    }
}

@Composable
fun DatePickerSection(
    selectedDate: String,
    onDateSelected: (String) -> Unit
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Fecha",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Seleccionar Fecha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = { /* Read only */ },
                label = { Text("DD/MM/YYYY") },
                leadingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            Text(
                text = "Formato: DD/MM/YYYY",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun TimePickerSection(
    selectedTime: String,
    availableTimes: List<String>,
    onTimeSelected: (String) -> Unit
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Hora",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Seleccionar Hora",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mostrar las 4 primeras horas disponibles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableTimes.take(4).forEach { time ->
                    FilterChip(
                        onClick = { onTimeSelected(time) },
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

            if (availableTimes.size > 4) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTimes.drop(4).take(4).forEach { time ->
                        FilterChip(
                            onClick = { onTimeSelected(time) },
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
            }
        }
    }
}

@Composable
fun ReasonSection(
    reason: String,
    onReasonChanged: (String) -> Unit
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Motivo",
                    tint = MediTurnBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Motivo de Consulta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reason,
                onValueChange = onReasonChanged,
                label = { Text("Describe el motivo de tu consulta...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
        }
    }
}

@Composable
fun SummaryCard(
    doctor: Doctor?,
    date: String,
    time: String,
    reason: String,
    onConfirm: () -> Unit
) {
    val isFormValid = date.isNotBlank() && time.isNotBlank()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumen de la Cita",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            doctor?.let {
                Text(
                    text = "Doctor: ${it.name}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Fecha: $date",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Hora: $time",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total a pagar: S/ ${it.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MediTurnBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediTurnBlue
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = isFormValid
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Confirmar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirmar Cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ConfirmAppointmentDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Simulación",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Simulación")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediTurnBlue
                )
            ) {
                Text("Entendido")
            }
        }
    )
}