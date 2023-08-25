package com.afs.restapi.mapper;

import com.afs.restapi.EmployeeRequest;
import com.afs.restapi.entity.Employee;
import org.springframework.beans.BeanUtils;

public class EmployeeMapper {
    private EmployeeMapper(){

    }

    public static Employee toEntity(EmployeeRequest employeeRequest){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }

}
