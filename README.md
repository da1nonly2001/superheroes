# Superheroes

This web application makes web service calls to [Marvel](http://developer.marvel.com/) and presents data to users.

The code runs on Java 1.8, Tomcat 7, and MySQL 5.5.  You'll need that if you want to run this project on your own server.  In addition to that, you'll need to sign up at Marvel's developer portal to get a set of private and public keys, update keys.sql accordingly.  Once you have the code, execute the create.sql and keys.sql scripts to set up the database.  You'll have to create a user with access to your schema, set the username and password in the Spring application context file (applicationContext.xml).  Execute ant compile and you'll be good to go! Once started, the application will start populating your database.  Feel free to provide feedback.