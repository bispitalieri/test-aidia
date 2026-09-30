package it.aidia.birthdaynewsletter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Provides one configured time zone to the scheduler, API and sample-data initializer.
 */
@Component
public class NewsletterClock {
  private final ZoneId zone;

  public NewsletterClock(@Value("${newsletter.schedule.zone:Europe/Rome}") String zone) {
    this.zone = ZoneId.of(zone);
  }

  public ZoneId zone() {
    return zone;
  }

  public LocalDate today() {
    return LocalDate.now(zone);
  }
}
