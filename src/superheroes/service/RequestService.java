package superheroes.service;

import org.springframework.stereotype.Service;
import superheroes.entity.Character;
import superheroes.entity.*;

import java.util.List;

/**
 * Created by catop on 4/13/15.
 */
@Service
public interface RequestService {

    public List<Character> getCharactersByPopularity(Integer limit);

    public List<Character> getCharacters(Integer limit, Integer offset);

    public List<Character> getSimilarCharactersByName(String name);

    public Character getCharacterByName(String name);

    public Character getCharacterById(Integer id);

    public List<Comic> getComicsByPopularity(Integer limit);

    public List<Comic> getComics(Integer limit, Integer offset);

    public List<Comic> getSimilarComicsByTitle(String title);

    public Comic getComicByTitle(String title);

    public Comic getComicById(Integer id);

    public List<Creator> getCreatorsByPopularity(Integer limit);

    public List<Creator> getCreators(Integer limit, Integer offset);

    public List<Creator> getSimilarCreatorsByName(String name);

    public Creator getCreatorByName(String name);

    public Creator getCreatorById(Integer id);

    public List<Event> getEventsByPopularity(Integer limit);

    public List<Event> getEvents(Integer limit, Integer offset);

    public List<Event> getSimilarEventsByTitle(String title);

    public Event getEventByTitle(String title);

    public Event getEventById(Integer id);

    public List<Series> getSeriesByPopularity(Integer limit);

    public List<Series> getSeries(Integer limit, Integer offset);

    public List<Series> getSimilarSeriesByTitle(String title);

    public Series getSeriesByTitle(String title);

    public Series getSeriesById(Integer id);

    public List<Story> getStoriesByPopularity(Integer limit);

    public List<Story> getStories(Integer limit, Integer offset);

    public List<Story> getSimilarStoriesByTitle(String title);

    public Story getStoryByTitle(String title);

    public Story getStoryById(Integer id);

}
