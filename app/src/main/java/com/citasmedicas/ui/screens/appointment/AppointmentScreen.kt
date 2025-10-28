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
import com.citasmedicas.data.repository.AppointmentRepository
import com.citasmedicas.data.repository.SlotRepository
import com.citasmedicas.data.repository.DoctorRepository
import com.citasmedicas.model.Doctor
import com.citasmedicas.model.Appointment
import com.citasmedicas.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla para agendar una cita médica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    appointmentId: String? = null, // ID de cita para reprogramar
    doctorId: String,
    onNavigateBack: () -> Unit,
    onAppointmentScheduled: () -> Unit
) {
    val doctorRepository = remember { DoctorRepository() }
    val appointmentRepository = remember { AppointmentRepository() }
    val slotRepository = remember { SlotRepository() }

    // Estado para el doctor
    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var existingAppointment by remember { mutableStateOf<Appointment?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isReprogramming by remember { mutableStateOf(false) }

    LaunchedEffect(appointmentId, doctorId) {
        isLoading = true

        android.util.Log.d("AppointmentScreen", "appointmentId: '$appointmentId', doctorId: '$doctorId'")

        // Si hay appointmentId, estamos reprogramando
        if (appointmentId != null && appointmentId.isNotBlank() && appointmentId != "null") {
            android.util.Log.d("AppointmentScreen", "MODO REPROGRAMAR - buscando cita: $appointmentId")
            existingAppointment = appointmentRepository.getAppointmentById(appointmentId)
            android.util.Log.d("AppointmentScreen", "existingAppointment encontrada: $existingAppointment")

            existingAppointment?.let {
                isReprogramming = true
                doctor = doctorRepository.getDoctorById(it.doctorId)
                android.util.Log.d("AppointmentScreen", "isReprogramming = true, doctorId: ${it.doctorId}")
            }
        } else {
            // Modo crear
            android.util.Log.d("AppointmentScreen", "MODO CREAR")
            doctor = doctorRepository.getDoctorById(doctorId)
            isReprogramming = false
        }

        isLoading = false
    }

    // Estado para los campos del formulario
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Si estamos reprogramando, cargar datos existentes
    LaunchedEffect(existingAppointment, isReprogramming) {
        existingAppointment?.let {
            selectedDate = it.date
            selectedTime = it.time
            reason = it.reason
        }
    }

    // Estado para el DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        initialDisplayMode = DisplayMode.Picker
    )

    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Convertir fecha seleccionada a string
    val selectedDateString = remember(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.format(millis)
        } ?: ""
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
                title = if (isReprogramming) "Reprogramar Cita" else "Agendar Cita",
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
                        selectedDate = selectedDateString,
                        onDateClick = { showDatePickerDialog = true }
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
                        date = selectedDateString,
                        time = selectedTime,
                        reason = reason,
                        onConfirm = {
                            showDialog = true
                        },
                        validationMessage = validateAppointmentForm(selectedDateString, selectedTime, reason)
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
                    showDialog = false
                    // Crear o actualizar la cita
                    doctor?.let {
                        android.util.Log.d("AppointmentScreen", "isReprogramming: $isReprogramming, existingAppointment: $existingAppointment")

                        val success = if (isReprogramming && existingAppointment != null) {
                            // Actualizar cita existente - MODO REPROGRAMAR
                            android.util.Log.d("AppointmentScreen", "UPDATING existing appointment: ${existingAppointment!!.id}")
                            val updatedAppointment = existingAppointment!!.copy(
                                date = selectedDateString,
                                time = selectedTime,
                                reason = reason
                            )
                            appointmentRepository.updateAppointment(updatedAppointment)
                        } else {
                            // Crear nueva cita - MODO CREAR
                            android.util.Log.d("AppointmentScreen", "CREATING new appointment")
                            val newAppointment = Appointment(
                                id = UUID.randomUUID().toString(),
                                doctorId = it.id,
                                doctorName = it.name,
                                specialty = it.specialty,
                                date = selectedDateString,
                                time = selectedTime,
                                reason = reason,
                                price = it.price
                            )
                            appointmentRepository.createAppointment(newAppointment)
                        }
                        if (success) {
                            showSuccessDialog = true
                        } else {
                            errorMessage = "Error al agendar la cita"
                        }
                    }
                },
                onDismiss = { showDialog = false },
                doctor = doctor,
                date = selectedDateString,
                time = selectedTime
            )
        }

        // Dialog de éxito
        if (showSuccessDialog) {
            SuccessAppointmentDialog(
                isReprogramming = isReprogramming,
                onConfirm = {
                    showSuccessDialog = false
                    onAppointmentScheduled()
                }
            )
        }

        // Mostrar error si hay
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                errorMessage = null
            }
        }

        // Dialog de DatePicker
        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismiss = { showDatePickerDialog = false },
                onConfirm = {
                    selectedDate = it
                    showDatePickerDialog = false
                },
                datePickerState = datePickerState
            )
        }
    }
}

