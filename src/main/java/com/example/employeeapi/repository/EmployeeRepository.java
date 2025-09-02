package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employee by email 
	
    Optional<Employee> findByEmail(String email);

    // Find all employees in a given department
    List<Employee> findByDepartment(String department);

    // We can add more custom queries here if needed
}
