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
    private List<Person> persons;

    /**
     * Creates the object.
     */
    public PeopleResult() {}
    public PeopleResult(List<Person> people) {
        this.persons = people;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
