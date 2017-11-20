package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.ResourceCategoryDAO;
import com.metacube.helpdesk.dao.TeamDAO;
import com.metacube.helpdesk.dao.TicketApprovalDAO;
import com.metacube.helpdesk.dao.TicketDAO;
import com.metacube.helpdesk.dao.TicketHistoryDAO;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.model.TicketApproval;
import com.metacube.helpdesk.model.TicketHistory;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.utility.TicketCreationResponse;
import com.metacube.helpdesk.utility.Validation;
import com.metacube.helpdesk.vo.TicketStatusCount;

@Service("ticketService")
public class TicketServiceImpl implements TicketService {

    @Resource
    LoginDAO loginDAO;
    @Resource
    TicketDAO ticketDAO;
    @Resource
    TeamDAO teamDAO;
    @Resource
    TicketApprovalDAO ticketApprovalDAO;
    @Resource
    TicketHistoryDAO ticketHistoryDAO;
    @Resource
    EmployeeDAO employeeDAO;
    @Resource
    ResourceCategoryDAO resourceCategoryDAO;
    @Resource
    TicketService ticketService;
    @Resource
    EmployeeService employeeService;
    @Resource
    TeamService teamService;
    @Resource
    LoginService loginService;

    public Response validateTicketObject(TicketDTO ticketDTO) {
        if (ticketDTO.getRequestType().equals("New")) {
            if (Validation.isNull(ticketDTO.getRequestedResource())) {
                return new Response(0, null, "Please fill all required fields");
            }
        }
        if (Validation.isNull(ticketDTO.getRequesterName())
                || Validation.isEmpty(ticketDTO.getRequesterName())
                || Validation.isNull(ticketDTO.getRequestedFor())
                || Validation.isEmpty(ticketDTO.getRequestedFor())
                || Validation.isNull(ticketDTO.getRequestType())
                || Validation.isEmpty(ticketDTO.getRequestType())
                || Validation.isNull(ticketDTO.getPriority())
                || Validation.isEmpty(ticketDTO.getPriority())
                || Validation.isNull(ticketDTO.getStatus())
                || Validation.isEmpty(ticketDTO.getStatus())
                || Validation.isNull(ticketDTO.getLastUpdatedByUsername())
                || Validation.isEmpty(ticketDTO.getLastUpdatedByUsername())
                || Validation.isNull(ticketDTO.getLastDateOfUpdate())
                || Validation.isNull(ticketDTO.getRequestDate())
                || Validation.isNull(ticketDTO.getTeamName())
                || Validation.isEmpty(ticketDTO.getTeamName())) {
            return new Response(0, null, "Please fill all required fields");
        }
        return null;
    }

