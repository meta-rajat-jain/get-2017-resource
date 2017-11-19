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
import com.metacube.helpdesk.vo.GetResponse;

@Service("organisationService")
public class OrganisationServiceImpl implements OrganisationService {

    @Resource
    LoginDAO loginDAO;
    @Resource
    OrganisationDAO organisationDAO;
    @Resource
    LoginService loginService;

    public Response validateOrganisationObject(OrganisationDTO organisationDTO) {
        if (Validation.isNull(organisationDTO.getName())
                || Validation.isNull(organisationDTO.getLogin())
                || Validation.isNull(organisationDTO.getContactNumber())
                || Validation.isNull(organisationDTO.getLogin().getUsername())
                || Validation.isNull(organisationDTO.getLogin().getPassword())
                || Validation.isNull(organisationDTO.getDomain())
                || Validation.isEmpty(organisationDTO.getName())
                || Validation.isEmpty(organisationDTO.getContactNumber())
                || Validation.isEmpty(organisationDTO.getDomain())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        if (!Validation.validateInput(organisationDTO.getDomain(),
                Constants.DOMAIN_REGEX)) {
            return new Response(0, null, MessageConstants.INVALID_DOMAIN);
        }
        if (!Validation.validateInput(organisationDTO.getLogin().getUsername(),
                Constants.EMAILREGEX)) {
            return new Response(0, null, MessageConstants.INVALID_EMAIL_ADDRESS);
        }
        if (!Validation
                .validateInput(organisationDTO.getName(), Constants.NAME)) {
            return new Response(0, null, MessageConstants.INVALID_NAME);
        }
        if (!Validation.validateInput(organisationDTO.getContactNumber(),
                Constants.CONTACT_NUMBER_REGEX)) {
            return new Response(0, null,
                    MessageConstants.INVALID_CONTACT_NUMBER);
        }
        return null;
    }

    @Override
    public Response create(OrganisationDTO organisationDTO) {
        Organisation organisation = null;
        Response response = validateOrganisationObject(organisationDTO);
        if (response != null) {
            return response;
        } else {
            if (loginDAO.get(organisationDTO.getLogin().getUsername()) != null) {
                return new Response(2, null,
                        MessageConstants.USERNAME_ALREADY_EXIST);
            }
            if (organisationDAO.getByDomain(organisationDTO.getDomain()) != null) {
                return new Response(2, null,
                        MessageConstants.DOMAIN_ALREADY_EXIST);
            }
            if (organisationDAO.getByName(organisationDTO.getName()) != null) {
                return new Response(2, null,
                        MessageConstants.ORGANISATION_ALREADY_EXIST);
            }
            organisationDTO.getLogin().setEnabled(true);
            LogIn logIn = loginService.createLogIn(organisationDTO.getLogin());
            if (loginDAO.create(logIn).equals(Status.Success)) {
                organisation = dtoToModel(organisationDTO);
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
            // creation of default helpdesk account
            // to be made a new method
            LogIn logInHelpdesk = new LogIn();
            logInHelpdesk.setUsername("ithelpdesk@"
                    + organisationDTO.getDomain());
            try {
                logInHelpdesk.setPassword(SimpleMD5
                        .hashingWithConstantSalt(organisationDTO.getLogin()
                                .getPassword()));
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
            logInHelpdesk.setAuthorisationToken(null);
            logInHelpdesk.setEnabled(true);
            if (!loginDAO.create(logInHelpdesk).equals(Status.Success)) {
                return new Response(0, null,
                        MessageConstants.HELPDESK_ACCOUNT_NOT_CREATED);
            }
            if (organisationDAO.create(organisation).equals(Status.Success)) {
                return new Response(1, null, MessageConstants.ACCOUNT_CREATED);
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
        }
    }

    @Override
    public Organisation getOrganisationFromUserName(String username) {
        String[] orgDomainFromUsername = username.split("@");
        return organisationDAO.getByDomain(orgDomainFromUsername[1]);
    }

    @Override
    public GetResponse<List<OrganisationDTO>> getAllOrganisation() {
        List<Organisation> orgList = organisationDAO.getAll();
        if (orgList == null) {
            return new GetResponse<List<OrganisationDTO>>(new Response(0, null,
                    MessageConstants.NO_CONTENT), null);
        }
        List<OrganisationDTO> orgDTOList = new ArrayList<OrganisationDTO>();
        for (Organisation org : orgList) {
            orgDTOList.add(modelToDTO(org));
        }
        return new GetResponse<List<OrganisationDTO>>(new Response(1, null,
                "fetch List from payload"), orgDTOList);
    }

    @Override
    public List<String> getAllOrganisationDomains() {
        // TODO Auto-generated method stub
        List<String> orgDomainsList = organisationDAO
                .getAllOrganisationDomains();
        return orgDomainsList;
    }

    private OrganisationDTO modelToDTO(Organisation organisation) {
        // TODO Auto-generated method stub
        OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.setContactNumber(organisation.getContactNumber());
        organisationDTO.setDomain(organisation.getDomain());
        organisationDTO.setName(organisation.getOrgName());
        organisationDTO.setLogin(loginService.modelToDto(organisation
                .getUsername()));
        return organisationDTO;
    }

    private Organisation dtoToModel(OrganisationDTO organisationDTO) {
        Organisation organisation = new Organisation();
        organisation.setContactNumber(organisationDTO.getContactNumber());
        organisation.setDomain(organisationDTO.getDomain());
        organisation.setOrgName(organisationDTO.getName());
        organisation.setUsername(loginDAO.get((organisationDTO.getLogin())
                .getUsername()));
        return organisation;
    }
}
