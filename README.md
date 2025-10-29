# 🏥 MediTurn – Sistema de Gestión de Citas Médicas

## 📑 Índice

- [Descripción](#-descripción)
- [Público Objetivo](#-público-objetivo)
- [Historias de Usuario](#-historias-de-usuario)
- [Equipo y Roles](#-equipo-y-roles)
- [Arquitectura](#️-arquitectura)
- [Tecnologías Utilizadas](#️-tecnologías-utilizadas)
- [Pantallas Principales](#-pantallas-principales)
- [Instalación y Uso](#-instalación-y-uso)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Diseño UI/UX](#-diseño-uiux)
- [Video de Demostración](#-video-de-demostración)
- [Contribuidores](#️-contribuidores)

## 📋 Descripción

MediTurn es una aplicación móvil Android desarrollada en Kotlin con Jetpack Compose que permite a los usuarios gestionar sus citas médicas de manera eficiente. La aplicación facilita la búsqueda de médicos por especialidad, visualización de perfiles detallados, agendado, edición y cancelación de citas, además de incluir soporte para teleconsulta.

## 🎯 Público Objetivo

Pacientes que buscan una solución digital para:
- Buscar médicos por especialidad y ubicación
- Agendar y gestionar citas médicas de forma autónoma
- Acceder a consultas médicas vía teleconsulta
- Mantener un historial organizado de sus citas

## 📝 Historias de Usuario

1. **Como paciente, quiero buscar un médico por especialidad para encontrar atención adecuada.**
   - Permite buscar médicos filtrando por especialidad, nombre o ubicación
   - Filtros adicionales: ciudad y soporte de teleconsulta

2. **Como paciente, quiero ver el perfil del médico para conocer su experiencia y horarios.**
   - Muestra información completa: especialidad, calificación, años de experiencia, ubicación, horarios disponibles y opción de teleconsulta

3. **Como paciente, quiero agendar una cita seleccionando fecha y hora disponibles.**
   - Formulario de agendamiento con selector de fecha y hora
   - Validación de campos (fecha, hora y motivo de consulta)

4. **Como paciente, quiero editar o cancelar una cita en caso de imprevisto.**
   - Reprogramación de citas existentes (modificar fecha/hora)
   - Cancelación de citas con confirmación

5. **Como paciente, quiero ver mis próximas citas en un calendario para organizarme mejor.**
   - Vista de citas próximas y pasadas con pestañas
   - Detalles completos de cada cita programada

6. **Como paciente, quiero recibir recordatorios para no olvidar mis citas médicas.**
   - Sistema de notificaciones integrado
   - Badge con contador de recordatorios no leídos


## 👥 Equipo y Roles

- **Líder Técnico**: Ronaldo Meza Pastrana
- **Diseñador UI/UX**: Yordy Aldair Pillaca Ramos
- **Tester/Documentador**: Yordy Pillaca & Ronaldo Meza

## 🏗 Arquitectura

### Patrón de Diseño
**MVVM (Model-View-ViewModel) + Repository Pattern**

### Capas
```
UI (Compose) 
  ↓
Repositories 
  ↓
Room Database (DAO/Entities) + DataSource (seed) + Mappers
```

### Navegación
- **Navigation Compose**: Sistema de navegación declarativa y type-safe

### Estructura de Paquetes

```
com.citasmedicas/
├─ data/
│  ├─ repository/        # Lógica de negocio y acceso a datos
│  ├─ local/            # Room Database
│  │  ├─ dao/           # Data Access Objects
│  │  ├─ entity/        # Entidades de base de datos
│  │  └─ converter/     # Converters para tipos complejos
│  ├─ datasource/       # Datos simulados (seed)
│  ├─ mapper/           # Mappers entre entidades y modelos
│  └─ session/          # Gestión de sesión de usuario
├─ model/               # Modelos de dominio
├─ navigation/          # Configuración de navegación
├─ ui/
│  ├─ screens/          # Pantallas principales
│  │  ├─ auth/         # Login
│  │  ├─ home/         # Pantalla principal
│  │  ├─ search/       # Búsqueda de médicos
│  │  ├─ doctor/       # Detalle de médico
│  │  ├─ appointment/  # Agendar/Reprogramar cita
│  │  ├─ calendar/     # Mis citas (próximas/pasadas)
│  │  ├─ profile/      # Perfil de usuario
│  │  └─ notifications/# Notificaciones
│  ├─ components/      # Componentes reutilizables
│  └─ theme/           # Tema y estilos Material 3
└─ util/               # Utilidades y helpers
```

## 🛠 Tecnologías Utilizadas

- **Lenguaje**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose (Material 3)
- **Arquitectura**: MVVM + Repository Pattern
- **Base de Datos**: Room 2.6.1
- **Navegación**: Navigation Compose 2.8.4
- **Persistencia Local**: DataStore Preferences 1.1.1
- **Build Tool**: Gradle 8.13 (AGP)
- **Compilación**: KSP (Kotlin Symbol Processing)
- **Responsive Design**: Detección automática de tamaño de pantalla



## 📱 Pantallas Principales

1. **Login**: Autenticación de usuario
2. **Home**: Pantalla principal con búsqueda y atajos
3. **Búsqueda**: Lista de médicos con filtros avanzados
4. **Detalle de Médico**: Información completa y opción de agendar
5. **Agendar Cita**: Formulario para crear/reprogramar citas
6. **Mis Citas**: Calendario con próximas y pasadas
7. **Perfil**: Información del usuario

## 🚀 Instalación y Uso

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK (API 24+)

### Pasos de Instalación

1. **Clonar el repositorio**:
```bash
git clone <url-del-repositorio>
cd CitasMedicas
```

2. **Abrir en Android Studio**:
   - Abre Android Studio
   - Selecciona "Open" y navega a la carpeta del proyecto
   - Espera a que Gradle sincronice las dependencias

3. **Ejecutar la aplicación**:
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en "Run" o presiona `Shift + F10`

## 📸 Capturas de Pantalla

- [Login](screenshots/login.png)
- [Home](screenshots/home.png)
- [Búsqueda](screenshots/search.png)
- [Detalle Médico](screenshots/doctor_detail.png)
- [Agendar Cita](screenshots/appointment.png)
- [Mis Citas](screenshots/my_appointments.png)
- [Perfil](screenshots/profile.png)

## 🎨 Diseño UI/UX

**Prototipo en Figma**: [Ver diseño](https://www.figma.com/make/FH36eujiFfqT1cngOcDgnj/MediTurn-Medical-App-Prototype?node-id=0-1&p=f&t=HRCvHoCCqUgY9pQ6-0&fullscreen=1)
En el inicio de sesión, seleccionar "Continuar sin registro" para visualizar el funcionamiento del prototipo.
 
## 📺 Video de Demostración
Mira el video en YouTube: [Ver en YouTube](https://youtu.be/4UQsxduhgw0?si=3bduj4s_c8LRVfEf)

## 👨‍💻 Contribuidores
- Ronaldo Meza Pastrana
- Yordy Aldair Pillaca Ramos
