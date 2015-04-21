package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by catop on 3/11/15.
 */
public class Story {

    private static final Logger LOGGER = LogManager.getLogger(Story.class.getName());

    private Integer id;
    private String title;
    private String description;
    private String resourceUri;
    private String type;
    private Date modified;
    private String thumbnail;
    private Set<Creator> creators;
    private Set<Character> characters;
    private Set<Series> series;
    private Set<Comic> comics;
    private Set<Event> events;
    private Integer originalIssueId;
    private Date updated;
    private Integer popularity;

    public Story() {
        creators = new HashSet<>();
        characters = new HashSet<>();
        series = new HashSet<>();
        comics = new HashSet<>();
        events = new HashSet<>();
        popularity = 0;
    }

    public void addCreator(Creator creator) {
        LOGGER.trace("IN: this.id=" + id + ", creator.id=" + (creator == null ? null : creator.getId()));
        creators.add(creator);
    }

    public void addCharacter(Character character) {
        LOGGER.trace("IN: this.id=" + id + ", character.id=" + (character == null ? null : character.getId()));
        characters.add(character);
    }

    public void addSeries(Series seriesEntity) {
        LOGGER.trace("IN: this.id=" + id + ", seriesEntity.id=" + (seriesEntity == null ? null : seriesEntity.getId()));
        series.add(seriesEntity);
    }

    public void addComic(Comic comic) {
        LOGGER.trace("IN: this.id=" + id + ", comic.id=" + (comic == null ? null : comic.getId()));
        comics.add(comic);
    }

    public void addEvent(Event event) {
        LOGGER.trace("IN: this.id=" + id + ", event.id=" + (event == null ? null : event.getId()));
        events.add(event);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<Creator> getCreators() {
        return creators;
    }

    public void setCreators(Set<Creator> creators) {
        this.creators = creators;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public Set<Series> getSeries() {
        return series;
    }

    public void setSeries(Set<Series> series) {
        this.series = series;
    }

    public Set<Comic> getComics() {
        return comics;
    }

    public void setComics(Set<Comic> comics) {
        this.comics = comics;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Integer getOriginalIssueId() {
        return originalIssueId;
    }

    public void setOriginalIssueId(Integer originalIssueId) {
        this.originalIssueId = originalIssueId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Story)) return false;

        Story story = (Story) o;

        if (id != null ? !id.equals(story.id) : story.id != null) return false;
        if (title != null ? !title.equals(story.title) : story.title != null) return false;
        return !(type != null ? !type.equals(story.type) : story.type != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Story{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", resourceUri='").append(resourceUri).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", modified=").append(modified);
        sb.append(", thumbnail='").append(thumbnail).append('\'');
        sb.append(", creators.size=").append(creators == null ? null : creators.size());
        sb.append(", characters.size=").append(characters == null ? null : characters.size());
        sb.append(", series.size=").append(series == null ? null : series.size());
        sb.append(", comics.size=").append(comics == null ? null : comics.size());
        sb.append(", events.size=").append(events == null ? null : events.size());
        sb.append(", originalIssueId=").append(originalIssueId);
        sb.append(", updated=").append(updated);
        sb.append(", popularity=").append(popularity);
        sb.append('}');
        return sb.toString();
    }
}
