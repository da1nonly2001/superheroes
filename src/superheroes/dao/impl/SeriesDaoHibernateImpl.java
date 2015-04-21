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
import superheroes.dao.SeriesDao;
import superheroes.entity.Series;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class SeriesDaoHibernateImpl implements SeriesDao {

    private static final Logger LOGGER = LogManager.getLogger(SeriesDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSeries(Series series) {
        LOGGER.trace("IN: series=" + series);
        Session session = sessionFactory.getCurrentSession();
        session.save(series);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSeries(Series series) {
        LOGGER.trace("IN: series=" + series);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(series);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeSeries(Series series) {
        LOGGER.trace("IN: series=" + series);
        Session session = sessionFactory.getCurrentSession();
        session.merge(series);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeAllSeries(List<Series> seriesList) {
        LOGGER.trace("IN: seriesList.size=" + seriesList == null ? null : seriesList.size());
        Session session = sessionFactory.getCurrentSession();
        for (Series series : seriesList) {
            session.merge(session);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSeries(Series series) {
        LOGGER.trace("IN: series=" + series);
        Session session = sessionFactory.getCurrentSession();
        session.delete(series);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Series getSeriesById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Series series = (Series) session.get(Series.class, id);
        initializeSeries(series);
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getAllSeries() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Series");
        List<Series> series = query.list();
        LOGGER.trace("OUT: series.size=" + series.size());
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Series getSeriesByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Series.class);
        Series series = (Series) criteria.setMaxResults(1).add(Restrictions.like("title", title)).uniqueResult();
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeriesByPopularity(Integer limit) {
        LOGGER.trace("IN: limit:" + limit);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Series.class);
        List<Series> series = criteria.setMaxResults(limit).setReadOnly(true).addOrder(Order.desc("popularity")).list();
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeries(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Series.class);
        List<Series> series = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeriesListByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        title = "%" + title + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Series.class);
        List<Series> series = criteria.setMaxResults(10).add(Restrictions.like("title", title)).list();
        for (Series seriez : series) {
            initializeSeries(seriez);
        }
        return series;
    }

    private Series initializeSeries(Series series) {
        LOGGER.trace("IN");
        if (series != null) {
            Hibernate.initialize(series.getCharacters());
            Hibernate.initialize(series.getUrls());
            Hibernate.initialize(series.getCreators());
            Hibernate.initialize(series.getComics());
            Hibernate.initialize(series.getCreators());
            Hibernate.initialize(series.getStories());
            Hibernate.initialize(series.getEvents());
        }
        return series;
    }
}
