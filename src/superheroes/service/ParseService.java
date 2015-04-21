package superheroes.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import superheroes.entity.Character;
import superheroes.entity.*;

import java.util.List;

/**
 * Created by catop on 3/11/15.
 */
@Service
public interface ParseService {

    /**
     * Parses the provided jsonObject and
     * creates a List of Character entities.
     *
     * @param jsonObject JSON object that contains values used to create Character entities
     * @return A List of Character entities
     */
    public List<Character> getCharactersFromJson(JSONObject jsonObject);

    /**
     * Parses the provided jsonObject and
     * creates a List of Comic entities.
     *
     * @param jsonObject JSON object that contains values used to create Comic entities
     * @return A List of Comic entities
     */
    public List<Comic> getComicsFromJson(JSONObject jsonObject);

    /**
     * Parses the provided jsonObject and
     * creates a List of Creator entities.
     *
     * @param jsonObject JSON object that contains values used to create Creator entities
     * @return A List of Creator entities
     */
    public List<Creator> getCreatorsFromJson(JSONObject jsonObject);

    /**
     * Parses the provided jsonObject and
     * creates a List of Event entities.
     *
     * @param jsonObject JSON object that contains values used to create Event entities
     * @return A List of Event entities
     */
    public List<Event> getEventsFromJson(JSONObject jsonObject);

    /**
     * Parses the provided jsonObject and
     * creates a List of Series entities.
     *
     * @param jsonObject JSON object that contains values used to create Series entities
     * @return A List of Series entities
     */
    public List<Series> getSeriesFromJson(JSONObject jsonObject);

    /**
     * Parses the provided jsonObject and
     * creates a List of Story entities.
     *
     * @param jsonObject JSON object that contains values used to create Story entities
     * @return A List of Story entities
     */
    public List<Story> getStoriesFromJson(JSONObject jsonObject);

}
