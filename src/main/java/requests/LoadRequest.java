package requests;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

/**
 * An object representation of an HTTP /load request body.
 */
public class LoadRequest implements Request {

    /**
     * An array of users to add to the database
     */
    private List<User> users;

    /**
     * An array of persons to add to the database.
     */
    private List<Person> persons;

    /**
     * An array of events to add to the database.
     */
    private List<Event> events;

    /**
     * Default constructor.
     */
    public LoadRequest() {}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
