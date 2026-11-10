# FIDNESS WEB
## Proyecto Final – Avance #2: Desarrollo del 50 % del proyecto

**Universidad Fidélitas**  
**Curso:** Desarrollo de Aplicaciones Web y Patrones  
**Proyecto:** Fidness Web  
**Equipo:** Grupo #3  
**Etapa:** Segundo avance del proyecto final  
**Año:** 2026

---

## 1. Descripción del proyecto

Fidness Web es una aplicación web orientada a la administración de actividades de entrenamiento físico, diseñada para facilitar la interacción entre administradores, entrenadores y clientes mediante una plataforma centralizada.

El sistema permite administrar usuarios, categorías y ejercicios, programar clases de entrenamiento, gestionar solicitudes de reserva y consultar información personalizada de acuerdo con el rol de cada usuario.

Durante el segundo avance se desarrollaron e integraron las principales funcionalidades del sistema, utilizando una arquitectura Modelo-Vista-Controlador (MVC), mecanismos de autenticación y autorización, y persistencia de datos mediante MySQL.

Este entregable corresponde a la etapa de implementación de las historias de usuario definidas durante el primer avance, con el objetivo de alcanzar al menos el 50 % de desarrollo requerido.

## 2. Tecnologías utilizadas

El proyecto fue desarrollado utilizando las siguientes tecnologías:

- **Java 17:** lenguaje de programación principal.
- **Spring Boot 3.5.5:** framework para el desarrollo de la aplicación.
- **Spring MVC:** gestión de controladores, solicitudes y navegación.
- **Spring Security:** autenticación y autorización mediante roles.
- **Spring Data JPA:** acceso y persistencia de información.
- **Hibernate:** mapeo objeto-relacional.
- **Thymeleaf:** generación de interfaces HTML dinámicas.
- **Bootstrap 5:** diseño y adaptación de las interfaces.
- **MySQL:** sistema gestor de bases de datos.
- **Maven:** administración de dependencias y compilación.
- **Apache NetBeans:** entorno de desarrollo utilizado.

## 3. Arquitectura del sistema

La aplicación utiliza el patrón Modelo-Vista-Controlador (MVC), con una organización en diferentes paquetes.

**Domain:** contiene las entidades que representan los elementos principales del sistema, como usuarios, roles, clientes, entrenadores, categorías, ejercicios, clases y reservas.

**Repository:** contiene las interfaces encargadas de consultar y almacenar información mediante Spring Data JPA.

**Service:** implementa la lógica de negocio, validaciones y operaciones necesarias para el funcionamiento del sistema.

**Controller:** recibe las solicitudes HTTP, coordina los procesos y establece la navegación entre las diferentes interfaces.

**Security:** contiene la configuración de autenticación, cifrado de contraseñas y control de acceso.

**Templates:** almacena las interfaces HTML desarrolladas con Thymeleaf y Bootstrap.

Esta estructura facilita la organización del código, la separación de responsabilidades y el mantenimiento de las funcionalidades implementadas.

## 4. Roles del sistema

### 4.1. Administrador

El administrador dispone de funcionalidades para gestionar los principales componentes de Fidness Web.

Entre las operaciones implementadas se encuentran la administración de clientes, entrenadores, categorías y ejercicios, incluyendo registro, consulta, edición y gestión de estados.

También dispone de un panel principal que muestra indicadores generales sobre los registros y actividades del sistema.

### 4.2. Entrenador

El entrenador puede programar, consultar, modificar y cancelar clases de entrenamiento.

Además, dispone de una agenda semanal, un módulo para revisar y procesar solicitudes de reserva y una sección para consultar los asistentes confirmados.

Su panel principal presenta información personalizada y estadísticas relacionadas con las clases y reservas bajo su responsabilidad.

### 4.3. Cliente

El cliente puede registrarse desde la pantalla pública de la aplicación, iniciar sesión, consultar clases disponibles y solicitar reservas.

También puede revisar el historial de sus reservas, consultar sus estados, cancelar reservas cuando corresponda y actualizar su información personal.

Su panel principal presenta estadísticas de participación y la próxima clase confirmada, cuando existe una reserva futura.

## 5. Funcionalidades implementadas

Durante el segundo avance se desarrollaron las siguientes funcionalidades:

1. Inicio y cierre de sesión mediante Spring Security.
2. Control de acceso diferenciado por roles.
3. Registro público de clientes.
4. Cifrado de contraseñas mediante BCrypt.
5. Administración de clientes y entrenadores.
6. Administración de categorías y ejercicios.
7. Registro, edición y cancelación de clases.
8. Consulta de agenda semanal del entrenador.
9. Visualización de clases disponibles para clientes.
10. Solicitud y consulta de reservas.
11. Confirmación y rechazo de solicitudes por entrenadores.
12. Consulta de asistentes confirmados.
13. Actualización del perfil personal del cliente.
14. Paneles personalizados para los tres roles.
15. Indicadores estadísticos y accesos rápidos.
16. Validaciones de formularios y mensajes de confirmación.
17. Persistencia de información mediante MySQL.

## 6. Base de datos

El sistema utiliza una base de datos MySQL denominada:

`fidness_web`

Las tablas principales son:

