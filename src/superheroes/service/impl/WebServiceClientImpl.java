package superheroes.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import superheroes.constants.Endpoint;
import superheroes.constants.EntityType;
import superheroes.dao.*;
import superheroes.entity.Character;
import superheroes.entity.*;
import superheroes.service.Authorization;
import superheroes.service.ParseService;
import superheroes.service.WebServiceClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by catop on 4/11/15.
 */
@Component
public class WebServiceClientImpl implements WebServiceClient, ApplicationListener<ContextRefreshedEvent> {

    private final static Logger LOGGER = LogManager.getLogger(WebServiceClientImpl.class.getName());

    private static Boolean hasStarted = Boolean.FALSE;

    private static final Boolean enableAutoWsClient = Boolean.TRUE;

    private static Integer wsCounter = 0;

    private static Long lastWsCall = 0l;

    private static String OFFSET_TOO_HIGH_ERROR = "The requested offset is higher than the available amount of entities, try lowering your offset value.";

    private final static Integer MAX_LIMIT = 100;

    private final Integer INITIAL_OFFSET = 0;

    private final Integer REQUEST_AMOUNT = 100;

    private final static Long sleepInterval = 1000l * 120; //120 seconds
    private final static Long snoozeInterval = 1000l * 90;
    private final static Long haltInterval = 1000l * 60;

    private final static Queue<Integer> characterQueue = new LinkedList<>();
    private final static Queue<Integer> comicQueue = new LinkedList<>();
    private final static Queue<Integer> creatorQueue = new LinkedList<>();
    private final static Queue<Integer> eventQueue = new LinkedList<>();
    private final static Queue<Integer> seriesQueue = new LinkedList<>();
    private final static Queue<Integer> storyQueue = new LinkedList<>();

    @Autowired
    private Authorization authorization;

    @Autowired
    private ParseService parseService;

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private ComicDao comicDao;

    @Autowired
    private CreatorDao creatorDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private SeriesDao seriesDao;

    @Autowired
    private StoryDao storyDao;

