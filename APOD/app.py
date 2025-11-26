import requests
import os
from dotenv import load_dotenv

load_dotenv()

api_key = os.getenv("NASA_API_KEY")
url = f"https://api.nasa.gov/planetary/apod?api_key={api_key}&thumbs=True&date=1996-01-11"

payload = {}
headers = {}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)
