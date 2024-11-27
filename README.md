# Deploying the Application

To run as a standalone application, generate the jar file by running:

```mvn clean package```

Navigate to your base project directory, and then to the /target folder to find the executable 
rs1124-0.0.1-SNAPSHOT.jar. If you move the jar file to another directory before executing, make 
sure to move the target/data folder to the same location.

# Test Cases

Test Cases corresponding to the proof scenarios can be found in /src/main/resources/test-cases.json.
The JUnit test class is test/java/rs._1._4.rs1124/service/process/checkout/CheckoutProcessTest.