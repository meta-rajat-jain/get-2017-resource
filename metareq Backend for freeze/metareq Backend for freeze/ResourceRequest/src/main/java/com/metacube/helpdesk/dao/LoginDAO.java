package com.metacube.helpdesk.dao;

import org.springframework.stereotype.Repository;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface LoginDAO {

    LogIn get(String loginId);

    Status create(LogIn login);

    Response destroyAuthorisationToken(String username);

    Status update(LogIn login);
}
