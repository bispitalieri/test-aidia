package it.aidia.birthdaynewsletter.controller;

import it.aidia.birthdaynewsletter.config.NewsletterClock;
import it.aidia.birthdaynewsletter.repository.NotificationLogRepository;
import it.aidia.birthdaynewsletter.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.*;

class NotificationControllerTest {
  @Test
  void manualSendWithoutDateUsesConfiguredNewsletterClock() {
    NotificationService service = mock(NotificationService.class);
    NewsletterClock clock = mock(NewsletterClock.class);
    when(clock.today()).thenReturn(LocalDate.of(2026, 10, 1));
    when(service.sendFor(LocalDate.of(2026, 10, 1))).thenReturn(Map.of("status", "COMPLETED"));
    NotificationController controller = new NotificationController(service, mock(NotificationLogRepository.class), clock);

    controller.send(null);

    verify(service).sendFor(LocalDate.of(2026, 10, 1));
  }
}
