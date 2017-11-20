package com.metacube.helpdesk.service;

import java.util.List;
import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.ResourceDTO;
import com.metacube.helpdesk.model.ItResource;
import com.metacube.helpdesk.model.ResourceCategory;

public interface ResourceService {

    ResourceCategoryDTO modelToDto(ResourceCategory resourceCategory);

    ResourceCategory dtoToModel(ResourceCategoryDTO resourceCategoryDTO);

    List<ResourceCategoryDTO> getAllResourceCategory();

    ResourceDTO modelToDto(ItResource resource);

    ItResource dtoToModel(ResourceDTO resourceDTO);

    List<ResourceDTO> getResourcesBasedOnCategory(String categoryName);
}
