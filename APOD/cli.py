#!/usr/bin/env python3
import os
import json
from datetime import datetime, timedelta
from app import download_apod, IMAGES_DIR


def validate_date(date_str: str) -> bool:
	"""Valida formato de fecha YYYY-MM-DD"""
	try:
		datetime.strptime(date_str, "%Y-%m-%d")
		return True
	except ValueError:
		return False


def option_download_by_date():
	"""Opción 1: Descargar APOD por fecha específica"""
	print("\n" + "="*50)
	print("Descargar APOD por fecha")
	print("="*50)
	
	while True:
		date = input("Ingresa la fecha (YYYY-MM-DD) o 'volver': ").strip()
		
		if date.lower() == "volver":
			return
		
		if not validate_date(date):
			print("❌ Formato inválido. Usa YYYY-MM-DD")
			continue
		
		print(f"Descargando APOD de {date}...")
		image_path, msg = download_apod(date)
		print(msg)
		
		if image_path:
			print(f"📁 Guardado en: {image_path}")
			
			# Mostrar metadata si existe
			metadata_path = image_path.rsplit(".", 1)[0] + ".json"
			if os.path.exists(metadata_path):
				with open(metadata_path, "r", encoding="utf-8") as f:
					metadata = json.load(f)
					print(f"\n📋 Título: {metadata.get('title', 'N/A')}")
		
		break


def option_download_range():
	"""Opción 2: Descargar APODs en un rango de fechas"""
	print("\n" + "="*50)
	print("Descargar APOD - Rango de fechas")
	print("="*50)
	
	while True:
		start_date = input("Fecha inicial (YYYY-MM-DD) o 'volver': ").strip()
		
		if start_date.lower() == "volver":
			return
		
		if not validate_date(start_date):
			print("❌ Formato inválido. Usa YYYY-MM-DD")
			continue
		
		end_date = input("Fecha final (YYYY-MM-DD): ").strip()
		
		if not validate_date(end_date):
			print("❌ Formato inválido. Usa YYYY-MM-DD")
			continue
		
		start = datetime.strptime(start_date, "%Y-%m-%d")
		end = datetime.strptime(end_date, "%Y-%m-%d")
		
		if start > end:
			print("❌ La fecha inicial debe ser anterior a la final")
			continue
		
		days_diff = (end - start).days + 1
		if days_diff > 100:
			print(f"⚠️  Vas a descargar {days_diff} imágenes. ¿Continuar? (s/n)")
			if input().strip().lower() != "s":
				return
		
		print(f"Descargando {days_diff} APOD(s)...")
		current = start
		success_count = 0
		
		while current <= end:
			date_str = current.strftime("%Y-%m-%d")
			image_path, msg = download_apod(date_str)
			
			if image_path:
				success_count += 1
				print(f"  ✓ {date_str}: {os.path.basename(image_path)}")
			else:
				print(f"  ✗ {date_str}: {msg}")
			
			current += timedelta(days=1)
		
		print(f"\n✅ Completado: {success_count}/{days_diff} descargas exitosas")
		break


def option_view_images():
	"""Opción 3: Ver imágenes descargadas con metadata"""
	print("\n" + "="*50)
	print("Imágenes descargadas")
	print("="*50)
	
	if not os.path.exists(IMAGES_DIR):
		print("No hay imágenes descargadas aún.")
		return
	
	files = [f for f in os.listdir(IMAGES_DIR) if not f.endswith(".json")]
	
	if not files:
		print("No hay imágenes descargadas aún.")
		return
	
	print(f"\nTotal: {len(files)} imagen(es)\n")
	
	for idx, filename in enumerate(sorted(files), 1):
		filepath = os.path.join(IMAGES_DIR, filename)
		file_size = os.path.getsize(filepath) / 1024  # KB
		
		# Obtener metadata si existe
		metadata_path = filepath.rsplit(".", 1)[0] + ".json"
		title = "Sin título"
		date = "Desconocida"
		
		if os.path.exists(metadata_path):
			try:
				with open(metadata_path, "r", encoding="utf-8") as f:
					metadata = json.load(f)
					title = metadata.get("title", "Sin título")
					date = metadata.get("date", "Desconocida")
			except:
				pass
		
		print(f"{idx}. {filename}")
		print(f"   Tamaño: {file_size:.1f} KB")
		print(f"   Fecha: {date}")
		print(f"   Título: {title}")
		print()


def option_download_today():
	"""Opción 4: Descargar APOD de hoy"""
	print("\n" + "="*50)
	print("Descargar APOD de hoy")
	print("="*50)
	
	today = datetime.now().strftime("%Y-%m-%d")
	print(f"Descargando APOD de {today}...")
	
	image_path, msg = download_apod(today)
	print(msg)
	
	if image_path:
		print(f"📁 Guardado en: {image_path}")
		
		# Mostrar metadata
		metadata_path = image_path.rsplit(".", 1)[0] + ".json"
		if os.path.exists(metadata_path):
			with open(metadata_path, "r", encoding="utf-8") as f:
				metadata = json.load(f)
				print(f"\n📋 Título: {metadata.get('title', 'N/A')}")


def show_menu():
	"""Muestra el menú principal"""
	print("\n" + "╔" + "="*48 + "╗")
	print("║" + " "*12 + "NASA APOD - Descargador Local" + " "*9 + "║")
	print("╚" + "="*48 + "╝")
	print("\n1. Descargar APOD por fecha específica")
	print("2. Descargar APOD en rango de fechas")
	print("3. Ver imágenes descargadas")
	print("4. Descargar APOD de hoy")
	print("5. Salir")
	print()


def main():
	"""Función principal del CLI"""
	while True:
		show_menu()
		choice = input("Selecciona una opción (1-5): ").strip()
		
		if choice == "1":
			option_download_by_date()
		elif choice == "2":
			option_download_range()
		elif choice == "3":
			option_view_images()
		elif choice == "4":
			option_download_today()
		elif choice == "5":
			print("\n👋 ¡Hasta luego!")
			break
		else:
			print("\n❌ Opción no válida. Intenta de nuevo.")


if __name__ == "__main__":
	try:
		main()
	except KeyboardInterrupt:
		print("\n\n👋 Programa interrumpido.")
	except Exception as e:
		print(f"\n❌ Error inesperado: {e}")
