package it.aidia.birthdaynewsletter.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeRequestValidationTest {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void activeMustBePresentInEmployeeRequests() {
    EmployeeRequest request = new EmployeeRequest("Luca", "Neri", "luca@example.com", "Platform", "Firenze", LocalDate.of(1992, 6, 15), null);

    assertTrue(validator.validate(request).stream().anyMatch(v -> v.getPropertyPath().toString().equals("active")));
  }
}
