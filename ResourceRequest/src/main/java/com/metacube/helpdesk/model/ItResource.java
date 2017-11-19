package com.metacube.helpdesk.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ItResource")
public class ItResource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "resourceId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int resourceId;
    @Column(name = "resourceName", nullable = false)
    private String resourceName;
    @ManyToOne
    @JoinColumn(name = "resourceCategory", nullable = false)
    private ResourceCategory resourceCategory;

    public ItResource() {
    }

    public ItResource( String resourceName, ResourceCategory resourceCategory ) {
        this.resourceName = resourceName;
        this.resourceCategory = resourceCategory;
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

    public ResourceCategory getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(ResourceCategory resourceCategory) {
        this.resourceCategory = resourceCategory;
    }
}
