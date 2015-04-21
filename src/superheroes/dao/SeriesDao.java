package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Series;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface SeriesDao {

    public void saveSeries(Series series);

    public void updateSeries(Series series);

    public void mergeSeries(Series series);

    public void mergeAllSeries(List<Series> seriesList);

    public void deleteSeries(Series series);

    public Series getSeriesById(Integer id);

    public Series getSeriesByTitle(String title);

    public List<Series> getAllSeries();

    public List<Series> getSeriesByPopularity(Integer limit);

    public List<Series> getSeries(Integer limit, Integer offset);

    public List<Series> getSeriesListByTitle(String title);

}
