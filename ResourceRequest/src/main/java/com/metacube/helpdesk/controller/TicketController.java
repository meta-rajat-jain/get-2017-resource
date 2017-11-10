package com.metacube.helpdesk.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.ResourceDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.ResourceCategory;
import com.metacube.helpdesk.service.ResourceService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Response;

@CrossOrigin
@Controller
@RequestMapping(value = "/ticket")
public class TicketController {

    @Resource
    ResourceService resourceService;
    
    @Resource
    TicketService ticketService;
    
    @RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
    public @ResponseBody List<ResourceCategoryDTO> getAllResourceCategory(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        return resourceService.getAllResourceCategory(authorisationToken, username);
    }
    
    @RequestMapping(value = "/getAllCategoryBasedResources", method = RequestMethod.GET)
    public @ResponseBody List<ResourceDTO> getAllCategoryBasedResources(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestParam String resourceCategory) {
        return resourceService.getResourcesBasedOnCategory(authorisationToken, username,resourceCategory);
    }
    
    @RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
    public @ResponseBody Response saveTicket(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        return ticketService.saveTicket(authorisationToken, username,ticketDTO );
    }
    
    @RequestMapping(value = "/updateTicket", method = RequestMethod.POST)
    public @ResponseBody Response updateTicket(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        return ticketService.ticketUpdateApprovalChange(authorisationToken, username,ticketDTO );
    }
    
    @RequestMapping(value = "/getAllTicketsOfEmployee", method = RequestMethod.POST)
    public @ResponseBody List<TicketDTO> getAllTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeDTO) {
        return ticketService.getAllTicketsOfEmployee(authorisationToken, username,employeeDTO.getLogin().getUsername() );
    }
    @RequestMapping(value = "/getAllTicketsOfLoggedInEmployee", method = RequestMethod.POST)
    public @ResponseBody List<TicketDTO> getAllTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        return ticketService.getAllTicketsOfEmployee(authorisationToken, username,username );
    }
    
    @RequestMapping(value = "/getAllStatusBasedTicketsForApprover/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        return ticketService.getAllStatusBasedTicketsForApprover(authorisationToken, username,status);
    }  
}
