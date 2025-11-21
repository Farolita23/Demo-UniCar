PROYECTO: UniCar - Aplicación Web Proyecto Integrado 2025/26
1. INTRODUCCIÓN
UniCar es una aplicación web desarrollada bajo una arquitectura de servicios desacoplados (Frontend y Backend). El proyecto está diseñado para alojar una aplicacion en tiempo real que facilite el echo de encontrar medio de transporte para un centro de estudios en sevilla.
Esta documentación provee las instrucciones necesarias para la configuración, instalación y ejecución del proyecto en un entorno de desarrollo local.
2. ARQUITECTURA Y TECNOLOGÍAS
El proyecto UniCar se basa en las siguientes tecnologías:

Backend
  Java
  Spring Boot (Maven)

Frontend
  JavaScript
  Angular


MySQL base de datos relacional.


Servidor Local
  Apache/MySQL
  XAMPP
Entorno de desarrollo para la base de datos.
  phpMyAdmin/hibernate

3. REQUISITOS DEL SISTEMA
Para una correcta instalación y ejecución de UniCar, se requieren las siguientes herramientas y dependencias:
Java Development Kit (JDK): Versión 17 o superior.
Apache Maven: Versión 3.8.x o superior.
Node.js y npm: Versión LTS recomendada.
Angular CLI: Se instala globalmente (npm install -g @angular/cli).
XAMPP: Paquete de servidor local (Apache y MySQL).
Git: Para la clonación del repositorio.


5. GUÍA DE INSTALACIÓN Y CONFIGURACIÓN
Siga los siguientes pasos secuenciales para configurar el entorno de desarrollo.
4.1. Configuración de la Base de Datos (XAMPP/MySQL)
Iniciar XAMPP:
Abra el Panel de Control de XAMPP e inicie los módulos Apache y MySQL.
Creación de la Base de Datos:
Acceda a phpMyAdmin a través de http://localhost/phpmyadmin.
Cree una nueva base de datos con el nombre base_datos_UniCar.
La estructura de las tablas será generada automáticamente por Hibernate al iniciar el backend.
4.2. Instalación del Backend (Spring Boot)
El backend aloja la lógica de negocio y los servicios REST que consume el frontend.
Clonar el Repositorio:
Bash
git clone https://github.com/Farolita23/Demo-UniCar.git
cd dondeEstes/backend


Verificación de la Configuración:
Localice el archivo de configuración (src/main/resources/application.properties o application.yml).
Asegúrese de que los parámetros de conexión a MySQL sean correctos:
Properties
spring.datasource.url=jdbc:mysql://localhost:3306/base_datos_UniCar?useSSL=false&serverTimezone=UTC
spring.datasource.username=usuario
spring.datasource.password=usuario
spring.jpa.hibernate.ddl-auto=update


Compilación y Ejecución:
Ejecute los siguientes comandos utilizando Maven en el directorio raíz del backend:
Bash
mvn clean install
mvn spring-boot:run
El servicio API estará disponible en http://localhost:8080.
4.3. Instalación del Frontend (Angular)
El frontend es la interfaz de usuario de UniCar.
Navegar al Directorio:
Abra una nueva terminal y navegue al directorio del frontend:
Bash
cd dondeEste/frontend


Instalar Dependencias:
Instale las dependencias de Node.js requeridas:
Bash
npm install


Verificación de la URL de la API:
Localice el archivo de configuración del entorno (src/environments/environment.ts).
Confirme que la variable que apunta al backend coincida con la dirección del servicio Spring Boot:
TypeScript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api' 
};


Ejecutar la Aplicación:
Inicie el servidor de desarrollo de Angular:
Bash
ng serve
La aplicación web será accesible en http://localhost:4200.
5. ACCESO Y USO
Una vez que el servidor Spring Boot (puerto 8080) y el servidor Angular (puerto 4200) estén activos, la aplicación UniCar estará operativa y disponible a través del navegador web en la siguiente dirección:
URL de Acceso: http://localhost:4200

