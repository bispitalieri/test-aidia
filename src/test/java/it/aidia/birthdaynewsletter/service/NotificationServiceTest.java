package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.entity.Employee;
import it.aidia.birthdaynewsletter.entity.NewsletterSettings;
import it.aidia.birthdaynewsletter.entity.NotificationLog;
import it.aidia.birthdaynewsletter.repository.EmployeeRepository;
import it.aidia.birthdaynewsletter.repository.NewsletterSettingsRepository;
import it.aidia.birthdaynewsletter.repository.NotificationLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
  private EmployeeRepository employees;
  private NotificationLogRepository logs;
  private NewsletterSettingsRepository settings;
  private NotificationService service;

  @BeforeEach
  void setUp() {
    employees = mock(EmployeeRepository.class);
    logs = mock(NotificationLogRepository.class);
    settings = mock(NewsletterSettingsRepository.class);
    service = new NotificationService(employees, logs, settings);
    when(settings.findById(1L)).thenReturn(Optional.of(new NewsletterSettings()));
    when(logs.save(any(NotificationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void sendsBirthdayListOnlyToOtherActiveEmployeesInSameTeamAndIncludesAge() {
    LocalDate date = LocalDate.of(2026, 9, 30);
    Employee birthday = employee(1L, "Giulia", "Rossi", "Platform", LocalDate.of(1994, 9, 30));
    Employee recipient = employee(2L, "Marco", "Bianchi", "Platform", LocalDate.of(1992, 6, 15));
    Employee differentTeam = employee(3L, "Sara", "Verdi", "People", LocalDate.of(1990, 1, 1));
    when(employees.findByActiveTrue()).thenReturn(List.of(birthday, recipient, differentTeam));

    var result = service.sendFor(date);

    assertEquals(1, result.get("sentCount"));
    var logCaptor = org.mockito.ArgumentCaptor.forClass(NotificationLog.class);
    verify(logs).save(logCaptor.capture());
    assertEquals("marco.bianchi@example.com", logCaptor.getValue().getRecipientEmail());
    assertTrue(logCaptor.getValue().getBody().contains("Giulia Rossi (32 anni)"));
    verify(logs, never()).save(argThat(log -> log != null && "sara.verdi@example.com".equals(log.getRecipientEmail())));
  }

  @Test
  void treatsFebruaryTwentyEighthAsBirthdayForFebruaryTwentyNinthBirth() {
    LocalDate date = LocalDate.of(2025, 2, 28);
    Employee leapDayBirthday = employee(1L, "Alba", "Neri", "Design", LocalDate.of(2000, 2, 29));
    Employee recipient = employee(2L, "Lia", "Blu", "Design", LocalDate.of(1995, 7, 1));
    when(employees.findByActiveTrue()).thenReturn(List.of(leapDayBirthday, recipient));

    service.sendFor(date);

    var captor = org.mockito.ArgumentCaptor.forClass(NotificationLog.class);
    verify(logs).save(captor.capture());
    assertTrue(captor.getValue().getBody().contains("Alba Neri (25 anni)"));
  }

  @Test
  void repeatedManualSendForSameDateCreatesDuplicateHistoryEntries() {
    LocalDate date = LocalDate.of(2026, 9, 30);
    Employee birthday = employee(1L, "Giulia", "Rossi", "Platform", date.minusYears(32));
    Employee recipient = employee(2L, "Marco", "Bianchi", "Platform", LocalDate.of(1992, 6, 15));
    when(employees.findByActiveTrue()).thenReturn(List.of(birthday, recipient));

    service.sendFor(date);
    service.sendFor(date);

    verify(logs, times(2)).save(any(NotificationLog.class));
  }

  private Employee employee(Long id, String first, String last, String team, LocalDate birthDate) {
    Employee employee = mock(Employee.class);
    when(employee.getId()).thenReturn(id);
    when(employee.getFirstName()).thenReturn(first);
    when(employee.getLastName()).thenReturn(last);
    when(employee.getEmail()).thenReturn(first.toLowerCase() + "." + last.toLowerCase() + "@example.com");
    when(employee.getTeam()).thenReturn(team);
    when(employee.getBirthDate()).thenReturn(birthDate);
    return employee;
  }
}
