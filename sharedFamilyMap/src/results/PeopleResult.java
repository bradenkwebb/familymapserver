package results;

import model.Person;

import java.util.List;

/**
 * An object representation of an HTTP /person response body.
 */
public class PeopleResult extends Result {

    /**
     * The list of Person objects obtained from the database in the user's family tree.
     */
    private List<Person> data;

    /**
     * Creates the object.
     */
    public PeopleResult(List<Person> people) {
        this.data = people;
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
