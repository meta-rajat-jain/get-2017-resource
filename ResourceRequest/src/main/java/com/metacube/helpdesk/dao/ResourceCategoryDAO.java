package com.metacube.helpdesk.dao;

import java.util.List;

import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.model.ResourceCategory;


public interface ResourceCategoryDAO {

    ResourceCategory getResourceCategoryByName(String categoryName);

    List<ResourceCategory> getAllResourceCategory();

}
