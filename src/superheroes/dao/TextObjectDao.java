package superheroes.dao;

import org.springframework.stereotype.Repository;
import superheroes.entity.TextObject;

import java.util.List;

/**
 * Created by catop on 4/4/15.
 */
@Repository
public interface TextObjectDao {

    public void saveTextObject(TextObject textObject);

    public void updateTextObject(TextObject textObject);

    public void deleteTextObject(TextObject textObject);

    public TextObject getTextObjectById(Long id);

    public List<TextObject> getAllTextObjects();

}
