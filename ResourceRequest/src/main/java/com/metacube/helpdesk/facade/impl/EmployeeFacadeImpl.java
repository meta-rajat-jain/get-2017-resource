package com.metacube.helpdesk.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.facade.EmployeeFacade;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.utility.Response;


@Component("employeeFacade")
public class EmployeeFacadeImpl implements EmployeeFacade{
    
    @Resource
    EmployeeService employeeService;

    @Override
    public Response create(EmployeeDTO employee) {
        // TODO Auto-generated method stub
        return employeeService.create(employee) ;
    }
}
