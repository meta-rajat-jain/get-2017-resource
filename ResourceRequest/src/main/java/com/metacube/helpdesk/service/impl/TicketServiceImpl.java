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
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.model.TicketApproval;
import com.metacube.helpdesk.model.TicketHistory;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TicketService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
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
    LoginService loginService;

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
        ticket.setLastUpdatedBy(ticketDTO
                .getLastUpdatedByUsername());
        ticket.setCorrespondingTeam(teamDAO.getTeamByName(ticketDTO.getTeamName()));
        return ticket;
    }

    @Override
    public Response saveTicket(String authorisationTokenFromLogin,
            String username, TicketDTO ticketDTO) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return new Response(0, null, "One or more header is missing");
        }
        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {
            ticketDTO.setLastUpdatedByUsername(username);
            ticketDTO.setRequesterName(username);
            if(ticketDTO.getTeamName()==null){
            	if(employeeDAO.getEmployee(loginDAO.get(ticketDTO.getRequesterName())).getDesignation().equals(Designation.Manager)){
            		 String[] splittedUsername = username.split("@");
            	        String teamName = "TEAM " + splittedUsername[0];
            	        ticketDTO.setTeamName(teamName);
            	        ticketDTO.setStatus(Constants.TICKET_APPROVED);
            	}else{
            		 return new Response(1, authorisationTokenFromLogin,
                             "You are not authorize to generate ticket");
                 
            	}
            }
            Ticket ticket = dtoToModel(ticketDTO);
            ticket.setLastDateOfUpdate(new Date());
            ticket.setRequestDate(new Date());
            int ticketNo=ticketDAO.saveTicket(ticket) ;
            ticket.setTicketNo(ticketNo);
            saveTicketApproval( ticket);
            return new Response(0, authorisationTokenFromLogin,
                        "Generated Ticket Successfully");

        }
        return new Response(0, null, MessageConstants.UNAUTHORISED_USER);
    }

    @Override
    public Status saveTicketHistory(Ticket ticket) {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setTicket(ticket);
        ticketHistory.setPreviousStatus(ticket.getStatus());
        ticketHistory.setPreviousPriority(ticket.getPriority());
        ticketHistory.setPreviousComment(ticket.getComment());
        ticketHistory.setLastUpdatedBy(ticket.getLastUpdatedBy());
        ticketHistory.setLastDateOfUpdate(ticket.getLastDateOfUpdate());

        return ticketHistoryDAO.saveTicketHistory(ticketHistory);
    }
    
    @Override
    public Status saveTicketApproval(Ticket ticket) {
        TicketApproval ticketApproval = new TicketApproval();
        if(ticket.getRequester().getDesignation().equals(Designation.Manager)){
        		ticketApproval.setApproveStatus(Constants.TICKET_APPROVED);
        	 ticketApproval.setHelpdeskApprovalStatus(Constants.TICKET_APPROVED);
        }else{
        	 ticketApproval.setApproveStatus(Constants.TICKET_OPEN);
        	 ticketApproval.setHelpdeskApprovalStatus(Constants.TICKET_OPEN);
        }
        ticketApproval.setApprover(ticket.getCorrespondingTeam().getManager());
       
        ticketApproval.setTicketNo(ticket);
        return ticketApprovalDAO.saveTicketApproval(ticketApproval);
    }
    
    @Override
    public List<TicketDTO> getAllStatusBasedTicketsOfUserr(
    String authorisationTokenFromLogin, String username, String status) {
       	List<TicketDTO> allTicketsDTO;
       	
       	if (loginService
                   .authorizeRequest(authorisationTokenFromLogin, username)) {
       	allTicketsDTO = new ArrayList<TicketDTO>();
       	List<Ticket> allTickets = ticketDAO
                       .getTicketsOfEmployeeBasedOnStatus(
                               employeeDAO.getEmployee(loginDAO.get(username)),
                               status);
       	for (Ticket ticket : allTickets) {
                    allTicketsDTO.add(modelToDto(ticket));
                }
                return allTicketsDTO;
       	}
    return null;
    }

    @Override
    public Response updateTicket(String authorisationTokenFromLogin,
            String username, TicketDTO ticketDTO) {
        Ticket ticket = ticketDAO.getTicket(ticketDTO.getTicketNo());
        if (ticket != null) {
            if (saveTicketHistory(ticket).equals(Status.Success)) {
                ticketDTO.setLastUpdatedByUsername(username);
                ticketDTO.setLastDateOfUpdate(new Date());
                ticketDTO.setRequestDate(ticket.getRequestDate());
                if (ticketDAO.updateTicket(dtoToModel(ticketDTO)).equals(
                        Status.Success)) {
                    return new Response(0, authorisationTokenFromLogin,
                            "Ticket has been updated successfully");
                }
            }
        }

        return new Response(0, null, "Ticket with this ticket no not exist");
    }

    @Override
    public List<TicketDTO> getAllTicketsOfEmployee(
            String authorisationTokenFromLogin, String username,
            String employeeName) {
        List<TicketDTO> allTicketsDTO;

        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {
            allTicketsDTO = new ArrayList<TicketDTO>();
            List<Ticket> allTickets = ticketDAO
                    .getTicketsGeneratedByEmployee(employeeDAO
                            .getEmployee(loginDAO.get(employeeName)));
            for (Ticket ticket : allTickets) {
                allTicketsDTO.add(modelToDto(ticket));
            }
            return allTicketsDTO;
        }
        return null;
    }


    @Override
    public List<TicketDTO> getAllStatusBasedTicketsForApprover(
            String authorisationTokenFromLogin, String username, String status) {
        List<TicketDTO> allTicketsDTO;

        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {
            allTicketsDTO = new ArrayList<TicketDTO>();
            List<Ticket> allTickets = ticketApprovalDAO
                    .getTicketsOfApproverBasedOnStatus(
                            employeeDAO.getEmployee(loginDAO.get(username)),
                            status);
            for (Ticket ticket : allTickets) {
                allTicketsDTO.add(modelToDto(ticket));
            }
            return allTicketsDTO;
        }
        return null;
    }

    @Override
    public List<TicketDTO> getAllHelpdeskStatusBasedTickets(
            String authorisationTokenFromLogin, String username, String status) {
        List<TicketDTO> allTicketsDTO;

        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {
            allTicketsDTO = new ArrayList<TicketDTO>();
            List<Ticket> allTickets = ticketApprovalDAO
                    .getAllHelpdeskStatusBasedTickets(status);
            for (Ticket ticket : allTickets) {
                allTicketsDTO.add(modelToDto(ticket));
            }
            return allTicketsDTO;
        }
        return null;
    }

    @Override
    public Response ticketUpdateApprovalChange(String authorisationTokenFromLogin, String username,
            TicketDTO ticketDTO) {
        TicketApproval approvalObjectForCurrentTicket = ticketApprovalDAO
                .get(ticketDTO.getTicketNo());
        //System.out.println(ticketDTO.getTicketNo());
        if (username.equals(
                approvalObjectForCurrentTicket.getApprover().getUsername()
                        .getUsername())||username.toLowerCase().contains("helpdesk")||username.equalsIgnoreCase(ticketDTO.getRequesterName())) {
               approvalObjectForCurrentTicket.setApproveStatus(ticketDTO.getStatus());
               approvalObjectForCurrentTicket.setHelpdeskApprovalStatus(ticketDTO.getStatus());
               ticketApprovalDAO.update(approvalObjectForCurrentTicket);
               
        }
        return updateTicket(authorisationTokenFromLogin, username, ticketDTO);
    }
    

	
	@Override
	public List<TicketStatusCount> getAllTicketCountOnStatus(
			String authorisationToken, String username) {
		
		return ticketDAO.getTicketCountForStatusOfEmployee(employeeDAO.getEmployee(loginDAO.get(username)));
	}
	
	@Override
	public TicketDTO getTicket(String authorisationToken, String username,
			TicketDTO ticketDTO) {
		return modelToDto(ticketDAO.getTicket(ticketDTO.getTicketNo()));

	}

	@Override
	public List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
			String authorisationToken, String username) {
		return ticketApprovalDAO.getTicketsCountOfApproverBasedOnStatus(employeeDAO.getEmployee(loginDAO.get(username)));
	}
	
	@Override
    public List<TicketStatusCount> getAllStatusBasedTicketsCountForHelpdesk(
            String authorisationToken, String username) {
        return ticketApprovalDAO.getTicketsCountOfHelpDeskBasedOnStatus();
    }
    
}
