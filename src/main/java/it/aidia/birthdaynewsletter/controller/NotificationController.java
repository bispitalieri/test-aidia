package it.aidia.birthdaynewsletter.controller;

import it.aidia.birthdaynewsletter.entity.NotificationLog;
import it.aidia.birthdaynewsletter.config.NewsletterClock;
import it.aidia.birthdaynewsletter.repository.NotificationLogRepository;
import it.aidia.birthdaynewsletter.service.NotificationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
  private final NotificationService service;
  private final NotificationLogRepository logs;
  private final NewsletterClock newsletterClock;

  public NotificationController(NotificationService service, NotificationLogRepository logs, NewsletterClock newsletterClock) {
    this.service = service;
    this.logs = logs;
    this.newsletterClock = newsletterClock;
  }

  @PostMapping("/send")
  public Map<String, Object> send(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return service.sendFor(date == null ? newsletterClock.today() : date);
  }

  @GetMapping("/history")
  public List<NotificationLog> history(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return date == null ? logs.findAllByOrderBySentAtDesc() : logs.findByBirthdayDateOrderBySentAtDesc(date);
  }
}
