package it.aidia.birthdaynewsletter.controller;

import it.aidia.birthdaynewsletter.dto.*;
import it.aidia.birthdaynewsletter.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final EmployeeService service;

  public EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @GetMapping
  public List<EmployeeResponse> all() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public EmployeeResponse get(@PathVariable Long id) {
    return service.find(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EmployeeResponse create(@Valid @RequestBody EmployeeRequest request) {
    return service.create(request);
  }

  @PutMapping("/{id}")
  public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
