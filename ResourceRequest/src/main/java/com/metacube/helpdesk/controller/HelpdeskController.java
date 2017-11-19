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
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Validation;
import com.metacube.helpdesk.vo.TicketStatusCount;

@CrossOrigin
@Controller
@RequestMapping(value = "/helpdesk")
public class HelpdeskController {

    @Resource
    TicketService ticketService;

    @RequestMapping(value = "/getAllStatusBasedTickets/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllHelpdeskStatusBasedTickets(
                authorisationToken, username, status);
    }

    @RequestMapping(value = "/getTicketCountForHelpDesk", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        return ticketService.getAllStatusBasedTicketsCountForHelpdesk(
                authorisationToken, username);
    }
}
