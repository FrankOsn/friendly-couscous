import os
import sys
import requests
from dotenv import load_dotenv

load_dotenv()

api_key = os.getenv("NASA_API_KEY")
if not api_key:
	print("NASA_API_KEY no está configurada. Coloca la clave en .env o en las variables de entorno.")
	sys.exit(1)

date = os.getenv("APOD_DATE", "1996-01-11")
api_url = f"https://api.nasa.gov/planetary/apod?api_key={api_key}&thumbs=True&date={date}"

resp = requests.get(api_url)
resp.raise_for_status()
data = resp.json()

media_type = data.get("media_type")
if media_type == "image":
	image_url = data.get("hdurl") or data.get("url")
elif media_type == "video":
	image_url = data.get("thumbnail_url") or data.get("url")
else:
	image_url = None

if not image_url:
	print("No se encontró una imagen para guardar.")
	print(data)
	sys.exit(1)

images_dir = os.path.join(os.path.dirname(__file__), "images")
os.makedirs(images_dir, exist_ok=True)

filename = os.path.basename(image_url.split("?")[0])
if not filename:
	filename = f"apod_{date}.jpg"

filepath = os.path.join(images_dir, filename)

with requests.get(image_url, stream=True) as r:
	r.raise_for_status()
	with open(filepath, "wb") as f:
		for chunk in r.iter_content(chunk_size=8192):
			if chunk:
				f.write(chunk)

print(f"Imagen guardada en: {filepath}")
