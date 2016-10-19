Feature: Arithmetic operations

  Scenario: Integer addition
    Given two integers 1 and 1
    When add the two integers
    Then the result is 2

#  Scenario: Integer subtraction
#    Given two integers 1 and 1
#    When subtract the first to the second
#    Then the result is 0

  Scenario Outline: Integer addition
    Given two integers <a> and <b>
    When add the two integers
    Then the result is <result>

    Examples:
      | a | b  | result |
      | 1 | 1  | 2      |
      | 1 | 0  | 1      |