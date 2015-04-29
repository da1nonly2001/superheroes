package superheroes.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.*;
import superheroes.entity.Character;
import superheroes.entity.*;
import superheroes.service.RequestService;
import superheroes.service.WebServiceClient;

import java.util.List;

/**
 * Created by catop on 4/13/15.
 */
@Component
public class RequestServiceImpl implements RequestService {

    private final static Logger LOGGER = LogManager.getLogger(RequestServiceImpl.class.getName());

    @Autowired
    WebServiceClient webServiceClient;

    @Autowired
    CharacterDao characterDao;

    @Autowired
    ComicDao comicDao;

    @Autowired
    CreatorDao creatorDao;

    @Autowired
    EventDao eventDao;

    @Autowired
    SeriesDao seriesDao;

    @Autowired
    StoryDao storyDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharactersByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        List<Character> characters = characterDao.getCharactersByPopularity(limit);
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharacters(Integer limit, Integer offset) {
        LOGGER.trace("IN");
        List<Character> characters = characterDao.getCharacters(limit, offset);
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getSimilarCharactersByName(String name) {
        LOGGER.trace("IN: name=" + name);
        List<Character> characters = characterDao.getCharactersByName(name);
        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Character getCharacterByName(String name) {
        LOGGER.trace("IN: name=" + name);
        Character character = characterDao.getCharacterByName(name);
        return character;
    }

    /**
     * Method first looks for a Character object in the database using the provided
     * id parameter.  If the returned Character object does not pass validation,
     * then it means that it's missing properties that we want it to have.  This
     * method will make an effort to populate the Character's state and return
     * as-is.  In other words, the method may or may not be faster the next time
     * it looks for the same id, depending on whether it was able to populate
     * the object properly.
     *
     * @param id A unique value used to look for a Character
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Character getCharacterById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        Character character = characterDao.getCharacterById(id);

        if (character == null) {
            LOGGER.warn("Found no character by id: " + id + ", id added to queue.");
            webServiceClient.addCharacterIdToQueue(id);
        } else if (StringUtils.isBlank(character.getDescription())
                || character.getSeries().isEmpty()
                || character.getEvents().isEmpty()
                || character.getComics().isEmpty()
                || character.getStories().isEmpty()
                || character.getUrls().isEmpty()) {
            LOGGER.debug("Character.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addCharacterIdToQueue(id);
            character.setPopularity(character.getPopularity() + 1);
        } else {
            character.setPopularity(character.getPopularity() + 1);
        }

        return character;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComicsByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        List<Comic> comics = comicDao.getComicsByPopularity(limit);
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComics(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        List<Comic> comics = comicDao.getComics(limit, offset);
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getSimilarComicsByTitle(String title) {
        LOGGER.trace("IN: name=" + title);
        List<Comic> comics = comicDao.getComicsByTitle(title);
        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Comic getComicByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Comic comic = comicDao.getComicByTitle(title);
        return comic;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Comic getComicById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        Comic comic = comicDao.getComicById(id);

        if (comic == null) {
            LOGGER.warn("Found no comic by id: " + id + ", id added to queue.");
            webServiceClient.addComicIdToQueue(id);
        } else if (StringUtils.isBlank(comic.getDescription())
                || comic.getCharacters().isEmpty()
                || comic.getSeries().isEmpty()
                || comic.getEvents().isEmpty()
                || comic.getCreators().isEmpty()
                || comic.getStories().isEmpty()) {
            LOGGER.debug("Comic.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addComicIdToQueue(id);
            comic.setPopularity(comic.getPopularity() + 1);
        } else {
            comic.setPopularity(comic.getPopularity() + 1);
        }
        return comic;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreatorsByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        List<Creator> creators = creatorDao.getCreatorsByPopularity(limit);
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreators(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        List<Creator> creators = creatorDao.getCreators(limit, offset);
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getSimilarCreatorsByName(String name) {
        LOGGER.trace("IN: name=" + name);
        List<Creator> creators = creatorDao.getCreatorsByName(name);
        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Creator getCreatorByName(String name) {
        LOGGER.trace("IN: name=" + name);
        Creator creator = creatorDao.getCreatorByName(name);
        return creator;
    }

    /**
     * Method looks for Creator as stored internally. It tests
     * the object to see if it lacks data, in which case it will
     * make an attempt to populate the Creator identified by
     * the provided id with more data.  It makes no promise as to
     * when this effort will be made, nor that it will be
     * successful, so the object is returned as-is.  Returns null
     * if no entity is found by the provided id.
     *
     * @param id Identifies the Creator entity
     * @return Creator entity
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Creator getCreatorById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        Creator creator = creatorDao.getCreatorById(id);

        if (creator == null) {
            LOGGER.warn("Found no creator by id: " + id + ", id added to queue.");
            webServiceClient.addCreatorIdToQueue(id);
        } else if (StringUtils.isBlank(creator.getFullName())
                || creator.getStories().isEmpty()
                || creator.getEvents().isEmpty()
                || creator.getSeries().isEmpty()
                || creator.getComics().isEmpty()) {
            LOGGER.debug("Creator.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addCreatorIdToQueue(id);
            creator.setPopularity(creator.getPopularity() + 1);
        } else {
            creator.setPopularity(creator.getPopularity() + 1);
        }

        return creator;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEventsByPopularity(Integer limit) {
        LOGGER.trace("IN");
        List<Event> events = eventDao.getEventsByPopularity(limit);
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEvents(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        List<Event> events = eventDao.getEvents(limit, offset);
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getSimilarEventsByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        List<Event> events = eventDao.getEventsByTitle(title);
        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Event getEventByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Event event = eventDao.getEventByTitle(title);
        return event;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Event getEventById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        Event event = eventDao.getEventById(id);

        if (event == null) {
            LOGGER.warn("Found no event by id: " + id + ", id added to queue.");
            webServiceClient.addEventIdToQueue(id);
        } else if (StringUtils.isBlank(event.getDescription())
                || event.getComics().isEmpty()
                || event.getSeries().isEmpty()
                || event.getStories().isEmpty()
                || event.getCharacters().isEmpty()
                || event.getCreators().isEmpty()) {
            LOGGER.debug("Event.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addEventIdToQueue(id);
            event.setPopularity(event.getPopularity() + 1);
        } else {
            event.setPopularity(event.getPopularity() + 1);
        }
        return event;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeriesByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        List<Series> series = seriesDao.getSeriesByPopularity(limit);
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeries(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        List<Series> series = seriesDao.getSeries(limit, offset);
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSimilarSeriesByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        List<Series> series = seriesDao.getSeriesListByTitle(title);
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Series getSeriesByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Series series = seriesDao.getSeriesByTitle(title);
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Series getSeriesById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        Series series = seriesDao.getSeriesById(id);

        if (series == null) {
            LOGGER.warn("Found no series by id: " + id + ", id added to queue.");
            webServiceClient.addSeriesIdToQueue(id);
        } else if (StringUtils.isBlank(series.getDescription())
                || series.getCreators().isEmpty()
                || series.getCharacters().isEmpty()
                || series.getStories().isEmpty()
                || series.getComics().isEmpty()
                || series.getEvents().isEmpty()) {
            LOGGER.debug("Series.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addSeriesIdToQueue(id);
            series.setPopularity(series.getPopularity() + 1);
        } else {
            series.setPopularity(series.getPopularity() + 1);
        }
        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStoriesByPopularity(Integer limit) {
        LOGGER.trace("IN: limit=" + limit);
        List<Story> stories = storyDao.getStoriesByPopularity(limit);
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStories(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        List<Story> stories = storyDao.getStories(limit, offset);
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getSimilarStoriesByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        List<Story> stories = storyDao.getStoriesByTitle(title);
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Story getStoryByTitle(String title) {
        LOGGER.trace("IN: title=" + title);
        Story story = storyDao.getStoryByTitle(title);
        return story;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Story getStoryById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null) {
            String error = "Illegal id argument: " + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Story story = storyDao.getStoryById(id);

        if (story == null) {
            LOGGER.warn("Found no story by that id: " + id + ", id added to queue.");
            webServiceClient.addStoryIdToQueue(id);
        } else if (StringUtils.isBlank(story.getDescription())
                || story.getEvents().isEmpty()
                || story.getComics().isEmpty()
                || story.getCharacters().isEmpty()
                || story.getCreators().isEmpty()
                || story.getSeries().isEmpty()) {
            LOGGER.debug("Story.id=" + id + " may be incompletely populated: scheduled for further processing.");
            webServiceClient.addStoryIdToQueue(id);
            story.setPopularity(story.getPopularity() + 1);
        } else {
            story.setPopularity(story.getPopularity() + 1);
        }
        return story;
    }
}
