package com.afs.restapi.service;

import com.afs.restapi.dto.CompanyRequest;
import com.afs.restapi.dto.CompanyResponse;
import com.afs.restapi.entity.Company;
import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.CompanyNotFoundException;
import com.afs.restapi.mapper.CompanyMapper;
import com.afs.restapi.repository.CompanyRepository;
import com.afs.restapi.repository.EmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyMapper::toResponse)
                .peek(companyResponse -> companyResponse.setEmployeeCount(getResponseEmployeeCount(companyResponse.getId())))
                .collect(Collectors.toList());

    }

    public CompanyResponse findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        CompanyResponse companyResponse = CompanyMapper.toResponse(company);
        companyResponse.setEmployeeCount(getResponseEmployeeCount(company.getId()));
        return companyResponse;
    }

    public List<CompanyResponse> findByPage(Integer pageNumber, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(pageNumber - 1, pageSize)).stream()
                .map(CompanyMapper::toResponse)
                .peek(companyResponse -> companyResponse.setEmployeeCount(getResponseEmployeeCount(companyResponse.getId())))
                .collect(Collectors.toList());
    }

    public void update(Long id, Company company) {
        Company toBeUpdatedCompany = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        toBeUpdatedCompany.setName(company.getName());
        companyRepository.save(toBeUpdatedCompany);
    }

    public CompanyResponse create(CompanyRequest companyRequest) {
        Company savedCompany = companyRepository.save(CompanyMapper.toEntity(companyRequest));
        CompanyResponse companyResponse = CompanyMapper.toResponse(savedCompany);
        companyResponse.setEmployeeCount(getResponseEmployeeCount(savedCompany.getId()));

        return companyResponse;
    }


    public List<Employee> findEmployeesByCompanyId(Long id) {
        return employeeRepository.findAllByCompanyId(id);
    }

    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    private Integer getResponseEmployeeCount(Long id) {
        List<Employee> employeesByCompanyId = findEmployeesByCompanyId(id);
        if (employeesByCompanyId == null || employeesByCompanyId.isEmpty()) {
            return 0;
        }
        return employeesByCompanyId.size();
    }
}
