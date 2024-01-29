/*
 * Test Interaction through the Command Line Interface
 */
package Testers;

import Exceptions.InvalidAction;
import Statics.Search;
import UserInput.*;
import DataStorage.Book;
import DataStorage.Student;
import Exceptions.FileOverwrite;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Celia
 */
public class Tester {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        
        /* To create file data to test the program, uncomment this code.
        CreateTestFiles test = new CreateTestFiles();
        try {
            test.newStudents();
            test.newBooks();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        User u;
        
        while (true) {

            
                System.out.println("\nEnter 'c' to continue to program\nEnter 'late' to test the late fee application\nEnter admin pass to log in\nEnter 'q' to quit");
                Scanner in = new Scanner(System.in);
                String entry = in.next();
                switch (entry) {
                    case "c":   // Open User Program for inputs
                        u = new User();
                        u.getInput();
                        continue;
                    case "late": 
                        // Get random book and student to test late fees on
                        try {
                            Book testBook = Search.getBook("random", "").get(0);
                            Student testStudent = Search.getStudent("random", "").get(0);
                             // Prompt user for days late
                            System.out.println(String.format("Random book %s and student %s chosen for the late fees demo.\nEnter the amount of days late", testBook.getName(), testStudent.getName()));
                            int daysLate = in.nextInt();
                            LateFeeTester late = new LateFeeTester(daysLate, testStudent, testBook);
                            
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidAction ex) {
                            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileOverwrite ex) {
                        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    case "q":   // Break Infinity loop
                        break;
                    default:    // Try admin login with password
                        u = new Admin();
                        try {
                            adminLogin((Admin) u, entry);
                            continue;
                        } catch (InvalidAction ex) {
                            System.out.println(ex.getMessage());
                            continue;
                        }
                }
                break;
        }

    }

    /**
     *
     * @param a: Admin object
     * @param password: user entered password
     * @throws InvalidAction if password is invalid
     */
    public static void adminLogin(Admin a, String password) throws InvalidAction {
        if (password.equals(a.getPassword())) {
            a.getInput();
        } else {
            throw new InvalidAction("Admin Login", String.format("%s not recognized", password));
        }
    }

}
