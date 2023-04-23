# The SportApp

## Group members:
Ólafur Pálsson: olp10@hi.is
Brynjólfur Steingrímsson: brs26@hi.is

## Description:
The Sports App is an android app that provides access to a single point of access to information 
and data on a wide selection of sports and activities that would otherwise have been 
difficult or time consuming to find information about.

Unlike most currently available online services, that focus mainly on the most popular sports.
Our product is intended to allow a diverse collection of activities and competitive sports, that 
have thus far been largely neglected by online news-outlets/services/media, to flourish by enabling 
people to share resources and communicate with each other on the fly via threads and comments. 

## Running the app

Run the application with a virtual or physical android device using API level 31 or higher.


### Locally (development)
Set up and run the SportAppBackend

Make sure the NetworkManager class is using BASE_URL = "http://10.0.2.2:8080"
(The default port for the Apache Tomcat HTTP server)


### Externally (deployment)

Make sure the NetworkManager class is using BASE_URL = "https://hugbunadarverkefni2-production.up.railway.app"