package superheroes.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.CreatorDao;
import superheroes.entity.Creator;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class CreatorDaoHibernateImpl implements CreatorDao {

    private static final Logger LOGGER = LogManager.getLogger(CreatorDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCreator(Creator creator) {
        LOGGER.trace("IN: creator=" + creator);
        Session session = sessionFactory.getCurrentSession();
        session.save(creator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCreator(Creator creator) {
        LOGGER.trace("IN: creator=" + creator);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(creator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeCreator(Creator creator) {
        LOGGER.trace("IN: creator=" + creator);
        Session session = sessionFactory.getCurrentSession();
        session.merge(creator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeCreators(List<Creator> creators) {
        LOGGER.trace("IN: creators.size=" + creators == null ? null : creators.size());
        Session session = sessionFactory.getCurrentSession();
        for (Creator creator : creators) {
            session.merge(creator);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCreator(Creator creator) {
        LOGGER.trace("IN: creator=" + creator);
        Session session = sessionFactory.getCurrentSession();
        session.delete(creator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Creator getCreatorById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Creator creator = (Creator) session.get(Creator.class, id);
        initializeCreator(creator);
        return creator;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getAllCreators() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Creator");
        List<Creator> creators = query.list();
        LOGGER.trace("OUT: creators.size=" + creators.size());
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Creator getCreatorByName(String name) {
        LOGGER.trace("IN: name=" + name);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Creator.class);
        Creator creator = (Creator) criteria.setMaxResults(1).add(Restrictions.like("fullName", name)).uniqueResult();
        return creator;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreatorsByPopularity(Integer limit) {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Creator.class);
        List<Creator> creators = criteria.setMaxResults(limit).setReadOnly(true).addOrder(Order.desc("popularity")).list();
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreators(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Creator.class);
        List<Creator> creators = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreatorsByName(String name) {
        LOGGER.trace("IN: name=" + name);
        name = "%" + name + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Creator.class);
        List<Creator> creators = criteria.setMaxResults(10).add(Restrictions.like("fullName", name)).list();
        for (Creator creator : creators) {
            initializeCreator(creator);
        }
        return creators;
    }

    private Creator initializeCreator(Creator creator) {
        LOGGER.trace("IN");
        if (creator != null) {
            Hibernate.initialize(creator.getEvents());
            Hibernate.initialize(creator.getSeries());
            Hibernate.initialize(creator.getStories());
            Hibernate.initialize(creator.getComics());
            Hibernate.initialize(creator.getUrls());
        }
        return creator;
    }
}
