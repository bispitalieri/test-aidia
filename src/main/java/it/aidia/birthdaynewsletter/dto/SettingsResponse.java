package it.aidia.birthdaynewsletter.dto;

import it.aidia.birthdaynewsletter.entity.NewsletterSettings;

public record SettingsResponse(String subjectTemplate, String bodyTemplate, boolean enabled) {
  public static SettingsResponse from (NewsletterSettings s){
    return new SettingsResponse(s.getSubjectTemplate(), s.getBodyTemplate(), s.isEnabled());
  }
}