    @Override
    public TicketDTO modelToDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket.getTicketNo());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setRequestType(ticket.getRequestType());
        ticketDTO.setRequesterName(ticket.getRequester().getUsername()
                .getUsername());
        ticketDTO.setRequestedFor(ticket.getRequestedFor().getUsername()
                .getUsername());
        ticketDTO.setRequestedResource(ticket.getRequestedResource());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setComment(ticket.getComment());
        ticketDTO.setSeatLocation(ticket.getSeatLocation());
        ticketDTO.setRequestDate(ticket.getRequestDate());
        ticketDTO.setLastDateOfUpdate(ticket.getLastDateOfUpdate());
        ticketDTO.setLastUpdatedByUsername(ticket.getLastUpdatedBy());
        ticketDTO.setTeamName(ticket.getCorrespondingTeam().getTeamName());
        return ticketDTO;
    }

    @Override
    public Ticket dtoToModel(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setTicketNo(ticketDTO.getTicketNo());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setSeatLocation(ticketDTO.getSeatLocation());
        ticket.setRequestType(ticketDTO.getRequestType());
        ticket.setComment(ticketDTO.getComment());
        ticket.setRequestedResource(ticketDTO.getRequestedResource());
        ticket.setRequester(employeeDAO.getEmployee(loginDAO.get(ticketDTO
                .getRequesterName())));
        ticket.setRequestedFor(employeeDAO.getEmployee(loginDAO.get(ticketDTO
                .getRequestedFor())));
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setLastDateOfUpdate(ticketDTO.getLastDateOfUpdate());
        ticket.setRequestDate(ticketDTO.getRequestDate());
        ticket.setLastUpdatedBy(ticketDTO.getLastUpdatedByUsername());
        ticket.setCorrespondingTeam(teamDAO.getTeamByName(ticketDTO
                .getTeamName()));
        return ticket;
    }

    @Override
    public TicketCreationResponse saveTicket(String username,
            TicketDTO ticketDTO) {
        ticketDTO.setLastUpdatedByUsername(username);
        ticketDTO.setRequesterName(username);
        ticketDTO.setLastDateOfUpdate(new Date());
        ticketDTO.setRequestDate(new Date());
        if (ticketDTO.getTeamName() == null) {
            if (employeeDAO
                    .getEmployee(loginDAO.get(ticketDTO.getRequesterName()))
                    .getDesignation().equals(Designation.Manager)) {
                String[] splittedUsername = username.split("@");
                String teamName = "TEAM " + splittedUsername[0];
                ticketDTO.setTeamName(teamName);
                ticketDTO.setStatus(Constants.TICKET_APPROVED);
                if (!(teamService.getAllEmployeesUnderHead(ticketDTO
                        .getRequesterName()).contains(employeeService
                        .modelToDto(employeeDAO.getEmployee(loginDAO
                                .get(ticketDTO.getRequestedFor())))))) {
                    return new TicketCreationResponse(
                            new Response(
                                    0,
                                    null,
                                    MessageConstants.ACCESS_DENIED_TO_GENERATE_TICKET_FOR_EMPLOYEE),
                            0);
                }
            } else {
                return new TicketCreationResponse(new Response(0, null,
                        MessageConstants.ACCESS_DENIED_TO_GENERATE_TICKET), 0);
            }
        }
        Response response = validateTicketObject(ticketDTO);
        if (response != null) {
            return new TicketCreationResponse(response, 0);
        }
        if (teamDAO.getTeamByName(ticketDTO.getTeamName()) == null) {
            return new TicketCreationResponse(new Response(0, null,
                    MessageConstants.TEAM_NOT_EXIST), 0);
        }
        Ticket ticket = dtoToModel(ticketDTO);
        int ticketNo = ticketDAO.saveTicket(ticket);
        if (ticketNo == 0) {
            return new TicketCreationResponse(new Response(0, null,
                    MessageConstants.TICKET_GENERATION_FAILED), ticketNo);
        }
        ticket.setTicketNo(ticketNo);
        if (!saveTicketApproval(ticket).equals(Status.Success)) {
            return new TicketCreationResponse(new Response(0, null,
                    MessageConstants.TICKET_GENERATION_FAILED), ticketNo);
        }
        return new TicketCreationResponse(new Response(1, null,
                MessageConstants.GENERATED_TICKET_SUCCESSFULLY), ticketNo);
    }

    @Override
    public Status saveTicketHistory(int ticketNo) {
        Ticket ticket = ticketDAO.getTicket(ticketNo);
        if (ticket != null) {
            TicketHistory ticketHistory = new TicketHistory();
            ticketHistory.setTicket(ticket);
            ticketHistory.setPreviousStatus(ticket.getStatus());
            ticketHistory.setPreviousPriority(ticket.getPriority());
            ticketHistory.setPreviousComment(ticket.getComment());
            ticketHistory.setLastUpdatedBy(ticket.getLastUpdatedBy());
            ticketHistory.setLastDateOfUpdate(ticket.getLastDateOfUpdate());
            return ticketHistoryDAO.saveTicketHistory(ticketHistory);
        }
        return Status.Failure;
    }

    @Override
    public Status saveTicketApproval(Ticket ticket) {
        TicketApproval ticketApproval = new TicketApproval();
        if (ticket.getRequester().getDesignation().equals(Designation.Manager)) {
            ticketApproval.setApproveStatus(Constants.TICKET_APPROVED);
            ticketApproval.setHelpdeskApprovalStatus(Constants.TICKET_APPROVED);
        } else {
            ticketApproval.setApproveStatus(Constants.TICKET_OPEN);
            ticketApproval.setHelpdeskApprovalStatus(Constants.TICKET_OPEN);
        }
        ticketApproval.setApprover(ticket.getCorrespondingTeam().getManager());
        ticketApproval.setTicketNo(ticket);
        return ticketApprovalDAO.saveTicketApproval(ticketApproval);
    }

    @Override
    public List<TicketDTO> getAllStatusBasedTicketsOfUser(String username,
            String status) {
        List<TicketDTO> allTicketsDTO = new ArrayList<TicketDTO>();
        Employee employee = employeeDAO.getEmployee(loginDAO.get(username));
        List<Ticket> allTickets = ticketDAO.getTicketsOfEmployeeBasedOnStatus(
                employee, status);
        for (Ticket ticket : allTickets) {
            allTicketsDTO.add(modelToDto(ticket));
        }
        return allTicketsDTO;
    }

    @Override
    public Response updateTicket(String username, TicketDTO ticketDTO) {
        if (Validation.isNull(ticketDTO.getTicketNo())) {
            return new Response(0, null, "Please specify ticket number");
        }
        Response response = validateTicketObject(ticketDTO);
        if (response != null) {
            return response;
        }
        if (teamDAO.getTeamByName(ticketDTO.getTeamName()) == null) {
            return new Response(0, null, MessageConstants.TEAM_NOT_EXIST);
        }
        if (saveTicketHistory(ticketDTO.getTicketNo()).equals(Status.Success)) {
            ticketDTO.setLastUpdatedByUsername(username);
            ticketDTO.setLastDateOfUpdate(new Date());
            if (ticketDAO.updateTicket(dtoToModel(ticketDTO)).equals(
                    Status.Success)) {
                return new Response(1, null,
                        MessageConstants.TICKET_UPDATED_SUCCESSFULLY);
            } else {
                new Response(0, null, MessageConstants.TICKET_UPDATION_FAILED);
            }
        } else {
            new Response(0, null, "Unable to save ticket history");
        }
        return new Response(0, null, MessageConstants.TICKET_NOT_EXIST);
    }

    @Override
    public List<TicketDTO> getAllTicketsOfEmployee(String employeeName) {
        List<TicketDTO> allTicketsDTO = new ArrayList<TicketDTO>();
        if (employeeName == null) {
            return allTicketsDTO;
        }
        List<Ticket> allTickets = ticketDAO
                .getTicketsGeneratedByEmployee(employeeDAO.getEmployee(loginDAO
                        .get(employeeName)));
        for (Ticket ticket : allTickets) {
            allTicketsDTO.add(modelToDto(ticket));
        }
        return allTicketsDTO;
    }

    @Override
    public List<TicketDTO> getAllStatusBasedTicketsForApprover(String username,
            String status) {
        List<TicketDTO> allTicketsDTO = new ArrayList<TicketDTO>();
        // Pending to check that status belongs from set of predefined
        // status : Status to be made enumeration
        List<Ticket> allTickets = ticketApprovalDAO
                .getTicketsOfApproverBasedOnStatus(
                        employeeDAO.getEmployee(loginDAO.get(username)), status);
        for (Ticket ticket : allTickets) {
            allTicketsDTO.add(modelToDto(ticket));
        }
        return allTicketsDTO;
    }

    @Override
    public List<TicketDTO> getAllHelpdeskStatusBasedTickets(String username,
            String status) {
        List<TicketDTO> allTicketsDTO = new ArrayList<TicketDTO>();
        // status validation need to be added
        List<Ticket> allTickets = ticketApprovalDAO
                .getAllHelpdeskStatusBasedTickets(status);
        for (Ticket ticket : allTickets) {
            allTicketsDTO.add(modelToDto(ticket));
        }
        return allTicketsDTO;
    }

    @Override
    public Response ticketUpdateApprovalChange(String username,
            TicketDTO ticketDTO) {
        System.out.println(ticketDTO.getTicketNo());
        TicketApproval approvalObjectForCurrentTicket = ticketApprovalDAO
                .get(ticketDTO.getTicketNo());
        if (approvalObjectForCurrentTicket == null) {
            return new Response(0, null, "Unable to fetch this ticket");
        }
        if (username.equals(approvalObjectForCurrentTicket.getApprover()
                .getUsername().getUsername())
                || username.toLowerCase().contains("helpdesk")
                || username.equalsIgnoreCase(ticketDTO.getRequesterName())) {
            approvalObjectForCurrentTicket.setApproveStatus(ticketDTO
                    .getStatus());
            approvalObjectForCurrentTicket.setHelpdeskApprovalStatus(ticketDTO
                    .getStatus());
            if (!ticketApprovalDAO.update(approvalObjectForCurrentTicket)
                    .equals(Status.Success)) {
                return new Response(0, null, "Unable to update this ticket");
            }
        }
        return updateTicket(username, ticketDTO);
    }

    @Override
    public List<TicketStatusCount> getAllTicketCountOnStatus(String username) {
        Employee employee = employeeDAO.getEmployee(loginDAO.get(username));
        if (!Validation.isNull(employee)) {
            return ticketDAO.getTicketCountForStatusOfEmployee(employee);
        }
        return new ArrayList<TicketStatusCount>();
    }

    @Override
    public TicketDTO getTicket(TicketDTO ticketDTO) {
        Ticket ticket = ticketDAO.getTicket(ticketDTO.getTicketNo());
        if (ticket == null) {
            return new TicketDTO();
        }
        return modelToDto(ticket);
    }

    @Override
    public List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            String username) {
        Employee employee = employeeDAO.getEmployee(loginDAO.get(username));
        if (employee != null) {
            return ticketApprovalDAO
                    .getTicketsCountOfApproverBasedOnStatus(employee);
        }
        return new ArrayList<TicketStatusCount>();
    }

    @Override
    public List<TicketStatusCount> getAllStatusBasedTicketsCountForHelpdesk() {
        return ticketApprovalDAO.getTicketsCountOfHelpDeskBasedOnStatus();
    }
}
