package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

   
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(employeeDetails.getName());
                    employee.setEmail(employeeDetails.getEmail());
                    employee.setDepartment(employeeDetails.getDepartment());
                    employee.setSalary(employeeDetails.getSalary());
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

   
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}