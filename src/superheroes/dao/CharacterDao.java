package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.Character;

import java.util.List;

/**
 * Created by catop on 4/3/15.
 */
@Repository
public interface CharacterDao {

    public void saveCharacter(Character character);

    public void updateCharacter(Character character);

    public void mergeCharacter(Character character);

    public void mergeCharacters(List<Character> characters);

    public void deleteCharacter(Character character);

    public Character getCharacterById(Integer id);

    public Character getCharacterByName(String name);

    public List<Character> getAllCharacters();

    public List<Character> getCharactersByPopularity(Integer limit);

    public List<Character> getCharacters(Integer limit, Integer offset);

    public List<Character> getCharactersByName(String name);

}
