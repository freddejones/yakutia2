package se.freddejones.game.yakutia.dao.impl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

public abstract class AbstractDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
