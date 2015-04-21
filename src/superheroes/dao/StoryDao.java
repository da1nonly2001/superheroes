package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Story;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface StoryDao {

    public void saveStory(Story story);

    public void updateStory(Story story);

    public void mergeStory(Story story);

    public void mergeStories(List<Story> stories);

    public void deleteStory(Story story);

    public Story getStoryById(Integer id);

    public Story getStoryByTitle(String title);

    public List<Story> getAllStories();

    public List<Story> getStoriesByPopularity(Integer limit);

    public List<Story> getStories(Integer limit, Integer offset);

    public List<Story> getStoriesByTitle(String title);

}
