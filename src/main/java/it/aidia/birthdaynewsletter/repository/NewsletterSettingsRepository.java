package it.aidia.birthdaynewsletter.repository;

import it.aidia.birthdaynewsletter.entity.NewsletterSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSettingsRepository extends JpaRepository<NewsletterSettings, Long> {
}
