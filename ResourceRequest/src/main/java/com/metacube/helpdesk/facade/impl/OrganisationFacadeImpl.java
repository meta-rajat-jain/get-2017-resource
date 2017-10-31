package com.metacube.helpdesk.facade.impl;

import java.util.List;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.facade.OrganisationFacade;
import com.metacube.helpdesk.utility.Response;

public class OrganisationFacadeImpl implements OrganisationFacade{
    


    @Override
    public Response create(OrganisationDTO organisationDTO) {
        // TODO Auto-generated method stub
        return create(organisationDTO);
    }

    @Override
    public List<OrganisationDTO> getAllOrganisation() {
        // TODO Auto-generated method stub
        return getAllOrganisation();
    }

    @Override
    public List<String> getAllOrganisationDomains() {
        // TODO Auto-generated method stub
        return getAllOrganisationDomains();
    }

    }
