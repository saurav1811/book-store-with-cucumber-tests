Feature: Purchase Books By Name from Book Store

  Scenario: Generate Invoice for Successful Purchase of Books By Name
    Given I have the request body from file "books-order-request.json"
    When I POST to "/api/book-store/purchase-books-by-name"
    Then the response status should be 200
    And the response should match the content of "invoice.json"
