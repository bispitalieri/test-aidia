package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.entity.*;
import it.aidia.birthdaynewsletter.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
  private final EmployeeRepository employees;
  private final NotificationLogRepository logs;
  private final NewsletterSettingsRepository settings;

  public NotificationService(EmployeeRepository employees, NotificationLogRepository logs, NewsletterSettingsRepository settings) {
    this.employees = employees;
    this.logs = logs;
    this.settings = settings;
  }

  @Transactional
  public Map<String, Object> sendFor(LocalDate date) {
    NewsletterSettings config = settings.findById(1L).orElseGet(() -> settings.save(new NewsletterSettings()));
    if (!config.isEnabled())
      return Map.of("date", date, "status", "DISABLED", "sentCount", 0, "message", "Newsletter disabilitata");
    List<Employee> active = employees.findByActiveTrue();
    Map<String, List<Employee>> birthdays = active.stream().filter(e -> isBirthday(e.getBirthDate(), date))
        .collect(Collectors.groupingBy(Employee::getTeam));
    int count = 0;
    for (var teamEntry : birthdays.entrySet()) {
      List<Employee> recipients = active.stream().filter(e -> e.getTeam().equalsIgnoreCase(teamEntry.getKey()))
          .filter(e -> teamEntry.getValue().stream().noneMatch(b -> b.getId().equals(e.getId()))).toList();
      if (recipients.isEmpty()) continue;
      String birthdayNames = teamEntry.getValue().stream().map(e -> e.getFirstName() + " " + e.getLastName() + " (" + age(e.getBirthDate(), date) + " anni)").collect(Collectors.joining(", "));
      String subject = config.getSubjectTemplate().replace("{team}", teamEntry.getKey());
      String body = config.getBodyTemplate().replace("{team}", teamEntry.getKey()).replace("{birthdays}", birthdayNames).replace("{date}", date.toString());
      for (Employee recipient : recipients) {
        try {
          // Mock delivery for the exercise: inspect docker compose logs app.
          log.info("MOCK EMAIL to={} subject={} body={}", recipient.getEmail(), subject, body);
          logs.save(new NotificationLog(date, recipient.getEmail(), subject, body, "SENT", Instant.now(), null));
          count++;
        } catch (RuntimeException ex) {
          logs.save(new NotificationLog(date, recipient.getEmail(), subject, body, "FAILED", Instant.now(), ex.getMessage()));
        }
      }
    }
    return Map.of("date", date, "status", "COMPLETED", "sentCount", count, "birthdayTeams", birthdays.size());
  }

  private boolean isBirthday(LocalDate birth, LocalDate date) {
    if (birth.getMonthValue() == 2 && birth.getDayOfMonth() == 29 && !date.isLeapYear())
      return date.getMonthValue() == 2 && date.getDayOfMonth() == 28;
    return birth.getMonthValue() == date.getMonthValue() && birth.getDayOfMonth() == date.getDayOfMonth();
  }

  private int age(LocalDate birth, LocalDate date) {
    return date.getYear() - birth.getYear();
  }
}
