# Sistema Mercadía - Gestión de Espacios Comerciales

Sistema web para la gestión de espacios comerciales en tiendas Mercadía del Grupo Iconn. Permite digitalizar el proceso de asignación de espacios a proveedores dentro de las tiendas.

## Características Principales

- **Arquitectura MVC** con Spring Boot y Thymeleaf
- **Interfaz responsiva** y sencilla para uso en computadoras y dispositivos móviles
- **Sistema de roles** (Administrador, Comercial, Tienda)
- **CRUD completo** de proveedores y formularios
- **Validación** de campos obligatorios
- **Búsqueda y filtrado** avanzado
- **Reportes** por tienda y proveedor
- **Seguridad** con autenticación, sesiones y encriptación de contraseñas
- **Base de datos PostgreSQL**

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security 6**
- **Spring Data JPA**
- **Thymeleaf**
- **PostgreSQL**
- **Bootstrap 5.3.0**
- **Maven**

## Requisitos Previos

1. **Java 17** o superior
2. **PostgreSQL 12** o superior
3. **Maven 3.6** o superior

## Configuración de la Base de Datos

1. Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE mercadia_db;
CREATE USER mercadia_user WITH PASSWORD 'mercadia_password';
GRANT ALL PRIVILEGES ON DATABASE mercadia_db TO mercadia_user;
```

2. Ajustar la configuración en `src/main/resources/application.properties` si es necesario:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mercadia_db
spring.datasource.username=mercadia_user
spring.datasource.password=mercadia_password
```

## Instalación y Ejecución

1. **Clonar el repositorio** (si corresponde) o descargar el código fuente

2. **Compilar el proyecto**:
```bash
./mvnw clean compile
```

3. **Ejecutar el proyecto**:
```bash
./mvnw spring-boot:run
```

4. **Acceder al sistema**:
   - URL: http://localhost:8080/mercadia
   - El sistema se ejecuta en el puerto 8080 con el contexto `/mercadia`

## Usuarios de Prueba

El sistema incluye usuarios de prueba que se crean automáticamente:

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `password` | ADMINISTRADOR | Acceso completo al sistema |
| `comercial` | `password` | COMERCIAL | Gestión de formularios y proveedores |
| `tienda001` | `password` | TIENDA | Captura de formularios (Tienda TDA001) |

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

## Funcionalidades por Rol

### Administrador
- Gestión completa de usuarios
- Gestión completa de proveedores
- Gestión completa de formularios
- Acceso a todos los reportes
- Configuración del sistema

### Comercial
- Visualización de todos los formularios
- Gestión de proveedores
- Reportes por tienda y proveedor
- Edición de formularios de cualquier tienda

### Tienda
- Captura de formularios para su tienda asignada
- Visualización de formularios propios
- Consulta de proveedores
- Dashboard con estadísticas de su tienda

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

## Configuración de Desarrollo

Para desarrollo, se recomienda:

1. **Configurar perfil de desarrollo** en `application-dev.properties`:
```properties
spring.jpa.show-sql=true
logging.level.com.merca.merca=DEBUG
spring.thymeleaf.cache=false
```

2. **Usar H2 en memoria** para pruebas rápidas:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

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

## Contribución

Para contribuir al proyecto:

1. Seguir las convenciones de código establecidas
2. Escribir pruebas unitarias para nuevas funcionalidades
3. Documentar cambios significativos
4. Usar commits descriptivos

## Licencia

Este proyecto es parte del sistema interno de Mercadía - Grupo Iconn.

## Contacto

Para soporte técnico o consultas sobre el sistema, contactar al equipo de desarrollo.

---

**Nota**: Este sistema está diseñado para uso interno de Mercadía y contiene configuraciones específicas para el entorno de producción del Grupo Iconn.
