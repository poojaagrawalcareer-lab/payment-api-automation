Feature: Payment API
  @smoke
  Scenario Outline: Successful payment with different data

    When I send a payment request with "<merchantId>", <amount>, and "<currency>"
    Then the response status code should be 200
    And the response should contain payment status "APPROVED"
    And the provider payment request should be sent

    Examples:
      | merchantId | amount | currency |
      | M123       | 100    | USD      |
      | M123       | 200    | USD      |
      | M456       | 150    | EUR      |

  @negative
  Scenario: Payment with invalid token

    When I send a payment request with invalid token
    Then the response status code should be 401
    And the response should contain error message "Invalid or missing token"
    And the error response should match the error schema

  @negative
  Scenario: Payment without authentication token

    When I send a payment request without token
    Then the response status code should be 401
    And the response should contain error message "Invalid or missing token"
    And the error response should match the error schema

  @declined
  Scenario: Declined payment

    When I send a declined payment request
    Then the response status code should be 200
    And the response should contain payment status "DECLINED"

  @negative
  Scenario Outline: Payment with missing merchantId

    When I send a payment request with "<merchantId>", <amount>, and "<currency>"
    Then the response status code should be 400
    And the response should contain error message "merchantId is required"
    And the provider payment request should not be sent

    Examples:
      | merchantId | amount | currency |
      |            | 100    | USD      |
  @negative
  Scenario Outline: Payment with invalid amount

    When I send a payment request with "<merchantId>", <amount>, and "<currency>"
    Then the response status code should be 400
    And the response should contain error message "amount must be greater than 0"
    And the provider payment request should not be sent

    Examples:
      | merchantId | amount | currency |
      | M123       | -100   | USD      |
      | M123       | 0      | USD      |
  @negative
  Scenario Outline: Payment with unsupported currency

    When I send a payment request with "<merchantId>", <amount>, and "<currency>"
    Then the response status code should be 400
    And the response should contain error message "Unsupported currency"
    And the provider payment request should not be sent

    Examples:
      | merchantId | amount | currency |
      | M123       | 100    | XYZ      |