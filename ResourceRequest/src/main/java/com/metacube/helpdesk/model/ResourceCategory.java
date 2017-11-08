package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ResourceCategory")
public class ResourceCategory implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
	@Column(name="categoryId")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int categoryId;
	
	@Column(name="categoryName", nullable = false)
	private String categoryName;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="parentCategory")
	private ResourceCategory parentCategory;
	
	
	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public ResourceCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ResourceCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public ResourceCategory(String categoryName, ResourceCategory parentCategory) {
	
		this.categoryName = categoryName;
		this.parentCategory = parentCategory;
	}

	public ResourceCategory() {
		
	}	
}
