# Receipt-Processor
Stores receipt information for later evualation.

##Requirements

For building and running the application you need:

- [Maven 3](https://maven.apache.org)
- [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
##**Clone the application**
```bash
https://github.com/retegrete/receipt-processor.git
```


##Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.app.receipt.processor.ReceiptProcessorApplicationKt` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```bash
mvn clean install
mvn spring-boot:run
```
Additionally, you can use Docker with the following Docker commands assuming you have docker installed.
```
docker pull retegrete/receipt-processor:retegrete
docker run -d -p 8080:8080 --name receipt-processor retegrete/receipt-processor:retegrete
```

## About

The service is just a simple receipt view and count points REST application. It uses an in-memory database (H2) to store the receipt, and generate an ID. Once the data is stored it can be retrieved via ID. With this ID it can be analyzed and given points based on the following criteria: 
* One point for every alphanumeric character in the retailer name.
* 50 points if the total is a round dollar amount with no cents.
* 25 points if the total is a multiple of `0.25`.
* 5 points for every two items on the receipt.
* If the trimmed length of the item description is a multiple of 3, multiply the price by `0.2` and round up to the nearest integer. The result is the number of points earned.
* 6 points if the day in the purchase date is odd.
* 10 points if the time of purchase is after 2:00pm and before 4:00pm.

## Endpoints

Process Receipt
```
POST receipts/process
Accept: application/json
Content-Type: application/json
JSON STRUCTURE:
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}

RESPONSE: HTTP 201 (Created)
Location header: http://localhost:8080/receipts/process
```
Award Points
```
POST receipts/{id}/points
Accept: application/json
Content-Type: application/json
RESPONSE: HTTP 200 (ok)
JSON STRUCTURE: 
{
"points":"109"
}
Location header: http://localhost:8080/receipts/1/points
```
## H2 DB

In memory database is accessed via:
``http://localhost:8080/h2-console
``

Credentials are stored in``
application.properties``

