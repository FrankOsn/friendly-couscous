# friendly-couscous

Un proyecto **dual** (Python 3 + Java 21 LTS) para descargar y gestionar imágenes de la **NASA Astronomy Picture of the Day (APOD)** API con interfaz CLI interactiva.

## 📋 Descripción

Este proyecto permite:
- ✅ Descargar imágenes APOD por fecha específica
- ✅ Descargar múltiples APOD en rangos de fechas
- ✅ Guardar metadata (título, descripción, fecha) en JSON
- ✅ Visualizar imágenes descargadas con información completa
- ✅ Menú interactivo CLI fácil de usar
- ✅ Manejo seguro de API keys con variables de entorno
- ✅ **Disponible en dos lenguajes**: Python 3 y Java 21 LTS

## 🚀 Inicio Rápido

### Opción A: Python 3 (APOD/)

#### 1. Navegar a la carpeta Python
```bash
cd APOD
```

#### 2. Crear virtual environment
```bash
python3 -m venv venv
source venv/bin/activate  # En macOS/Linux
# o en Windows:
# venv\Scripts\Activate
```

#### 3. Instalar dependencias
```bash
pip install -r requirements.txt
```

#### 4. Configurar API key
Crea un archivo `.env`:
```
NASA_API_KEY=tu_clave_aqui
```

#### 5. Ejecutar CLI
```bash
python3 cli.py
```

---

### Opción B: Java 21 LTS (APOD_JAVA/)

#### 1. Requisitos previos
- Java 21 LTS instalado
- NASA API key configurada

#### 2. Configurar API key
```bash
export NASA_API_KEY="tu_clave_aqui"
# O crear un archivo .env en APOD_JAVA/
```

#### 3. Ejecutar desde APOD_JAVA
```bash
cd APOD_JAVA
java -jar target/apod-downloader-1.0.0-jar-with-dependencies.jar
```

Si aún no está compilado:
```bash
cd APOD_JAVA
mvn clean package -DskipTests
java -jar target/apod-downloader-1.0.0-jar-with-dependencies.jar
```

---

## 📱 Menú Principal

Ambas versiones (Python y Java) comparten la misma interfaz:

```
╔================================================╗
║     NASA APOD - Local Downloader               ║
╚================================================╝

1. Download APOD by specific date
2. Download APOD for a date range
3. View downloaded images
4. Download today's APOD
5. Exit
```

## 🔧 Opciones del CLI

### Opción 1: Descargar por fecha específica
- Ingresa una fecha en formato `YYYY-MM-DD`
- Descarga la imagen en alta resolución
- Guarda metadata automáticamente

### Opción 2: Descargar rango de fechas
- Especifica fecha inicial y final
- Descarga todas las imágenes del rango
- Alerta si intenta descargar > 100 imágenes
- Muestra progreso de descarga

### Opción 3: Ver imágenes descargadas
- Lista todas las imágenes descargadas
- Muestra tamaño del archivo en KB
- Muestra fecha y título del APOD
- Metadata en archivos `.json` adjuntos

### Opción 4: Descargar APOD de hoy
- Descarga automáticamente el APOD del día actual
- Muestra título y metadata

### Opción 5: Salir
- Cierra la aplicación

## 📁 Estructura del Proyecto

```
friendly-couscous/
├── APOD/                       # Versión Python 3
│   ├── app.py                  # Módulo con funciones de descarga
│   ├── cli.py                  # Interfaz CLI interactiva
│   ├── requirements.txt        # Dependencias Python
│   ├── pytest.ini              # Configuración de pytest
│   ├── test_app.py             # Tests unitarios (9 tests ✅)
│   ├── test_save_image.py      # Tests de guardado
│   ├── .env                    # Variables de entorno
│   └── images/                 # Imágenes descargadas
│
├── APOD_JAVA/                  # Versión Java 21 LTS
│   ├── pom.xml                 # Configuración Maven
│   ├── src/
│   │   └── main/java/com/frankosn/apod/
│   │       ├── ApodApplication.java        # Punto de entrada
│   │       ├── cli/ApodCli.java            # Interfaz CLI
│   │       ├── service/ApodService.java    # Integración NASA API
│   │       ├── storage/ApodStorage.java    # Persistencia JSON
│   │       ├── config/ConfigLoader.java    # Configuración
│   │       ├── model/
│   │       │   ├── ApodData.java
│   │       │   ├── ApodMetadata.java
│   │       │   └── MediaType.java
│   │       └── util/
│   │           ├── DateUtil.java
│   │           └── FileManager.java
│   ├── target/
│   │   └── apod-downloader-1.0.0-jar-with-dependencies.jar
│   ├── .env                    # Variables de entorno
│   └── images/                 # Imágenes descargadas
│
├── README.md                   # Este archivo
├── JAVA_MIGRATION_ANALYSIS.md  # Análisis de migración
└── .gitignore                  # Excluye .env, imágenes, etc.
```

