package it.aidia.birthdaynewsletter.controller;

import it.aidia.birthdaynewsletter.dto.*;
import it.aidia.birthdaynewsletter.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
  private final SettingsService service;

  public SettingsController(SettingsService service) {
    this.service = service;
  }

  @GetMapping
  public SettingsResponse get() {
    return service.get();
  }

  @PutMapping
  public SettingsResponse update(@Valid @RequestBody SettingsRequest request) {
    return service.update(request);
  }
}
