package superheroes.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * Created by catop on 4/7/15.
 */
public class Key {

    private static final Logger LOGGER = LogManager.getLogger(Key.class.getName());

    private String type;
    private String value;

    public final static String PUBLIC_KEY = "public_key";
    public final static String PRIVATE_KEY = "private_key";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (StringUtils.isEmpty(type)) {
            String errorMsg = "Illegal type value: " + type;
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;

        Key key = (Key) o;

        if (type != null ? !type.equals(key.type) : key.type != null) return false;
        return !(value != null ? !value.equals(key.value) : key.value != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Key{");
        sb.append("type='").append(type).append('\'');
        sb.append(", value='").append(value == null ? null : "*****").append('\'');
        sb.append('}');
        return sb.toString();
    }
}
