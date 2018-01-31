package com.metacube.helpdesk.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.Response;

@CrossOrigin
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().toString().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers",
                    "Content-Type, username, authorisationToken");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods",
                    "GET, POST, OPTIONS");
            response.setContentType("application/json");
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers",
                    "Content-Type, username, authorisationToken");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods",
                    "GET, POST, OPTIONS");
            if (request.getHeader("authorisationToken") == null
                    || request.getHeader("username") == null) {
                response.setContentType("application/json");
                response.getWriter()
                        .write(new Response(0, null,
                                "One or more headers are missing").toString());
                response.setStatus(400);
                return false;
            } else if (!loginService.authenticateRequest(
                    request.getHeader("authorisationToken"),
                    request.getHeader("username"))) {
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write(
                        new Response(0, null, "Unable to authenticate user")
                                .toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, username, authorisationToken");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, username, authorisationToken");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    }
}