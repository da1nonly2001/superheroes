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
import superheroes.dao.StoryDao;
import superheroes.entity.Story;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class StoryDaoHibernateImpl implements StoryDao {

    private static final Logger LOGGER = LogManager.getLogger(StoryDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveStory(Story story) {
        LOGGER.trace("IN: story.id=" + story == null ? null : story.getId());
        Session session = sessionFactory.getCurrentSession();
        session.save(story);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStory(Story story) {
        LOGGER.trace("IN: story.id=" + story == null ? null : story.getId());
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(story);
    }

    /**
     * If you have a transient entity that needs to
     * be reattached and persisted, then use this
     * method.
     *
     * @param story Transient or detached entity that needs to be persisted
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeStory(Story story) {
        LOGGER.trace("IN: story.id=" + story == null ? null : story.getId());
        Session session = sessionFactory.getCurrentSession();
        session.merge(story);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeStories(List<Story> stories) {
        LOGGER.trace("IN: stories.size=" + stories == null ? null : stories.size());
        Session session = sessionFactory.getCurrentSession();
        for (Story story : stories) {
            session.merge(story);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteStory(Story story) {
        LOGGER.trace("IN: story=" + story);
        Session session = sessionFactory.getCurrentSession();
        session.delete(story);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Story getStoryById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Story story = (Story) session.get(Story.class, id);
        initializeStory(story);
        return story;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getAllStories() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Story");
        List<Story> stories = query.list();
        LOGGER.trace("OUT: stories.size=" + stories.size());
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Story getStoryByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Story.class);
        Story story = (Story) criteria.setMaxResults(1).add(Restrictions.like("title", title)).uniqueResult();
        return story;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStoriesByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Story.class);
        List<Story> stories = criteria.setMaxResults(limit).setReadOnly(true).addOrder(Order.desc("popularity")).list();
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStories(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Story.class);
        List<Story> stories = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStoriesByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        title = "%" + title + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Story.class);
        List<Story> stories = criteria.setMaxResults(10).add(Restrictions.like("title", title)).list();
        for (Story story : stories) {
            initializeStory(story);
        }
        return stories;
    }

    private Story initializeStory(Story story) {
        LOGGER.trace("IN");
        if (story != null) {
            Hibernate.initialize(story.getEvents());
            Hibernate.initialize(story.getCreators());
            Hibernate.initialize(story.getCharacters());
            Hibernate.initialize(story.getComics());
            Hibernate.initialize(story.getSeries());
        }
        return story;
    }
}
