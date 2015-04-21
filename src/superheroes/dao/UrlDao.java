package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Url;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface UrlDao {

    public void saveUrl(Url url);

    public void updateUrl(Url url);

    public void deleteUrl(Url url);

    public Url getUrlById(Long id);

    public List<Url> getAllUrls();

}
