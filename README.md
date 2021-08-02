# Checkout Payment Gateway


## How to Run

The application can be run from the terminal or an IDE of your choice

---
The bank simulator is currently configured to listen on port 8787. 
If you would like to change this you will need to open BankSimulator/src/main/resources/application.properties 
and update the server.port value. 

    server.port=8787

You will also need to open PayzGateAPI/src/main/resources/application.properties and configure the port for the bank.base.url.property to match

    bank.base.url=http://localhost:8787

You can also change the server.port value here if the default of 8080 is problematic.

---


When using a terminal `cd` to the project directory and then:

    ./gradlew build -- to build the application
    ./gradlew test -- to run the unit tests
    ./gradlew bootRun -- to run the gateway and bank simulator simultaneously
    ./gradlew clean -- to clean the application

or alternatively you can use the IDE's builtin plugin for Gradle for these Gradle goals

_Note: Gradle version should be 6.7+_

The OpenApi library has been integrated into the gateway solution and provides a UI that with the default configuration
should be avaialble here:

    http://localhost:8080/swagger-ui.html

This will allow you to see and test the gateway api.

---

## Assumptions

- Security/Authentication is not required at this point
- Only required to process credit card payments
- The currency doesn't matter in this case
- Client will send json and expect json back
- Client is making a synchronous call and expecting an immediate response

## Improvements

- Implement mechanism to ensure payment only submitted once - I believe Kafka has something that can be leveraged for this
- Kafka could also be used for event sourcing, which would allow reconstructing past states if something happened to the service
- If not using kafka, then would implement messaging to send request/response data to a queue, to allow a consumer to batch insert into a database and gives an alternative way to track events 
- Security - needs some putting in so only merchant can initiate payment requests and access historical payment records
- Allow storing of payees and their payment details - requires secure storage and regulation adherence
- Support multiple currencies
- Support more payment types
- Implement a monitoring solution
- Implement circuit-breaker pattern for bank client
- Implement a time-out with retries for the bank client, but only if able to ensure payment is only submitted once - would need confidence that the bank hadn't received or started processing the request
 
## Cloud Technologies

- Load balancing/Kubernetes with auto-scaling - to create more instances if load ramps up and to remove instances when load dies down
- distributed caching - to speed up fetching existing user payment details when multiple instances deployed
- if not using Kafka, then SQS as more reliable and quicker to deploy than activemq/rabbitmq in an instance  