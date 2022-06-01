# R2DBC Springboot Webflux Mutli-Repo and One to Many Implementation
This project is an example for 
* Usage of Multiple Repositories based on context
* One-to-many relationship between Postgresql tables (Person, Address) using R2DBC and custom join queries
* Custom Row Deserializer implementation as well
* Pagination has been implemented to fetch results


_Note_: The field on which we are fetching results should also be indexed or we see badSQL Grammar errors


### Steps to run
1. Setup postgresql tables (locally or on server)
2. Create tables and schema using src/main/resources/sampledb.sql
3. Run the application.java (note by default properties are picked from application-local.properties as profie is local)
4. Populate the tables with dummy values.
5. Use the following curl command to get the results and see it uses read db url

    `curl --location --request GET 'http://localhost:8081/persons?emailId=test@email.com'`
6. Use the following curl command to update the last name and see it uses write db url

   `curl --location --request PUT 'http://localhost:8081/persons/lastName?emailId=test@email.com' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "lastName": "new last name"
   }'`