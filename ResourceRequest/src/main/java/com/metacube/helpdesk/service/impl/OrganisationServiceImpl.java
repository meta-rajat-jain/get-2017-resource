package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.utility.Validation;

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
        
        if (Validation.isNull(organisationDTO.getName())
                || Validation.isNull(organisationDTO.getLogin())
                || Validation.isNull(organisationDTO.getContactNumber())
                || Validation.isNull(organisationDTO.getLogin().getUsername())
                || Validation.isNull(organisationDTO.getLogin().getPassword())
                || Validation.isNull(organisationDTO.getDomain())
                || Validation.isEmpty(organisationDTO.getName())
                || Validation.isEmpty(organisationDTO.getContactNumber())
                || Validation.isEmpty(organisationDTO.getDomain())) {
            return new Response(status, authorisationToken,
                    "Please fill all required fields");
        }
        //regex for ac.in also
        if (!Validation.validateInput(organisationDTO.getDomain()
                , Constants.DOMAIN_REGEX)) {
            return new Response(status, authorisationToken,
                    "Incorrect format of Domain");
        }
        
        if (!Validation.validateInput(organisationDTO.getLogin()
                .getUsername(), Constants.EMAILREGEX)) {
            return new Response(status, authorisationToken,
                    "Incorrect format of email");
        }

        if (!Validation.validateInput(organisationDTO.getContactNumber(),
                Constants.CONTACT_NUMBER_REGEX)) {
            return new Response(status, authorisationToken,
                    "Incorrect format of contact number");
        }

        if (loginDAO.get(organisationDTO.getLogin().getUsername()) != null) {
            return new Response(2, authorisationToken,
                    MessageConstants.USERNAME_ALREADY_EXIST);
        }
        
        if (organisationDAO.getByDomain(organisationDTO.getDomain()) != null) {
            return new Response(2, authorisationToken,
                    "organisation with this domain already exist");
        }
        if (organisationDAO.getByName(organisationDTO.getName()) != null) {
            return new Response(2, authorisationToken,
                    "organisation with this name already exist");
        }
        
        LogIn logIn=new LogIn();
        logIn.setUsername(organisationDTO.getLogin().getUsername());
        try {
         logIn.setPassword(SimpleMD5.hashingWithConstantSalt(organisationDTO.getLogin().getPassword()));
         } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
             e.printStackTrace();
         }
        logIn.setAuthorisationToken(null);
        logIn.setEnabled(true);
        
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
    public  Organisation getOrganisationFromUserName(String userName){
        String[] orgDomainFromUsername = userName
                .split("@");
        System.out.println(orgDomainFromUsername[0]+"kh"+orgDomainFromUsername[1]);
        return organisationDAO.getByDomain(orgDomainFromUsername[1]);
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
    
    @Override
    public List<String> getAllOrganisationDomains() {
        // TODO Auto-generated method stub
        List<String> orgDomainsList = organisationDAO.getAllOrganisationDomains();
        return orgDomainsList;
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