@Composable
fun AppointmentHeaderSection(
    doctor: Doctor?,
    title: String = "Agendar Cita",
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
                text = title,
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
    onDateClick: () -> Unit
) {
    Card(
        onClick = onDateClick,
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

            // Mostrar fecha seleccionada o placeholder
            OutlinedTextField(
                value = selectedDate.ifEmpty { "Selecciona una fecha" },
                onValueChange = { },
                label = { Text("Fecha de la cita") },
                leadingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledTextColor = if (selectedDate.isNotEmpty()) Color.Black else Color.Gray,
                    disabledContainerColor = Color.White,
                    disabledLabelColor = Color.Gray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    datePickerState: DatePickerState
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        onConfirm(formatter.format(millis))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediTurnBlue
                )
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Seleccionar Fecha", fontWeight = FontWeight.Bold) },
        text = {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
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
    onConfirm: () -> Unit,
    validationMessage: String? = null
) {
    val isFormValid = date.isNotBlank() && time.isNotBlank() && validationMessage == null

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

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de validación
            if (validationMessage != null) {
                Text(
                    text = validationMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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
    onDismiss: () -> Unit,
    doctor: Doctor?,
    date: String,
    time: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Confirmar Cita",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                doctor?.let {
                    Text("¿Deseas confirmar esta cita?", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Doctor: ${it.name}")
                    Text("Especialidad: ${it.specialty}")
                    Text("Fecha: $date")
                    Text("Hora: $time")
                    Text("Precio: S/ ${it.price}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediTurnBlue
                )
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SuccessAppointmentDialog(
    isReprogramming: Boolean,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = {
            Text(
                text = if (isReprogramming) "✓ Cita Reprogramada" else "✓ Cita Agendada",
                fontWeight = FontWeight.Bold,
                color = MediTurnBlue
            )
        },
        text = {
            Text(
                if (isReprogramming)
                    "Tu cita ha sido reprogramada exitosamente. Los cambios han sido guardados."
                else
                    "Tu cita ha sido agendada exitosamente. Recibirás una notificación recordatorio."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediTurnBlue
                )
            ) {
                Text("Aceptar")
            }
        }
    )
}

/**
 * Valida el formulario de agendamiento de cita
 * Retorna null si es válido, o un mensaje de error si hay problemas
 */
fun validateAppointmentForm(date: String, time: String, reason: String): String? {
    // Validar fecha
    if (date.isBlank()) {
        return "Por favor selecciona una fecha"
    }

    // Validar hora
    if (time.isBlank()) {
        return "Por favor selecciona una hora"
    }

    // Validar motivo (opcional pero recomendado)
    if (reason.isBlank()) {
        return "Por favor describe el motivo de la consulta"
    }

    // Validar que el motivo tenga al menos 10 caracteres
    if (reason.length < 10) {
        return "El motivo debe tener al menos 10 caracteres"
    }

    return null
}