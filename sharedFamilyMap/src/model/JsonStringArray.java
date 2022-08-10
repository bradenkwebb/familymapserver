package model;

import java.util.ArrayList;

/**
 * An object for representing a JSON array of Strings.
 */
public class JsonStringArray {
    /**
     * The ArrayList<>() of Strings.
     */
    private ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
