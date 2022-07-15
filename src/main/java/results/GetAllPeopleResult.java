package results;

import model.Person;

import java.util.List;

/**
 * An object representation of an HTTP /person response body.
 */
public class GetAllPeopleResponse implements Response {
    /**
     * Whether or not the server successfully implemented the request.
     */
    private boolean success;

    /**
     * The list of Person objects obtained from the database in the user's family tree.
     */
    private List<Person> persons;

    /**
     * If !success, the message to provide the client.
     */
    private String message;

    /**
     * Creates the object.
     */
    public GetAllPeopleResponse() {
        //TODO implement this constructor?
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
