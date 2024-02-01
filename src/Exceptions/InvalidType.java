/*
 * This error is thrown when Data recieves invalid dataStorage object type as argument
 */
package src.Exceptions;

/**
 *
 * @author Celia
 */
public class InvalidType extends Exception {

    /**
     *
     * @param type: String other than book or student
     */
    public InvalidType(String type) {
        super(String.format("%s is not a valid data type.\nEnter 'student' or 'book'", type));
    }
}
