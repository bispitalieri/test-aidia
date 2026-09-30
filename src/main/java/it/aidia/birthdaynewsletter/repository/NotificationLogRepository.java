package it.aidia.birthdaynewsletter.repository;

import it.aidia.birthdaynewsletter.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
  List<NotificationLog> findAllByOrderBySentAtDesc();

  List<NotificationLog> findByBirthdayDateOrderBySentAtDesc(LocalDate date);
}
