# Kuehne-Nagel_City-list

//Front-end setup,
setup node modules - npm install,
start frond end - ng serve,
application will be run at port 4200

//Back end
#Requirements
For building and running the application you need:
    JDK 1.8
    Gradle 7.6 or compatible
    MySql 5.7 or higher (database name - city-list-db or prefer)

#Special attention
    Please update apllication development properties as follow from provided temporery_keys.csv file or own s3 details
        s3.bucket.name=
        aws.s3.accessKey=
        aws.s3.secretKey=
    After than use http://localhost:8085/swagger-ui.html# get swagger UI.
    user /bulk-images api in bulk city list controller with provided city list csv for initial data setup
user swagger apis in web browser - http://localhost:8085/swagger-ui.html#,
User /bulk-images/bulk api for import csv file for initial city setup
