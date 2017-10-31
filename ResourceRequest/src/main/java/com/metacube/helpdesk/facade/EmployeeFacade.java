package com.metacube.helpdesk.facade;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.utility.Response;

public interface EmployeeFacade {
    Response create(EmployeeDTO employee); 
}
