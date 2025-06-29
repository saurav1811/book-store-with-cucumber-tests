# Book Store App
- This App is used to purchase books from the Book Store.
  - Start the Spring Boot App (starts on 8082) with in-memory H2 DB with pre-seeded data via flyway migrations
  - Sample API cURL:- 
    ```
    curl --location 'http://localhost:8082/api/book-store/purchase-books-by-name' \
      --header 'Content-Type: application/json' \
      --data '[
      {
      "bookName": "Harry Potter: The Chamber of Secrets",
      "quantity": 1
      },
      {
      "bookName": "Sherlock Holmes: The Hound of Baskervilles",
      "quantity": 2
      }
      ]'
    ```

References:-
1. [Running tests with Cucumber using JUnit5](https://softwareevolutivo.com.ec/en/cucumber-spring-boot-2-junit-5-2/)
2. [Official Documentation of Cucumber](https://cucumber.io/docs/installation/java)
3. [Cucumber With Junit-Jupiter-Platform-Engine](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine)


