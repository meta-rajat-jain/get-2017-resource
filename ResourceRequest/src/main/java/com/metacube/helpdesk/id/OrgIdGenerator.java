package com.metacube.helpdesk.id;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class OrgIdGenerator implements IdentifierGenerator {

private String DEFAULT_SEQUENCE_NAME = "orgID";
    
    @Override
    public Serializable generate(SessionImplementor sessionImpl, Object data)
            throws HibernateException {
        Serializable result = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String prefix = "Emp";
            connection = sessionImpl.connection();
            statement = connection.createStatement();                   
             try {  
                 resultSet = statement.executeQuery("call next value for "+DEFAULT_SEQUENCE_NAME);
             } catch(Exception ex) {
                 // if sequence is not found then creating the sequence
                 statement = connection.createStatement();
                 statement.execute("CREATE SEQUENCE "+DEFAULT_SEQUENCE_NAME);
                 System.out.println("Sequece Created successfully. ");
                 resultSet = statement.executeQuery("call next value for "+DEFAULT_SEQUENCE_NAME);
             }
            
            if(resultSet.next()) {
                int nextValue = resultSet.getInt(1);                
                String suffix = String.format("%05d", nextValue + 1);               
                result = prefix.concat(suffix);
                System.out.println("Custom generated Sequence value : "+result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
