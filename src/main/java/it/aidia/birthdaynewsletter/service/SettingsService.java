package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.dto.*;
import it.aidia.birthdaynewsletter.entity.NewsletterSettings;
import it.aidia.birthdaynewsletter.repository.NewsletterSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettingsService {
  private final NewsletterSettingsRepository repository;

  public SettingsService(NewsletterSettingsRepository repository) {
    this.repository = repository;
  }

  public SettingsResponse get() {
    return SettingsResponse.from(repository.findById(1L).orElseGet(() -> repository.save(new NewsletterSettings())));
  }

  public SettingsResponse update(SettingsRequest r) {
    NewsletterSettings s = repository.findById(1L).orElseGet(NewsletterSettings::new);
    s.update(r.subjectTemplate(), r.bodyTemplate(), r.enabled());
    return SettingsResponse.from(repository.save(s));
  }
}
