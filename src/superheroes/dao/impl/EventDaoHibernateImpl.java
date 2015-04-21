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
import superheroes.dao.EventDao;
import superheroes.entity.Event;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class EventDaoHibernateImpl implements EventDao {

    private static final Logger LOGGER = LogManager.getLogger(EventDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveEvent(Event event) {
        LOGGER.trace("IN: event=" + event);
        Session session = sessionFactory.getCurrentSession();
        session.save(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEvent(Event event) {
        LOGGER.trace("IN: event=" + event);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeEvent(Event event) {
        LOGGER.trace("IN: event=" + event);
        Session session = sessionFactory.getCurrentSession();
        session.merge(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeEvents(List<Event> events) {
        LOGGER.trace("IN: creators.size=" + events == null ? null : events.size());
        Session session = sessionFactory.getCurrentSession();
        for (Event event : events) {
            session.merge(event);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteEvent(Event event) {
        LOGGER.trace("IN: event=" + event);
        Session session = sessionFactory.getCurrentSession();
        session.delete(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Event getEventById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Event event = (Event) session.get(Event.class, id);
        initializeEvent(event);
        return event;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getAllEvents() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Event");
        List<Event> events = query.list();
        LOGGER.trace("OUT: events.size=" + events.size());
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Event getEventByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Event.class);
        Event event = (Event) criteria.setMaxResults(1).add(Restrictions.like("title", title)).uniqueResult();
        return event;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEventsByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Event.class);
        List<Event> events = criteria.setMaxResults(limit).setReadOnly(true).addOrder(Order.desc("popularity")).list();
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEvents(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Event.class);
        List<Event> events = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEventsByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        title = "%" + title + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Event.class);
        List<Event> events = criteria.setMaxResults(10).add(Restrictions.like("title", title)).list();
        for (Event event : events) {
            initializeEvent(event);
        }
        return events;
    }

    private Event initializeEvent(Event event) {
        LOGGER.trace("IN");
        if (event != null) {
            Hibernate.initialize(event.getUrls());
            Hibernate.initialize(event.getComics());
            Hibernate.initialize(event.getStories());
            Hibernate.initialize(event.getSeries());
            Hibernate.initialize(event.getCreators());
            Hibernate.initialize(event.getCharacters());
        }
        return event;
    }
}
