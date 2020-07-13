/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import connection.Connection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Huy Thông
 */
public class HibernateUtility {
    public static final Connection connection = new Connection();
    public static final SessionFactory sessionFactory = connection.getSessionFactory();
    public static final Session session = sessionFactory.openSession(); 
    
    public static SessionFactory factory;
    private HibernateUtility() {}
    
    public static synchronized SessionFactory getSessionFactory() {

        if (factory == null) {
            factory = new Configuration().configure("hibernate.cfg.xml").
                    buildSessionFactory();
        }
        return factory;
    }
}
