package com.metacube.helpdesk.dao;

import java.util.List;

import com.metacube.helpdesk.model.ItResource;
import com.metacube.helpdesk.model.ResourceCategory;

public interface ResourceDAO {

    List<ItResource> getAllCategoryBasedResources(ResourceCategory resourceCategory);

}
