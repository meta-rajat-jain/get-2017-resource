package com.metacube.helpdesk.service;

import com.metacube.helpdesk.utility.Response;

public interface LoginService {

    Response loginAuthentication(String loginId, String password);

}
