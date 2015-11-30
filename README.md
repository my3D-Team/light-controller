## Light Controller ##

**Warning : In progress !**

### Team
* Main developper : Corentin Azelart <corentin.azelart@capgemini.com>

### What ?
This is a Spring-Boot application to send Art-Net packet over Network. This application provide an API to turn on/off light, change color and apply effect.


### Light Controller KIT
* Light (PAR56 LED RGB DMX)
* Network switch (D-Link)
* Art-Net Node (ENTTEC OPENDMX Ethernet)
* A Raspberry Pi 2

### How ?
#### Connect the kit ?

#### Restart the kit ?


### RESTfull WS

#### 1. WS URI :

> **HTTP GET**
> /api/light/set/{address}/{color}
> Set the color of the light id by hexa color

---
 > **HTTP GET**
> /api/light/set/{address}/{color}/{effect}
> Set the color of the light id by hexa color and apply an effect

#### 2. WS Parameters :
* address : the light address, by default the first projector is 1
* color : the hexa color for the light (without # char)
* effect : the effect to apply (FULL, BLINK, STROB)

#### 3. WS Samples :
> **HTTP GET**
> /api/light/set/1/FF0000
> Turn on the first light in red

---
> **HTTP GET**
> /api/light/set/1/000000
> Turn off the first light

---
> **HTTP GET**
> /api/light/set/1/000000/BLINK
> Turn on the light in red and blink