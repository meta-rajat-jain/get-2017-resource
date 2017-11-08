package com.metacube.helpdesk.dto;

public class ResourceCategoryDTO {
    private int categoryId;
    private String categoryName;
    private String parentCategory;

    public ResourceCategoryDTO() {
    }

    public ResourceCategoryDTO(int categoryId, String categoryName,
            String parentCategory) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
}
