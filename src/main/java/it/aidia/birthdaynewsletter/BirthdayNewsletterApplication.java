package it.aidia.birthdaynewsletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BirthdayNewsletterApplication {

  public static void main(String[] args) {
    SpringApplication.run(BirthdayNewsletterApplication.class, args);
  }
}
