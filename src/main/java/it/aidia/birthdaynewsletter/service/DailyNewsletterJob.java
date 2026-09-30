package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.config.NewsletterClock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyNewsletterJob {
  private final NotificationService service;
  private final NewsletterClock newsletterClock;

  public DailyNewsletterJob(NotificationService service, NewsletterClock newsletterClock) {
    this.service = service;
    this.newsletterClock = newsletterClock;
  }

  @Scheduled(cron = "${newsletter.schedule.cron:0 0 8 * * *}", zone = "${newsletter.schedule.zone:Europe/Rome}")
  public void run() {
    service.sendFor(newsletterClock.today());
  }
}
