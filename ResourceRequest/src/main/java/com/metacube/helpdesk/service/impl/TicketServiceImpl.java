package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.ResourceCategoryDAO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.model.ResourceCategory;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.service.TicketService;

@Service("ticketService")
public class TicketServiceImpl implements TicketService {
    
    @Resource
    LoginDAO loginDAO;
    
    @Resource
    EmployeeDAO employeeDAO;
    
    @Resource
    ResourceCategoryDAO resourceCategoryDAO;
    
    @Resource
    TicketService ticketService;

    private Object loginService;
    
    @Override
    public TicketDTO modelToDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketNo(ticket.getTicketNo());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setRequestType(ticket.getRequestType());
        ticketDTO.setRequesterName(ticket.getRequester().getUsername().getUsername());
        ticketDTO.setRequestedFor(ticket.getRequestedFor().getUsername().getUsername());
        ticketDTO.setRequestedResource(ticket.getRequestedResource());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setComment(ticket.getComment());
        ticketDTO.setLocation(ticket.getLocation());
        return ticketDTO;
    }
    
    @Override
    public Ticket dtoToModel(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }
        
        Ticket ticket=new Ticket();
        ticket.setTicketNo(ticketDTO.getTicketNo());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setLocation(ticketDTO.getLocation());
        ticket.setRequestType(ticketDTO.getRequestType());
        ticket.setComment(ticketDTO.getComment());
        ticket.setRequestedResource(ticketDTO.getRequestedResource());
        ticket.setRequester(employeeDAO.getEmployee(loginDAO.get(ticketDTO.getRequesterName())));
        ticket.setRequestedFor(employeeDAO.getEmployee(loginDAO.get(ticketDTO.getRequestedFor())));
        
        return ticket;
    }

   

}
