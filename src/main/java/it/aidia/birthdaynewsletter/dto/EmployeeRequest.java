package it.aidia.birthdaynewsletter.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeRequest(@NotBlank @Size(max = 80) String firstName, @NotBlank @Size(max = 80) String lastName,
                              @NotBlank @Email @Size(max = 254) String email, @NotBlank @Size(max = 100) String team,
                              @NotBlank @Size(max = 100) String office, @NotNull @Past LocalDate birthDate, @NotNull Boolean active) {
}
