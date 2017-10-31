package com.metacube.helpdesk.dao;


import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Status;
@Repository
public interface LoginDAO {
    
    LogIn get(String loginId);
    Status create(LogIn login);
    void update(String username,String authorisationToken);
}
