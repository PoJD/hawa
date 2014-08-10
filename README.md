# Home Automation Web Application

This project was originally based on angular-seed project https://github.com/angular/angular-seed. Later on a maven webapp was created and the main contents of the original angular-seed project moved to match the maven structure and avoid using bower and all other node.js specific packages. The main aim of this webapp is to:

* Learn node.js and angular js in particular
* Play with Rapsberry Pi at home and control it via P4J
* Wire up the 2 above together (to have a angular js webapp run on a J2EE server leveraging P4J to talk to GPIO pins of RPi)
* Prepare a home automation using several sensors and relays for our new to be build house

## Technologies

* J2EE
* Spring
* P4J
* Maven
* Restfull WS in Java (Jersey and Jackson for JSON)
* Angular JS
* * Angular Toggle Switch: https://github.com/cgarvis/angular-toggle-switch
* * Angular Canv Gauge: https://github.com/Mikhus/canv-gauge
* Twitter Bootstrap
* JUnit, JMockit

## Getting started in Eclipse

* git clone git@github.com:PoJD/hawa.git
* cd hawa
* mvn clean install
* start Eclipse
* Import -> Existing Maven project (both rpi and hawa)
* Right click on hawa -> Configure -> Convert to AngularJS project
* Right click on hawa -> Configure facets, check Dynamic Web Module, enter homeAutomation as context root, src/main/webapp as web contents
* Edit your maven settings.xml and add a definition of a server, call it TomcatServer and add your username and password you setup in your maven manager app (or change pom.xml accordingly if you use a different container
* Deploy hawa on a local server and enjoy!

# Raspberry configuration

* Make sure the webapp user (e.g. tomcat) can access /dev/i2c and /sys/class/gpio (e.g. setup module loading and group permissions or consider running the webapp as root)
* Make sure i2c and 1_wire groups are autoloaded
* Make sure your web app is auto started
* Make sure the 1-Wire module is loaded to have the needed number of slave devices allowed (by default only 10)

# Sensors

* Bmp180BarometricSensor (I2C through Pi4J)
* Ds18B20TemperatureSensor (1-Wire, simple system commands)
* Dht22Am2302TemperatureAndHumiditySensor (Raw GPIO, JNI call to Adafruit c code using /dev/mem access directly for high frequency polling the sensor)
* HC-SR501 Adjust IR Pyroelectric Infrared PIR Motion Sensor (GPIO listener through Pi4J)
