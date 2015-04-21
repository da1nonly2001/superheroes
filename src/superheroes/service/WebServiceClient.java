package superheroes.service;

import org.springframework.stereotype.Service;
import superheroes.entity.Character;
import superheroes.entity.*;

import java.util.List;

/**
 * Created by catop on 4/11/15.
 */
@Service
public interface WebServiceClient {

    public Integer MAX_DAILY_CALLS = 3000;

    /**
     * Method performs a web service call and
     * returns a List of Character entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Character> getCharactersByRange(Integer limit, Integer offset);

    /**
     * Method performs a web service call and
     * returns a List of Comic entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Comic> getComicsByRange(Integer limit, Integer offset);

    /**
     * Method performs a web service call and
     * returns a List of Character entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Creator> getCreatorsByRange(Integer limit, Integer offset);

    /**
     * Method performs a web service call and
     * returns a List of Character entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Event> getEventsByRange(Integer limit, Integer offset);

    /**
     * Method performs a web service call and
     * returns a List of Character entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Series> getSeriesByRange(Integer limit, Integer offset);

    /**
     * Method performs a web service call and
     * returns a List of Character entities.
     *
     * @param limit  The amount of requested entities, cannot be less than 0 and must not be higher than 100
     * @param offset Used to request entities from the given offset
     * @return
     */
    public List<Story> getStoriesByRange(Integer limit, Integer offset);

    /**
     * Performs a web service call and returns a single Character entity
     *
     * @param id Specifies the Character requested
     * @return
     */
    public Character getCharacterById(Integer id);

    /**
     * Provide a character id to the client, the client
     * promises to perform a web service call to attain
     * more data for this particular id.  The client
     * makes no guarentee of the success of this operation
     * nor at what point the web service call will be made.
     *
     * @param id Character id that you wish to attain more data for
     */
    public void addCharacterIdToQueue(Integer id);

    public Boolean isCharacterIdQueued(Integer id);

    public void addComicIdToQueue(Integer id);

    public Boolean isComicIdQueued(Integer id);

    public void addCreatorIdToQueue(Integer id);

    public Boolean isCreatorIdQueued(Integer id);

    public void addEventIdToQueue(Integer id);

    public Boolean isEventIdQueued(Integer id);

    public void addSeriesIdToQueue(Integer id);

    public Boolean isSeriesIdQueued(Integer id);

    public void addStoryIdToQueue(Integer id);

    public Boolean isStoryIdQueued(Integer id);

    /**
     * Performs a web service call and returns a single Comic entity
     *
     * @param id Specifies the Comic requested
     * @return
     */
    public Comic getComicById(Integer id);

    /**
     * Performs a web service call and returns a single Creator entity
     *
     * @param id Specifies the Creator requested
     * @return
     */
    public Creator getCreatorById(Integer id);

    /**
     * Performs a web service call and returns a single Event entity
     *
     * @param id Specifies the Event requested
     * @return
     */
    public Event getEventById(Integer id);

    /**
     * Performs a web service call and returns a single Series entity
     *
     * @param id Specifies the Series requested
     * @return
     */
    public Series getSeriesById(Integer id);

    /**
     * Performs a web service call and returns a single Story entity
     *
     * @param id Specifies the Story requested
     * @return
     */
    public Story getStoryById(Integer id);

}
