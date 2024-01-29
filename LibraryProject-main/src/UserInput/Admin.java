

/*
 * Handle User Commands: Admin level clearance
 */
package UserInput;

import DataStorage.*;
import Exceptions.*;
import Statics.*;
import Testers.LateFeeTester;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe
 */
public class Admin extends User {

    // Admin Entry Strings
    private String password = "admin";
    private String function;
    private String key;
    private String value;
    private String category;
    private final String[] funcOptions = {"display", "search", "add", "delete", "modify", "withdraw", "return", "printbill", "payfines"};

    /**
     *
     * @return currently set admin password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password: set admin password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Guide User Inputs
     */
    @Override
    public void getInput() {

        try {
            // Show the options for User
            System.out.println("Command options are: ");
            for (String s : funcOptions) {
                System.out.println(s);
            }
            // Get User command
            System.out.println("Enter desired command: ");
            Scanner in = new Scanner(System.in);
            function = in.next();
            function = function.toLowerCase();

            // Get Book or Student
            if (function.equals("display") || function.equals("search")
                    || function.equals("add") || function.equals("delete")) {
                System.out.println(String.format("Enter type of file to %s. 'book' or 'student': ", function));
                category = in.next().toLowerCase();
            }

            //search(category);   
            switch (function) {
                case "display":
                    display(category);
                    break;
                case "search":
                    search(category);
                    break;
                case "add":
                    addEntry(category);
                    break;
                case "delete":
                    deleteEntry(category);
                    break;
                case "modify":
                    modEntry();
                    break;
                case "withdraw":
                    withdrawBook();
                    break;
                case "return":
                    returnBook();
                    break;
                case "printbill":
                    printBill();
                    break;
                case "payfines":
                    payFines();
                    break;
                default:
                    throw new InvalidAction(function, "Command does not exist");
            }
        } catch (InvalidAction | FileOverwrite | InvalidType | FileNotFoundException | InputMismatchException ex) {
            System.out.println(ex.getMessage());
            getInput();
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Search for book or student
     *
     * @param category: student or book
     * @throws InvalidType
     * @throws FileNotFoundException
     * @throws InvalidAction
     */
    public void search(String category) throws InvalidType, FileNotFoundException, InvalidAction, IOException {
        Scanner in = new Scanner(System.in);
        String showKeys[];

        // Create a new dataStorage object, cast as type, save keys values in showKeys
        switch (category) {
            case "student":
                Student studentObject = new Student("");
                showKeys = studentObject.getKeys();

                // Display values in showKeys
                for (String k : showKeys) {
                    System.out.println(k);
                }

                // Get Inputs for key and value
                System.out.println("Search by which key?");
                key = in.nextLine();
                System.out.println("Enter a value to match: ");
                value = in.nextLine();

                // Search method returns all student objects whose attributes match given value
                ArrayList<Student> students = Search.getStudent(key, value);
                System.out.println(String.format("Matches Found: %d", students.size()));
                // Use format method to show all student info
                students.forEach(s -> {
                    System.out.println(Arrays.toString(s.format()));
                });
                break;

            case "book":
                super.search();
                break;

            default:
                // Throw Invalid Type Error if user input is not "student" or "book"
                throw new InvalidType(category);
        }

    }

    /**
     * Display info about all students or books
     *
     * @param category: student or book
     * @throws InvalidType
     * @throws FileNotFoundException
     */
    public void display(String category) throws InvalidType, FileNotFoundException, InvalidAction, IOException {

        switch (category) {

            case "student":

                // getAll returns all of given category
                DataStorage[] students = Data.getAll(category);
                // Use format method to show all student info
                for (DataStorage s : students) {
                    System.out.println(Arrays.toString(s.format()));
                }
                break;

            case "book":
                super.display();
                break;

            default:
                throw new InvalidType(category);
        }
    }

    /**
     * add a new entry in file
     *
     * @param category: student or book
     * @throws InvalidType
     * @throws FileNotFoundException
     * @throws FileOverwrite
     */
    public void addEntry(String category) throws InvalidType, FileNotFoundException, FileOverwrite, IOException {
        Scanner in = new Scanner(System.in);

        switch (category) {
            case "book":
                System.out.println("Enter an ID for new book: ");
                Book b = new Book(in.next());
                in.nextLine();
                System.out.println("Enter book author: ");
                b.setAuthor(in.nextLine());
                System.out.println("Enter book name: ");
                b.setName(in.nextLine());
                System.out.println("Enter date published: ");
                b.setDatePublished(in.nextLine());
                System.out.println("Enter book publisher: ");
                b.setPublisher(in.nextLine());
                System.out.println("Enter book description: ");
                b.setDescription(in.nextLine());
                // after getting all necessary input for book object, write it to file
                Data.addNewEntry(b);
                break;
            case "student":
                System.out.println("Enter an ID for new student: ");
                Student s = new Student(in.next());
                in.nextLine();
                System.out.println("Enter student name: ");
                s.setName(in.nextLine());
                //after getting input for student object, write to file
                Data.addNewEntry(s);
                break;
            default:
                throw new InvalidType(category);
        }

    }

    /**
     * Modify a book entry in file
     *
     * @throws InvalidType
     * @throws FileNotFoundException
     * @throws FileOverwrite
     */
    public void modEntry() throws InvalidType, InvalidAction, FileOverwrite, IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter ID of book to modify: ");
        String id = in.next();
        in.nextLine();
        try {
            Book book = (Book) Data.writeToObject("book", id);

            System.out.println("Book item fields are 'author', 'name', 'datepub', 'publisher', 'desc'");
            System.out.println("Enter field to change: ");
            String field = in.nextLine().toLowerCase();
            System.out.println("Enter new value for this field: ");
            value = in.nextLine();
            switch (field) {
                case "author":
                    book.setAuthor(value);
                    Data.overwrite(book);
                    break;
                case "name":
                    book.setName(value);
                    Data.overwrite(book);
                    break;
                case "datepub":
                    book.setDatePublished(value);
                    Data.overwrite(book);
                    break;
                case "publisher":
                    book.setPublisher(value);
                    Data.overwrite(book);
                    break;
                case "desc":
                    book.setDescription(value);
                    Data.overwrite(book);
                    break;
                default:
                    throw new InvalidType(field);
        
            }
        }
        catch (FileNotFoundException f) {
            throw new InvalidAction("Modify", String.format("Book with %s %s not found", "ID", id));
        }
        
    }

    /**
     * Delete a file
     *
     * @param category: student or book
     * @throws InvalidType
     * @throws FileNotFoundException
     */
    public void deleteEntry(String category) throws InvalidType, FileNotFoundException, FileOverwrite, IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter ID of file to be deleted: ");
        String id = in.next();
        switch (category) {
            case "student":
                try{                
                    DataStorage sdata = Data.writeToObject(category, id);
                    Data.delete(sdata);
                } catch (FileNotFoundException f) {
                    System.out.println(String.format("%s with ID: %s not found", category, id));
                }

                break;
            case "book":
                try {
                    DataStorage bdata = Data.writeToObject(category, id);
                    Data.delete(bdata);
                } catch (FileNotFoundException f) {
                    System.out.println(String.format("%s with ID: %s not found", category, id));
                }
                
                break;
            default:
                // Throw Invalid Type Error if user input is not "student" or "book"
                throw new InvalidType(category);
        }
    }

