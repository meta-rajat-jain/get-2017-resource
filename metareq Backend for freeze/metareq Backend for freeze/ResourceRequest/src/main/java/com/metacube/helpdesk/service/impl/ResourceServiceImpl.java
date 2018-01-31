package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.metacube.helpdesk.dao.ResourceCategoryDAO;
import com.metacube.helpdesk.dao.ResourceDAO;
import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.ResourceDTO;
import com.metacube.helpdesk.model.ItResource;
import com.metacube.helpdesk.model.ResourceCategory;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.ResourceService;
import com.metacube.helpdesk.utility.Validation;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    @Resource
    ResourceCategoryDAO resourceCategoryDAO;
    @Resource
    ResourceDAO resourceDAO;
    @Resource
    LoginService loginService;

    @Override
    public ResourceCategoryDTO modelToDto(ResourceCategory resourceCategory) {
        if (resourceCategory == null) {
            return null;
        }
        ResourceCategoryDTO resourceCategoryDTO = new ResourceCategoryDTO();
        resourceCategoryDTO.setCategoryId(resourceCategory.getCategoryId());
        resourceCategoryDTO.setCategoryName(resourceCategory.getCategoryName());
        resourceCategoryDTO.setParentCategory(resourceCategory
                .getParentCategory() != null ? resourceCategory
                .getParentCategory().getCategoryName() : null);
        return resourceCategoryDTO;
    }

    @Override
    public ResourceCategory dtoToModel(ResourceCategoryDTO resourceCategoryDTO) {
        if (resourceCategoryDTO == null) {
            return null;
        }
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setCategoryId(resourceCategoryDTO.getCategoryId());
        resourceCategory.setCategoryName(resourceCategoryDTO.getCategoryName());
        resourceCategory.setParentCategory(resourceCategoryDAO
                .getResourceCategoryByName(resourceCategoryDTO
                        .getCategoryName()));
        return resourceCategory;
    }

    @Override
    public ResourceDTO modelToDto(ItResource resource) {
        if (resource == null) {
            return null;
        }
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setResourceId(resource.getResourceId());
        resourceDTO.setResourceName(resource.getResourceName());
        resourceDTO.setResourceCategoryName(resource.getResourceCategory()
                .getCategoryName());
        return resourceDTO;
    }

    @Override
    public ItResource dtoToModel(ResourceDTO resourceDTO) {
        if (resourceDTO == null) {
            return null;
        }
        ItResource resource = new ItResource();
        resource.setResourceId(resourceDTO.getResourceId());
        resource.setResourceName(resourceDTO.getResourceName());
        resource.setResourceCategory(resourceCategoryDAO
                .getResourceCategoryByName(resourceDTO
                        .getResourceCategoryName()));
        return resource;
    }

    @Override
    public List<ResourceCategoryDTO> getAllResourceCategory() {
        List<ResourceCategoryDTO> allResourceCategoriesDTO = new ArrayList<ResourceCategoryDTO>();
        for (ResourceCategory resourceCategory : resourceCategoryDAO
                .getAllResourceCategory()) {
            allResourceCategoriesDTO.add(modelToDto(resourceCategory));
        }
        return allResourceCategoriesDTO;
    }

    @Override
    public List<ResourceDTO> getResourcesBasedOnCategory(String categoryName) {
        List<ResourceDTO> allCategoryBasedResources = new ArrayList<ResourceDTO>();
        if (Validation.isNull(categoryName) || Validation.isEmpty(categoryName)) {
            return allCategoryBasedResources;
        }
        for (ItResource resource : resourceDAO
                .getAllCategoryBasedResources(resourceCategoryDAO
                        .getResourceCategoryByName(categoryName))) {
            allCategoryBasedResources.add(modelToDto(resource));
        }
        return allCategoryBasedResources;
    }
}
