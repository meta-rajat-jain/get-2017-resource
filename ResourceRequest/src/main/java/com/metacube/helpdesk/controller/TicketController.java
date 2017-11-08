package com.metacube.helpdesk.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.ResourceDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.ResourceCategory;
import com.metacube.helpdesk.service.ResourceService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.service.TicketService;

@CrossOrigin
@Controller
@RequestMapping(value = "/ticket")
public class TicketController {

    @Resource
    ResourceService resourceService;
    
    
    
    @RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
    public @ResponseBody List<ResourceCategoryDTO> getAllResourceCategory(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        return resourceService.getAllResourceCategory(authorisationToken, username);
    }
    
    @RequestMapping(value = "/getAllCategoryBasedResources", method = RequestMethod.POST)
    public @ResponseBody List<ResourceDTO> getAllCategoryBasedResources(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody ResourceDTO resource) {
        return resourceService.getResourcesBasedOnCategory(authorisationToken, username, resource.getResourceCategoryName());
    }
    
}
