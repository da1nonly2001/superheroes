package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by catop on 3/11/15.
 */
public class Creator {

    private static final Logger LOGGER = LogManager.getLogger(Creator.class.getName());

    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String fullName;
    private Date modified;
    private String thumbnailPath;
    private String thumbnailExtension;
    private String resourceUri;
    private Set<Comic> comics;
    private Set<Series> series;
    private Set<Story> stories;
    private Set<Event> events;
    private Set<Url> urls;
    private Date updated;
    private Integer popularity;

    public Creator() {
        comics = new HashSet<>();
        series = new HashSet<>();
        stories = new HashSet<>();
        events = new HashSet<>();
        urls = new HashSet<>();
        popularity = 0;
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

    public void addEvent(Event event) {
        LOGGER.trace("IN: this.id=" + id + ", event.id=" + (event == null ? null : event.getId()));
        events.add(event);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
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

    public Set<Story> getStories() {
        return stories;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Url> getUrls() {
        return urls;
    }

    public void setUrls(Set<Url> urls) {
        this.urls = urls;
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
        if (!(o instanceof Creator)) return false;

        Creator creator = (Creator) o;

        if (firstName != null ? !firstName.equals(creator.firstName) : creator.firstName != null) return false;
        if (fullName != null ? !fullName.equals(creator.fullName) : creator.fullName != null) return false;
        if (id != null ? !id.equals(creator.id) : creator.id != null) return false;
        if (lastName != null ? !lastName.equals(creator.lastName) : creator.lastName != null) return false;
        if (middleName != null ? !middleName.equals(creator.middleName) : creator.middleName != null) return false;
        if (suffix != null ? !suffix.equals(creator.suffix) : creator.suffix != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Creator{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", suffix='").append(suffix).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", modified=").append(modified);
        sb.append(", thumbnailPath='").append(thumbnailPath).append('\'');
        sb.append(", thumbnailExtension='").append(thumbnailExtension).append('\'');
        sb.append(", resourceUri='").append(resourceUri).append('\'');
        sb.append(", comics.size=").append(comics == null ? null : comics.size());
        sb.append(", series.size=").append(series == null ? null : series.size());
        sb.append(", stories.size=").append(stories == null ? null : stories.size());
        sb.append(", events.size=").append(events == null ? null : events.size());
        sb.append(", urls=").append(urls);
        sb.append(", updated=").append(updated);
        sb.append(", popularity=").append(popularity);
        sb.append('}');
        return sb.toString();
    }
}
