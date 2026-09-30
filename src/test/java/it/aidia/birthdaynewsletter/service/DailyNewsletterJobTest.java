package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.config.NewsletterClock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class DailyNewsletterJobTest {
  @Test
  void scheduledSendUsesTheSameConfiguredNewsletterClockAsManualSend() {
    NotificationService service = mock(NotificationService.class);
    NewsletterClock clock = mock(NewsletterClock.class);
    when(clock.today()).thenReturn(LocalDate.of(2026, 10, 1));
    DailyNewsletterJob job = new DailyNewsletterJob(service, clock);

    job.run();

    verify(service).sendFor(LocalDate.of(2026, 10, 1));
  }
}
