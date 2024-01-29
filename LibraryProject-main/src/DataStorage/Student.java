/*
 * Class Student extends DataStorage, implements Withdrawable
 */
package DataStorage;

import Exceptions.InvalidAction;
import Statics.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Celia
 */
public class Student extends DataStorage {

    // Keys associated with student subclass
    private final String[] keys = new String[]{"A Number", "Name", "Withdrawn Books", "Fines Owed", "Back Fines"};

    // Instance Variables
    private String aNumber = "";
    private String name = "";
    private ArrayList<Book> withdrawnBooks = new ArrayList<>();

    private double backFines=0.0;   // Fines on returned books
    private double fines = 0.0;     // total fines

    /**
     * Constructor sets type in super class, passing String subtype as arg
     *
     * @param aNumber: unique id and filename
     */
    public Student(String aNumber) {
        super("student");
        this.aNumber = aNumber;
    }

    /**
     * Default Constructor
     */
    public Student() {
        super("student");
    }

    /**
     *
     * @return keys: String keys which correspond with variable values
     */
    @Override
    public String[] getKeys() {
        return keys;
    }

    // Setters and getters for instance variables
    /**
     *
     * @return aNumber: File name/Unique identifier
     */
    @Override
    public String getId() {
        return aNumber;
    }

    /**
     *
     * @param aNumber: File name/Unique identifier
     */
    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    /**
     *
     * @return Name of student
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return withdrawnBooks: ArrayList of books which student has withdrawn
     * currently
     */
    public ArrayList<Book> getWithdrawnBooks() {
        return withdrawnBooks;
    }

    /**
     *
     * @param withdrawnBooks ArrayList of books which student has withdrawn
     * currently
     */
    public void setWithdrawnBooks(ArrayList<Book> withdrawnBooks) {
        this.withdrawnBooks = withdrawnBooks;
    }

    /**
     *
     * @return fines owed on each book
     */
    public double getFines() {
        fines=0;
        withdrawnBooks.forEach(book -> {
        fines += book.getFine();
        });
        return fines;
    }

    /**
     *
     * @param fines: total fines student owes
     * @throws InvalidAction if arg is negative
     */
    public void setFines(double fines) {
        this.fines = fines;
    }
    
    public void setBackFines(double fines) throws InvalidAction{
        if (backFines >= 0) {
            this.backFines = fines;
        } else {
            throw new InvalidAction("Set Back Fines", "Student cannot pay more than balance");
        }
    }
    
    public double getBackFines(){
        return backFines;
    }

    /**
     *
     * @return hashtable of instance variables and their values
     */
    @Override
    public Hashtable getData() {

        Hashtable<String, String> studentData = new Hashtable<>();

        for (String key : keys) {

            switch (key) {

                case "A Number":
                    studentData.put(key, aNumber);
                    break;

                case "Name":
                    studentData.put(key, name);
                    break;

                case "Withdrawn Books":
                    String bookIDs = "";
                    if (withdrawnBooks != null && withdrawnBooks.size() > 0) {
                        for (Book book : withdrawnBooks) {
                            if (book != null) {
                                bookIDs += book.getId();
                                bookIDs += " ";
                            }
                        }
                    }
                    studentData.put(key, bookIDs);
                    break;

                case "Fines Owed":
                    String convertFines = "" + getFines();
                    studentData.put(key, convertFines);
                    break;
                    
                case "Back Fines":
                    String convertBackFines = ""+ this.backFines;
                    studentData.put(key, convertBackFines);

            }

        }

        return studentData;

    }

    /**
     * Set all instance variables according to keys and values
     *
     * @param data is hashtable of instance variable keys and corresponding
     * values
     */
    public void setData(Hashtable data) {

        Enumeration<String> dataKeys = data.keys();
        while (dataKeys.hasMoreElements()) {
            String k = dataKeys.nextElement();

            switch (k) {
                case "A Number":
                    setaNumber((String) data.get(k));
                    break;
                case "Name":
                    setName((String) data.get(k));
                    break;
                case "Withdrawn Books":
                    String bookID = (String) data.get(k);
                    String[] bookIDs = bookID.split(" ");
                    ArrayList<Book> books = new ArrayList<>();
                    for (String id : bookIDs) {
                        try {
                            Book book = (Book) Data.writeToObject("book", id);
                            books.add(book);
                        } catch (FileNotFoundException ex) {
                        } catch (IOException ex) {
                    Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                    setWithdrawnBooks(books);
                    break;
                case "Fines Owed":
                    setFines(Double.parseDouble((String) data.get(k)));
                case "Back Fines":
                {
                    try {
                        setBackFines(Double.parseDouble((String) data.get(k)));
                    } catch (InvalidAction ex) {}
                }
                break;


            }
        }
    }

    /**
     * Add a book to withdrawnBooks
     *
     * @param book is the book for this student to withdraw
     * @throws InvalidAction if student already has this book withdrawn
     */
    public void withdraw(Book book) throws InvalidAction {

        // throw error if book already withdrawn by this student
        if (withdrawnBooks.size() > 0) {
            Enumeration<Book> b = Collections.enumeration(withdrawnBooks);
            while (b.hasMoreElements()) {
                Book thisB = b.nextElement();
                if (thisB.getId().equals(book.getId())) {
                    // Throw new exception here
                    throw new InvalidAction("Withdrawl", "Student already has book.");
                }
            }
        }
        book.withdraw();
        withdrawnBooks.add(book);

    }

    /**
     *
     * @param book is book student is returning
     * @throws InvalidAction if student does not have book withdrawn
     */
    public void returnBook(Book book) throws InvalidAction {

        boolean success = false;
        // throw error if book is not withdrawn by student
        for (int i=0; i<withdrawnBooks.size(); i++) {
            if(withdrawnBooks.get(i).getId().equals(book.getId())){
                // set back fines
                this.backFines += book.getFine();
                book.returnBook();
                withdrawnBooks.remove(i);
                success=true;
            }
        }
        
        if (!success) {
            throw new InvalidAction("Return", "Student does not have book.");
        }

    }

    /**
     *
     * @return true if no fines are owed
     */
    @Override
    public boolean canWithdraw() {
        return getFines() == 0 && getBackFines()==0;
    }

}
