package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by catop on 3/11/15.
 */
public class TextObject {

    private static final Logger LOGGER = LogManager.getLogger(TextObject.class.getName());

    private Integer id;
    private Comic comic;
    private String type;
    private String language;
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextObject)) return false;

        TextObject that = (TextObject) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        return !(text != null ? !text.equals(that.text) : that.text != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TextObject{");
        sb.append("type='").append(type).append('\'');
        sb.append("id='").append(id).append('\'');
        sb.append("comic.id='").append(comic == null ? null : comic.getId()).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
