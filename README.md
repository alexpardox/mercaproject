# Sistema Mercadía - Gestión de Espacios Comerciales

## Descripción

Sistema web para la gestión de espacios comerciales en tiendas Mercadía del Grupo Iconn. Permite digitalizar el proceso de asignación de espacios a proveedores dentro de las tiendas.

### Problema Identificado

Actualmente, el proceso de gestión y asignación de espacios comerciales a proveedores se realiza manualmente, lo que conlleva:
- Duplicidad de información
- Dificultad para rastrear acuerdos comerciales
- Falta de visibilidad sobre espacios disponibles
- Procesos administrativos lentos e ineficientes

### Solución

Mercadía digitaliza todo el proceso mediante:
- Registro centralizado de proveedores
- Control de espacios comerciales por tienda
- Gestión de formularios digitales
- Sistema de roles específicos para cada nivel operativo
- Reportes y análisis en tiempo real

## Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Requerimientos](#requerimientos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Pruebas](#pruebas-y-cicd)
- [Despliegue](#-despliegue-en-producción)
- [Contribución](#contribución)
- [Licencia](#licencia)
- [Contacto](#contacto)

## Arquitectura

El sistema Mercadía sigue una arquitectura MVC (Modelo-Vista-Controlador) implementada con Spring Boot:

- **Modelo**: Entidades JPA para la persistencia de datos
- **Vista**: Thymeleaf + Bootstrap para la interfaz de usuario
- **Controlador**: Spring MVC para la lógica de negocio

La arquitectura se compone de las siguientes capas:
1. **Capa de Presentación**: Thymeleaf + Bootstrap
2. **Capa de Controladores**: Spring MVC
3. **Capa de Servicios**: Spring Services
4. **Capa de Persistencia**: Spring Data JPA + PostgreSQL

### Diagrama de Arquitectura

```mermaid
flowchart TD
    Client[Cliente Web] --> FE[Frontend - Thymeleaf + Bootstrap]
    FE --> Controller[Controladores Spring MVC]
    Controller --> Service[Servicios de Negocio]
    Service --> Repository[Repositorios JPA]
    Repository --> DB[(PostgreSQL/Supabase)]
    
    Security[Spring Security] --> Controller
    Config[Configuración] --> Service
```

## Requerimientos

### Requerimientos de Servidor

- **Servidor de Aplicación**: Compatible con Servlet 6.0+ (Tomcat embebido incluido)
- **Servidor Web**: Apache, Nginx o similar para producción
- **Base de Datos**: PostgreSQL 12+
- **JDK**: Java 17+
- **Memoria**: Mínimo 512MB RAM (1GB+ recomendado)
- **Almacenamiento**: 500MB+ para la aplicación y logs

### Dependencias Principales

- **Spring Boot**: 3.3.2
- **Spring Security**: 6
- **Spring Data JPA**: Incluido en Spring Boot
- **Thymeleaf**: Incluido en Spring Boot
- **PostgreSQL Driver**: Runtime
- **Bootstrap**: 5.3.0 (WebJars)
- **Maven**: 3.6+ (Gestión de dependencias)

## Instalación

### Ambiente de Desarrollo

1. **Requisitos previos**:
   - Java 17+
   - Maven 3.6+
   - PostgreSQL 12+
   - Git

2. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/alexpardox/mercaproject.git
   cd mercaproject
   ```

3. **Configurar la base de datos PostgreSQL**:
   ```sql
   CREATE DATABASE mercadia_db;
   CREATE USER mercadia_user WITH PASSWORD 'tu_contraseña_segura';
   GRANT ALL PRIVILEGES ON DATABASE mercadia_db TO mercadia_user;
   ```

4. **Configurar variables de entorno**:
   Crea un archivo `.env` en la raíz del proyecto (no incluirlo en Git):
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/mercadia_db
   DB_USERNAME=mercadia_user
   DB_PASSWORD=tu_contraseña_segura
   SPRING_PROFILES_ACTIVE=dev
   ```

5. **Compilar el proyecto**:
   ```bash
   ./mvnw clean compile
   ```

6. **Ejecutar la aplicación en modo desarrollo**:
   ```bash
   ./mvnw spring-boot:run
   ```

7. **Acceder a la aplicación**:
   - URL: http://localhost:8080/mercadia
   - Credenciales predeterminadas: admin/password

## Configuración

### Archivos de Configuración

- **`application.properties`**: Configuración principal
- **`application-dev.properties`**: Configuración para desarrollo
- **`application-production.properties`**: Configuración para producción
- **`.env`**: Variables de entorno (no incluir en Git)
- **`system.properties`**: Configuración para Heroku (versión de Java)
- **`Procfile`**: Configuración para Heroku (comandos de inicio)

### Configuración de Base de Datos

La aplicación usa PostgreSQL para todos los entornos. Configura las siguientes variables:

- `DB_URL`: URL de conexión a PostgreSQL
- `DB_USERNAME`: Usuario de la base de datos
- `DB_PASSWORD`: Contraseña de la base de datos

### Configuración de Seguridad

- `JWT_SECRET`: Clave secreta para la generación de tokens JWT
- `JWT_EXPIRATION`: Tiempo de expiración del token en milisegundos

### Configuración de Perfiles

- **`dev`**: Para desarrollo local
- **`production`**: Para entorno de producción (Heroku)

## Uso

### Usuarios Predefinidos

El sistema inicializa automáticamente los siguientes usuarios (solo si no existen):

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `password` | ADMINISTRADOR | Acceso completo al sistema |
| `comercial` | `password` | COMERCIAL | Gestión de formularios y proveedores |
| `tienda001` | `password` | TIENDA | Captura de formularios (Tienda TDA001) |

### Funcionalidades por Rol

#### Administrador
- Gestión completa de usuarios
- Gestión completa de proveedores
- Gestión completa de formularios
- Acceso a todos los reportes
- Configuración del sistema

#### Comercial
- Visualización de todos los formularios
- Gestión de proveedores
- Reportes por tienda y proveedor
- Edición de formularios de cualquier tienda

#### Tienda
- Captura de formularios para su tienda asignada
- Visualización de formularios propios
- Consulta de proveedores
- Dashboard con estadísticas de su tienda

## Pruebas y CI/CD

### Ejecutar Tests Manualmente

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar tests específicos
./mvnw test -Dtest=ProveedorTest

# Ejecutar tests con reportes
./mvnw clean test jacoco:report
```

### Integración Continua con GitHub Actions

Este proyecto está configurado con GitHub Actions para ejecutar automáticamente las pruebas en cada push y pull request.

**Configuración GitHub Actions:**
- ✅ **Archivos configurados**: `.github/workflows/`
- ✅ **Tests automáticos**: Se ejecutan en Java 17
- ✅ **Base de datos de prueba**: PostgreSQL
- ✅ **Notificaciones**: Email en fallos

**Estados de Build:**
- [![Build Status](https://github.com/alexpardox/mercaproject/actions/workflows/main.yml/badge.svg)](https://github.com/alexpardox/mercaproject/actions)

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/merca/merca/
│   │   ├── config/          # Configuración de seguridad e inicialización
│   │   ├── controller/      # Controladores MVC
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositorios de datos
│   │   ├── service/         # Servicios de negocio
│   │   └── MercaApplication.java
│   └── resources/
│       ├── static/          # Recursos estáticos (CSS, JS, imágenes)
│       ├── templates/       # Plantillas Thymeleaf
│       └── application.properties
└── test/
    └── java/                # Pruebas unitarias
```

## Entidades Principales

### Usuario
- Información personal y credenciales
- Rol y tienda asignada
- Control de acceso y sesiones

### Proveedor
- Datos fiscales (RFC, razón social)
- Información de contacto
- Estado (Activo, Inactivo, Suspendido)

### Formulario
- Información de la tienda
- Proveedor asignado
- Detalles del espacio (área, tipo, medidas)
- Vigencia y precio acordado
- Estados (Activo, Vencido, Cancelado)

## Personalización

### Estilos CSS
Los estilos personalizados se encuentran en las plantillas Thymeleaf usando variables CSS:
- `--mercadia-blue`: Color principal de la marca
- `--mercadia-light-blue`: Color secundario

### Validaciones
Las validaciones se pueden personalizar en las entidades usando anotaciones de Bean Validation.

### Reportes
Se pueden agregar nuevos reportes implementando métodos en los repositorios y servicios correspondientes.

## Troubleshooting

### Error de Conexión a Base de Datos
- Verificar que PostgreSQL esté ejecutándose
- Comprobar credenciales en `application.properties`
- Asegurar que la base de datos y usuario existan

### Error de Permisos
- Verificar que el usuario tenga los permisos necesarios
- Comprobar la configuración de Spring Security

### Error de Puerto Ocupado
- Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

## 🐘 Configuración de Supabase para Producción

### 1. Crear Proyecto en Supabase

1. Ve a [supabase.com](https://supabase.com)
2. Crea una nueva cuenta o inicia sesión
3. Crea un nuevo proyecto
4. Anota las credenciales de conexión

### 2. Ejecutar Script de Base de Datos

Ejecuta el script SQL en el editor de Supabase (archivo: `database/mercadia_postgresql.sql`):

El script incluye:
- ✅ Creación de tablas con relaciones
- ✅ Índices para rendimiento
- ✅ Datos de ejemplo con usuarios predeterminados
- ✅ Triggers de auditoría automática
- ✅ Políticas de seguridad RLS
- ✅ Vistas y funciones útiles

### 3. Configurar Variables de Entorno

Crea un archivo `.env` basado en `.env.example`:

```bash
cp .env.example .env
```

Actualiza con tus credenciales de Supabase:

```env
DB_HOST=db.tu-proyecto-supabase.supabase.co
DB_USERNAME=postgres
DB_PASSWORD=tu-password-supabase
DB_URL=postgresql://postgres:tu-password@db.tu-proyecto.supabase.co:5432/postgres
JWT_SECRET=tu_clave_jwt_muy_segura_de_al_menos_32_caracteres
SPRING_PROFILES_ACTIVE=production
```

### 4. Ejecutar con PostgreSQL

```bash
# Usar perfil de producción con Supabase
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

## 👥 Usuarios por Defecto (Supabase)

Después de ejecutar el script de base de datos:

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin@mercadia.com | admin123 | ADMIN |
| gerente@mercadia.com | gerente123 | GERENTE |
| empleado@mercadia.com | empleado123 | EMPLEADO |

## 🚀 Despliegue en Producción

### Variables de Entorno Requeridas

```env
DB_URL=postgresql://postgres:password@db.proyecto.supabase.co:5432/postgres
SPRING_PROFILES_ACTIVE=production
JWT_SECRET=clave_jwt_muy_segura_de_al_menos_32_caracteres
SERVER_PORT=8080
```

### Comando de Despliegue

```bash
# Compilar para producción
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/merca-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

## 🔒 Archivos de Configuración

- `.env` - Variables de entorno locales (no incluir en Git)
- `.env.example` - Plantilla de variables de entorno
- `application-production.properties` - Configuración para producción
- `database/mercadia_postgresql.sql` - Script de base de datos para Supabase

## 🗺️ Roadmap del Proyecto

El desarrollo del Sistema Mercadía sigue la siguiente hoja de ruta:

### Fase 1: Fundación (Completado)
- ✅ Análisis de requerimientos y diseño de arquitectura
- ✅ Configuración del entorno de desarrollo
- ✅ Implementación del modelo de datos básico
- ✅ Autenticación y gestión de usuarios
- ✅ Interfaz de usuario básica con Thymeleaf

### Fase 2: Funcionalidades Core (En Progreso)
- ✅ Gestión completa de proveedores
- ✅ Sistema de formularios digitales
- ✅ Asignación de espacios comerciales
- 🔄 Dashboard para visualización de datos
- 🔄 Notificaciones y alertas

### Fase 3: Optimización (Q3 2025)
- 📅 Mejoras de rendimiento en consultas de base de datos
- 📅 Implementación de caché para operaciones frecuentes
- 📅 Optimización de interfaz de usuario
- 📅 Mejoras de accesibilidad y UX
- 📅 Refactorización de código legacy

### Fase 4: Escalabilidad (Q4 2025)
- 📅 Arquitectura de microservicios para módulos clave
- 📅 API RESTful para integración con sistemas externos
- 📅 Implementación de sistema de eventos y mensajería
- 📅 Soporte para múltiples bases de datos
- 📅 Mejoras en la seguridad y auditoría

### Fase 5: Expansión (Q1-Q2 2026)
- 📅 Aplicación móvil para tiendas
- 📅 Análisis avanzado de datos y reportes personalizados
- 📅 Inteligencia artificial para recomendaciones
- 📅 Integración con sistemas ERP del grupo Iconn
- 📅 Expansión a nuevas unidades de negocio

## Contribución

Para contribuir al proyecto:

1. Seguir las convenciones de código establecidas
2. Escribir pruebas unitarias para nuevas funcionalidades
3. Documentar cambios significativos
4. Usar commits descriptivos

## Licencia

Este proyecto es parte del un programa profesional entre universidad y empresa privada.

## Contacto

Para soporte técnico o consultas sobre el sistema, contactar al equipo de desarrollo.

---

**Nota**: Este sistema está diseñado para proyecto final de Taller de productividad basada en herramientas tecnológicas
