package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.vo.GetResponse;

public interface OrganisationService {

    Response create(OrganisationDTO organisationDTO);

    GetResponse<List<OrganisationDTO>> getAllOrganisation();
    
    List<String> getAllOrganisationDomains();

    Organisation getOrganisationFromUserName(String userName);

}
