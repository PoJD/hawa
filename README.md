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
* Import -> Existing Maven project
* Right click -> Configure -> Convert to AngularJS project
* Right click -> Configure facets, check Dynamic Web Module, enter homeAutomation as context root, src/main/webapp as web contents
* Deploy on a local server and enjoy!