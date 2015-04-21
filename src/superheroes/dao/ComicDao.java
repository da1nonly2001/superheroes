package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Comic;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface ComicDao {

    public void saveComic(Comic comic);

    public void updateComic(Comic comic);

    public void mergeComic(Comic comic);

    public void mergeComics(List<Comic> comics);

    public void deleteComic(Comic comic);

    public Comic getComicById(Integer id);

    public Comic getComicByTitle(String title);

    public List<Comic> getAllComics();

    public List<Comic> getComicsByPopularity(Integer limit);

    public List<Comic> getComics(Integer limit, Integer offset);

    public List<Comic> getComicsByTitle(String title);

}
