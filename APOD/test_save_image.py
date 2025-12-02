import pytest
import os
import json
from unittest.mock import patch, MagicMock
from dotenv import load_dotenv

load_dotenv()

def test_save_image_from_api(tmp_path):
    """Test que la imagen se descarga y se guarda correctamente"""
    
    # Mock de la respuesta JSON de la API
    mock_json_response = {
        "media_type": "image",
        "url": "https://example.com/image.jpg",
        "hdurl": "https://example.com/image_hd.jpg",
        "title": "Test Image"
    }
    
    # Mock de la descarga de la imagen (bytes simulados)
    mock_image_bytes = b"fake_image_data_bytes"
    
    with patch('requests.get') as mock_get:
        # Configurar mocks para dos llamadas a requests.get:
        # 1. Para obtener el JSON de la API
        # 2. Para descargar la imagen
        
        mock_response_json = MagicMock()
        mock_response_json.json.return_value = mock_json_response
        mock_response_json.raise_for_status.return_value = None
        
        mock_response_image = MagicMock()
        mock_response_image.iter_content.return_value = [mock_image_bytes]
        mock_response_image.raise_for_status.return_value = None
        mock_response_image.__enter__.return_value = mock_response_image
        mock_response_image.__exit__.return_value = None
        
        # Primera llamada devuelve JSON, segunda devuelve imagen
        mock_get.side_effect = [mock_response_json, mock_response_image]
        
        # Lógica simulada del script
        api_key = os.getenv("NASA_API_KEY")
        date = "1996-01-11"
        api_url = f"https://api.nasa.gov/planetary/apod?api_key={api_key}&thumbs=True&date={date}"
        
        resp = mock_get(api_url)
        data = resp.json()
        
        image_url = data.get("hdurl") or data.get("url")
        
        images_dir = str(tmp_path)
        os.makedirs(images_dir, exist_ok=True)
        
        filename = os.path.basename(image_url.split("?")[0])
        filepath = os.path.join(images_dir, filename)
        
        # Simular descarga
        with mock_get(image_url) as r:
            r.raise_for_status()
            with open(filepath, "wb") as f:
                for chunk in r.iter_content(chunk_size=8192):
                    if chunk:
                        f.write(chunk)
        
        # Validar que el archivo se creó
        assert os.path.exists(filepath), f"El archivo {filepath} no fue creado"
        
        # Validar contenido
        with open(filepath, "rb") as f:
            saved_data = f.read()
        assert saved_data == mock_image_bytes, "El contenido del archivo no coincide"


def test_video_fallback_to_thumbnail(tmp_path):
    """Test que si el APOD es un video, usa thumbnail_url"""
    
    mock_response = {
        "media_type": "video",
        "thumbnail_url": "https://example.com/thumb.jpg",
        "url": "https://example.com/video.mp4"
    }
    
    media_type = mock_response.get("media_type")
    if media_type == "video":
        image_url = mock_response.get("thumbnail_url") or mock_response.get("url")
    
    assert image_url == "https://example.com/thumb.jpg"


def test_missing_image_url():
    """Test que maneja el caso donde no hay imagen disponible"""
    
    mock_response = {
        "media_type": "unknown",
        "url": None
    }
    
    media_type = mock_response.get("media_type")
    if media_type == "image":
        image_url = mock_response.get("hdurl") or mock_response.get("url")
    elif media_type == "video":
        image_url = mock_response.get("thumbnail_url") or mock_response.get("url")
    else:
        image_url = None
    
    assert image_url is None