    /**
     * withdraw a book for a student
     *
     *
     * @throws InvalidType
     * @throws FileNotFoundException
     * @throws InvalidAction
     * @throws FileOverwrite
     */
    public void withdrawBook() throws InvalidType, FileNotFoundException, InvalidAction, FileOverwrite, IOException {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter withdrawing student's A Number (Starting with A): ");
        String sID = in.next();
        //try
        Student student = Search.getStudent("A Number", sID).get(0);
        //catch
        
        System.out.println("Enter ID of book to be withdrawn: ");
        String bID = in.next();
        
        //try
        Book book = Search.getBook("ID", bID).get(0);
        //catch
        
        if (student.canWithdraw() && book.canWithdraw()) {
            student.withdraw(book);
        } else if (!student.canWithdraw()){
            System.out.println(String.format("No withdraw for %s. They owe %s in fines", student.getName(), student.getFines()+student.getBackFines()));
        } else if (!book.canWithdraw()){
            System.out.println(String.format("%s was withdrawn on %s", book.getName(), book.getDateWithdrawn()));
        }
        ArrayList<Book> studentBooks = student.getWithdrawnBooks();
        System.out.println(String.format("All %s's withdrawn books", student.getName()));
        System.out.println("ID\tTitle\t\tAuthor\t\tDate Withdrawn\t\tFees Owed");
        studentBooks.forEach(bk -> {
            System.out.println(String.format("%s\t%s\t%s\t%s\t%s", bk.getId(), bk.getName(), bk.getAuthor(), bk.getDateWithdrawn(), bk.getFine()));
        });
        // Commit changes to file
        Data.overwrite(student);
        Data.overwrite(book);
    }

