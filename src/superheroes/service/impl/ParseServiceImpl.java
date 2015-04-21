package superheroes.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.*;
import superheroes.entity.Character;
import superheroes.entity.*;
import superheroes.service.ParseService;
import superheroes.service.WebServiceClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by catop on 3/11/15.
 */
@Component
public class ParseServiceImpl implements ParseService {

    private static final Logger LOGGER = LogManager.getLogger(ParseServiceImpl.class.getName());

    @Autowired
    WebServiceClient webServiceClient;

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

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormatFull;

    public ParseServiceImpl() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatFull = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    }

    private String parseStringFromJson(JSONObject jsonObject, String key) {
        LOGGER.trace("IN: key=" + key);
        if (jsonObject == null) {
            String error = "jsonObject argument cannot be null, was: " + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        String value = null;
        try {
            value = jsonObject.isNull(key) ? null : jsonObject.getString(key);
        } catch (JSONException e) {
            LOGGER.error("Could not attain a value from key=" + key + ". ", e);
        }
        return value;
    }

    private List<Event> parseEventsFromJson(JSONArray array) {
        LOGGER.trace("IN");
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);

                Integer id = data.getInt("id");
                Event event = eventDao.getEventById(id);
                if (event == null)
                    event = new Event();

                String title = parseStringFromJson(data, "title");
                String description = parseStringFromJson(data, "description");
                String resourceURI = parseStringFromJson(data, "resourceURI");

                String modifiedStr = parseStringFromJson(data, "modified");
                Date modified = new Date();
                try {
                    modified = dateFormatFull.parse(modifiedStr);
                } catch (ParseException e) {
                    LOGGER.error("Unable to parse modifier for event.", e);
                }

                Date start = new Date();
                String startStr = parseStringFromJson(data, "start");
                try {
                    start = dateFormat.parse(startStr);
                } catch (ParseException e) {
                    LOGGER.error(e);
                }

                Date end = new Date();
                String endStr = parseStringFromJson(data, "end");
                try {
                    end = dateFormat.parse(endStr);
                } catch (ParseException e) {
                    LOGGER.error(e);
                }

                Integer nextId = null;
                JSONObject nextJson = data.isNull("next") ? null : data.getJSONObject("next");
                if (nextJson != null)
                    nextId = getIdFromResourceUri(nextJson.getString("resourceURI"));

                Integer previousId = null;
                JSONObject previousJson = data.isNull("previous") ? null : data.getJSONObject("previous");
                if (previousJson != null)
                    previousId = getIdFromResourceUri(previousJson.getString("resourceURI"));

                String thumbnailPath = null;
                String thumbnailExtension = null;
                if (data.isNull("thumbnail") == false) {
                    JSONObject thumbnail = data.getJSONObject("thumbnail");
                    thumbnailPath = parseStringFromJson(thumbnail, "path");
                    thumbnailExtension = parseStringFromJson(thumbnail, "extension");
                }

                event.setId(id);
                event.setTitle(title);
                event.setDescription(description);
                event.setResourceUri(resourceURI);
                event.setModified(modified);
                event.setStart(start);
                event.setEnd(end);
                event.setNextId(nextId);
                event.setPreviousId(previousId);
                event.setThumbnailPath(thumbnailPath);
                event.setThumbnailExtension(thumbnailExtension);

                JSONArray urlArray = data.getJSONArray("urls");
                Set<Url> urls = parseUrlsFromJsonArray(urlArray);
                event.setUrls(urls);

                JSONArray creatorsArray = data.getJSONObject("creators").getJSONArray("items");
                Set<Creator> simpleCreators = parseSimpleCreatorsFromJsonArray(creatorsArray);
                Set<Creator> creators = new HashSet<>();
                creators.addAll(event.getCreators());
                creators.addAll(favorExistingCreators(simpleCreators));
                for (Creator creator : creators) {
                    creator.addEvent(event);
                }
                event.setCreators(creators);

                JSONArray characterArray = data.getJSONObject("characters").getJSONArray("items");
                Set<Character> simpleCharacters = parseSimpleCharactersFromJsonArray(characterArray);
                Set<Character> characters = new HashSet<>();
                characters.addAll(event.getCharacters());
                characters.addAll(favorExistingCharacters(simpleCharacters));
                for (Character character : characters) {
                    character.addEvent(event);
                }
                event.setCharacters(characters);

                JSONArray storyArray = data.getJSONObject("stories").getJSONArray("items");
                Set<Story> simpleStories = parseSimpleStoriesFromJsonArray(storyArray);
                Set<Story> stories = new HashSet<>();
                stories.addAll(event.getStories());
                stories.addAll(favorExistingStories(simpleStories));
                for (Story story : stories) {
                    story.addEvent(event);
                }
                event.setStories(stories);

                JSONArray comicArray = data.getJSONObject("comics").getJSONArray("items");
                Set<Comic> simpleComics = parseSimpleComicsFromJsonArray(comicArray);
                Set<Comic> comics = new HashSet<>();
                comics.addAll(event.getComics());
                comics.addAll(favorExistingComics(simpleComics));
                for (Comic comic : comics) {
                    comic.addEvent(event);
                }
                event.setComics(comics);

                JSONArray seriesArray = data.getJSONObject("series").getJSONArray("items");
                Set<Series> simpleSeriesSet = parseSimpleSeriesFromJsonArray(seriesArray);
                Set<Series> seriesSet = new HashSet<>();
                seriesSet.addAll(event.getSeries());
                seriesSet.addAll(favorExistingSeries(simpleSeriesSet));
                for (Series series : seriesSet) {
                    series.addEvent(event);
                }
                event.setSeries(seriesSet);

                eventDao.mergeEvent(event);

                events.add(event);
            } catch (JSONException e) {
                LOGGER.error("Something went wrong when parsing json data", e);
            }
        }
        return events;
    }

    private List<Creator> parseCreatorsFromJson(JSONArray array) {
        LOGGER.trace("IN: array.length=" + (array == null ? null : array.length()));

        List<Creator> creators = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);

                Integer id = data.getInt("id");
                Creator creator = creatorDao.getCreatorById(id);
                if (creator == null)
                    creator = new Creator();

                String firstName = parseStringFromJson(data, "firstName");
                String middleName = parseStringFromJson(data, "middleName");
                String lastName = parseStringFromJson(data, "lastName");
                String suffix = parseStringFromJson(data, "suffix");
                String fullName = parseStringFromJson(data, "fullName");
                String modifiedStr = parseStringFromJson(data, "modified");
                Date modified = new Date();
                try {
                    modified = dateFormatFull.parse(modifiedStr);
                } catch (ParseException e) {
                    LOGGER.error("Unable to parse modifier for creator.", e);
                }
                String resourceUri = parseStringFromJson(data, "resourceURI");

                String thumbnailPath = null;
                String thumbnailExtension = null;
                if (data.isNull("thumbnail") == false) {
                    JSONObject thumbnail = data.getJSONObject("thumbnail");
                    thumbnailPath = parseStringFromJson(thumbnail, "path");
                    thumbnailExtension = parseStringFromJson(thumbnail, "extension");
                }

                creator.setId(id);
                creator.setFirstName(firstName);
                creator.setMiddleName(middleName);
                creator.setLastName(lastName);
                creator.setSuffix(suffix);
                creator.setFullName(fullName);
                creator.setModified(modified);
                creator.setUpdated(new Date());
                creator.setResourceUri(resourceUri);
                creator.setThumbnailPath(thumbnailPath);
                creator.setThumbnailExtension(thumbnailExtension);

                JSONArray comicArray = data.getJSONObject("comics").getJSONArray("items");
                Set<Comic> simpleComics = parseSimpleComicsFromJsonArray(comicArray);
                Set<Comic> comics = new HashSet<>();
                comics.addAll(creator.getComics());
                comics.addAll(favorExistingComics(simpleComics));
                for (Comic comic : comics) {
                    comic.addCreator(creator);
                }
                creator.setComics(comics);

                JSONArray seriesArray = data.getJSONObject("series").getJSONArray("items");
                Set<Series> simpleSeries = parseSimpleSeriesFromJsonArray(seriesArray);
                Set<Series> seriesSet = new HashSet<>();
                seriesSet.addAll(creator.getSeries());
                seriesSet.addAll(favorExistingSeries(simpleSeries));
                for (Series series : seriesSet) {
                    series.addCreator(creator);
                }
                creator.setSeries(seriesSet);

                JSONArray storyArray = data.getJSONObject("stories").getJSONArray("items");
                Set<Story> simpleStories = parseSimpleStoriesFromJsonArray(storyArray);
                Set<Story> stories = new HashSet<>();
                stories.addAll(creator.getStories());
                stories.addAll(favorExistingStories(simpleStories));
                for (Story story : stories) {
                    story.addCreator(creator);
                }
                creator.setStories(stories);

                //events
                JSONArray eventArray = data.getJSONObject("events").getJSONArray("items");
                Set<Event> simpleEvents = parseSimpleEventsFromJsonArray(eventArray);
                Set<Event> events = new HashSet<>();
                events.addAll(creator.getEvents());
                events.addAll(favorExistingEvents(simpleEvents));
                for (Event event : events) {
                    event.addCreator(creator);
                }
                creator.setEvents(events);

                JSONArray urlArray = data.getJSONArray("urls");
                Set<Url> urls = parseUrlsFromJsonArray(urlArray);
                creator.setUrls(urls);

                creatorDao.mergeCreator(creator);

                creators.add(creator);
            } catch (JSONException e) {
                LOGGER.error("Something went wrong when parsing json data", e);
            }
        }
        return creators;
    }

    private List<Character> parseCharactersFromJson(JSONArray array) {
        LOGGER.trace("IN: array.length=" + (array == null ? null : array.length()));

        List<Character> characters = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);
                LOGGER.debug("jsonArray[" + i + "] = " + data);

                Integer id = data.getInt("id");
                Character character = characterDao.getCharacterById(id);
                if (character == null)
                    character = new Character();

                String name = parseStringFromJson(data, "name");
                String description = parseStringFromJson(data, "description");
                String modifiedStr = parseStringFromJson(data, "modified");
                Date modified = new Date();
                try {
                    modified = dateFormatFull.parse(modifiedStr);
                } catch (ParseException e) {
                    LOGGER.error("Unable to parse modifier for character.", e);
                }
                String resourceUri = parseStringFromJson(data, "resourceURI");

                String thumbnailPath = null;
                String thumbnailExtension = null;
                if (data.isNull("thumbnail") == false) {
                    JSONObject thumbnail = data.getJSONObject("thumbnail");
                    thumbnailPath = parseStringFromJson(thumbnail, "path");
                    thumbnailExtension = parseStringFromJson(thumbnail, "extension");
                }

                character.setId(id);
                character.setThumbnailPath(thumbnailPath);
                character.setThumbnailExtension(thumbnailExtension);
                character.setName(name);
                character.setDescription(description);
                character.setModified(modified);
                character.setResourceUri(resourceUri);
                character.setUpdated(new Date());

                JSONArray urlArray = data.getJSONArray("urls");
                Set<Url> urls = parseUrlsFromJsonArray(urlArray);
                character.setUrls(urls);

                JSONArray storyArray = data.getJSONObject("stories").getJSONArray("items");
                Set<Story> simpleStories = parseSimpleStoriesFromJsonArray(storyArray);
                Set<Story> stories = new HashSet<>();
                stories.addAll(character.getStories());
                stories.addAll(favorExistingStories(simpleStories));
                for (Story story : stories) {
                    story.addCharacter(character);
                }
                character.setStories(stories);

                JSONArray seriesArray = data.getJSONObject("series").getJSONArray("items");
                Set<Series> simpleSeries = parseSimpleSeriesFromJsonArray(seriesArray);
                Set<Series> seriesSet = new HashSet<>();
                seriesSet.addAll(character.getSeries());
                seriesSet.addAll(favorExistingSeries(simpleSeries));
                for (Series series : seriesSet) {
                    series.addCharacter(character);
                }
                character.setSeries(seriesSet);

                JSONArray comicArray = data.getJSONObject("comics").getJSONArray("items");
                Set<Comic> simpleComics = parseSimpleComicsFromJsonArray(comicArray);
                Set<Comic> comics = new HashSet<>();
                comics.addAll(character.getComics());
                comics.addAll(favorExistingComics(simpleComics));
                for (Comic comic : comics) {
                    comic.addCharacter(character);
                }
                character.setComics(comics);

                JSONArray eventArray = data.getJSONObject("events").getJSONArray("items");
                Set<Event> simpleEvents = parseSimpleEventsFromJsonArray(eventArray);
                Set<Event> events = new HashSet<>();
                events.addAll(character.getEvents());
                events.addAll(favorExistingEvents(simpleEvents));
                for (Event event : events) {
                    event.addCharacter(character);
                }
                character.setEvents(events);

                LOGGER.debug("character: " + character.toString());

                characterDao.mergeCharacter(character);

                characters.add(character);
            } catch (JSONException e) {
                LOGGER.error("Something went wrong when parsing json data", e);
            }
        }

        return characters;
    }

    private List<Comic> parseComicsFromJson(JSONArray array) {
        LOGGER.trace("IN: array.length=" + (array == null ? null : array.length()));

        List<Comic> comics = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject data = array.getJSONObject(i);

            Integer id = data.getInt("id");
            Comic comic = comicDao.getComicById(id);
            if (comic == null)
                comic = new Comic();

            Long digitalId = data.getLong("digitalId");
            String title = parseStringFromJson(data, "title");
            data.getString("title");
            Integer issueNumber = data.getInt("issueNumber");
            String variantDescription = parseStringFromJson(data, "variantDescription");
            String description = parseStringFromJson(data, "description");
            String modifiedStr = parseStringFromJson(data, "modified");
            Date modified = new Date();
            try {
                modified = dateFormatFull.parse(modifiedStr);
            } catch (ParseException e) {
                LOGGER.error("Unable to parse modifier for comic.", e);
            }
            String isbn = parseStringFromJson(data, "isbn");
            String upc = parseStringFromJson(data, "upc");
            String diamondCode = parseStringFromJson(data, "diamondCode");
            String ean = parseStringFromJson(data, "ean");
            String issn = parseStringFromJson(data, "issn");
            String format = parseStringFromJson(data, "format");
            Integer pageCount = data.getInt("pageCount");
            String resourceUri = parseStringFromJson(data, "resourceURI");

            comic.setId(id);
            comic.setDigitalId(digitalId);
            comic.setTitle(title);
            comic.setIssueNumber(issueNumber);
            comic.setVariantDescription(variantDescription);
            comic.setDescription(description);
            comic.setModified(modified);
            comic.setUpdated(new Date());
            comic.setIsbn(isbn);
            comic.setUpc(upc);
            comic.setDiamondCode(diamondCode);
            comic.setEan(ean);
            comic.setIssn(issn);
            comic.setFormat(format);
            comic.setPageCount(pageCount);
            comic.setResourceUri(resourceUri);

            String thumbnailPath = null;
            String thumbnailExtension = null;
            if (data.isNull("thumbnail") == false) {
                JSONObject thumbnail = data.getJSONObject("thumbnail");
                thumbnailPath = parseStringFromJson(thumbnail, "path");
                thumbnailExtension = parseStringFromJson(thumbnail, "extension");
            }

            comic.setThumbnailPath(thumbnailPath);
            comic.setThumbnailExtension(thumbnailExtension);

            JSONArray urlArray = data.getJSONArray("urls");
            Set<Url> urls = parseUrlsFromJsonArray(urlArray);
            comic.setUrls(urls);

            JSONArray textObjectArray = data.getJSONArray("textObjects");
            Set<TextObject> textObjects = parseTextObjectsFromJsonArray(textObjectArray);
            for (TextObject textObject : textObjects) {
                textObject.setComic(comic);
            }
            comic.setTextObjects(textObjects);

            JSONObject seriesJson = data.getJSONObject("series");
            Series series = parseSimpleSeriesFromJsonObject(seriesJson);
            Series existingSeries = seriesDao.getSeriesById(series.getId());
            if (existingSeries != null)
                series = existingSeries;
            series.addComic(comic);
            Set seriesSet = new HashSet<>();
            seriesSet.add(series);
            comic.setSeries(seriesSet);

            JSONArray creatorsArray = data.getJSONObject("creators").getJSONArray("items");
            Set<Creator> simpleCreators = parseSimpleCreatorsFromJsonArray(creatorsArray);
            Set<Creator> creators = new HashSet<>();
            creators.addAll(comic.getCreators());
            creators.addAll(favorExistingCreators(simpleCreators));
            for (Creator creator : creators) {
                creator.addComic(comic);
            }
            comic.setCreators(creators);

            JSONArray charactersArray = data.getJSONObject("characters").getJSONArray("items");
            Set<Character> simpleCharacters = parseSimpleCharactersFromJsonArray(charactersArray);
            Set<Character> characters = new HashSet<>();
            characters.addAll(comic.getCharacters());
            characters.addAll(favorExistingCharacters(simpleCharacters));
            for (Character character : characters) {
                character.addComic(comic);
            }
            comic.setCharacters(characters);

            JSONArray storyArray = data.getJSONObject("stories").getJSONArray("items");
            Set<Story> simpleStories = parseSimpleStoriesFromJsonArray(storyArray);
            Set<Story> stories = new HashSet<>();
            stories.addAll(comic.getStories());
            stories.addAll(favorExistingStories(simpleStories));
            for (Story story : stories) {
                story.addComic(comic);
            }
            comic.setStories(stories);

            JSONArray eventArray = data.getJSONObject("events").getJSONArray("items");
            Set<Event> simpleEvents = parseSimpleEventsFromJsonArray(eventArray);
            Set<Event> events = new HashSet<>();
            events.addAll(comic.getEvents());
            events.addAll(favorExistingEvents(simpleEvents));
            for (Event event : events) {
                event.addComic(comic);
            }
            comic.setEvents(events);

            comicDao.mergeComic(comic);

            comics.add(comic);
        }
        return comics;
    }

    private List<Series> parseSeriesFromJson(JSONArray array) {
        LOGGER.trace("IN");

        List<Series> seriesList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);

                Integer id = data.getInt("id");
                Series series = seriesDao.getSeriesById(id);
                if (series == null)
                    series = new Series();

                String title = parseStringFromJson(data, "title");
                String description = parseStringFromJson(data, "description");
                String resourceUri = parseStringFromJson(data, "resourceURI");
                String rating = parseStringFromJson(data, "rating");
                String type = parseStringFromJson(data, "type");

                String startYearStr = parseStringFromJson(data, "startYear");
                Date startYear = new Date();
                try {
                    startYear = dateFormat.parse(startYearStr);

                } catch (ParseException e) {
                    LOGGER.error(e);
                }

                String endYearStr = parseStringFromJson(data, "endYear");
                Date endYear = new Date();
                try {
                    endYear = dateFormat.parse(endYearStr);
                } catch (ParseException e) {
                    LOGGER.error(e);
                }

                String modifiedStr = parseStringFromJson(data, "modified");
                Date modified = new Date();
                try {
                    modified = dateFormatFull.parse(modifiedStr);
                } catch (ParseException e) {
                    LOGGER.error("Unable to parse modifier for series.", e);
                }

                String thumbnailPath = null;
                String thumbnailExtension = null;
                if (data.isNull("thumbnail") == false) {
                    JSONObject thumbnail = data.getJSONObject("thumbnail");
                    thumbnailPath = parseStringFromJson(thumbnail, "path");
                    thumbnailExtension = parseStringFromJson(thumbnail, "extension");
                }

                series.setId(id);
                series.setTitle(title);
                series.setDescription(description);
                series.setResourceUri(resourceUri);
                series.setStartYear(startYear);
                series.setEndYear(endYear);
                series.setRating(rating);
                series.setType(type);
                series.setModified(modified);
                series.setUpdated(new Date());
                series.setThumbnailPath(thumbnailPath);
                series.setThumbnailExtension(thumbnailExtension);

                JSONArray urlArray = data.getJSONArray("urls");
                Set<Url> urls = parseUrlsFromJsonArray(urlArray);
                series.setUrls(urls);

                JSONArray creatorsArray = data.getJSONObject("creators").getJSONArray("items");
                Set<Creator> simpleCreators = parseSimpleCreatorsFromJsonArray(creatorsArray);
                Set<Creator> creators = new HashSet<>();
                creators.addAll(series.getCreators());
                creators.addAll(favorExistingCreators(simpleCreators));
                for (Creator creator : creators) {
                    creator.addSeries(series);
                }
                series.setCreators(creators);

                JSONArray characterArray = data.getJSONObject("characters").getJSONArray("items");
                Set<Character> simpleCharacters = parseSimpleCharactersFromJsonArray(characterArray);
                Set<Character> characters = new HashSet<>();
                characters.addAll(series.getCharacters());
                characters.addAll(favorExistingCharacters(simpleCharacters));
                for (Character character : characters) {
                    character.addSeries(series);
                }
                series.setCharacters(characters);

                JSONArray storyArray = data.getJSONObject("stories").getJSONArray("items");
                Set<Story> simpleStories = parseSimpleStoriesFromJsonArray(storyArray);
                Set<Story> stories = favorExistingStories(simpleStories);
                for (Story story : stories) {
                    story.addSeries(series);
                }
                series.setStories(stories);

                JSONArray comicArray = data.getJSONObject("comics").getJSONArray("items");
                Set<Comic> simpleComics = parseSimpleComicsFromJsonArray(comicArray);
                Set<Comic> comics = new HashSet<>();
                comics.addAll(series.getComics());
                comics.addAll(favorExistingComics(simpleComics));
                for (Comic comic : comics) {
                    comic.addSeries(series);
                }
                series.setComics(comics);

                JSONArray eventArray = data.getJSONObject("events").getJSONArray("items");
                Set<Event> simpleEvents = parseSimpleEventsFromJsonArray(eventArray);
                Set<Event> events = new HashSet<>();
                events.addAll(series.getEvents());
                events.addAll(favorExistingEvents(simpleEvents));
                for (Event event : events) {
                    event.addSeries(series);
                }
                series.setEvents(events);

                seriesDao.mergeSeries(series);

                seriesList.add(series);
            } catch (JSONException je) {
                LOGGER.error("Something went wrong when parsing json data", je);
            }
        }

        return seriesList;
    }

    private List<Story> parseStoriesFromJson(JSONArray array) {
        LOGGER.trace("IN");

        List<Story> stories = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject data = array.getJSONObject(i);

                Integer id = data.getInt("id");
                Story story = storyDao.getStoryById(id);
                if (story == null)
                    story = new Story();

                JSONObject originalIssueJson = data.getJSONObject("originalIssue");
                Integer originalIssueId = getIdFromResourceUri(originalIssueJson.getString("resourceURI"));
                story.setOriginalIssueId(originalIssueId);

                String title = parseStringFromJson(data, "title");
                String description = parseStringFromJson(data, "description");
                String resourceUri = parseStringFromJson(data, "resourceURI");
                String type = parseStringFromJson(data, "type");
                String modifiedStr = parseStringFromJson(data, "modified");
                Date modified = new Date();
                try {
                    modified = dateFormatFull.parse(modifiedStr);
                } catch (ParseException e) {
                    LOGGER.error("Unable to parse modifier for story.", e);
                }
                String thumbnail = parseStringFromJson(data, "thumbnail");

                story.setId(id);
                story.setTitle(title);
                story.setDescription(description);
                story.setResourceUri(resourceUri);
                story.setType(type);
                story.setModified(modified);
                story.setUpdated(new Date());
                story.setThumbnail(thumbnail);

                JSONArray comicArray = data.getJSONObject("comics").getJSONArray("items");
                Set<Comic> simpleComics = parseSimpleComicsFromJsonArray(comicArray);
                Set<Comic> comics = new HashSet<>();
                comics.addAll(story.getComics());
                comics.addAll(favorExistingComics(simpleComics));
                for (Comic comic : comics) {
                    comic.addStory(story);
                }
                story.setComics(comics);

                JSONArray creatorsArray = data.getJSONObject("creators").getJSONArray("items");
                Set<Creator> simpleCreators = parseSimpleCreatorsFromJsonArray(creatorsArray);
                Set<Creator> creators = new HashSet<>();
                creators.addAll(story.getCreators());
                creators.addAll(favorExistingCreators(simpleCreators));
                for (Creator creator : creators) {
                    creator.addStory(story);
                }
                story.setCreators(creators);

                JSONArray characterArray = data.getJSONObject("characters").getJSONArray("items");
                Set<Character> simpleCharacters = parseSimpleCharactersFromJsonArray(characterArray);
                Set<Character> characters = new HashSet<>();
                characters.addAll(story.getCharacters());
                characters.addAll(favorExistingCharacters(simpleCharacters));
                for (Character character : characters) {
                    character.addStory(story);
                }
                story.setCharacters(characters);

                JSONArray seriesArray = data.getJSONObject("series").getJSONArray("items");
                Set<Series> simpleSeriesSet = parseSimpleSeriesFromJsonArray(seriesArray);
                Set<Series> seriesSet = new HashSet<>();
                seriesSet.addAll(story.getSeries());
                seriesSet.addAll(favorExistingSeries(simpleSeriesSet));
                for (Series series : seriesSet) {
                    series.addStory(story);
                }
                story.setSeries(seriesSet);

                JSONArray eventArray = data.getJSONObject("events").getJSONArray("items");
                Set<Event> simpleEvents = parseSimpleEventsFromJsonArray(eventArray);
                Set<Event> events = new HashSet<>();
                events.addAll(story.getEvents());
                events.addAll(favorExistingEvents(simpleEvents));
                for (Event event : events) {
                    event.addStory(story);
                }
                story.setEvents(events);

                storyDao.mergeStory(story);

                stories.add(story);
            } catch (JSONException e) {
                LOGGER.error("Something went wrong when parsing json data", e);
            }
        }
        return stories;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Creator> getCreatorsFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Creator> creators = parseCreatorsFromJson(jsonArray);

        return creators;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Character> getCharactersFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Character> characters = parseCharactersFromJson(jsonArray);

        return characters;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Comic> getComicsFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Comic> comics = parseComicsFromJson(jsonArray);

        return comics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> getEventsFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Event> events = parseEventsFromJson(jsonArray);


        return events;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Series> getSeriesFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Series> series = parseSeriesFromJson(jsonArray);

        return series;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Story> getStoriesFromJson(JSONObject jsonObject) {
        LOGGER.trace("IN");
        if (jsonObject == null || jsonObject.length() == 0) {
            String error = "Json cannot be null or empty, jsonObject=" + jsonObject;
            LOGGER.error(error);
            throw new IllegalArgumentException(error);
        }
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("results");
        List<Story> stories = parseStoriesFromJson(jsonArray);

        return stories;
    }

    /**
     * A helper method that parses a bare minimum of fields for a
     * Series entity, and returns a single Series object.
     *
     * @param jsonObject
     * @return
     */
    private Series parseSimpleSeriesFromJsonObject(JSONObject jsonObject) {
        //LOGGER.trace("IN");

        String seriesResourceUri = parseStringFromJson(jsonObject, "resourceURI");
        Integer seriesId = getIdFromResourceUri(seriesResourceUri);
        String name = jsonObject.getString("name");

        Series series = new Series();
        series.setId(seriesId);
        series.setResourceUri(seriesResourceUri);
        series.setTitle(name);
        series.setUpdated(new Date());

        return series;
    }

    private Integer getIdFromResourceUri(String resourceUri) {
        LOGGER.trace("IN");
        int idStartIndex = resourceUri.lastIndexOf('/') + 1;
        Integer id = new Integer(resourceUri.substring(idStartIndex));
        return id;
    }

    private Set<Creator> parseSimpleCreatorsFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Creator> creators = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            String resourceUri = parseStringFromJson(jsonObject, "resourceURI");
            Integer creatorId = getIdFromResourceUri(resourceUri);
            String name = parseStringFromJson(jsonObject, "name");
            jsonObject.getString("name");

            Creator creator = new Creator();
            creator.setId(creatorId);
            creator.setFullName(name);
        }
        return creators;
    }

    private Set<Event> parseSimpleEventsFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Event> events = new HashSet<>();
        for (int j = 0; j < array.length(); j++) {
            JSONObject eventObject = array.getJSONObject(j);

            String eventName = parseStringFromJson(eventObject, "name");
            String eventResourceUri = parseStringFromJson(eventObject, "resourceURI");

            int idStartIndex = eventResourceUri.lastIndexOf('/') + 1;
            Integer eventId = new Integer(eventResourceUri.substring(idStartIndex));

            Event event = new Event();
            event.setId(eventId);
            event.setTitle(eventName);
            event.setResourceUri(eventResourceUri);
            event.setUpdated(new Date());

            events.add(event);
        }
        return events;
    }

    private Set<Comic> parseSimpleComicsFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Comic> comicSet = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject comicJson = array.getJSONObject(i);

            String name = parseStringFromJson(comicJson, "name");
            String resourceUri = parseStringFromJson(comicJson, "resourceURI");
            Integer comicId = getIdFromResourceUri(resourceUri);

            Comic comic = new Comic();
            comic.setId(comicId);
            comic.setTitle(name);
            comic.setResourceUri(resourceUri);
            comic.setUpdated(new Date());

            comicSet.add(comic);
        }
        return comicSet;
    }

    private Set<Story> parseSimpleStoriesFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Story> stories = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            String resourceUri = parseStringFromJson(jsonObject, "resourceURI");
            Integer storyId = getIdFromResourceUri(resourceUri);
            String name = parseStringFromJson(jsonObject, "name");
            String type = parseStringFromJson(jsonObject, "type");

            Story story = new Story();
            story.setId(storyId);
            story.setTitle(name);
            story.setType(type);
            story.setResourceUri(resourceUri);

            stories.add(story);
        }
        return stories;
    }

    /**
     * A helper method that parses a simple amount of information
     * into a set of Series entity objects.  Simple in this regard
     * means that the amount of fields that will be populated are
     * bare minimum and not comprehensive.
     *
     * @param array
     * @return
     */
    private Set<Series> parseSimpleSeriesFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Series> seriesSet = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String name = parseStringFromJson(jsonObject, "name");
            String resourceUri = parseStringFromJson(jsonObject, "resourceURI");
            Integer seriesId = getIdFromResourceUri(resourceUri);

            Series series = new Series();
            series.setId(seriesId);
            series.setTitle(name);
            series.setResourceUri(resourceUri);
            series.setUpdated(new Date());

            seriesSet.add(series);
        }
        return seriesSet;
    }

    /**
     * A helper method that parses a simple amount of information
     * into a set of Character entity objects.  Simple in this regard
     * means that the amount of fields that will be populated are
     * bare minimum and not comprehensive.
     *
     * @param array
     * @return
     */
    private Set<Character> parseSimpleCharactersFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<Character> characterSet = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String name = parseStringFromJson(jsonObject, "name");
            String resourceUri = parseStringFromJson(jsonObject, "resourceURI");
            Integer id = getIdFromResourceUri(resourceUri);

            Character character = new Character();
            character.setId(id);
            character.setName(name);
            character.setResourceUri(resourceUri);
            character.setUpdated(new Date());

            characterSet.add(character);
        }
        return characterSet;
    }

    private Set<Url> parseUrlsFromJsonArray(JSONArray urlArray) {
        LOGGER.trace("IN");
        Set<Url> urls = new HashSet<>();
        for (int i = 0; i < urlArray.length(); i++) {
            JSONObject urlJson = urlArray.getJSONObject(i);
            String type = parseStringFromJson(urlJson, "type");
            String urlStr = parseStringFromJson(urlJson, "url");
            Url url = new Url();
            url.setType(type);
            url.setUrl(urlStr);
            urls.add(url);
        }
        return urls;
    }

    private Set<TextObject> parseTextObjectsFromJsonArray(JSONArray array) {
        LOGGER.trace("IN");
        Set<TextObject> textObjects = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject textJson = array.getJSONObject(i);
            String type = parseStringFromJson(textJson, "type");
            String language = parseStringFromJson(textJson, "language");
            String text = parseStringFromJson(textJson, "text");

            TextObject textObject = new TextObject();
            textObject.setType(type);
            textObject.setLanguage(language);
            textObject.setText(text);

            textObjects.add(textObject);
        }
        return textObjects;
    }

    private Set<Character> favorExistingCharacters(Set<Character> simpleCharacters) {
        LOGGER.trace("IN");
        Set<Character> characters = new HashSet<>();
        for (Character character : simpleCharacters) {
            Character existingCharacter = characterDao.getCharacterById(character.getId());
            if (existingCharacter != null)
                characters.add(existingCharacter);
            else
                characters.add(character);
        }
        return characters;
    }

    /**
     * Replace all Comic entities in the provided Set
     * with existing Comic entities, if possible.  This
     * helps when you have Set of Comic entities with
     * little more than their ID property populated
     * and a more heavily populated equivalent has
     * already been persisted.
     *
     * @param simpleComics A Set of Comic entities
     * @return An updated set of Comic entities
     */
    private Set<Comic> favorExistingComics(Set<Comic> simpleComics) {
        LOGGER.trace("IN");
        Set<Comic> comics = new HashSet<>();
        for (Comic comic : simpleComics) {
            Comic existingComic = comicDao.getComicById(comic.getId());
            if (existingComic != null)
                comics.add(existingComic);
            else
                comics.add(comic);
        }
        return comics;
    }

    private Set<Creator> favorExistingCreators(Set<Creator> simpleCreators) {
        LOGGER.trace("IN");
        Set<Creator> creators = new HashSet<>();
        for (Creator creator : simpleCreators) {
            Creator existingCreator = creatorDao.getCreatorById(creator.getId());
            if (existingCreator != null)
                creators.add(existingCreator);
            else
                creators.add(creator);
        }
        return creators;
    }

    private Set<Event> favorExistingEvents(Set<Event> simpleEvents) {
        LOGGER.trace("IN");
        Set<Event> events = new HashSet<>();
        for (Event event : simpleEvents) {
            Event existingEvent = eventDao.getEventById(event.getId());
            if (existingEvent != null)
                events.add(existingEvent);
            else
                events.add(event);
        }
        return events;
    }

    private Set<Series> favorExistingSeries(Set<Series> simpleSeries) {
        LOGGER.trace("IN");
        Set<Series> seriesSet = new HashSet<>();
        for (Series series : simpleSeries) {
            Series existingSeries = seriesDao.getSeriesById(series.getId());
            if (existingSeries != null)
                seriesSet.add(existingSeries);
            else
                seriesSet.add(series);
        }
        return seriesSet;
    }

    private Set<Story> favorExistingStories(Set<Story> simpleStories) {
        LOGGER.trace("IN");
        Set<Story> stories = new HashSet<>();
        for (Story story : simpleStories) {
            Story existingStories = storyDao.getStoryById(story.getId());
            if (existingStories != null)
                stories.add(existingStories);
            else
                stories.add(story);
        }
        return stories;
    }
}
