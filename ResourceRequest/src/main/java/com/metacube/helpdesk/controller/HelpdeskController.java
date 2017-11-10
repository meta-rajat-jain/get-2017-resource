package com.metacube.helpdesk.controller;

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
        return ticketService.getAllHelpdeskStatusBasedTickets(authorisationToken, username,status);
    }
}
