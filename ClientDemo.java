package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;

public class ClientDemo {
    public static void main(String[] args) {
        // Initialize Hibernate
        try (Session session = new Configuration()
                .configure("hibernate.cfg.xml") // Use Hibernate configuration file
                .addAnnotatedClass(Department.class)
                .buildSessionFactory()
                .openSession()) {

            Transaction transaction = session.beginTransaction();

            // Insert sample records
            insertSampleDepartments(session);

            // Update department name and location using HQL
            int departmentId = 1;
            String newName = "Human Resources";
            String newLocation = "Building B";
            updateDepartment(session, departmentId, newName, newLocation);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleDepartments(Session session) {
        if (session.createQuery("from Department").getResultList().isEmpty()) {
            session.save(new Department("IT", "Building A", "Alice"));
            session.save(new Department("Finance", "Building C", "Bob"));
            session.save(new Department("Marketing", "Building D", "Charlie"));
            System.out.println("Sample departments added.");
        } else {
            System.out.println("Sample departments already exist.");
        }
    }

    private static void updateDepartment(Session session, int departmentId, String newName, String newLocation) {
        String hql = "update Department set name = ?1, location = ?2 where departmentId = ?3";
        Query<?> query = session.createQuery(hql);
        query.setParameter(1, newName);
        query.setParameter(2, newLocation);
        query.setParameter(3, departmentId);

        int rowsAffected = query.executeUpdate();
        System.out.println(rowsAffected + " row(s) updated.");
    }
}
