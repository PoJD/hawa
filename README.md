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
* Apache FTP Server, Commons Collections
* Maven
* H2 DB for local run, MySQL for server side run (no code is db dependent though, only property files sql.properties)
* JUnit, JMockit
* Restfull WS in Java (Jersey and Jackson for JSON)
* Node.js Angular.js
* Angular Toggle Switch: https://github.com/cgarvis/angular-toggle-switch
* Google Gauge Node.js library using d3.js (custom angular port): https://developers.google.com/chart/interactive/docs/gallery/gauge?csw=1
* Nvd3 Node.js graph library: http://cmaurer.github.io/angularjs-nvd3-directives/
* Angular calendar using open source JQuery full calendar: https://github.com/angular-ui/ui-calendar/tree/master/demo
* ngDialog https://github.com/likeastore/ngDialog
* Twitter Bootstrap

## Getting started in Eclipse

* git clone git@github.com:PoJD/hawa.git
* cd hawa
* mvn clean install
* start Eclipse
* Import -> Existing Maven project (both rpi and hawa)
* Right click on hawa -> Configure -> Convert to AngularJS project
* Right click on hawa -> Configure facets, check Dynamic Web Module, enter homeAutomation as context root, src/main/webapp as web contents
* Edit your maven settings.xml and add a definition of a server, call it TomcatServer and add your username and password you setup in your maven manager app (or change pom.xml accordingly if you use a different container
* Edit email.properties, replace the text REPLACE with your own email properties (server and user and other properties). See applicationContext.xml for more details. COnsider ignoring this file in git from that moment on.
* Deploy hawa on a local server and enjoy!
* To deploy latest version on Rpi (assuming you run on tomcat): mvn clean install tomcat7:redeploy -Prpi

# Raspberry configuration

* Make sure the webapp user (e.g. tomcat) can access /dev/i2c and /sys/class/gpio (e.g. setup module loading and group permissions or consider running the webapp as root)
* Make sure i2c, 1_wire, v4l2 and sound modules are autoloaded (w1-gpio i2c-bcm2708 i2c-dev bcm2835-v4l2 snd_bcm2835)
* Make sure the raspistill tool is available (to be able to take pictures from camera) and in $PATH (by default in Raspian, might be needed to be added in other distros) - currently not used by the app
* Make sure mjpg-streamer with uvc video input and http output plugins is installed on the box (to be able to view video)
* Make sure your web app is auto started
* Make sure the 1-Wire module is loaded to have the needed number of slave devices allowed (by default only 10)
* Install some DB (I used MySQL). Use ddl.sql and indexes.sql inside src/main/db to setup the schema. Use database.sql in the same directory to create the db in MySQL. Fine tune sql.properties inside rpi-resources if you plan to use other than MySQL DB and the syntax there is incorrect for your DB

# Raspberry webapp configuration
* Add following switches to the VM args: -XX:+UseThreadPriorities -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

# Sensors and low level peripherals

* Bmp180BarometricSensor (I2C through Pi4J)
* TSL2561LightSensor (I2C through Pi4J)
* Ds18B20TemperatureSensor (1-Wire, simple system commands)
* Dht22Am2302TemperatureAndHumiditySensor (Raw GPIO, JNI call to Adafruit c code using /dev/mem access directly for high frequency polling the sensor)
* HC-SR501 Adjust IR Pyroelectric Infrared PIR Motion Sensor (GPIO listener through Pi4J)
* Button switch (Pi4J)
* MCP23017 expansion board - setup in RoomSpecification - each room could use different expansion board to control Solid State Relays to control the lights
* MCP3008 Analog to Digital converter - using SPI