## 🔐 Seguridad

- **API Key protegida**: Se usa `.env` para variables sensibles
- **`.gitignore` configurado**: Excluye `.env`, `images/`, `venv/`, `__pycache__/`, `target/`
- **GitHub Secrets**: Para CI/CD, configura `NASA_API_KEY` en Settings → Secrets

## 🧪 Tests

### Python (APOD/)
```bash
cd APOD
pytest test_app.py test_save_image.py -v
```

**Cobertura de tests (9 tests ✅):**
- ✅ Validación de API key
- ✅ Extracción de URLs de imágenes
- ✅ Manejo de videos (fallback a thumbnail)
- ✅ Descarga de imágenes
- ✅ Guardado de metadata
- ✅ Validación de formatos de fecha
- ✅ Manejo de errores

### Java (APOD_JAVA/)
```bash
cd APOD_JAVA
mvn clean test
```

## 📦 Dependencias

### Python (APOD/)
```
requests==2.31.0       # Para peticiones HTTP
python-dotenv==1.0.0   # Para variables de entorno
pytest==9.0.1          # Para testing
pytest-dotenv==0.5.2   # Para tests con .env
```

### Java (APOD_JAVA/)
```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.11.0</version>
</dependency>
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.12</version>
</dependency>
```

## 🔄 Flujo de uso típico

### Python
1. **Ejecutar**: `python3 cli.py`
2. **Seleccionar opción**: Ej. `1` para descargar por fecha
3. **Ingresar fecha**: `2025-12-02`
4. **Esperar descarga**: La imagen se guarda en `APOD/images/`
5. **Ver metadata**: Se crea automáticamente un archivo `.json` con info

### Java
1. **Ejecutar**: `java -jar target/apod-downloader-1.0.0-jar-with-dependencies.jar`
2. **Seleccionar opción**: Ej. `4` para descargar APOD de hoy
3. **Esperar descarga**: La imagen se guarda en `APOD_JAVA/images/`
4. **Ver metadata**: Se crea automáticamente un archivo `.json` con info

### Ejemplo de metadata (idéntico en ambas versiones):
```json
{
  "title": "M77: Spiral Galaxy with an Active Center",
  "explanation": "This stunning galaxy contains...",
  "date": "2025-12-02",
  "media_type": "image",
  "url": "https://apod.nasa.gov/apod/image/...",
  "hdurl": "https://apod.nasa.gov/apod/image/...",
  "image_path": "images/M77_Hubble_3681.jpg"
}
```

## 🛠️ Desarrollo

### Python (APOD/)

Las funciones principales en `app.py`:
- `fetch_apod(date)` - Obtiene datos de la API
- `extract_image_url(data)` - Extrae URL según tipo de media
- `download_image(url, date)` - Descarga imagen
- `save_metadata(data, path)` - Guarda metadata JSON
- `download_apod(date)` - Orquesta el proceso completo

### Java (APOD_JAVA/)

Clases principales:
- `ApodApplication` - Punto de entrada
- `ApodCli` - Interfaz CLI interactiva
- `ApodService` - Integración con NASA API (OkHttp3)
- `FileManager` - Gestión de descargas y archivos
- `ApodStorage` - Persistencia de metadata (Gson)
- `ConfigLoader` - Carga de configuración (.env)
- `DateUtil` - Validación y formato de fechas

## 📝 Notas Técnicas

### Versión Python
- Usa `requests` para peticiones HTTP
- Manejo de variables de entorno con `python-dotenv`
- Tests con `pytest`

### Versión Java 21
- Compilada con Java 21 LTS
- Fat JAR con todas las dependencias incluidas
- Usa `OkHttp3` para peticiones HTTP
- Usa `Gson` para serialización JSON
- Manejo de variables de entorno con `dotenv-java`
- Logging con `SLF4J + Logback`
- Construcción con Maven

### Características comunes
- Las imágenes se guardan en alta resolución (`hdurl`) cuando está disponible
- Para videos, se descarga la miniatura
- Se evitan descargas duplicadas (verifica si archivo ya existe)
- Manejo robusto de errores de red y fechas inválidas

## 🔗 Recursos

- **NASA APOD API**: https://api.nasa.gov/
- **Documentación API**: https://github.com/nasa/apod-api
- **Solicitar API Key**: https://api.nasa.gov/
- **Java 21 LTS**: https://www.oracle.com/java/technologies/downloads/

## 📄 Licencia

Proyecto educativo. Respeta los términos de uso de NASA APOD API.

---

**Creado por**: FrankOsn  
**Última actualización**: Diciembre 2, 2025  
**Versiones**: Python 3 ✅ | Java 21 LTS ✅