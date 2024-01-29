/*
 * Generate test Student and Book files 
 */
package Testers;

import DataStorage.Student;
import DataStorage.Book;
import Exceptions.FileOverwrite;
import Statics.Data;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Celia
 */
public class CreateTestFiles {

    // Student Values
    private static String[] aNums = {
        "A001",
        "A002",
        "A003",
        "A004",
        "A005",
        "A006",
        "A007",
        "A008",
        "A009",
        "A010"
    };
    private static String[] names = {
        "Michael",
        "Christopher",
        "Jessica",
        "Matthew",
        "Ashley",
        "Jennifer",
        "Joshua",
        "Amanda",
        "Daniel",
        "David"
    };

    // Book Values
    private static String[] ids = {
        "001",
        "002",
        "003",
        "004",
        "005",
        "006",
        "007",
        "008",
        "009",
        "010"
    };
    private static String[] authors = {
        "J.R.R. Tolkien",
        "Joseph Heller",
        "Fyodor Dostoevsky",
        "Cormac McCarthy ",
        "Gabriel García Márquez",
        "J.R.R. Tolkien",
        "Amor Towles",
        "George Orwell",
        "Anthony Doerr",
        "John Steinbeck"
    };
    private static String[] titles = {
        "The Hobbit",
        "Catch-22",
        "Crime and Punishment",
        "Blood Meridian",
        "One Hundred Years of Solitude",
        "The Lord of the Rings",
        "A Gentleman in Moscow",
        "1984",
        "All the Light We Cannot See",
        "East of Eden"
    };
    private static String[] dates = {
        "1937",
        "1961",
        "1886",
        "1985",
        "1967",
        "1955",
        "2016",
        "1949",
        "2014",
        "1952"
    };
    private static String[] publishers = {
        "Houghton Mifflin",
        "Simon & Schuster",
        "Penguin Books",
        "Vintage Books",
        "Harper",
        "Houghton Mifflin Harcourt",
        "Penguin Books",
        "Houghton Mifflin Harcourt",
        "Scribner",
        "Penguin Books"
    };
    private static String description = "Lorem ipsum dolor sit amet";

    /**
     * create new entries in student.txt
     *
     * @throws FileOverwrite
     * @throws FileNotFoundException
     */
    public static void newStudents() throws FileOverwrite, FileNotFoundException, IOException {

        for (int i = 0; i < aNums.length; i++) {
            Student s = new Student(aNums[i]);
            s.setName(names[i]);
            Data.addNewEntry(s);
        }

    }

    /**
     * Create new entries in book.txt
     *
     * @throws FileOverwrite
     * @throws FileNotFoundException
     */
    public static void newBooks() throws FileOverwrite, FileNotFoundException, IOException {

        for (int i = 0; i < ids.length; i++) {
            Book b = new Book(ids[i]);
            b.setAuthor(authors[i]);
            b.setName(titles[i]);
            b.setDatePublished(dates[i]);
            b.setPublisher(publishers[i]);
            b.setDescription(description);
            Data.addNewEntry(b);
        }

    }

}
