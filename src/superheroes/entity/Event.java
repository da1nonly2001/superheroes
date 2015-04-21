package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by catop on 3/11/15.
 */
public class Event {

    private static final Logger LOGGER = LogManager.getLogger(Event.class.getName());

    private Integer id;
    private String title;
    private String description;
    private String resourceUri;
    private Set<Url> urls;
    private Date modified;
    private Date start;
    private Date end;
    private String thumbnailPath;
    private String thumbnailExtension;
    private Set<Creator> creators;
    private Set<Character> characters;
    private Set<Story> stories;
    private Set<Comic> comics;
    private Set<Series> series;
    private Integer nextId;
    private Integer previousId;
    private Date updated;
    private Integer popularity;

    public Event() {
        creators = new HashSet<>();
        characters = new HashSet<>();
        stories = new HashSet<>();
        comics = new HashSet<>();
        series = new HashSet<>();
        urls = new HashSet<>();
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

    public void addComic(Comic comic) {
        LOGGER.trace("IN: this.id=" + id + ", comic.id=" + (comic == null ? null : comic.getId()));
        comics.add(comic);
    }

    public void addSeries(Series seriesEntity) {
        LOGGER.trace("IN: this.id=" + id + ", seriesEntity.id=" + (seriesEntity == null ? null : seriesEntity.getId()));
        series.add(seriesEntity);
    }

    public void addStory(Story story) {
        LOGGER.trace("IN: this.id=" + id + ", story.id=" + (story == null ? null : story.getId()));
        stories.add(story);
    }

    public void addUrl(Url url) {
        LOGGER.trace("IN: this.id=" + id + ", url=" + url);
        urls.add(url);
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

    public Set<Url> getUrls() {
        return urls;
    }

    public void setUrls(Set<Url> urls) {
        this.urls = urls;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailExtension() {
        return thumbnailExtension;
    }

    public void setThumbnailExtension(String thumbnailExtension) {
        this.thumbnailExtension = thumbnailExtension;
    }

    public String getThumbnail() {
        String thumbnail = null;
        if (getThumbnailPath() != null && getThumbnailExtension() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getThumbnailPath());
            sb.append('.');
            sb.append(getThumbnailExtension());
            thumbnail = sb.toString();
        }
        return thumbnail;
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

    public Set<Story> getStories() {
        return stories;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public Set<Comic> getComics() {
        return comics;
    }

    public void setComics(Set<Comic> comics) {
        this.comics = comics;
    }

    public Set<Series> getSeries() {
        return series;
    }

    public void setSeries(Set<Series> series) {
        this.series = series;
    }

    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }

    public Integer getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Integer previousId) {
        this.previousId = previousId;
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
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        return !(resourceUri != null ? !resourceUri.equals(event.resourceUri) : event.resourceUri != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (resourceUri != null ? resourceUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", resourceUri='").append(resourceUri).append('\'');
        sb.append(", urls=").append(urls);
        sb.append(", modified=").append(modified);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", thumbnailPath='").append(thumbnailPath).append('\'');
        sb.append(", thumbnailExtension='").append(thumbnailExtension).append('\'');
        sb.append(", creators.size=").append(creators == null ? null : creators.size());
        sb.append(", characters.ids={");
        for (Character character : characters) {
            sb.append(character.getId()).append(",");
        }
        sb.append("}");
        sb.append(", stories.size=").append(stories == null ? null : stories.size());
        sb.append(", comics.size=").append(comics == null ? null : comics.size());
        sb.append(", series.size=").append(series == null ? null : series.size());
        sb.append(", nextId=").append(nextId);
        sb.append(", previousId=").append(previousId);
        sb.append(", updated=").append(updated);
        sb.append(", popularity=").append(popularity);
        sb.append('}');
        return sb.toString();
    }
}
