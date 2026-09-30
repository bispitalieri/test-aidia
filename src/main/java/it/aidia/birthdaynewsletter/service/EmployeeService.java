package it.aidia.birthdaynewsletter.service;

import it.aidia.birthdaynewsletter.dto.*;
import it.aidia.birthdaynewsletter.entity.Employee;
import it.aidia.birthdaynewsletter.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EmployeeService {
  private final EmployeeRepository repository;

  public EmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public List<EmployeeResponse> findAll() {
    return repository.findAllByOrderByLastNameAscFirstNameAsc().stream().map(EmployeeResponse::from).toList();
  }

  @Transactional(readOnly = true)
  public EmployeeResponse find(Long id) {
    return EmployeeResponse.from(get(id));
  }

  public EmployeeResponse create(EmployeeRequest r) {
    ensureEmailAvailable(r.email(), null);
    return EmployeeResponse.from(repository.save(new Employee(r.firstName().trim(), r.lastName().trim(), r.email().trim().toLowerCase(), r.team().trim(), r.office().trim(), r.birthDate(), Boolean.TRUE.equals(r.active()))));
  }

  public EmployeeResponse update(Long id, EmployeeRequest r) {
    Employee e = get(id);
    ensureEmailAvailable(r.email(), id);
    e.update(r.firstName().trim(), r.lastName().trim(), r.email().trim().toLowerCase(), r.team().trim(), r.office().trim(), r.birthDate(), Boolean.TRUE.equals(r.active()));
    return EmployeeResponse.from(e);
  }

  public void delete(Long id) {
    repository.delete(get(id));
  }

  private Employee get(Long id) {
    return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dipendente non trovato"));
  }

  private void ensureEmailAvailable(String email, Long id) {
    repository.findByEmailIgnoreCase(email.trim()).filter(e -> !e.getId().equals(id)).ifPresent(e -> {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già utilizzata");
    });
  }
}
