/*
 * Arbitrarily change the dates to test the late fees system
 */
package Testers;

import DataStorage.Book;
import DataStorage.Student;
import Exceptions.*;
import Statics.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Celia
 */
public final class LateFeeTester {

    private final int daysLate;
    private final Student testStudent;
    private final Book testBook;

    /**
     *
     * @param daysLate
     * @param testStudent
     * @param testBook
     * @throws InvalidAction
     * @throws FileNotFoundException
     * @throws FileOverwrite
     */
    public LateFeeTester(int daysLate, Student testStudent, Book testBook) throws InvalidAction, FileNotFoundException, FileOverwrite, IOException {
        this.daysLate = daysLate;
        this.testStudent = testStudent;
        this.testBook = testBook;
        checkOut();
    }

    /**
     * Check out book on behalf of a student
     *
     * @throws InvalidAction
     * @throws FileNotFoundException
     * @throws FileOverwrite
     */
    public void checkOut() throws InvalidAction, FileNotFoundException, FileOverwrite, IOException {

        // Show default test case info
        System.out.println("Student before withdrawing book");
        showInfo();

        // Withdraw book, set test values
        testStudent.withdraw(testBook);
        testBook.testFine(daysLate);

        // Commit changes to file
        Data.overwrite(testBook);
        Data.overwrite(testStudent);

        // Display Student info again
        System.out.println("Student after withdrawing book");
        showInfo();
    }

    /**
     * Show Student and Book Info
     */
    public void showInfo() {
        System.out.println(Arrays.toString(testStudent.format()));
        System.out.println(String.format("Student Fines: %s", testStudent.getFines()));
    }

}
