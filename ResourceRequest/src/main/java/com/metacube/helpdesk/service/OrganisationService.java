package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Response;

public interface OrganisationService {

    Response create(OrganisationDTO organisationDTO);

    List<OrganisationDTO> getAllOrganisation();
    
    List<String> getAllOrganisationDomains();

    Organisation getOrganisationFromUserName(String userName);

}
