package com.afs.restapi.mapper;

import com.afs.restapi.dto.CompanyRequest;
import com.afs.restapi.dto.CompanyResponse;
import com.afs.restapi.entity.Company;
import org.springframework.beans.BeanUtils;

public class CompanyMapper {

    private CompanyMapper() {

    }

    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        return company;
    }

    public static CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        return companyResponse;
    }
}
