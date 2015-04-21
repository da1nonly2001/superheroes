package superheroes.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.UrlDao;
import superheroes.entity.Url;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class UrlDaoHibernateImpl implements UrlDao {

    private static final Logger LOGGER = LogManager.getLogger(UrlDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUrl(Url url) {
        LOGGER.trace("IN: url=" + url);
        Session session = sessionFactory.getCurrentSession();
        session.save(url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUrl(Url url) {
        LOGGER.trace("IN: url=" + url);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUrl(Url url) {
        LOGGER.trace("IN: url=" + url);
        Session session = sessionFactory.getCurrentSession();
        session.delete(url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Url getUrlById(Long id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Url url = (Url) session.get(Url.class, id);
        return url;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Url> getAllUrls() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Url");
        List<Url> urls = query.list();
        LOGGER.trace("OUT: urls.size=" + urls.size());
        return urls;
    }
}
