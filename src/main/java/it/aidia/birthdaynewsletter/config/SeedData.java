package it.aidia.birthdaynewsletter.config;

import it.aidia.birthdaynewsletter.entity.Employee;
import it.aidia.birthdaynewsletter.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeedData implements CommandLineRunner {
  private final EmployeeRepository employees;
  private final NewsletterClock newsletterClock;

  public SeedData(EmployeeRepository employees, NewsletterClock newsletterClock) {
    this.employees = employees;
    this.newsletterClock = newsletterClock;
  }

  @Override
  public void run(String... args) {
    if (employees.count() != 0) return;
    LocalDate today = newsletterClock.today();
    employees.save(new Employee("Giulia", "Rossi", "giulia.rossi@example.com", "Platform", "Firenze", today.minusYears(32), true));
    employees.save(new Employee("Marco", "Bianchi", "marco.bianchi@example.com", "Platform", "Firenze", today.minusYears(29).plusDays(1), true));
    employees.save(new Employee("Sara", "Verdi", "sara.verdi@example.com", "People", "Milano", today.minusYears(35), true));
  }
}
