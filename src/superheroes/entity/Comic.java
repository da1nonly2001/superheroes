package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by catop on 3/11/15.
 */
public class Comic {

    private static final Logger LOGGER = LogManager.getLogger(Comic.class.getName());

    private Integer id;
    private Long digitalId;
    private String title;
    private Integer issueNumber;
    private String variantDescription;
    private String description;
    private Date modified;
    private String isbn;
    private String upc;
    private String diamondCode;
    private String ean;
    private String issn;
    private String format;
    private Integer pageCount;
    private Set<TextObject> textObjects;
    private String resourceUri;
    private Set<Series> series;
    private Date onSaleDate;
    private Date focDate;
    private Float printPrice;
    private String thumbnailPath;
    private String thumbnailExtension;
    private Set<Creator> creators;
    private Set<Character> characters;
    private Set<Story> stories;
    private Set<Event> events;
    private Set<Url> urls;
    private Date updated;
    private Integer popularity;

    public Comic() {
        textObjects = new HashSet<>();
        series = new HashSet<>();
        creators = new HashSet<>();
        characters = new HashSet<>();
        stories = new HashSet<>();
        events = new HashSet<>();
        urls = new HashSet<>();
        popularity = 0;
    }

    public void addTextObject(TextObject textObject) {
        LOGGER.trace("IN: this.id=" + id + ", textObject=" + textObject);
        textObjects.add(textObject);
    }

    public void addSeries(Series seriesEntity) {
        LOGGER.trace("IN: this.id=" + id + ", seriesEntity.id=" + (seriesEntity == null ? null : seriesEntity.getId()));
        series.add(seriesEntity);
    }

    public void addCreator(Creator creator) {
        LOGGER.trace("IN: this.id=" + id + ", creator.id=" + (creator == null ? null : creator.getId()));
        creators.add(creator);
    }

    public void addCharacter(Character character) {
        LOGGER.trace("IN: this.id=" + id + ", character.id=" + (character == null ? null : character.getId()));
        characters.add(character);
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
        LOGGER.trace("IN: url=" + url);
        urls.add(url);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(Long digitalId) {
        this.digitalId = digitalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getVariantDescription() {
        return variantDescription;
    }

    public void setVariantDescription(String variantDescription) {
        this.variantDescription = variantDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getDiamondCode() {
        return diamondCode;
    }

    public void setDiamondCode(String diamondCode) {
        this.diamondCode = diamondCode;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Set<TextObject> getTextObjects() {
        return textObjects;
    }

    public void setTextObjects(Set<TextObject> textObjects) {
        this.textObjects = textObjects;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Set<Series> getSeries() {
        return series;
    }

    public void setSeries(Set<Series> series) {
        this.series = series;
    }

    public Date getOnSaleDate() {
        return onSaleDate;
    }

    public void setOnSaleDate(Date onSaleDate) {
        this.onSaleDate = onSaleDate;
    }

    public Date getFocDate() {
        return focDate;
    }

    public void setFocDate(Date focDate) {
        this.focDate = focDate;
    }

    public Float getPrintPrice() {
        return printPrice;
    }

    public void setPrintPrice(Float printPrice) {
        this.printPrice = printPrice;
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
        if (!(o instanceof Comic)) return false;

        Comic comic = (Comic) o;

        if (id != null ? !id.equals(comic.id) : comic.id != null) return false;
        if (title != null ? !title.equals(comic.title) : comic.title != null) return false;
        return !(resourceUri != null ? !resourceUri.equals(comic.resourceUri) : comic.resourceUri != null);

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
        final StringBuilder sb = new StringBuilder("Comic{");
        sb.append("id=").append(id);
        sb.append(", digitalId=").append(digitalId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", issueNumber=").append(issueNumber);
        sb.append(", variantDescription='").append(variantDescription).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", modified=").append(modified);
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append(", upc='").append(upc).append('\'');
        sb.append(", diamondCode='").append(diamondCode).append('\'');
        sb.append(", ean='").append(ean).append('\'');
        sb.append(", issn='").append(issn).append('\'');
        sb.append(", format='").append(format).append('\'');
        sb.append(", pageCount=").append(pageCount);
        sb.append(", textObjects=").append(textObjects);
        sb.append(", resourceUri='").append(resourceUri).append('\'');
        sb.append(", series=").append(series);
        sb.append(", onSaleDate=").append(onSaleDate);
        sb.append(", focDate=").append(focDate);
        sb.append(", printPrice=").append(printPrice);
        sb.append(", thumbnailPath='").append(thumbnailPath).append('\'');
        sb.append(", thumbnailExtension='").append(thumbnailExtension).append('\'');
        sb.append(", creators.size=").append(creators == null ? null : creators.size());
        sb.append(", characters.size=").append(characters == null ? null : characters.size());
        sb.append(", stories.size=").append(stories == null ? null : stories.size());
        sb.append(", events.size=").append(events == null ? null : events.size());
        sb.append(", urls=").append(urls);
        sb.append(", updated=").append(updated);
        sb.append(", popularity=").append(popularity);
        sb.append('}');
        return sb.toString();
    }
}
