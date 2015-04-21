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
import superheroes.dao.CharacterDao;
import superheroes.entity.Character;

import java.util.List;

/**
 * Created by catop on 4/3/15.
 */
@Component
public class CharacterDaoHibernateImpl implements CharacterDao {

    private static final Logger LOGGER = LogManager.getLogger(CharacterDaoHibernateImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Attempts to represent the character entity in the database.
     * Note that you should only use this method if you are certain that this entity
     * don't already exist in the database, in which case this is the fastest way
     * to save the entity.  If you are unsure, then use the updateCharacter method
     * instead.
     *
     * @param character The character entity object to be saved
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCharacter(Character character) {
        LOGGER.trace("IN: character=" + character);
        Session session = sessionFactory.getCurrentSession();
        session.save(character);
    }

    /**
     * Attempts to represent the character entity in the database.
     * It is safe to use this method to save an entity that has not
     * already been saved in the database, but note that this convenience
     * comes at a slight performance cost.
     *
     * @param character The character entity object to be saved
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCharacter(Character character) {
        LOGGER.trace("IN: character=" + character);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(character);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeCharacter(Character character) {
        LOGGER.trace("IN: character=" + character);
        Session session = sessionFactory.getCurrentSession();
        session.merge(character);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mergeCharacters(List<Character> characters) {
        LOGGER.trace("IN: characters.size=" + characters == null ? null : characters.size());
        Session session = sessionFactory.getCurrentSession();
        for (Character character : characters) {
            session.merge(character);
        }
    }

    /**
     * Deletes the character entity from its representation in
     * the database.  Note that this does not nullify the
     * comic Java reference, which will continue to live
     * while in scope.
     *
     * @param character The character entity to be deleted from the database
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCharacter(Character character) {
        LOGGER.trace("IN: character=" + character);
        Session session = sessionFactory.getCurrentSession();
        session.delete(character);
    }

    /**
     * Returns a specific character entity object found by id.
     * If no object is found, then a null value is returned.
     *
     * @param id The character id the desired character entity possess
     * @return Returns a Character entity object or null
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Character getCharacterById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        Session session = sessionFactory.getCurrentSession();
        Character character = (Character) session.get(Character.class, id);
        initializeCharacter(character);
        return character;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Character getCharacterByName(String name) {
        LOGGER.trace("IN: name=" + name);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Character.class);
        Character character = (Character) criteria.setMaxResults(1).add(Restrictions.like("name", name)).uniqueResult();
        return character;
    }

    /**
     * Uses HQL to return all character entities managed by Hibernate
     *
     * @return Returns a list of character entities
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getAllCharacters() {
        LOGGER.trace("IN");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from superheroes.entity.Character");
        List<Character> characters = query.list();
        LOGGER.trace("OUT: characters.size=" + characters.size());
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharactersByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Character.class);
        List<Character> characters = criteria.setMaxResults(limit).addOrder(Order.desc("popularity")).setReadOnly(true).list();
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharacters(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Character.class);
        List<Character> characters = criteria.setFirstResult(offset).setReadOnly(true).setMaxResults(limit).list();
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharactersByName(String name) {
        LOGGER.trace("IN: name=" + name);
        name = "%" + name + "%";
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(superheroes.entity.Character.class);
        List<Character> characters = criteria.setMaxResults(10).add(Restrictions.like("name", name)).list();
        for (Character character : characters) {
            initializeCharacter(character);
        }
        return characters;
    }

    private Character initializeCharacter(Character character) {
        LOGGER.trace("IN");
        if (character != null) {
            Hibernate.initialize(character.getComics());
            Hibernate.initialize(character.getEvents());
            Hibernate.initialize(character.getSeries());
            Hibernate.initialize(character.getStories());
            Hibernate.initialize(character.getUrls());
        }
        return character;
    }
}
