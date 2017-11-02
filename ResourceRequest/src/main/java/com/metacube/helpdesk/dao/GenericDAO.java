package com.metacube.helpdesk.dao;

public class GenericDAO<T> {
    private Class<T> modelClass;

    public Class<T> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public GenericDAO(Class<T> modelClass) {
        this.modelClass = modelClass;
    }
    
}
