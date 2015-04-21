package superheroes.constants;

/**
 * Created by catop on 4/7/15.
 */
public final class Endpoint {

    private static final String MARVEL_GATEWAY = "http://gateway.marvel.com/v1/public";
    public static final String CHARACTERS = MARVEL_GATEWAY + "/characters";
    public static final String COMICS = MARVEL_GATEWAY + "/comics";
    public static final String CREATORS = MARVEL_GATEWAY + "/creators";
    public static final String EVENTS = MARVEL_GATEWAY + "/events";
    public static final String SERIES = MARVEL_GATEWAY + "/series";
    public static final String STORIES = MARVEL_GATEWAY + "/stories";

}