    private static void halt() {
        LOGGER.trace("IN");
        try {
            Thread.sleep(haltInterval);
        } catch (InterruptedException e) {
            LOGGER.error("Thread halt interrupted.", e);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.trace("IN");
        if (!hasStarted) {
            hasStarted = Boolean.TRUE;

            Runnable autoWsClient = new Runnable() {
                private Logger LOGGER = LogManager.getLogger(Runnable.class.getName());

                private Integer comicOffset = INITIAL_OFFSET;
                private Integer characterOffset = INITIAL_OFFSET;
                private Integer creatorOffset = INITIAL_OFFSET;
                private Integer eventOffset = INITIAL_OFFSET;
                private Integer storyOffset = INITIAL_OFFSET;
                private Integer seriesOffset = INITIAL_OFFSET;
                private Boolean continueParsing = Boolean.TRUE;

                private void sleep() {
                    LOGGER.trace("IN");
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (InterruptedException e) {
                        continueParsing = Boolean.FALSE;
                        LOGGER.error("autoWsClient interrupted, shutting down parsing.", e);
                    }
                }

                @Override
                public void run() {
                    LOGGER.trace("IN");
                    while (continueParsing) {
                        LOGGER.debug("continue parsing");

                        try {
                            LOGGER.debug("initiating web service call, characterOffset: " + characterOffset);
                            WebServiceClientImpl.this.getCharactersByRange(REQUEST_AMOUNT, characterOffset);
                            characterOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting characterOffset.", e);
                            characterOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }

                        try {
                            LOGGER.debug("initiating web service call, comicOffset: " + comicOffset);
                            WebServiceClientImpl.this.getComicsByRange(REQUEST_AMOUNT, comicOffset);
                            characterOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting comicOffset.", e);
                            comicOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }

                        try {
                            LOGGER.debug("initiating web service call, creatorOffset: " + creatorOffset);
                            WebServiceClientImpl.this.getCreatorsByRange(REQUEST_AMOUNT, creatorOffset);
                            creatorOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting creatorOffset.", e);
                            creatorOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }

                        try {
                            LOGGER.debug("initiating web service call, eventOffset: " + eventOffset);
                            WebServiceClientImpl.this.getEventsByRange(REQUEST_AMOUNT, eventOffset);
                            eventOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting eventOffset.", e);
                            eventOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }

                        try {
                            LOGGER.debug("initiating web service call, storyOffset: " + storyOffset);
                            WebServiceClientImpl.this.getStoriesByRange(REQUEST_AMOUNT, storyOffset);
                            storyOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting storyOffset.", e);
                            storyOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }

                        try {
                            LOGGER.debug("initiating web service call, seriesOffset: " + seriesOffset);
                            WebServiceClientImpl.this.getSeriesByRange(REQUEST_AMOUNT, seriesOffset);
                            seriesOffset += REQUEST_AMOUNT;
                        } catch (IllegalArgumentException e) {
                            LOGGER.warn("Request failed, resetting seriesOffset.", e);
                            seriesOffset = 0;
                        } catch (Exception e) {
                            LOGGER.error(e);
                        } finally {
                            sleep();
                        }
                    }
                }
            };

            Runnable autoWsQueueClient = new Runnable() {

                private Boolean continueParsing = Boolean.TRUE;

                private void sleep() {
                    LOGGER.trace("IN");
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (InterruptedException e) {
                        continueParsing = Boolean.FALSE;
                        LOGGER.error("parserUtil interrupted, shutting down parsing.", e);
                    }
                }

                private void snooze() {
                    LOGGER.trace("IN");
                    try {
                        Thread.sleep(snoozeInterval);
                    } catch (InterruptedException e) {
                        continueParsing = Boolean.FALSE;
                        LOGGER.error("autoWsClient interrupted, shutting down parsing.", e);
                    }
                }

                @Override
                public void run() {
                    while (continueParsing) {
                        LOGGER.trace("iterating ws thread, characterQueue.size=" + characterQueue.size() + ", comicQueue.size=" + comicQueue.size() + ", creatorQueue.size=" + creatorQueue.size() + ", eventQueue.size=" + eventQueue.size() + ", seriesQueue.size=" + seriesQueue.size() + ", storyQueue.size=" + storyQueue.size());
                        if (!characterQueue.isEmpty()) {
                            Integer id = characterQueue.peek();
                            try {
                                Character character = WebServiceClientImpl.this.getCharacterById(id);

                                if (character == null) {
                                    LOGGER.warn("Could not find data for character id: " + id);
                                } else {
                                    snooze();

                                    Set<Comic> comics = new HashSet<>();
                                    comics.addAll(character.getComics());
                                    comics.addAll(WebServiceClientImpl.this.getComicsByTypeId(EntityType.CHARACTER, id));
                                    character.setComics(comics);

                                    snooze();

                                    Set<Series> series = new HashSet<>();
                                    series.addAll(character.getSeries());
                                    series.addAll(WebServiceClientImpl.this.getSeriesByTypeId(EntityType.CHARACTER, id));
                                    character.setSeries(series);

                                    snooze();

                                    Set<Story> stories = new HashSet<>();
                                    stories.addAll(character.getStories());
                                    stories.addAll(WebServiceClientImpl.this.getStoriesByTypeId(EntityType.CHARACTER, id));
                                    character.setStories(stories);

                                    snooze();

                                    Set<Event> events = new HashSet<>();
                                    events.addAll(character.getEvents());
                                    events.addAll(WebServiceClientImpl.this.getEventsByTypeId(EntityType.CHARACTER, id));
                                    character.setEvents(events);

                                    characterQueue.remove(id);
                                    characterDao.mergeCharacter(character);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for character id: " + id, e);
                            }
                        }

                        if (!comicQueue.isEmpty()) {
                            Integer id = comicQueue.peek();
                            try {
                                Comic comic = WebServiceClientImpl.this.getComicById(id);

                                snooze();

                                if (comic == null) {
                                    LOGGER.warn("Could not find data for comic id: " + id);
                                } else {
                                    snooze();

                                    Set<Series> series = new HashSet<>();
                                    series.addAll(comic.getSeries());
                                    series.addAll(WebServiceClientImpl.this.getSeriesByTypeId(EntityType.COMIC, id));
                                    comic.setSeries(series);

                                    Set<Creator> creators = new HashSet<>();
                                    creators.addAll(comic.getCreators());
                                    creators.addAll(WebServiceClientImpl.this.getCreatorsByTypeId(EntityType.COMIC, id));
                                    comic.setCreators(creators);

                                    Set<Character> characters = new HashSet<>();
                                    characters.addAll(comic.getCharacters());
                                    characters.addAll(WebServiceClientImpl.this.getCharactersByTypeId(EntityType.COMIC, id));
                                    comic.setCharacters(characters);

                                    Set<Story> stories = new HashSet<>();
                                    stories.addAll(comic.getStories());
                                    stories.addAll(WebServiceClientImpl.this.getStoriesByTypeId(EntityType.COMIC, id));
                                    comic.setStories(stories);

                                    Set<Event> events = new HashSet<>();
                                    events.addAll(comic.getEvents());
                                    events.addAll(WebServiceClientImpl.this.getEventsByTypeId(EntityType.COMIC, id));
                                    comic.setEvents(events);

                                    comicDao.mergeComic(comic);
                                    comicQueue.remove(id);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for comic id: " + id, e);
                            }
                        }

                        if (!creatorQueue.isEmpty()) {
                            Integer id = creatorQueue.remove();
                            try {
                                Creator creator = WebServiceClientImpl.this.getCreatorById(id);

                                if (creator == null) {
                                    LOGGER.warn("Could not find data for creator id: " + id);
                                } else {
                                    snooze();

                                    Set<Comic> comics = new HashSet<>();
                                    comics.addAll(creator.getComics());
                                    comics.addAll(WebServiceClientImpl.this.getComicsByTypeId(EntityType.CREATOR, id));
                                    creator.setComics(comics);

                                    Set<Series> series = new HashSet<>();
                                    series.addAll(creator.getSeries());
                                    series.addAll(WebServiceClientImpl.this.getSeriesByTypeId(EntityType.CREATOR, id));
                                    creator.setSeries(series);

                                    Set<Story> stories = new HashSet<>();
                                    stories.addAll(creator.getStories());
                                    stories.addAll(WebServiceClientImpl.this.getStoriesByTypeId(EntityType.CREATOR, id));
                                    creator.setStories(stories);

                                    Set<Event> events = new HashSet<>();
                                    events.addAll(creator.getEvents());
                                    events.addAll(WebServiceClientImpl.this.getEventsByTypeId(EntityType.CREATOR, id));
                                    creator.setEvents(events);

                                    creatorDao.mergeCreator(creator);
                                    creatorQueue.remove(id);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for comic id: " + id, e);
                            }
                        }

                        if (!eventQueue.isEmpty()) {
                            Integer id = eventQueue.remove();
                            try {
                                Event event = WebServiceClientImpl.this.getEventById(id);

                                if (event == null) {
                                    LOGGER.warn("Could not find data for event id: " + id);
                                } else {
                                    snooze();

                                    Set<Creator> creators = new HashSet<>();
                                    creators.addAll(event.getCreators());
                                    creators.addAll(WebServiceClientImpl.this.getCreatorsByTypeId(EntityType.EVENT, id));
                                    event.setCreators(creators);

                                    snooze();

                                    Set<Character> characters = new HashSet<>();
                                    characters.addAll(event.getCharacters());
                                    characters.addAll(WebServiceClientImpl.this.getCharactersByTypeId(EntityType.EVENT, id));
                                    event.setCharacters(characters);

                                    snooze();

                                    Set<Story> stories = new HashSet<>();
                                    stories.addAll(event.getStories());
                                    stories.addAll(WebServiceClientImpl.this.getStoriesByTypeId(EntityType.EVENT, id));
                                    event.setStories(stories);

                                    snooze();

                                    Set<Comic> comics = new HashSet<>();
                                    comics.addAll(event.getComics());
                                    comics.addAll(WebServiceClientImpl.this.getComicsByTypeId(EntityType.EVENT, id));
                                    event.setComics(comics);

                                    snooze();

                                    Set<Series> series = new HashSet<>();
                                    series.addAll(event.getSeries());
                                    series.addAll(WebServiceClientImpl.this.getSeriesByTypeId(EntityType.EVENT, id));
                                    event.setSeries(series);

                                    eventDao.mergeEvent(event);
                                    eventQueue.remove(id);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for event id: " + id, e);
                            }
                        }

                        if (!seriesQueue.isEmpty()) {
                            Integer id = seriesQueue.remove();
                            try {
                                Series series = WebServiceClientImpl.this.getSeriesById(id);

                                if (series == null) {
                                    LOGGER.warn("Could not find data for series id: " + id);
                                } else {
                                    snooze();

                                    Set<Creator> creators = new HashSet<>();
                                    creators.addAll(series.getCreators());
                                    creators.addAll(WebServiceClientImpl.this.getCreatorsByTypeId(EntityType.SERIES, id));
                                    series.setCreators(creators);

                                    snooze();

                                    Set<Character> characters = new HashSet<>();
                                    characters.addAll(series.getCharacters());
                                    characters.addAll(WebServiceClientImpl.this.getCharactersByTypeId(EntityType.SERIES, id));
                                    series.setCharacters(characters);

                                    snooze();

                                    Set<Story> stories = new HashSet<>();
                                    stories.addAll(series.getStories());
                                    stories.addAll(WebServiceClientImpl.this.getStoriesByTypeId(EntityType.SERIES, id));
                                    series.setStories(stories);

                                    snooze();

                                    Set<Comic> comics = new HashSet<>();
                                    comics.addAll(series.getComics());
                                    comics.addAll(WebServiceClientImpl.this.getComicsByTypeId(EntityType.SERIES, id));
                                    series.setComics(comics);

                                    snooze();

                                    Set<Event> events = new HashSet<>();
                                    events.addAll(series.getEvents());
                                    events.addAll(WebServiceClientImpl.this.getEventsByTypeId(EntityType.SERIES, id));
                                    series.setEvents(events);

                                    seriesDao.mergeSeries(series);
                                    seriesQueue.remove(id);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for series id: " + id, e);
                            }
                        }

                        if (!storyQueue.isEmpty()) {
                            Integer id = storyQueue.remove();
                            try {
                                Story story = WebServiceClientImpl.this.getStoryById(id);

                                if (story == null) {
                                    LOGGER.warn("Could not find data for story id: " + id);
                                } else {
                                    snooze();

                                    Set<Creator> creators = new HashSet<>();
                                    creators.addAll(story.getCreators());
                                    creators.addAll(WebServiceClientImpl.this.getCreatorsByTypeId(EntityType.STORY, id));
                                    story.setCreators(creators);

                                    snooze();

                                    Set<Character> characters = new HashSet<>();
                                    characters.addAll(story.getCharacters());
                                    characters.addAll(WebServiceClientImpl.this.getCharactersByTypeId(EntityType.STORY, id));
                                    story.setCharacters(characters);

                                    snooze();

                                    Set<Series> series = new HashSet<>();
                                    series.addAll(story.getSeries());
                                    series.addAll(WebServiceClientImpl.this.getSeriesByTypeId(EntityType.STORY, id));
                                    story.setSeries(series);

                                    snooze();

                                    Set<Comic> comics = new HashSet<>();
                                    comics.addAll(story.getComics());
                                    comics.addAll(WebServiceClientImpl.this.getComicsByTypeId(EntityType.SERIES, id));
                                    story.setComics(comics);

                                    snooze();

                                    Set<Event> events = new HashSet<>();
                                    events.addAll(story.getEvents());
                                    events.addAll(WebServiceClientImpl.this.getEventsByTypeId(EntityType.SERIES, id));
                                    story.setEvents(events);

                                    storyDao.mergeStory(story);
                                    storyQueue.remove(id);

                                    sleep();
                                }
                            } catch (Exception e) {
                                LOGGER.error("Something went wrong when gathering data for story id: " + id, e);
                            }
                        }
                        sleep();
                    }
                }
            };

            Thread generalPopulatorThread = new Thread(autoWsClient);
            generalPopulatorThread.setPriority(Thread.MIN_PRIORITY);
            if (enableAutoWsClient) {
                generalPopulatorThread.start();
            }

            Thread specificPopulatorThread = new Thread(autoWsQueueClient);
            specificPopulatorThread.setPriority(Thread.MIN_PRIORITY);
            if (enableAutoWsClient) {
                specificPopulatorThread.start();
            }
        }
    }

    /**
     * Helps create a path for the base endpoints that return
     * deep collections, albeit not fully populated ones.
     *
     * @param endpoint
     * @param limit
     * @param offset
     * @return
     */
    private String getBasePath(String endpoint, Integer limit, Integer offset) {
        LOGGER.trace("IN");

        StringBuilder path = appendApiParamsToPath(endpoint);
        path.append("&").append("limit=").append(limit);
        path.append("&").append("offset=").append(offset);
        return path.toString();
    }

    /**
     * Sets apiKey, timestamp and hash to the path.
     * If this is not done to the path before making
     * a client based web service call, then the call
     * is likely to fail as these parameters are
     * required fields on Marvel's endpoint.
     *
     * @param charSequence
     * @return
     */
    private StringBuilder appendApiParamsToPath(CharSequence charSequence) {
        LOGGER.trace("IN: charSequence=" + charSequence);

        String apiKey = authorization.getApiKey();
        Long timestamp = authorization.getTimestamp();
        String hash = authorization.getMd5Hash();

        StringBuilder path = new StringBuilder(charSequence);
        path.append("?");
        path.append("apikey=").append(apiKey);
        path.append("&").append("ts=").append(timestamp);
        path.append("&").append("hash=").append(hash);

        return path;
    }

    private static void incrementWebServiceCounter() {
        LOGGER.trace("IN: wsCounter=" + wsCounter);
        wsCounter++;
    }

    /**
     * Makes a HTTP GET request to the provided path
     * and returns JSONObject for further processing.
     *
     * @param path Path to the external document
     * @return JSONObject for further processing
     */
    private static synchronized JSONObject getJsonFromPath(String path) {
        LOGGER.trace("IN: path=" + path);

        if (wsCounter > MAX_DAILY_CALLS) {
            String error = "The amount of web service calls have surpassed maximum allowed calls for the day.  No more calls may be made till the counter has been reset.  Call request has been blocked.  Amount of web service calls done today: " + wsCounter + ", maximum allowed calls per day: " + MAX_DAILY_CALLS;
            LOGGER.error(error);
            throw new IllegalStateException(error);
        }

        if (new Date().getTime() - lastWsCall < haltInterval) {
            LOGGER.debug("Web service calls happening too quickly, halting.");
            halt();
        }

        lastWsCall = new Date().getTime();

        incrementWebServiceCounter();

        InputStream is = null;
        JSONObject jsonObject = null;
        StringWriter writer = new StringWriter();
        try {
            URL url = new URL(path);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("GET");
            is = new BufferedInputStream(urlConn.getInputStream());

            writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");

            jsonObject = new JSONObject(writer.toString());

        } catch (Exception e) {
            LOGGER.error("Encountered exception when attempting to acquire a jsonObject from this path: " + path, e);
            try {
                IOUtils.copy(is, writer, "UTF-8");
                LOGGER.error(writer.toString());
            } catch (IOException ioe) {
                LOGGER.error("Problem encountered logging error.", ioe);
            }
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
                LOGGER.error("Unable to close input stream from this path: " + path, ioe);
            }
        }
        return jsonObject;
    }

    @Override
    public List<Character> getCharactersByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Character> characters = null;

        String path = getBasePath(Endpoint.CHARACTERS, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        characters = parseService.getCharactersFromJson(jsonObject);

        return characters;
    }

    @Override
    public List<Comic> getComicsByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Comic> comics = null;

        String path = getBasePath(Endpoint.COMICS, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        comics = parseService.getComicsFromJson(jsonObject);

        return comics;
    }

    @Override
    public List<Creator> getCreatorsByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Creator> creators = null;

        String path = getBasePath(Endpoint.CREATORS, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        creators = parseService.getCreatorsFromJson(jsonObject);

        return creators;
    }

    @Override
    public List<Event> getEventsByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Event> events = null;

        String path = getBasePath(Endpoint.EVENTS, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        events = parseService.getEventsFromJson(jsonObject);

        return events;
    }

    @Override
    public List<Series> getSeriesByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Series> series = null;

        String path = getBasePath(Endpoint.SERIES, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        series = parseService.getSeriesFromJson(jsonObject);

        return series;
    }

    @Override
    public List<Story> getStoriesByRange(Integer limit, Integer offset) {
        LOGGER.trace("IN: limit=" + limit + ", offset=" + offset);
        if ((limit == null || offset == null) || (limit < 0 || limit > 100) || (offset < 0)) {
            String error = "Illegal numeric values provided, limit=" + limit + ", offset=" + offset;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        List<Story> stories = null;

        String path = getBasePath(Endpoint.STORIES, limit, offset);
        JSONObject jsonObject = getJsonFromPath(path);

        Integer totalCharacters = jsonObject.getJSONObject("data").getInt("total");
        if (offset > totalCharacters) {
            LOGGER.error(OFFSET_TOO_HIGH_ERROR);
            throw new IllegalArgumentException(OFFSET_TOO_HIGH_ERROR);
        }

        stories = parseService.getStoriesFromJson(jsonObject);

        return stories;
    }

    @Override
    public Character getCharacterById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.CHARACTERS).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Character> characters = parseService.getCharactersFromJson(jsonObject);
        Character character = null;
        if (characters == null || characters.size() == 0) {
            String warning = "Unable to provide a character by that id";
            LOGGER.warn(warning);
        } else {
            character = characters.get(0);
        }
        return character;
    }

    @Override
    public void addCharacterIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (!isCharacterIdQueued(id))
            characterQueue.add(id);
    }

    @Override
    public Boolean isCharacterIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return characterQueue.contains(id);
    }

    @Override
    public void addComicIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (!isComicIdQueued(id))
            comicQueue.add(id);
    }

    @Override
    public Boolean isComicIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return comicQueue.contains(id);
    }

