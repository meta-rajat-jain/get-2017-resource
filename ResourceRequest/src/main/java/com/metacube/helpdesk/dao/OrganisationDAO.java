package com.metacube.helpdesk.dao;



import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Status;

public interface OrganisationDAO {
    Status create(Organisation organisation);
    Organisation get(String domain);
}
