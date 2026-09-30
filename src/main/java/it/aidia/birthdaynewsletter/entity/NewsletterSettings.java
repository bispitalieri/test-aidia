package it.aidia.birthdaynewsletter.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "newsletter_settings")
public class NewsletterSettings {
  @Id
  private Long id = 1L;
  @Column(nullable = false, length = 200)
  private String subjectTemplate = "Buon compleanno, {team}!";
  @Column(nullable = false, columnDefinition = "text")
  private String bodyTemplate = "Oggi festeggiamo {birthdays}.";
  @Column(nullable = false)
  private boolean enabled = true;

  public NewsletterSettings() {
  }

  public Long getId() {
    return id;
  }

  public String getSubjectTemplate() {
    return subjectTemplate;
  }

  public String getBodyTemplate() {
    return bodyTemplate;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void update(String subjectTemplate, String bodyTemplate, boolean enabled) {
    this.subjectTemplate = subjectTemplate;
    this.bodyTemplate = bodyTemplate;
    this.enabled = enabled;
  }
}
