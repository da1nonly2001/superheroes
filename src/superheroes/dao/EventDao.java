package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Event;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface EventDao {

    public void saveEvent(Event event);

    public void updateEvent(Event event);

    public void mergeEvent(Event event);

    public void mergeEvents(List<Event> events);

    public void deleteEvent(Event event);

    public Event getEventById(Integer id);

    public Event getEventByTitle(String title);

    public List<Event> getAllEvents();

    public List<Event> getEventsByPopularity(Integer limit);

    public List<Event> getEvents(Integer limit, Integer offset);

    public List<Event> getEventsByTitle(String title);

}
