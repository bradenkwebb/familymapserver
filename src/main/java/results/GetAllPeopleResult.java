package results;

import model.Person;

import java.util.List;

/**
 * An object representation of an HTTP /person response body.
 */
public class GetAllPeopleResult extends Result {

    /**
     * The list of Person objects obtained from the database in the user's family tree.
     */
    private List<Person> persons;

    /**
     * Creates the object.
     */
    public GetAllPeopleResult() {
        //TODO implement this constructor?
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
