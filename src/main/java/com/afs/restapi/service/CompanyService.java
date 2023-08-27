package com.afs.restapi.service;

import com.afs.restapi.dto.CompanyRequest;
import com.afs.restapi.dto.CompanyResponse;
import com.afs.restapi.dto.EmployeeResponse;
import com.afs.restapi.entity.Company;
import com.afs.restapi.exception.CompanyNotFoundException;
import com.afs.restapi.mapper.CompanyMapper;
import com.afs.restapi.mapper.EmployeeMapper;
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
                .collect(Collectors.toList());

    }

    public CompanyResponse findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        return CompanyMapper.toResponse(company);
    }

    public List<CompanyResponse> findByPage(Integer pageNumber, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(pageNumber - 1, pageSize)).stream()
                .map(CompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void update(Long id, CompanyRequest companyRequest) {
        Company toBeUpdatedCompany = companyRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
        toBeUpdatedCompany.setName(companyRequest.getName());
        companyRepository.save(toBeUpdatedCompany);
    }

    public CompanyResponse create(CompanyRequest companyRequest) {
        Company savedCompany = companyRepository.save(CompanyMapper.toEntity(companyRequest));
        return CompanyMapper.toResponse(savedCompany);

    }


    public List<EmployeeResponse> findEmployeesByCompanyId(Long id) {
        return employeeRepository.findAllByCompanyId(id).stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

}
