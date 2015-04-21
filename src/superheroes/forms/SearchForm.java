package superheroes.forms;

/**
 * Created by catop on 4/15/15.
 */
public class SearchForm {

    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchForm{");
        sb.append("searchString='").append(searchString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
