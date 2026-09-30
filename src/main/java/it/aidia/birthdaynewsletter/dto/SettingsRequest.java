package it.aidia.birthdaynewsletter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SettingsRequest(@NotBlank @Size(max = 200) String subjectTemplate, @NotBlank @Size(max = 5000) String bodyTemplate, boolean enabled) {
}
