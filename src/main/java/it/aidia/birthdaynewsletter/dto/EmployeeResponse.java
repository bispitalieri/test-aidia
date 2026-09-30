package it.aidia.birthdaynewsletter.dto;

import it.aidia.birthdaynewsletter.entity.Employee;

import java.time.LocalDate;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, String team, String office, LocalDate birthDate, boolean active) {
  public static EmployeeResponse from (Employee e){
    return new EmployeeResponse(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getTeam(), e.getOffice(), e.getBirthDate(), e.isActive());
  }
}
