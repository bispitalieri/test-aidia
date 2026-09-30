package it.aidia.birthdaynewsletter.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(name = "uk_employee_email", columnNames = "email"))
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 80)
  private String firstName;
  @Column(nullable = false, length = 80)
  private String lastName;
  @Column(nullable = false, length = 254)
  private String email;
  @Column(nullable = false, length = 100)
  private String team;
  @Column(nullable = false, length = 100)
  private String office;
  @Column(nullable = false)
  private LocalDate birthDate;
  @Column(nullable = false)
  private boolean active = true;

  protected Employee() {
  }

  public Employee(String firstName, String lastName, String email, String team, String office, LocalDate birthDate, boolean active) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.team = team;
    this.office = office;
    this.birthDate = birthDate;
    this.active = active;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getTeam() {
    return team;
  }

  public String getOffice() {
    return office;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public boolean isActive() {
    return active;
  }

  public void update(String firstName, String lastName, String email, String team, String office, LocalDate birthDate, boolean active) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.team = team;
    this.office = office;
    this.birthDate = birthDate;
    this.active = active;
  }
}
