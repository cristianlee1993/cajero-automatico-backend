🏦 Cajero Automático - Sistema Completo
https://img.shields.io/badge/Angular-20-DD0031?style=for-the-badge&logo=angular
https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=springboot
https://img.shields.io/badge/Java-20-ED8B00?style=for-the-badge&logo=java
https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql

Sistema completo de cajero automático desarrollado con Angular 20 (Frontend) y Spring Boot 3 + Java 20 (Backend), con base de datos MySQL.

✨ Características Principales
💻 Frontend (Angular)
Interfaz moderna y responsive

Validación en tiempo real de montos

Manejo de estados reactivo

Experiencia de usuario optimizada

Soporte para decimales (.00 y .50)

🚀 Backend (Spring Boot)
API RESTful completa

Algoritmo inteligente de denominaciones

Persistencia en MySQL con JPA

Manejo de transacciones

Validación de reglas de negocio

🗃️ Base de Datos
Esquema optimizado para operaciones de cajero

Procedimientos almacenados para operaciones comunes

Vistas para reportes

Historial completo de transacciones

🏗️ Arquitectura del Sistema
text
cajero-automatico/
├── 📁 frontend/                 # Aplicación Angular
│   ├── src/app/
│   │   ├── components/cajero/   # Componente principal
│   │   ├── services/           # Servicios API
│   │   └── models/             # Interfaces TypeScript
│   └── angular.json
│
├── 📁 backend/                  # Aplicación Spring Boot
│   ├── src/main/java/com/baz/cajero/
│   │   ├── controller/         # Controladores REST
│   │   ├── service/            # Lógica de negocio
│   │   ├── repository/         # Acceso a datos
│   │   ├── entity/            # Entidades JPA
│   │   └── dto/               # Objetos transferencia
│   └── pom.xml
│
└── 🗃️ database/                # Scripts MySQL
    ├── schema.sql             # Esquema de base de datos
    ├── data.sql              # Datos iniciales
    └── procedures.sql        # Procedimientos almacenados
🚀 Instalación y Configuración
Prerrequisitos
Node.js 18+ y npm

Java 20

MySQL 8.0+

Angular CLI 20

Maven 3.6+

1. Clonar el Repositorio
bash
git clone https://github.com/tu-usuario/cajero-automatico.git
cd cajero-automatico
2. Configurar la Base de Datos
Crear la base de datos:
sql
CREATE DATABASE cajero_baz;
USE cajero_baz;

👨‍💻 Autor
Cristian Alonso Lee
