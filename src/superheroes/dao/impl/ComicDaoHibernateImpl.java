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
import superheroes.dao.ComicDao;
import superheroes.entity.Comic;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class ComicDaoHibernateImpl implements ComicDao {

    private static final Logger LOGGER = LogManager.getLogger(ComicDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Attempts to represent the comic entity in the database.
     * Note that you should only use this method if you are certain
     * that this entity don't already exist in the database, in
     * which case this is the fastest way to save the entity.  If
     * you are unsure, then use the updateComic method instead.
     *
     * @param comic The comic entity object to be saved
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComic(Comic comic) {
        LOGGER.trace("IN: comic=" + comic);
        Session session = sessionFactory.getCurrentSession();
        session.save(comic);
    }

    /**
     * This update method will save a comic entity in the database.
     * Note that if the comic entity does not already exist in the
     * database, then it will be saved, otherwise the existing one
     * will be updated with the values provided in the character
     * object.  This convenience comes at a slight penalty to performance,
     * so if you are certain that the object does not already exist,
     * then it is better to use the saveComic method.
     *
     * @param comic The comic entity object to be updated or saved
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateComic(Comic comic) {
        LOGGER.trace("IN: comic=" + comic);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(comic);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeComic(Comic comic) {
        LOGGER.trace("IN: comic=" + comic);
        Session session = sessionFactory.getCurrentSession();
        session.merge(comic);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeComics(List<Comic> comics) {
        LOGGER.trace("IN: comics.size=" + comics == null ? null : comics.size());
        Session session = sessionFactory.getCurrentSession();
        for (Comic comic : comics) {
            session.merge(comic);
        }
    }

    /**
     * Deletes the comic entity from its representation in
     * the database.  Note that this does not nullify the
     * comic Java reference, which will continue to live
     * while in scope.
     *
     * @param comic The comic entity to be deleted from the database
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteComic(Comic comic) {
        LOGGER.trace("IN: comic=" + comic);
        Session session = sessionFactory.getCurrentSession();
        session.delete(comic);
    }

    /**
     * Returns a specific comic entity object found by id.
     * If no object is found, then a null value is returned.
     *
     * @param id The comic id the desired comic entity possess
     * @return Returns a Comic entity object or null
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Comic getComicById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Comic comic = (Comic) session.get(Comic.class, id);
        initializeComic(comic);
        return comic;
    }

    /**
     * Uses HQL to return all comic entities managed by Hibernate
     *
     * @return Returns a list of comic entities
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getAllComics() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Comic");
        List<Comic> comics = query.list();
        LOGGER.trace("OUT: comics.size=" + comics.size());
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Comic getComicByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Comic.class);
        Comic comic = (Comic) criteria.setMaxResults(1).add(Restrictions.like("title", title)).uniqueResult();
        return comic;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComicsByPopularity(Integer limit) {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Comic.class);
        List<Comic> comics = criteria.setMaxResults(limit).setReadOnly(true).addOrder(Order.desc("popularity")).list();
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComics(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Comic.class);
        List<Comic> comics = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComicsByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        title = "%" + title + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Comic.class);
        List<Comic> comics = criteria.setMaxResults(10).add(Restrictions.like("title", title)).list();
        for (Comic comic : comics) {
            initializeComic(comic);
        }
        return comics;
    }

    private Comic initializeComic(Comic comic) {
        LOGGER.trace("IN");
        if (comic != null) {
            Hibernate.initialize(comic.getUrls());
            Hibernate.initialize(comic.getStories());
            Hibernate.initialize(comic.getSeries());
            Hibernate.initialize(comic.getEvents());
            Hibernate.initialize(comic.getCharacters());
            Hibernate.initialize(comic.getCreators());
            Hibernate.initialize(comic.getTextObjects());
        }
        return comic;
    }
}
