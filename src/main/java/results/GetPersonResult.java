package results;

/**
 * An object representation of an HTTP /person/[personID] response body.
 */
public class GetPersonResult extends Result {

    /**
     * The user with whom the obtained person is associated.
     */
    private String associatedUsername;

    /**
     * The first name of the person.
     */
    private String firstName;

    /**
     * The last name of the person.
     */
    private String lastName;

    /**
     * The gender of the person.
     */
    private char gender;

    /**
     * The personID of the person's father, if in the database.
     */
    private String fatherID;

    /**
     * The personID of the person's mother, if in the database.
     */
    private String motherID;

    /**
     * The personID of the person's spouse, if in the database.
     */
    private String spouseID;

    /**
     * Creates the object.
     */
    public GetPersonResult() {
        // TODO implement this constructor
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
