package com.metacube.helpdesk.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Validation;
import com.metacube.helpdesk.vo.TicketStatusCount;

@CrossOrigin
@Controller
@RequestMapping(value = "/helpdesk")
public class HelpdeskController {

    @Resource
    TicketService ticketService;
    @Resource
    LoginService loginService;

    /**
     * @param authorisationToken
     * @param username
     * @param status
     * @return list of all that tickets in which the helpdesk approval status is
     *         given status
     */
    @RequestMapping(value = "/getAllStatusBasedTickets/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // Null and empty check
        if (Validation.isNull(status) || Validation.isEmpty(status)) {
            return new ArrayList<TicketDTO>();
        }
        // calls service method
        return ticketService.getAllHelpdeskStatusBasedTickets(username, status);
    }

    /**
     * @param authorisationToken
     * @param username
     * @return helpdesk status based count of all the status
     */
    @RequestMapping(value = "/getTicketCountForHelpDesk", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        // to authenticate that logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        // calls service method
        return ticketService.getAllStatusBasedTicketsCountForHelpdesk();
    }
}
