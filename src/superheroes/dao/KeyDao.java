package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Key;

/**
 * Created by catop on 4/7/15.
 */
@Repository
public interface KeyDao {

    public Key getKey(String type);

}
