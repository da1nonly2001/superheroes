package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by catop on 3/11/15.
 */
public class Url {

    private static final Logger LOGGER = LogManager.getLogger(Url.class.getName());

    private Integer id;
    private String type;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Url)) return false;

        Url url1 = (Url) o;

        if (type != null ? !type.equals(url1.type) : url1.type != null) return false;
        return !(url != null ? !url.equals(url1.url) : url1.url != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Url{");
        sb.append("id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
