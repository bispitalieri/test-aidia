package it.aidia.birthdaynewsletter.config;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsletterClockTest {
  @Test
  void usesConfiguredTimeZone() {
    NewsletterClock clock = new NewsletterClock("Pacific/Auckland");

    assertEquals(ZoneId.of("Pacific/Auckland"), clock.zone());
  }
}
