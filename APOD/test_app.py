import pytest
import os
from dotenv import load_dotenv

load_dotenv()

def test_api_key_exists():
    assert os.getenv("NASA_API_KEY") is not None

def test_url_format():
    api_key = os.getenv("NASA_API_KEY")
    url = f"https://api.nasa.gov/planetary/apod?api_key={api_key}&thumbs=True&date=1996-01-11"
    assert api_key in url
    assert "api.nasa.gov" in url