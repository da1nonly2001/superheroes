package superheroes.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.KeyDao;
import superheroes.entity.Key;

/**
 * Created by catop on 4/7/15.
 */
@Component
public class KeyDaoHibernateImpl implements KeyDao {

    private static final Logger LOGGER = LogManager.getLogger(SeriesDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Key getKey(String type) {
        LOGGER.trace("IN: type=" + type);
        Session session = sessionFactory.getCurrentSession();
        Key key = (Key) session.get(Key.class, type);
        return key;
    }
}
