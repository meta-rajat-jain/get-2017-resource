package com.metacube.helpdesk.dao;



import java.util.List;

import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Status;

public interface OrganisationDAO {
    Status create(Organisation organisation);
    Organisation getByDomain(String domain);
    List<Organisation> getAll();
    List<String> getAllOrganisationDomains();
    Organisation getByName(String orgName);
}
