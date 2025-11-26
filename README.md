# friendly-couscous

## Testing NASA APIs
---
### APOD
Content and usage from:
https://github.com/nasa/apod-api
```text
Just a project to consume APOD API.
```

I did this app.py to test the usage of the AI companion on vscode.

Also, to test the proper way of creating the venv, activate it on MAC OS environments, with:

```bash
source venv/bin/activate
```

After that, 
virtualenv environment

1. Clone the repo

2. cd into the new directory

```bash 
cd apod-api
```
3. Create a new virtual environment env in the directory

```bash
python -m venv venv
```
4. Activate the new environment
```bash
.\venv\Scripts\Activate
```
5. Install dependencies in new environment
```bash
pip install -r requirements.txt
```
Run app
```bash
python app.py
```

When running, the request will be:

https://api.nasa.gov/planetary/apod?api_key={api_key}&thumbs=True&date=1996-01-11


The response would be something like:
```json
{
    "date": "1996-01-11",
    "explanation": "Have you heard about the great LASER light show in the sky? Well, nobody had until it was announced just yesterday by a team led by K. Davidson (U. Minnesota) and S. Johansson (U. Lund).  The research team discovered that the unusually variable star Eta Carinae emits ultraviolet light of such a specific color it is most probably LASER light! The artist's conception shown above depicts a possible model for the Hubble Space Telescope observations. In this model, Eta Carinae emits many LASER beams from its surrounding cloud of energized gas.  Infrared LASERS and microwave MASERS are extremely rare astrophysical phenomena, but this ultraviolet LASER is the first of its kind to be discovered.",
    "hdurl": "https://apod.nasa.gov/apod/image/eta_laser_big.gif",
    "media_type": "image",
    "service_version": "v1",
    "title": "Lasers in Eta Carinae",
    "url": "https://apod.nasa.gov/apod/image/eta_laser.gif"
}
```
