## Pre requiste
Install JDK 8

Install maven 3.x

Install MYSQL 5.7


Download source code from  https://github.com/ranjesh1/spring-boot2-rest-order-services to ~/workdir

## Setup - create database(if you don't want to use in memory database)

1. Start MYSQL server

`/usr/local/mysql/support-files/mysql.server start`

2. Create database

```cd /usr/local/mysql/bin

mysql -u root -p

mysql> create database rest_order_db;
```

## Configure application.properties with correct database credentials (if you don't want to use in memory database)

update database name, user name,password correctly

```
spring.datasource.username=root

spring.datasource.password=MyNewPass

spring.datasource.url=jdbc:mysql://localhost:3306/rest_order_db

spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect

```

## Configure application.properties for in memory database

```
spring.datasource.url=jdbc:h2:mem:testdb

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

```

## To run app
`mvn spring-boot:run`

## To run all tests
`mvn test`

##  To test application manually
Use Postman or any other REST client to send POST request with the following details

Post User

`http://localhost:8080/api/users`

Request Body

```
{
   "firstName":"Steve",
   "lastName":"Rob",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"

}
```


Response

```
{
   "id": 44,
   "firstName":"Steve",
   "lastName":"Rob",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"
}
```

Get User

`http://localhost:8080/api/users/44`

Response

```
{
   "id": 44,
   "firstName":"Steve",
   "lastName":"Rob",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"
}
```

Put User

`http://localhost:8080/api/users/44`

Request Body

```
{
   "firstName":"Steveupdated",
   "lastName":"Robupdated",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"
}
```


Response

```
{
   "id": 44,
   "firstName":"Steveupdated",
   "lastName":"Robupdated",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"
}
```


Patch User

`http://localhost:8080/api/users/44`

Request Body

```
{
   "firstName":"SteveupdatedAgain",
   "lastName":"RobupdatedAgain"
}
```

Response

```
{
   "id": 44,
   "firstName":"SteveupdatedAgain",
   "lastName":"RobupdatedAgain",
   "email":"steverob@test.com",
   "firstLineOfAddress":"111 Pinewood Grove",
   "secondLineOfAddress":"Commercial street",
   "town":"London",
   "postCode":"W7 8AG"
}
```

Post Order
`http://localhost:8080/api/api/users/44/orders`

Request Body

```
{
  "description":"Awesome phone",
  "priceInPence":1200,
  "completedStatus": false
}
```


Response
```
{
   "id": 45,
   "description":"Awesome phone",
   "priceInPence":1200,
   "completedStatus": false
}
```

Get Order

`http://localhost:8080/api/users/44/orders/45`

Response

```
{
   "id": 45,
   "description":"Awesome phone",
   "priceInPence":1200,
   "completedStatus": false
}
```


