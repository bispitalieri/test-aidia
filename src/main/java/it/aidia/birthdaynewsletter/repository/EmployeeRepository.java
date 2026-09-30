package it.aidia.birthdaynewsletter.repository;

import it.aidia.birthdaynewsletter.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  List<Employee> findAllByOrderByLastNameAscFirstNameAsc();

  List<Employee> findByActiveTrue();

  Optional<Employee> findByEmailIgnoreCase(String email);
}
