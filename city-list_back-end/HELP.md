# Read Me First
The following was discovered as part of building this project:

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
    After than user http://localhost:8085/bulk-images with provided city list csv for initial data setup
* The original package name 'Kuehne-Nagel.City-list' is invalid and this project uses 'KuehneNagel.Citylist' instead.

# Getting Started
    gradle bootRun or
    Another One way is to execute the main method in the KuehneNagel.Citylist.CityListApplication class from your IDE.


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/gradle-plugin/reference/html/#build-image)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

