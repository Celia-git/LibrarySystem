/*
 * Class Book extends DataStorage, implements Withdrawable
 */
package src.DataStorage;

import Exceptions.InvalidAction;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Hezekiah, McKye
 */
public class Book extends DataStorage {

    // Keys associated with book subclass
    private final String[] keys = new String[]{"ID", "Author", "Title", "Date Published", "Publisher", "Description"};

    // Instance variables - Edited by user:Admin
    private String id = "";
    private String author = "";
    private String title = "";
    private String datePublished = "";
    private String publisher = "";
    private String description = "";

    // Instance variables - Not edited by user
    private boolean available = true;
    private double timeWithdrawn = 0.0;   // Amount of days total that the book has been withdrawn
    private LocalDate dateWithdrawn;      // The date on which the book was withdrawn
    private static int withdrawWindow = 30;
    private final int RATE = 2;           // $ in late fees per days late

    /**
     * Constructors: Call super constructor, passing String subtype as arg
     *
     * @param id: File name/Unique identifier
     */
    public Book(String id) {
        super("book");
        this.id = id;
    }

    /**
     * Default Constructor
     */
    public Book() {
        super("book");
    }

    /**
     *
     * @return keys: String keys which correspond with variable values
     */
    @Override
    public String[] getKeys() {
        return keys;
    }

    // Setters + Getters for instance variables
    /**
     *
     * @return id: File name/Unique identifier
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     *
     * @param id: File name/Unique identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return author: String represents author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author: String represents author name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return title: String represents book title
     */
    @Override
    public String getName() {
        return title;
    }

    /**
     *
     * @param title: String represents book title
     */
    @Override
    public void setName(String title) {
        this.title = title;
    }

    /**
     *
     * @return datePublished
     */
    public String getDatePublished() {
        return datePublished;
    }

    /**
     *
     * @param datePublished
     */
    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    /**
     *
     * @return publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return data: hashtable of instance variables and their values
     */
    @Override
    public Hashtable getData() {

        Hashtable<String, String> bookData = new Hashtable<String, String>();

        for (String key : keys) {

            switch (key) {

                case "ID":
                    bookData.put(key, id);
                    break;

                case "Title":
                    bookData.put(key, title);
                    break;

                case "Author":
                    bookData.put(key, author);
                    break;

                case "Date Published":
                    bookData.put(key, datePublished);
                    break;

                case "Publisher":
                    bookData.put(key, publisher);
                    break;

                case "Description":
                    bookData.put(key, description);
                    break;
            }
        }
        bookData.put("Available", Boolean.toString(available));
        if (dateWithdrawn != null) {
            bookData.put("Date Withdrawn", dateWithdrawn.toString());
        }

        return bookData;
    }

    /**
     *
     * @param data: Set all instance variables according to keys and values
     */
    @Override
    public void setData(Hashtable data) {

        Enumeration<String> dataKeys = data.keys();
        while (dataKeys.hasMoreElements()) {
            String k = dataKeys.nextElement();

            switch (k) {
                case "ID":
                    setId((String) data.get(k));
                    break;

                case "Author":
                    setAuthor((String) data.get(k));
                    break;

                case "Title":
                    setName((String) data.get(k));
                    break;

                case "Date Published":
                    setDatePublished((String) data.get(k));
                    break;

                case "Publisher":
                    setPublisher((String) data.get(k));
                    break;

                case "Description":
                    setDescription((String) data.get(k));
                    break;
            }
        }

        // Set Available and Date Withdrawn
        if (data.get("Available") != null) {
            available = data.get("Available").equals("true");
        }
        if (data.get("Date Withdrawn") != null) {
            dateWithdrawn = LocalDate.parse((CharSequence) data.get("Date Withdrawn"));
        }
        // If not available, set Time withdrawn
        if (!available) {
            timeWithdrawn = ChronoUnit.DAYS.between(LocalDate.now(), dateWithdrawn);
            
        }
    }

    /**
     *
     * @return fine: amount owed on this book
     */
    public double getFine() {

        if (dateWithdrawn == null) {
            timeWithdrawn = 0;
        } else{
            timeWithdrawn = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), dateWithdrawn));
        }

        double fine = 0.0;

        // Calculate fine based on days late
        if (timeWithdrawn > withdrawWindow) {
            fine = RATE * (timeWithdrawn - withdrawWindow);
        }

        return fine;
    }

    /**
     *
     * @return Date withdrawn
     */
    public String getDateWithdrawn() {
        if (dateWithdrawn != null){
            return dateWithdrawn.toString();
        }
        return "";
    }

    /**
     * Mark this book as withdrawn
     *
     * @throws InvalidAction if book is not available
     */
    public void withdraw() throws InvalidAction {
        if (!available) {
            throw new InvalidAction("Withdrawl", "Book not available");
        } else {
            // Set dateWithdrawn to today's date  
            dateWithdrawn = LocalDate.now();
            available = false;
        }
    }

    /**
     * Return this book
     *
     * @throws InvalidAction if book is already returned
     */
    public void returnBook() throws InvalidAction {
        if (available) {
            throw new InvalidAction("Return", "Book is already returned");
        } else {
            timeWithdrawn = 0.0;
            dateWithdrawn = null;
            available = true;
        }
    }

    /**
     *
     * @return true if book is available
     */
    @Override
    public boolean canWithdraw() {
        return available;
    }

    /**
     * Test the program's late fees system by changing the date withdrawn
     *
     * @param daysLate specifies how many days the book will be late for the
     * test
     */
    public void testFine(int daysLate) {
        dateWithdrawn = LocalDate.now().minusDays(withdrawWindow + daysLate);
    }
    
    public String getDueDate(){
        return dateWithdrawn.plusDays(withdrawWindow).toString();

    }

}
