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
import superheroes.dao.TextObjectDao;
import superheroes.entity.TextObject;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Component
public class TextObjectDaoHibernateImpl implements TextObjectDao {

    private static final Logger LOGGER = LogManager.getLogger(TextObjectDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTextObject(TextObject textObject) {
        LOGGER.trace("IN: textObject=" + textObject);
        Session session = sessionFactory.getCurrentSession();
        session.save(textObject);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTextObject(TextObject textObject) {
        LOGGER.trace("IN: textObject=" + textObject);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(textObject);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTextObject(TextObject textObject) {
        LOGGER.trace("IN: textObject=" + textObject);
        Session session = sessionFactory.getCurrentSession();
        session.delete(textObject);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TextObject getTextObjectById(Long id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        TextObject textObject = (TextObject) session.get(TextObject.class, id);
        return textObject;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<TextObject> getAllTextObjects() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.TextObject");
        List<TextObject> textObjects = query.list();
        LOGGER.trace("OUT: textObjects.size=" + textObjects.size());
        return textObjects;
    }
}
