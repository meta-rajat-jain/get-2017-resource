package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import java.util.Date;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.ItResource;
import com.metacube.helpdesk.model.ResourceCategory;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.TicketCreationResponse;
import com.metacube.helpdesk.vo.TicketStatusCount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketServiceTest {

    @Autowired
    TicketService ticketService;
    static final String ADMIN_NAME = "Admin";
    static final String ADMIN_PASSWORD = "admin123";
    static final String ADMIN_USERNAME = "admin@metacube.com";
    static final String ADMIN_CONTACT_NUMBER = "987654321";
    static final String EMPLOYEE_NAME = "Vaishali Jain";
    static final String EMPLOYEE_PASSWORD = "metacube";
    static final String INCORRECT_PASSWORD = "thisisincorrectpassword";
    static final String EMPLOYEE_USERNAME = "vaishali.jain@metacube.com";
    static final String EMPLOYEE_CONTACT_NUMBER = "9876543210";
    static final String EMPLOYEE_ORG_DOMAIN = "metacube.com";
    static final String EMPLOYEE_DESIGNATION = "Member";
    static final String EMPLOYEE_NAME2 = "Gaurav Tak";
    static final String EMPLOYEE_NONVERIFIED = "gaurav.tak@metacube.com";
    static final String EMPLOYEE_NONVERIFIED2 = "shivam.lalwani@metacube.com";
    static final String EMPLOYEE_INACTIVE = "anushtha.gupta@metacube.com";
    static final String HELPDESK = "ithelpdesk@metacube.com";
    static final String EMPLOYEE_MANAGER = "shubham.sharma@metacube.com";
    static final String NORMAL_EMPLOYEE2 = "shreya.bordia@metacube.com";
    static final String NORMAL_EMPLOYEE3 = "udit.saxena@metacube.com";
    static final String NORMAL_EMPLOYEE4 = "pawan.manglani@metacube.com";
    int ticket_1_id = 1;
    int ticket_2_id = 2;
    int ticket_3_id = 3;
    int ticket_4_id = 4;
    static final ItResource resource1 = new ItResource(2, "MacBook",
            new ResourceCategory(1, "Hardware", null));
    static final ItResource resource2 = new ItResource(5, "VisualStudio",
            new ResourceCategory(2, "Software", null));

    @Test
    public void test71_SaveTicketByManagerSuccess() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedFor(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("provide me macbook");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("Cabin - B3F202");
        ticketDTO.setTeamName(null);
        ticketDTO.setStatus(Constants.TICKET_APPROVED);
        TicketCreationResponse response = ticketService.saveTicket(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(1, response.getResponse().getStatusCode());
        ticket_1_id = response.getTicketNo();
        assertEquals(MessageConstants.GENERATED_TICKET_SUCCESSFULLY, response
                .getResponse().getMessage());
    }

    @Test
    public void test72_SaveTicketByManagerForAnySubordinateSuccess() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to udits pc");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName(null);
        ticketDTO.setStatus(Constants.TICKET_APPROVED);
        TicketCreationResponse response = ticketService.saveTicket(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(1, response.getResponse().getStatusCode());
        ticket_2_id = response.getTicketNo();
        assertEquals(MessageConstants.GENERATED_TICKET_SUCCESSFULLY, response
                .getResponse().getMessage());
    }

    @Test
    public void test73_SaveTicketByNonManagerSuccess() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedFor(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("please provide macbook");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName("Team Shubham");
        ticketDTO.setStatus(Constants.TICKET_OPEN);
        TicketCreationResponse response = ticketService.saveTicket(
                EMPLOYEE_USERNAME, ticketDTO);
        assertEquals(1, response.getResponse().getStatusCode());
        ticket_3_id = response.getTicketNo();
        assertEquals(MessageConstants.GENERATED_TICKET_SUCCESSFULLY, response
                .getResponse().getMessage());
    }

    @Test
    public void test74_SaveTicketByManagerForAnyEmployeeNotUnderHim() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE2);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName(null);
        ticketDTO.setStatus(Constants.TICKET_OPEN);
        TicketCreationResponse response = ticketService.saveTicket(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(0, response.getResponse().getStatusCode());
        assertEquals(
                MessageConstants.ACCESS_DENIED_TO_GENERATE_TICKET_FOR_EMPLOYEE,
                response.getResponse().getMessage());
    }

    @Test
    public void test74_SaveTicketByNonManagerForAnyEmployee() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE4);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName(null);
        ticketDTO.setStatus(Constants.TICKET_OPEN);
        TicketCreationResponse response = ticketService.saveTicket(
                NORMAL_EMPLOYEE3, ticketDTO);
        assertEquals(0, response.getResponse().getStatusCode());
        assertEquals(MessageConstants.ACCESS_DENIED_TO_GENERATE_TICKET,
                response.getResponse().getMessage());
    }

    @Test
    public void test75_SaveTicketByNonManagerForHimselfSuccess() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName("Team shubham.sharma");
        ticketDTO.setStatus(Constants.TICKET_OPEN);
        TicketCreationResponse response = ticketService.saveTicket(
                NORMAL_EMPLOYEE3, ticketDTO);
        assertEquals(1, response.getResponse().getStatusCode());
        ticket_4_id = response.getTicketNo();
        assertEquals(MessageConstants.GENERATED_TICKET_SUCCESSFULLY, response
                .getResponse().getMessage());
    }

    @Test
    public void test76_SaveTicketByNonManagerForHimselfButNonExistantTeam() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setRequesterName(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName("Team NonExistant");
        ticketDTO.setStatus(Constants.TICKET_OPEN);
        TicketCreationResponse response = ticketService.saveTicket(
                NORMAL_EMPLOYEE3, ticketDTO);
        assertEquals(0, response.getResponse().getStatusCode());
        assertEquals(MessageConstants.TEAM_NOT_EXIST, response.getResponse()
                .getMessage());
    }

    @Test
    public void test77_UpdateTicketByManagerApprove() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket_3_id);
        ticketDTO.setRequesterName(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedFor(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("please folllow up on request");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setLastUpdatedByUsername(EMPLOYEE_USERNAME);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setTeamName("Team Shubham");
        ticketDTO.setStatus(Constants.TICKET_APPROVED);
        Response response = ticketService.ticketUpdateApprovalChange(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals(MessageConstants.TICKET_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test78_UpdateTicketByManagerDecline() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket_4_id);
        ticketDTO.setRequesterName(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("no you dont need it");
        ticketDTO.setPriority("Not Important");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName("Team shubham.sharma");
        ticketDTO.setLastUpdatedByUsername(NORMAL_EMPLOYEE3);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setStatus(Constants.TICKET_CLOSED);
        Response response = ticketService.ticketUpdateApprovalChange(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals(MessageConstants.TICKET_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test79_UpdateTicketByManagerNonExistantTicket() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(19999);
        ticketDTO.setRequesterName(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedFor(EMPLOYEE_USERNAME);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("no you dont need it");
        ticketDTO.setPriority("Not Important");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setTeamName("Team Shubham");
        ticketDTO.setLastUpdatedByUsername(EMPLOYEE_USERNAME);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setStatus(Constants.TICKET_CLOSED);
        Response response = ticketService.ticketUpdateApprovalChange(
                EMPLOYEE_MANAGER, ticketDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("Unable to fetch this ticket", response.getMessage());
    }

    @Test
    public void test81_UpdateTicketByHelpDeskApprove() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket_4_id);
        ticketDTO.setRequesterName(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setLastUpdatedByUsername(NORMAL_EMPLOYEE3);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setTeamName("Team shubham.sharma");
        ticketDTO.setStatus(Constants.TICKET_INPROGRESS);
        Response response = ticketService.ticketUpdateApprovalChange(HELPDESK,
                ticketDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals(MessageConstants.TICKET_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test82_UpdateTicketByHelpDeskCompleted() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket_2_id);
        ticketDTO.setRequesterName(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedFor(NORMAL_EMPLOYEE3);
        ticketDTO.setRequestedResource(resource2);
        ticketDTO.setComment("install to PC");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("GET room");
        ticketDTO.setLastUpdatedByUsername(EMPLOYEE_MANAGER);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setTeamName("Team shubham.sharma");
        ticketDTO.setStatus(Constants.TICKET_CLOSED);
        Response response = ticketService.ticketUpdateApprovalChange(HELPDESK,
                ticketDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals(MessageConstants.TICKET_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test83_UpdateTicketByHelpDeskNeedInfo() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket_1_id);
        ticketDTO.setRequesterName(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedFor(EMPLOYEE_MANAGER);
        ticketDTO.setRequestedResource(resource1);
        ticketDTO.setComment("Please specify model and configurations");
        ticketDTO.setPriority("Urgent");
        ticketDTO.setRequestType("New");
        ticketDTO.setSeatLocation("Cabin B3 F2 02");
        ticketDTO.setLastUpdatedByUsername(EMPLOYEE_MANAGER);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        ticketDTO.setTeamName("Team shubham.sharma");
        ticketDTO.setStatus(Constants.TICKET_NEEDINFO);
        Response response = ticketService.ticketUpdateApprovalChange(HELPDESK,
                ticketDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals(MessageConstants.TICKET_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test84_GetAllTicketsOfEmployee() {
        List<TicketDTO> tickets = ticketService
                .getAllTicketsOfEmployee(NORMAL_EMPLOYEE3);
        assertEquals(1, tickets.size());
    }

    @Test
    public void test85_GetAllStatusBasedTicketForApprover() {
        List<TicketDTO> tickets = ticketService
                .getAllStatusBasedTicketsForApprover(EMPLOYEE_MANAGER,
                        Constants.TICKET_INPROGRESS);
        assertEquals(1, tickets.size());
    }

    @Test
    public void test86_GetAllStatusBasedTicketForHelpdesk() {
        List<TicketDTO> tickets = ticketService
                .getAllHelpdeskStatusBasedTickets(HELPDESK,
                        Constants.TICKET_APPROVED);
        assertEquals(1, tickets.size());
    }

    @Test
    public void test87_GetAllStatusBasedTicketForEmployee() {
        List<TicketDTO> tickets = ticketService.getAllStatusBasedTicketsOfUser(
                NORMAL_EMPLOYEE3, Constants.TICKET_INPROGRESS);
        assertEquals(1, tickets.size());
    }

    @Test
    public void test88_GetStatusBasedCountTicketForEmployee() {
        List<TicketStatusCount> ticketCounts = ticketService
                .getAllTicketCountOnStatus(NORMAL_EMPLOYEE3);
        for (TicketStatusCount count : ticketCounts) {
            assertEquals(count.getStatus(), Constants.TICKET_INPROGRESS);
            assertEquals(Long.valueOf(1L), count.getCount());
        }
    }

    @Test
    public void test89_GetStatusBasedCountTicketForApprover() {
        List<TicketStatusCount> ticketCounts = ticketService
                .getAllStatusBasedTicketsCountForApprover(EMPLOYEE_MANAGER);
        assertEquals(Long.valueOf(1L), ticketCounts.get(0).getCount());
    }

    @Test
    public void test90_GetStatusBasedCountTicketForHelpDesk() {
        List<TicketStatusCount> ticketCounts = ticketService
                .getAllStatusBasedTicketsCountForHelpdesk();
        assertEquals(Long.valueOf(1L), ticketCounts.get(0).getCount());
    }
}