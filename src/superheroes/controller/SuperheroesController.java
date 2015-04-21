package superheroes.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import superheroes.entity.Character;
import superheroes.entity.*;
import superheroes.forms.SearchForm;
import superheroes.service.RequestService;
import superheroes.service.WebServiceClient;

import java.util.List;

/**
 * Created by catop on 4/7/15.
 */
@Controller
public class SuperheroesController {

    private static final Logger LOGGER = LogManager.getLogger(SuperheroesController.class.getName());

    @Autowired
    private RequestService requestService;

    @Autowired
    private WebServiceClient webServiceClient;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView coverPage() {
        LOGGER.trace("IN");

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/coverPage.jsp");

        List<Character> characters = requestService.getCharactersByPopularity(5);
        List<Comic> comics = requestService.getComicsByPopularity(5);
        List<Story> stories = requestService.getStoriesByPopularity(5);
        List<Event> events = requestService.getEventsByPopularity(5);

        modelAndView.addObject("characters", characters);
        modelAndView.addObject("comics", comics);
        modelAndView.addObject("stories", stories);
        modelAndView.addObject("events", events);

        SearchForm searchForm = new SearchForm();
        modelAndView.addObject("searchForm", searchForm);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView searchResult(SearchForm searchForm) {
        LOGGER.trace("IN: search searchForm=" + searchForm);

        String searchString = searchForm.getSearchString();

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/search.jsp");

        List<Character> characters = requestService.getSimilarCharactersByName(searchString);
        List<Comic> comics = requestService.getSimilarComicsByTitle(searchString);
        List<Event> events = requestService.getSimilarEventsByTitle(searchString);
        List<Series> series = requestService.getSimilarSeriesByTitle(searchString);
        List<Story> stories = requestService.getSimilarStoriesByTitle(searchString);

        modelAndView.addObject("characters", characters);
        modelAndView.addObject("comics", comics);
        modelAndView.addObject("events", events);
        modelAndView.addObject("series", series);
        modelAndView.addObject("stories", stories);

        Integer hits = characters.size() + comics.size() + events.size() + series.size() + stories.size();
        modelAndView.addObject("hits", hits);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/character", method = RequestMethod.GET)
    public ModelAndView character(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN");

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/character.jsp");

        Character character = requestService.getCharacterById(id);
        modelAndView.addObject("character", character);

        Boolean queuedId = webServiceClient.isCharacterIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/comic", method = RequestMethod.GET)
    public ModelAndView comic(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN: id=" + id);

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/comic.jsp");

        Comic comic = requestService.getComicById(id);
        modelAndView.addObject("comic", comic);

        Boolean queuedId = webServiceClient.isComicIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/creator", method = RequestMethod.GET)
    public ModelAndView creator(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN: id=" + id);

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/creator.jsp");

        Creator creator = requestService.getCreatorById(id);
        modelAndView.addObject("creator", creator);

        Boolean queuedId = webServiceClient.isCreatorIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public ModelAndView event(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN: id=" + id);

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/event.jsp");

        Event event = requestService.getEventById(id);
        modelAndView.addObject("event", event);

        Boolean queuedId = webServiceClient.isEventIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public ModelAndView story(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN: id=" + id);

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/story.jsp");

        Story story = requestService.getStoryById(id);
        modelAndView.addObject("story", story);

        Boolean queuedId = webServiceClient.isStoryIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/series", method = RequestMethod.GET)
    public ModelAndView series(@RequestParam(value = "id", required = true) Integer id) {
        LOGGER.trace("IN: id=" + id);

        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/series.jsp");

        Series series = requestService.getSeriesById(id);
        modelAndView.addObject("series", series);

        Boolean queuedId = webServiceClient.isSeriesIdQueued(id);
        modelAndView.addObject("queuedId", queuedId);

        return modelAndView;
    }
}