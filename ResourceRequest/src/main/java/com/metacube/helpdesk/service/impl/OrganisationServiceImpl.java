package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Status;

@Service("organisationService")
public class OrganisationServiceImpl implements OrganisationService {
    @Resource
    LoginDAO loginDAO;
    
    @Resource
    OrganisationDAO organisationDAO;
    
    @Resource
    LoginService loginService;
    
    @Override
    public Response create(OrganisationDTO organisationDTO){
        int status=0;
        String authorisationToken=null;
        String message = null;
        Organisation organisation=new Organisation();
        organisation.setContactNumber(organisationDTO.getContactNumber());
        organisation.setDomain(organisationDTO.getDomain());
        organisation.setOrgName(organisationDTO.getName());
        LogIn logIn=new LogIn();
        logIn.setUsername(organisationDTO.getLogin().getUsername());
        try {
         logIn.setPassword(SimpleMD5.hashingWithConstantSalt(organisationDTO.getLogin().getPassword()));
         } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
             e.printStackTrace();
         }
        logIn.setAuthorisationToken(null);
        
        Status addFlag = loginDAO.create(logIn);
        
        if(addFlag.equals(Status.Success)) {
            organisation.setUsername(logIn);
            
        } else {
            status = 0;
           message = MessageConstants.ACCOUNT_NOT_CREATED;
        }
        addFlag = organisationDAO.create(organisation);
        
        if(addFlag.equals(Status.Success)) {
            status = 1;
            message = MessageConstants.ACCOUNT_CREATED;
            
        } else {
            status = 0;
           message = MessageConstants.ACCOUNT_NOT_CREATED;
        }
        return new Response(status,authorisationToken,message);
    }

    @Override
    public List<OrganisationDTO> getAllOrganisation() {
        List<Organisation> orgList = organisationDAO.getAll();
        List<OrganisationDTO> orgDTOList = new ArrayList<OrganisationDTO>();
        for(Organisation org : orgList) {
            orgDTOList.add(modelToDTO(org));
        }
        return orgDTOList;  
    }

    private OrganisationDTO modelToDTO(Organisation organisation) {
        // TODO Auto-generated method stub
        OrganisationDTO organisationDTO=new OrganisationDTO();
        organisationDTO.setContactNumber(organisation.getContactNumber());
        organisationDTO.setDomain(organisation.getDomain());
        organisationDTO.setName(organisation.getOrgName());
        organisationDTO.setLogin(organisation.getUsername());
        
        return organisationDTO;
    }
    
    private Organisation dtoToModel(OrganisationDTO organisationDTO) {
        Organisation organisation=new Organisation();
        organisation.setContactNumber(organisationDTO.getContactNumber());
        organisation.setDomain(organisationDTO.getDomain());
        organisation.setOrgName(organisationDTO.getName());
        organisation.setUsername(loginDAO.get((organisationDTO.getLogin())
                .getUsername()));
        return organisation;
    }
}
