# MyPunch App 🥊

MyPunch App is the Android app of the global MyPunch project, it allow the user to record boxing session and saw chart and datas about performance like :

- Number of punch
- Velocity of each punch
- Strength of each punch

This app is linked with a [homemade NodeJS API](https://github.com/Amealky/MyPunch-API) and an [ESP32](https://fr.wikipedia.org/wiki/ESP32#:~:text=ESP32%20est%20une%20s%C3%A9rie%20de,mode%20double%2C%20et%20un%20DSP.) placed into a boxing glove

it use [BLE](https://fr.wikipedia.org/wiki/Bluetooth_%C3%A0_basse_consommation) to communicate with the ESP32 during session recording

It also use a [MPU-6050 Accelerometer and Gyroscope Sensor](https://www.conrad.fr/fr/p/joy-it-mpu6050-capteur-d-acceleration-1-pc-s-convient-pour-kits-de-developpement-bbc-micro-bit-arduino-raspberry-p-2136256.html?utm_source=google&utm_medium=surfaces&utm_campaign=shopping-feed&utm_content=free-google-shopping-clicks&utm_term=2136256&gad_source=1&gclid=EAIaIQobChMIvJ2e1ovyhQMVNYVoCR3koQG3EAQYASABEgLj8_D_BwE) plugged into the ESP32. Then the data we read from it during session recording is used as base for all the above calculation

Made with [Android Studio 4.1](https://developer.android.com/studio/archive?hl=en)

This is an end of school year project.

![Preview](https://i.ibb.co/jTbSybv/Capture-d-e-cran-2024-01-23-a-23-01-50.png)
