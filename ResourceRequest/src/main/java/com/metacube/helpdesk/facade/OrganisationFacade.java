package com.metacube.helpdesk.facade;

import java.util.List;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.utility.Response;

public interface OrganisationFacade {
    Response create(OrganisationDTO organisationDTO);

    List<OrganisationDTO> getAllOrganisation();
    
    List<String> getAllOrganisationDomains();
}
