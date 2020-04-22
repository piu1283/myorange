# Myorange
The aim of Myorange is to help people build their cloud storage systems within several lines of code and configuration. 
People may deploy our product on their server or a cloud service provider. 
They may choose the target disk they want their files to go into, e.g. Local disk, Amazon S3, Google Cloud Platform. 
We would provide a full permission system that enables you, as a root, to know every detail of the file system and users’ usage status. 
In addition, Myorange Drive also supports public file sharing. Files can be shared to others through a single url, with a potential limitation of times.

# Front-End
The front-end used vue.js, project site: [front-end](https://github.com/HoboRiceone/MyOrangeDriveFrontEnd/tree/master)

# how to run
- Install redis and mysql(require 8) in your computer
- Change the redis `host/port` and mysql `password` in the `Application.yml`.
- Create databse called `'db_test'` in your mysql.
- Enter project root path, use `./mvnw install` to generate jar package.
- Enter the directory of the generated jar. Use `java -jar myorange-0.0.1-SNAPSHOT.jar` to start the server.