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
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.ResourceService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.MessageConstants;
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
    @Resource
    LoginService loginService;

    /**
     * method returning list of all type of resource category
     * 
     * @param authorisationToken
     * @param username
     * @return
     */
    @RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
    public @ResponseBody List<ResourceCategoryDTO> getAllResourceCategory(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<ResourceCategoryDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<ResourceCategoryDTO>();
        }
        return resourceService.getAllResourceCategory();
    }

    /**
     * method returning all the resources based on category of the resource
     * 
     * @param authorisationToken
     * @param username
     * @param resourceCategory
     * @return
     */
    @RequestMapping(value = "/getAllCategoryBasedResources", method = RequestMethod.GET)
    public @ResponseBody List<ResourceDTO> getAllCategoryBasedResources(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestParam String resourceCategory) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<ResourceDTO>();
        }
        // null check for resource category
        if (Validation.isNull(resourceCategory)
                || Validation.isEmpty(resourceCategory)) {
            return new ArrayList<ResourceDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<ResourceDTO>();
        }
        return resourceService.getResourcesBasedOnCategory(resourceCategory);
    }

    @RequestMapping(value = "/saveTicket", method = RequestMethod.POST)
    public @ResponseBody Response saveTicket(
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        return ticketService.saveTicket(username, ticketDTO).getResponse();
    }

    @RequestMapping(value = "/updateTicket", method = RequestMethod.POST)
    public @ResponseBody Response updateTicket(
            @RequestHeader(value = "username") String username,
            @RequestBody TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
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
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        if (Validation.isNull(employeeDTO.getLogin().getUsername())
                || Validation.isEmpty(employeeDTO.getLogin().getUsername())) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllTicketsOfEmployee(employeeDTO.getLogin()
                .getUsername());
    }

    @RequestMapping(value = "/getAllTicketsOfLoggedInEmployee", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllTicketsOfEmployee(username);
    }

    @RequestMapping(value = "/getAllStatusBasedTicketsForApprover/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTickets(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        if (Validation.isNull(status) || Validation.isEmpty(status)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllStatusBasedTicketsForApprover(username,
                status);
    }

    @RequestMapping(value = "/getAllStatusBasedTicketsForHelpdesk/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTicketsForHelpdesk(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        if (Validation.isNull(status) || Validation.isEmpty(status)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllHelpdeskStatusBasedTickets(username, status);
    }

    @RequestMapping(value = "/getTicketCountForApprover", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        return ticketService.getAllStatusBasedTicketsCountForApprover(username);
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
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new TicketDTO();
        }
        if (ticketDTO.getTicketNo() == 0) {
            return new TicketDTO();
        }
        return ticketService.getTicket(ticketDTO);
    }

    @RequestMapping(value = "/getTicketCountByStatusOfRequester", method = RequestMethod.GET)
    public @ResponseBody List<TicketStatusCount> getAllStatusBasedTicketsCount(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketStatusCount>();
        }
        return ticketService.getAllTicketCountOnStatus(username);
    }

    @RequestMapping(value = "/getAllStatusBasedTickets/{status}", method = RequestMethod.GET)
    public @ResponseBody List<TicketDTO> getAllStatusBasedTicketsOfUser(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @PathVariable("status") String status) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TicketDTO>();
        }
        if (Validation.isNull(status) || Validation.isEmpty(status)) {
            return new ArrayList<TicketDTO>();
        }
        return ticketService.getAllStatusBasedTicketsOfUser(username, status);
    }
}
