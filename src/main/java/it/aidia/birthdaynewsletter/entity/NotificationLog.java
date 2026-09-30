package it.aidia.birthdaynewsletter.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "notification_logs")
public class NotificationLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private LocalDate birthdayDate;
  @Column(nullable = false, length = 254)
  private String recipientEmail;
  @Column(nullable = false, length = 1000)
  private String subject;
  @Column(nullable = false, columnDefinition = "text")
  private String body;
  @Column(nullable = false, length = 20)
  private String status;
  @Column(nullable = false)
  private Instant sentAt;
  @Column(length = 1000)
  private String errorMessage;

  protected NotificationLog() {
  }

  public NotificationLog(LocalDate birthdayDate, String recipientEmail, String subject, String body, String status, Instant sentAt, String errorMessage) {
    this.birthdayDate = birthdayDate;
    this.recipientEmail = recipientEmail;
    this.subject = subject;
    this.body = body;
    this.status = status;
    this.sentAt = sentAt;
    this.errorMessage = errorMessage;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getBirthdayDate() {
    return birthdayDate;
  }

  public String getRecipientEmail() {
    return recipientEmail;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getStatus() {
    return status;
  }

  public Instant getSentAt() {
    return sentAt;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
