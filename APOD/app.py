import os
import sys
import json
import requests
from datetime import datetime, timedelta
from dotenv import load_dotenv

load_dotenv()

API_KEY = os.getenv("NASA_API_KEY")
BASE_URL = "https://api.nasa.gov/planetary/apod"
IMAGES_DIR = os.path.join(os.path.dirname(__file__), "images")


def get_api_key():
	"""Obtiene la API key y valida que exista"""
	if not API_KEY:
		raise ValueError("NASA_API_KEY no está configurada. Coloca la clave en .env o en las variables de entorno.")
	return API_KEY


def fetch_apod(date: str) -> dict:
	"""Obtiene los datos del APOD para una fecha específica"""
	api_key = get_api_key()
	params = {
		"api_key": api_key,
		"date": date,
		"thumbs": "True"
	}
	resp = requests.get(BASE_URL, params=params)
	resp.raise_for_status()
	return resp.json()


def extract_image_url(data: dict) -> str:
	"""Extrae la URL de la imagen según el tipo de media"""
	media_type = data.get("media_type")
	
	if media_type == "image":
		return data.get("hdurl") or data.get("url")
	elif media_type == "video":
		return data.get("thumbnail_url") or data.get("url")
	
	return None


def download_image(image_url: str, date: str) -> str:
	"""Descarga la imagen y retorna la ruta del archivo guardado"""
	os.makedirs(IMAGES_DIR, exist_ok=True)
	
	filename = os.path.basename(image_url.split("?")[0])
	if not filename:
		filename = f"apod_{date}.jpg"
	
	filepath = os.path.join(IMAGES_DIR, filename)
	
	# Evitar descargar si ya existe
	if os.path.exists(filepath):
		return filepath
	
	with requests.get(image_url, stream=True) as r:
		r.raise_for_status()
		with open(filepath, "wb") as f:
			for chunk in r.iter_content(chunk_size=8192):
				if chunk:
					f.write(chunk)
	
	return filepath


def save_metadata(data: dict, image_path: str) -> None:
	"""Guarda metadata del APOD en un archivo JSON"""
	metadata = {
		"title": data.get("title"),
		"explanation": data.get("explanation"),
		"date": data.get("date"),
		"media_type": data.get("media_type"),
		"url": data.get("url"),
		"hdurl": data.get("hdurl"),
		"image_path": image_path
	}
	
	metadata_path = image_path.rsplit(".", 1)[0] + ".json"
	
	with open(metadata_path, "w", encoding="utf-8") as f:
		json.dump(metadata, f, indent=2, ensure_ascii=False)


def download_apod(date: str) -> tuple:
	"""Descarga el APOD de una fecha y guarda imagen + metadata"""
	try:
		data = fetch_apod(date)
		image_url = extract_image_url(data)
		
		if not image_url:
			return None, f"No se encontró imagen para {date}"
		
		image_path = download_image(image_url, date)
		save_metadata(data, image_path)
		
		return image_path, f"✓ Descargado: {os.path.basename(image_path)}"
	except requests.exceptions.RequestException as e:
		return None, f"Error de conexión: {e}"
	except Exception as e:
		return None, f"Error: {e}"


if __name__ == "__main__":
	# Mantiene compatibilidad con ejecución directa
	date = os.getenv("APOD_DATE", "1996-01-11")
	image_path, msg = download_apod(date)
	print(msg)
	if image_path:
		print(f"Guardado en: {image_path}")
