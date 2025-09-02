package com.example.employeeapi.controller;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    // To get all employees
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        Map<String, Object> response = new HashMap<>();
        response.put("count", employees.size());
        response.put("employees", employees);
        return ResponseEntity.ok(response);
    }

    // To get employee by ID
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Employee found");
                    response.put("employee", employee);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("error", "Employee not found with id: " + id)));
    }

    // To get employee by email
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        return employeeRepository.findByEmail(email)
                .map(employee -> ResponseEntity.ok(Map.of(
                        "message", "Employee found",
                        "employee", employee)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("error", "Employee not found with email: " + email)));
    }

    // To get employees by department
    @GetMapping("/department/{department}")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable String department) {
        List<Employee> employees = employeeRepository.findByDepartment(department);
        if (employees.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "No employees found in department: " + department));
        }
        return ResponseEntity.ok(Map.of(
                "count", employees.size(),
                "employees", employees));
    }

    // To create a new employee
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.createEmployee(employee);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee created successfully!");
        response.put("employee", savedEmployee);
        return ResponseEntity.status(201).body(response); // 201 Created
    }

    // To update employee
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails) {
        try {
            Employee updated = employeeService.updateEmployee(id, employeeDetails);
            return ResponseEntity.ok(Map.of(
                    "message", "Employee updated successfully!",
                    "employee", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Employee not found with id: " + id));
        }
    }

    // To delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(Map.of("message", "Employee deleted successfully!"));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Employee not found with id: " + id));
        }
    }
}
