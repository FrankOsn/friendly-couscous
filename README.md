# friendly-couscous

Un proyecto Python para descargar y gestionar imágenes de la **NASA Astronomy Picture of the Day (APOD)** API con interfaz CLI interactiva.

## 📋 Descripción

Este proyecto permite:
- ✅ Descargar imágenes APOD por fecha específica
- ✅ Descargar múltiples APOD en rangos de fechas
- ✅ Guardar metadata (título, descripción, fecha) en JSON
- ✅ Visualizar imágenes descargadas con información completa
- ✅ Menú interactivo CLI fácil de usar
- ✅ Manejo seguro de API keys con variables de entorno

## 🚀 Inicio Rápido

### 1. Clonar repositorio
```bash
git clone https://github.com/FrankOsn/friendly-couscous.git
cd friendly-couscous
```

### 2. Crear virtual environment
```bash
python3 -m venv venv
source venv/bin/activate  # En macOS/Linux
# o en Windows:
# venv\Scripts\Activate
```

### 3. Instalar dependencias
```bash
cd APOD
pip install -r requirements.txt
```

### 4. Configurar API key
Crea un archivo `.env` en la carpeta `APOD/`:
```
NASA_API_KEY=tu_clave_aqui
```

Obtén tu API key gratuita en: https://api.nasa.gov/

### 5. Ejecutar CLI
```bash
python3 cli.py
```

## 📱 Menú Principal

```
╔================================================╗
║     NASA APOD - Descargador Local              ║
╚================================================╝

1. Descargar APOD por fecha específica
2. Descargar APOD en rango de fechas
3. Ver imágenes descargadas
4. Descargar APOD de hoy
5. Salir
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
APOD/
├── app.py              # Módulo con funciones de descarga y API
├── cli.py              # Interfaz CLI con menú interactivo
├── requirements.txt    # Dependencias Python
├── pytest.ini          # Configuración de pytest
├── test_app.py         # Tests unitarios (9 tests ✅)
├── test_save_image.py  # Tests de guardado de imágenes
├── .env                # Variables de entorno (no se sube a Git)
└── images/             # Carpeta de imágenes descargadas
    ├── *.jpg/gif       # Imágenes APOD
    └── *.json          # Metadata de cada imagen
```

## 🔐 Seguridad

- **API Key protegida**: Se usa `.env` para variables sensibles
- **`.gitignore` configurado**: Excluye `.env`, `images/`, `venv/`, `__pycache__/`
- **GitHub Secrets**: Para CI/CD, configura `NASA_API_KEY` en Settings → Secrets

## 🧪 Tests

Ejecutar todos los tests:
```bash
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

## 📦 Dependencias

```
requests==2.31.0       # Para peticiones HTTP
python-dotenv==1.0.0   # Para variables de entorno
pytest==9.0.1          # Para testing
pytest-dotenv==0.5.2   # Para tests con .env
```

## 🔄 Flujo de uso típico

1. **Ejecutar**: `python3 cli.py`
2. **Seleccionar opción**: Ej. `1` para descargar por fecha
3. **Ingresar fecha**: `2025-12-02`
4. **Esperar descarga**: La imagen se guarda en `APOD/images/`
5. **Ver metadata**: Se crea automáticamente un archivo `.json` con info

### Ejemplo de metadata guardada:
```json
{
  "title": "M77: Spiral Galaxy with an Active Center",
  "explanation": "This stunning galaxy contains...",
  "date": "2025-12-02",
  "media_type": "image",
  "url": "https://apod.nasa.gov/apod/image/...",
  "hdurl": "https://apod.nasa.gov/apod/image/...",
  "image_path": "/path/to/APOD/images/M77_Hubble_3681.jpg"
}
```

## 🛠️ Desarrollo

### Agregar nuevas funcionalidades

Las funciones principales en `app.py`:
- `fetch_apod(date)` - Obtiene datos de la API
- `extract_image_url(data)` - Extrae URL según tipo de media
- `download_image(url, date)` - Descarga imagen
- `save_metadata(data, path)` - Guarda metadata JSON
- `download_apod(date)` - Orquesta el proceso completo

### Extender CLI

Agregar nuevas opciones en `cli.py`:
1. Crear función `option_nueva_funcionalidad()`
2. Agregar opción en `show_menu()`
3. Llamar en `main()`
4. Agregar tests correspondientes

## 📝 Notas

- Las imágenes se guardan en alta resolución (`hdurl`) cuando está disponible
- Para videos, se descarga la miniatura
- Se evitan descargas duplicadas (verifica si archivo ya existe)
- Manejo robusto de errores de red y fechas inválidas

## 🔗 Recursos

- **NASA APOD API**: https://api.nasa.gov/
- **Documentación API**: https://github.com/nasa/apod-api
- **Solicitar API Key**: https://api.nasa.gov/

## 📄 Licencia

Proyecto educativo. Respeta los términos de uso de NASA APOD API.

---

**Creado por**: FrankOsn  
**Última actualización**: Diciembre 2, 2025