    /**
     * return a book for a student
     *
     * @throws FileNotFoundException
     * @throws InvalidAction
     * @throws FileOverwrite
     */
    public void returnBook() throws FileNotFoundException, InvalidAction, FileOverwrite, IOException {
        Scanner in = new Scanner(System.in);
        //Entering student id to create student object
        System.out.println("Enter ID of student returning book: ");
        String sid = in.next();
        //try
        Student student = (Student) Data.writeToObject("student", sid);
        //catch
        
        //Book id for book object
        System.out.println("Enter ID of book being returned: ");
        String bid = in.next();
        
        //try
        Book book = (Book) Data.writeToObject("book", bid);
        //catch
        
        //Returning book from student account
        student.returnBook(book);

        // Commit Changes to file
        Data.overwrite(student);
        Data.overwrite(book);
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public void printBill() throws FileNotFoundException, IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter ID of student to print bill for: ");
        // Create student object using student ID to access student methods
        String sid = in.next();
        Student s = (Student) Data.writeToObject("student", sid);
        ArrayList<Book> studentBooks = s.getWithdrawnBooks();
        studentBooks.forEach(bk -> {
            System.out.println(String.format("%s has withdrawn: %s on: %s", s.getName(), bk.getName(), bk.getDateWithdrawn()));
            if (bk.getFine()>0){
                System.out.println(String.format("%s owes $%.2f in late fines on %s",s.getName(), bk.getFine(),bk.getName()));
            }
        });
        if (s.getBackFines()>0){
            System.out.println(String.format("%s owes $%.2f in back fines on previously returned books", s.getName(),s.getBackFines()));
        }
    }

    /**
     * pay student fines on returned books
     *
     * @throws FileNotFoundException
     * @throws InputMismatchException
     * @throws InvalidAction
     * @throws FileOverwrite
     */
    public void payFines() throws FileNotFoundException, InputMismatchException, InvalidAction, FileOverwrite, IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Pay fines for which student? (Enter ID number)");
        String sid = in.next();
 
        Student s = (Student) Data.writeToObject("student", sid);
        if(s.getBackFines()>0){
            System.out.println("Enter amount to pay in fines");
            float entry = in.nextFloat();
            s.setBackFines(s.getBackFines() - entry);
            System.out.println(String.format("%s now owes $%.2f", s.getName(), s.getBackFines()));
            Data.overwrite(s);
        } else if (s.getBackFines()==0&&s.getFines()>0){
            System.out.println(String.format("%s owes nothing on returned books and %s on withdrawn books. Return withdrawn books before paying fines", s.getName(), s.getFines()));
        } else if (s.getBackFines()==0 && s.getFines()==0){
            System.out.println(String.format("%s owes no money on returned or withdrawn books", s.getName()));
        }

    }

}
