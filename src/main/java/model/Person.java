package model;

/**
 * An object for storing all data for an individual person.
 */
public class Person {

    /**
     * A unique identifier for the person.
     */
    private String personID;

    /**
     * The username for the user whose family tree contains this person.
     */
    private String associatedUsername;

    /**
     * The person's first name.
     */
    private String firstName;

    /**
     * The person's surname.
     */
    private String lastName;

    /**
     * The person's gender, either 'f' or 'm'.
     */
    private char gender;

    /**
     * The personID for the person's father, if the father is in the tree.
     */
    private String fatherID;

    /**
     * The personID for the person's mother.
     */
    private String motherID;

    /**
     * The personID for the person's spouse.
     */
    private String spouseID;

    /**
     * Creates a person given all fields that must not be null.
     *
     * @param personID a unique identifier for the person.
     * @param associatedUsername the username for the user whose family tree contains this person.
     * @param firstName the value to set for the person's first name.
     * @param lastName the value to set for the person's last name.
     * @param gender the person's gender, either 'f' or 'm'.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, char gender) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Creates a person given all possible parameters.
     *
     * @param personID a unique identifier for the person.
     * @param associatedUsername the username for the user whose family tree contains this person.
     * @param firstName the value to set for the person's first name.
     * @param lastName the value to set for the person's last name.
     * @param gender the person's gender, either 'f' or 'm'.
     * @param fatherID the personID for the person's father.
     * @param motherId the personID for the person's mother.
     * @param spouseID the personID for the person's spouse.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, char gender,
                  String fatherID, String motherId, String spouseID) {
        Person(personID, associatedUsername, firstName, lastName, gender);
        this.fatherID = fatherID;
        this.motherID = motherId;
        this.spouseID = spouseID;
    }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public char getGender() { return gender; }

    public void setGender(char gender) { this.gender = gender; }

    public String getFatherID() { return fatherID; }

    public void setFatherID(String fatherID) { this.fatherID = fatherID; }

    public String getMotherID() { return motherID; }

    public void setMotherID(String motherID) { this.motherID = motherID; }

    public String getSpouseID() { return spouseID; }

    public void setSpouseID(String spouseID) { this.spouseID = spouseID; }

    /**
     * Determines whether the person is equal to the passed object.
     *
     * @param object the object to compare to the person
     * @return whether the person is equal to the provided object
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Person person = (Person) object;
        return gender == person.gender && personID.equals(person.personID) &&
                associatedUsername.equals(person.associatedUsername) && firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) && java.util.Objects.equals(fatherID, person.fatherID) &&
                java.util.Objects.equals(motherID, person.motherID) &&
                java.util.Objects.equals(spouseID, person.spouseID);
    }
}