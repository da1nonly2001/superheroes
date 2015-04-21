package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Creator;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface CreatorDao {

    public void saveCreator(Creator creator);

    public void updateCreator(Creator creator);

    public void mergeCreator(Creator creator);

    public void mergeCreators(List<Creator> creators);

    public void deleteCreator(Creator creator);

    public Creator getCreatorById(Integer id);

    public Creator getCreatorByName(String name);

    public List<Creator> getAllCreators();

    public List<Creator> getCreatorsByPopularity(Integer limit);

    public List<Creator> getCreators(Integer limit, Integer offset);

    public List<Creator> getCreatorsByName(String name);

}
