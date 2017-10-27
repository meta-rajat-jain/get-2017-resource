package com.metacube.helpdesk.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.dto.LoginDTO;
@Repository
public interface LoginDAO {
    
    LoginDTO get(String loginId);
    
}
