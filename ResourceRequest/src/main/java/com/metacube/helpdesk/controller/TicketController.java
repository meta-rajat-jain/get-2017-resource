package com.metacube.helpdesk.controller;

import java.util.ArrayList;
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
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.service.ResourceService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;
import com.metacube.helpdesk.vo.TicketStatusCount;

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
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<ResourceCategoryDTO>();
        }
        return resourceService.getAllResourceCategory(authorisationToken,
                username);
    }

    @RequestMapping(value = "/getAllCategoryBasedResources", method = RequestMethod.GET)
    public @ResponseBody List<ResourceDTO> getAllCategoryBasedResources(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestParam String resourceCategory) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<ResourceDTO>();
        }
        if (Validation.isNull(resourceCategory)
                || Validation.isEmpty(resourceCategory)) {
            return new ArrayList<ResourceDTO>();
        }
        return resourceService.getResourcesBasedOnCategory(authorisationToken,
                username, resourceCategory);
    }

    @RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
    public @ResponseBody Response saveTicket(
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return new Response(0, null,
                    "One or more required data is missing with request ");
        }
        return ticketService.saveTicket(username, ticketDTO);
    }

    @RequestMapping(value = "/updateTicket", method = RequestMethod.POST)
    public @ResponseBody Response updateTicket(
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return new Response(0, null,
                    "One or more required data is missing with request ");
        }
        return ticketService.ticketUpdateApprovalChange(username, ticketDTO);
    }

    @RequestMapping(value = "/getAllTicketsOfEmployee", method = RequestMethod.POST)
    public @ResponseBody List<TicketDTO> getAllTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeDTO) {
        if (employeeDTO == null || employeeDTO.getLogin() == null) {
            return new ArrayList<TicketDTO>();
        }
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllTicketsOfEmployee(authorisationToken,
                username, employeeDTO.getLogin().getUsername());
    }

    @RequestMapping(value = "/getAllTicketsOfLoggedInEmployee", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllTicketsOfEmployee(authorisationToken,
                username, username);
    }

    @RequestMapping(value = "/getAllStatusBasedTicketsForApprover/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllStatusBasedTicketsForApprover(
                authorisationToken, username, status);
    }

    @RequestMapping(value = "/getAllStatusBasedTicketsForHelpdesk/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTicketsForHelpdesk(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllHelpdeskStatusBasedTickets(
                authorisationToken, username, status);
    }

    @RequestMapping(value = "/getTicketCountForApprover", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        return ticketService.getAllStatusBasedTicketsCountForApprover(
                authorisationToken, username);
    }

    @RequestMapping(value = "/getTicket", method = RequestMethod.POST)
    public @ResponseBody TicketDTO getTicket(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new TicketDTO();
        }
        if (ticketDTO == null) {
            return new TicketDTO();
        }
        return ticketService.getTicket(authorisationToken, username, ticketDTO);
    }

    @RequestMapping(value = "/getTicketCountByStatusOfRequester", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCount(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        return ticketService.getAllTicketCountOnStatus(authorisationToken,
                username);
    }

    @RequestMapping(value = "/getAllStatusBasedTickets/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTicketsOfUser(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllStatusBasedTicketsOfUserr(
                authorisationToken, username, status);
    }
}