    @Override
    public void addCreatorIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (isCreatorIdQueued(id))
            creatorQueue.add(id);
    }

    @Override
    public Boolean isCreatorIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return creatorQueue.contains(id);
    }

    @Override
    public void addEventIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (!isEventIdQueued(id))
            eventQueue.add(id);
    }

    @Override
    public Boolean isEventIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return eventQueue.contains(id);
    }

    @Override
    public void addSeriesIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (isSeriesIdQueued(id))
            seriesQueue.add(id);
    }

    @Override
    public Boolean isSeriesIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return seriesQueue.contains(id);
    }

    @Override
    public void addStoryIdToQueue(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (!isStoryIdQueued(id))
            storyQueue.add(id);
    }

    @Override
    public Boolean isStoryIdQueued(Integer id) {
        LOGGER.trace("IN: id=" + id);
        return storyQueue.contains(id);
    }

    protected List<Comic> getComicsByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Comic> comics = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case CHARACTER:
                    endpoint = Endpoint.CHARACTERS;
                    break;
                case CREATOR:
                    endpoint = Endpoint.CREATORS;
                    break;
                case EVENT:
                    endpoint = Endpoint.EVENTS;
                    break;
                case SERIES:
                    endpoint = Endpoint.SERIES;
                    break;
                case STORY:
                    endpoint = Endpoint.STORIES;
                    break;
                case COMIC:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("comics");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Comic> comicList = parseService.getComicsFromJson(jsonObject);

            comics.addAll(comicList);

            offset += limit;
        } while (offset <= total);

        return comics;
    }

    protected List<Event> getEventsByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Event> events = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case CHARACTER:
                    endpoint = Endpoint.CHARACTERS;
                    break;
                case COMIC:
                    endpoint = Endpoint.COMICS;
                    break;
                case CREATOR:
                    endpoint = Endpoint.CREATORS;
                    break;
                case SERIES:
                    endpoint = Endpoint.SERIES;
                    break;
                case STORY:
                    endpoint = Endpoint.STORIES;
                    break;
                case EVENT:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("events");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Event> eventList = parseService.getEventsFromJson(jsonObject);

            events.addAll(eventList);

            offset += limit;
        } while (offset <= total);

        return events;
    }

    protected List<Character> getCharactersByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: type=" + type + ", id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Character> characters = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case COMIC:
                    endpoint = Endpoint.COMICS;
                    break;
                case CREATOR:
                    endpoint = Endpoint.CREATORS;
                    break;
                case EVENT:
                    endpoint = Endpoint.EVENTS;
                    break;
                case SERIES:
                    endpoint = Endpoint.SERIES;
                    break;
                case STORY:
                    endpoint = Endpoint.STORIES;
                    break;
                case CHARACTER:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("characters");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Character> characterList = parseService.getCharactersFromJson(jsonObject);

            characters.addAll(characterList);

            offset += limit;
        } while (offset <= total);

        return characters;
    }

    protected List<Creator> getCreatorsByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: type=" + type + ", id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Creator> creators = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case CHARACTER:
                    endpoint = Endpoint.CHARACTERS;
                    break;
                case COMIC:
                    endpoint = Endpoint.COMICS;
                    break;
                case EVENT:
                    endpoint = Endpoint.EVENTS;
                    break;
                case SERIES:
                    endpoint = Endpoint.SERIES;
                    break;
                case STORY:
                    endpoint = Endpoint.STORIES;
                    break;
                case CREATOR:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("creators");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Creator> creatorList = parseService.getCreatorsFromJson(jsonObject);

            creators.addAll(creatorList);

            offset += limit;
        } while (offset <= total);

        return creators;
    }

    protected List<Series> getSeriesByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: type=" + type + ", id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Series> series = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case CHARACTER:
                    endpoint = Endpoint.CHARACTERS;
                    break;
                case COMIC:
                    endpoint = Endpoint.COMICS;
                    break;
                case CREATOR:
                    endpoint = Endpoint.CREATORS;
                    break;
                case EVENT:
                    endpoint = Endpoint.EVENTS;
                    break;
                case STORY:
                    endpoint = Endpoint.STORIES;
                    break;
                case SERIES:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("series");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Series> seriesList = parseService.getSeriesFromJson(jsonObject);

            series.addAll(seriesList);

            offset += limit;
        } while (offset <= total);

        return series;
    }

    protected List<Story> getStoriesByTypeId(EntityType type, Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        Integer limit = MAX_LIMIT;
        Integer offset = 0;
        Integer total = 0;

        List<Story> stories = new ArrayList<>();

        do {
            StringBuilder path = new StringBuilder();

            String endpoint = null;
            switch (type) {
                case CHARACTER:
                    endpoint = Endpoint.CHARACTERS;
                    break;
                case COMIC:
                    endpoint = Endpoint.COMICS;
                    break;
                case CREATOR:
                    endpoint = Endpoint.CREATORS;
                    break;
                case EVENT:
                    endpoint = Endpoint.EVENTS;
                    break;
                case SERIES:
                    endpoint = Endpoint.SERIES;
                    break;
                case STORY:
                default:
                    String error = "Illegal type provided, type=" + type;
                    LOGGER.error(error);
                    throw new IllegalArgumentException(error);
            }

            path.append(endpoint).append('/').append(id);
            path.append('/').append("stories");
            path = appendApiParamsToPath(path);
            path.append("&limit=").append(limit);
            path.append("&offset=").append(offset);

            JSONObject jsonObject = getJsonFromPath(path.toString());
            JSONObject jsonMetaData = jsonObject.getJSONObject("data");

            total = jsonMetaData.getInt("total");

            List<Story> storyList = parseService.getStoriesFromJson(jsonObject);

            stories.addAll(storyList);

            offset += limit;
        } while (offset <= total);

        return stories;
    }

    @Override
    public Comic getComicById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.COMICS).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Comic> comics = parseService.getComicsFromJson(jsonObject);
        Comic comic = null;
        if (comics == null || comics.size() == 0) {
            String warning = "Unable to provide a comic by that id";
            LOGGER.warn(warning);
        } else {
            comic = comics.get(0);
        }
        return comic;
    }

    @Override
    public Creator getCreatorById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.CREATORS).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Creator> creators = parseService.getCreatorsFromJson(jsonObject);
        Creator creator = null;
        if (creators == null || creators.size() == 0) {
            String warning = "Unable to provide a creator by that id";
            LOGGER.warn(warning);
        } else {
            creator = creators.get(0);
        }
        return creator;
    }

    @Override
    public Event getEventById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.EVENTS).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Event> events = parseService.getEventsFromJson(jsonObject);
        Event event = null;
        if (events == null || events.size() == 0) {
            String warning = "Unable to provide a event by that id";
            LOGGER.warn(warning);
        } else {
            event = events.get(0);
        }
        return event;
    }

    @Override
    public Series getSeriesById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.SERIES).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Series> seriesList = parseService.getSeriesFromJson(jsonObject);
        Series series = null;
        if (seriesList == null || seriesList.size() == 0) {
            String warning = "Unable to provide a series by that id";
            LOGGER.warn(warning);
        } else {
            series = seriesList.get(0);
        }
        return series;
    }

    @Override
    public Story getStoryById(Integer id) {
        LOGGER.trace("IN: id=" + id);
        if (id == null || id < 0) {
            String error = "Illegal id value, id=" + id;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }

        StringBuilder path = new StringBuilder();
        path.append(Endpoint.STORIES).append('/').append(id);
        path = appendApiParamsToPath(path);
        JSONObject jsonObject = getJsonFromPath(path.toString());

        List<Story> stories = parseService.getStoriesFromJson(jsonObject);
        Story story = null;
        if (stories == null || stories.size() == 0) {
            String warning = "Unable to provide a story by that id";
            LOGGER.warn(warning);
        } else {
            story = stories.get(0);
        }
        return story;
    }
}