- `roles`
- `usuarios`
- `clientes`
- `entrenadores`
- `categorias`
- `ejercicios`
- `clases`
- `reservas`

Las relaciones entre estas tablas permiten gestionar la información de los usuarios, los entrenamientos programados y las reservas realizadas por los clientes.

### 6.1. Archivo de la base de datos

Se incluye la exportación SQL de la base de datos, que contiene su estructura y los registros de prueba utilizados durante el desarrollo.

**Archivo:** `01_FidnessWeb_BaseDatos.sql`

### 6.2. Archivo de consultas de prueba

Se incluye un archivo SQL adicional con consultas utilizadas para comprobar la información almacenada y verificar las operaciones realizadas.

**Archivo:** `02_FidnessWeb_Pruebas.sql`

## 7. Requisitos para ejecutar el proyecto

Para ejecutar Fidness Web se recomienda contar con:

- JDK 17.
- MySQL Server.
- MySQL Workbench, opcional para administrar la base de datos.
- Apache NetBeans o un IDE compatible con proyectos Maven.
- Maven, o Maven Wrapper si está incluido en el proyecto.
- Conexión a Internet para cargar los recursos externos de Bootstrap.

## 8. Instalación y configuración

### Paso 1. Extraer el proyecto

Descomprimir el archivo ZIP del segundo avance en una carpeta local.

### Paso 2. Importar la base de datos

Abrir MySQL Workbench y establecer una conexión con el servidor MySQL.

Seleccionar **Server → Data Import** y utilizar la opción **Import from Self-Contained File** para importar `01_FidnessWeb_BaseDatos.sql`.

Verificar que la base de datos `fidness_web` y sus tablas se encuentren disponibles.

### Paso 3. Configurar la conexión

Abrir el archivo:

`src/main/resources/application.properties`

Comprobar que la configuración de conexión corresponda con el servidor MySQL utilizado.

La aplicación debe apuntar a la base de datos `fidness_web`. Es necesario establecer las credenciales locales de MySQL correspondientes al equipo donde se ejecutará el proyecto.

### Paso 4. Abrir el proyecto en NetBeans

Abrir Apache NetBeans y seleccionar **File → Open Project**.

Buscar la carpeta de Fidness Web y abrir el proyecto Maven.

Esperar a que el entorno cargue las dependencias configuradas en `pom.xml`.

### Paso 5. Compilar y ejecutar

Ejecutar la opción **Clean and Build** para comprobar que el proyecto compile correctamente.

Posteriormente, ejecutar la aplicación desde NetBeans.

### Paso 6. Acceder a la aplicación

Con el servidor iniciado y utilizando la configuración de puerto empleada durante el desarrollo, abrir:

`http://localhost:8081/login`

Desde esta pantalla se puede iniciar sesión con una cuenta existente o utilizar la opción **Regístrate aquí** para crear una nueva cuenta de cliente.

## 9. Pruebas realizadas

Durante el desarrollo se efectuaron pruebas funcionales sobre los módulos implementados.

Se verificaron operaciones de registro, consulta, edición, cambios de estado, inicio y cierre de sesión, programación de clases, solicitudes de reserva, confirmación y rechazo de solicitudes, actualización de perfiles y visualización de indicadores.

También se utilizaron consultas SQL para comprobar que las operaciones realizadas desde las interfaces se reflejaran correctamente en la base de datos.

Las evidencias correspondientes a estas pruebas se presentan en el documento:

**Avance #2 Proyecto Final - Capturas de evidencia.pdf**

Este documento contiene capturas de las interfaces, operaciones ejecutadas y comprobaciones realizadas en MySQL.

## 10. Seguridad

Fidness Web utiliza Spring Security para gestionar la autenticación y restringir el acceso a los diferentes módulos según el rol asignado.

Las contraseñas se almacenan mediante un mecanismo de cifrado hash basado en BCrypt.

También se utiliza protección CSRF en los formularios y se aplican validaciones para reducir errores durante el procesamiento de información.

Las cuentas pueden administrarse mediante estados de activación y desactivación, lo que permite controlar su disponibilidad.

## 11. Estado actual del proyecto

El sistema se encuentra en la etapa correspondiente al segundo avance del proyecto final.

Se han implementado funcionalidades esenciales para los roles administrador, entrenador y cliente, incluyendo la integración entre los módulos de clases y reservas.

El desarrollo continuará con las historias de usuario pendientes, la ampliación de pruebas, el fortalecimiento de las validaciones y las mejoras necesarias para completar la versión final.

El porcentaje exacto de historias de usuario completadas debe contrastarse con el listado de requerimientos definido en el primer avance.

## 12. Observaciones

El proyecto fue desarrollado y probado en un entorno local.

Para ejecutarlo en otro equipo es necesario importar la base de datos, configurar las credenciales de conexión MySQL y verificar que el puerto utilizado por la aplicación se encuentre disponible.

Los registros incluidos en la exportación SQL tienen fines de desarrollo y demostración.

Se recomienda utilizar cuentas de prueba y evitar incorporar información personal real o credenciales sensibles en el entregable.

---

**Universidad Fidélitas**  
**Desarrollo de Aplicaciones Web y Patrones**  
**Proyecto Final – Segundo Avance**  
**Fidness Web – Grupo #10**  
**2026**