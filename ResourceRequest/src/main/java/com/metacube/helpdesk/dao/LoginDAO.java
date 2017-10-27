package com.metacube.helpdesk.dao;


import org.springframework.stereotype.Repository;
import com.metacube.helpdesk.modal.LogIn;
@Repository
public interface LoginDAO {
    
    LogIn get(String loginId);
    
}
