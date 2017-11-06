package com.metacube.helpdesk.dao.impl;

import java.util.List;






import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.metacube.helpdesk.dao.TeamDAO;
import com.metacube.helpdesk.model.Employee;

import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
@Repository("teamDAO")
@Transactional
public class TeamDAOImpl implements TeamDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
  /*  @Override
    public List<Team> getAllTeamsByHeadList(List<Employee> employeeList) {
        Session session = this.sessionFactory.getCurrentSession();
     // Criteria query
        Criteria cr = session.createCriteria(Team.class).add(Restrictions.in("teamHead", employeeList));              
        List<Team> allTeams = cr.list();
        return allTeams;
    }*/

  
    
    /*@Override
    public List<Employee> getAllEmployeesInTeam(List<Team> teamsUnderHead) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
           Criteria cr = session.createCriteria(EmployeeAssociatedTeam.class).add(Restrictions.eq("teamId", teamsUnderHead));              
           List<Employee> allEmployeesUnderTeam = cr.list();
           return allEmployeesUnderTeam;
    }
*/
    @Override
    public List<Team> getTeamForHead(Employee employee) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Team.class).add(Restrictions.eq("teamHead",employee ));              
        List<Team> teamList =  cr.list();
        return teamList;
    }



    @Override
    public Status createTeam(Team team) {
        System.out.println("In DAO");
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
           System.out.println(team.getOrganisation().getOrgId());
            Criteria cr = session.createCriteria(Team.class);
           
            session.save(team);

        } catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }



    @Override
    public Status addEmployeeToTeam(Employee employee) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(Employee.class);
           
            session.update(employee);
            
        } catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }



    @Override
    public Team getTeamByName(String teamName) {
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(Team.class).add(Restrictions.eq("teamName",teamName ));
           Team team = (Team) cr.uniqueResult();
           return team;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



   

}
