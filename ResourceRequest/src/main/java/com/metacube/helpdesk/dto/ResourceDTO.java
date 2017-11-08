package com.metacube.helpdesk.dto;

public class ResourceDTO {
    private int resourceId;
    private String resourceName;
    private String resourceCategoryName;
    
    
    
    public ResourceDTO() {
       
    }
    public ResourceDTO(int resourceId, String resourceName,
            String resourceCategoryName) {
      
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceCategoryName = resourceCategoryName;
    }
    public int getResourceId() {
        return resourceId;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getResourceCategoryName() {
        return resourceCategoryName;
    }
    public void setResourceCategoryName(String resourceCategoryName) {
        this.resourceCategoryName = resourceCategoryName;
    }
    
    
    
}
