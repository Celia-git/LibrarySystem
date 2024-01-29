/*
 * this exception is thrown when a withdrawl or return which has been requested is impossible
 */
package Exceptions;

/**
 *
 * @author Celia
 */
public class InvalidAction extends Exception {

    /**
     *
     * @param action is the failed action
     * @param reason is the reason for failure
     */
    public InvalidAction(String action, String reason) {
        super(String.format("%s failed. %s", action, reason));
    }

}
