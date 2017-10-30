package com.metacube.helpdesk.service;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.utility.Response;

public interface OrganisationService {

    Response create(OrganisationDTO organisationDTO);

}
