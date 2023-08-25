package com.afs.restapi.service;

import com.afs.restapi.dto.EmployeeRequest;
import com.afs.restapi.dto.EmployeeResponse;
import com.afs.restapi.dto.EmployeeUpdateRequest;
import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.EmployeeNotFoundException;
import com.afs.restapi.mapper.EmployeeMapper;
import com.afs.restapi.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
        return EmployeeMapper.toResponse(employee);
    }

    public void update(Long id, EmployeeUpdateRequest employeeUpdateRequest) {
        Employee toBeUpdatedEmployee = employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
        if (employeeUpdateRequest.getSalary() != null) {
            toBeUpdatedEmployee.setSalary(employeeUpdateRequest.getSalary());
        }
        if (employeeUpdateRequest.getAge() != null) {
            toBeUpdatedEmployee.setAge(employeeUpdateRequest.getAge());
        }
        employeeRepository.save(toBeUpdatedEmployee);
    }

    public List<Employee> findAllByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        Employee employee = EmployeeMapper.toEntity(employeeRequest);
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    public List<EmployeeResponse> findByPage(Integer pageNumber, Integer pageSize) {
        Page<Employee> employeesInThePage = employeeRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
        return employeesInThePage.stream().collect(Collectors.toList()).stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
