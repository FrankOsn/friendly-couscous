import pytest
import os
import json
from unittest.mock import patch, MagicMock
from dotenv import load_dotenv
import app

load_dotenv()

def test_api_key_exists():
    """Test que la API key está configurada"""
    assert app.API_KEY is not None

def test_extract_image_url_image():
    """Test que extrae hdurl para imágenes"""
    data = {
        "media_type": "image",
        "url": "https://example.com/image.jpg",
        "hdurl": "https://example.com/image_hd.jpg"
    }
    url = app.extract_image_url(data)
    assert url == "https://example.com/image_hd.jpg"

def test_extract_image_url_video():
    """Test que extrae thumbnail_url para videos"""
    data = {
        "media_type": "video",
        "thumbnail_url": "https://example.com/thumb.jpg",
        "url": "https://example.com/video.mp4"
    }
    url = app.extract_image_url(data)
    assert url == "https://example.com/thumb.jpg"

def test_extract_image_url_none():
    """Test que retorna None si no hay media válida"""
    data = {
        "media_type": "unknown"
    }
    url = app.extract_image_url(data)
    assert url is None

def test_save_metadata(tmp_path):
    """Test que guarda metadata en JSON"""
    test_image_path = tmp_path / "test.jpg"
    test_image_path.write_text("fake image")
    
    data = {
        "title": "Test APOD",
        "explanation": "This is a test",
        "date": "2025-12-02",
        "media_type": "image",
        "url": "https://example.com/image.jpg",
        "hdurl": "https://example.com/image_hd.jpg"
    }
    
    app.save_metadata(data, str(test_image_path))
    
    metadata_path = str(test_image_path).rsplit(".", 1)[0] + ".json"
    assert os.path.exists(metadata_path)
    
    with open(metadata_path, "r") as f:
        saved = json.load(f)
    
    assert saved["title"] == "Test APOD"
    assert saved["date"] == "2025-12-02"

def test_download_image(tmp_path, monkeypatch):
    """Test que descarga y guarda la imagen"""
    
    monkeypatch.setattr(app, "IMAGES_DIR", str(tmp_path))
    
    mock_image_bytes = b"fake_image_content"
    
    mock_response = MagicMock()
    mock_response.iter_content.return_value = [mock_image_bytes]
    mock_response.__enter__.return_value = mock_response
    mock_response.__exit__.return_value = None
    
    with patch('app.requests.get', return_value=mock_response):
        filepath = app.download_image("https://example.com/image.jpg", "2025-12-02")
    
    assert os.path.exists(filepath)
    with open(filepath, "rb") as f:
        assert f.read() == mock_image_bytes
