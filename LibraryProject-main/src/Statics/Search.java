package Statics;

import DataStorage.*;
import Exceptions.InvalidAction;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
 * Interacts with Data class to load files to objects, return object
 */
/**
 *
 * @author Celia, Caleb
 */
public final class Search {

    private Search() {
    }

    // CALEBS METHODS
    // Return the IDs and names of all objects of a given type
    /**
     *
     * @param subtype
     * @return String formattedData
     * @throws FileNotFoundException
     */
    public static String view(String subtype) throws FileNotFoundException, InvalidAction, IOException {

        //Store an array of DataStorage Objects:
        DataStorage[] allObjects = Data.getAll(subtype);
        String formattedData = "";

        // Loop objects
        for (DataStorage object : allObjects) {
            // Call object.getId(), object.getName()
            String id = object.getId();
            String name = object.getName();
            // Store these values in a formatted string
            String s = String.format(id + "     " + name + "\n");
            formattedData += s;
        }

        // Return Formatted String
        return formattedData;

    }

    /**
     *
     * @param subtype
     * @return all data related to all objects of a given type
     * @throws FileNotFoundException
     */
    public static String viewVerbose(String subtype) throws FileNotFoundException, InvalidAction, IOException {

        // Store an array or DataStorage Objects:
        DataStorage[] objects = Data.getAll(subtype);
        String formattedString = "";

        // Loop Objects, Call object.format()
        for (DataStorage o : objects) {
            String list = Arrays.toString(o.format());
            formattedString += list + "\n";
        }

        // Return formatted string
        return formattedString;

    }


    /**
     *
     * @param key
     * @param value
     * @return students whose attributes and values match parameters
     * @throws FileNotFoundException
     */
    public static ArrayList<Student> getStudent(String key, String value) throws FileNotFoundException, InvalidAction, IOException {
        ArrayList<String> studentIDs = Data.getDataIndices("student");
        ArrayList<Student> students = new ArrayList<>();

        // If student is searched by ID, return object with that filename
        if (key.equalsIgnoreCase("A Number")) {
            try {
                students.add((Student) Data.writeToObject("student", value));
            } catch(FileNotFoundException f) {
                throw new InvalidAction("Search", String.format("Student with %s : %s not found in database", key, value));
            }
            return students;
        }
        // If key is "random", return a random student
        else if (key.equalsIgnoreCase("random")) {
            Random randObject = new Random();
            int fileIndex = randObject.nextInt(studentIDs.size());
            try {
                students.add((Student) Data.writeToObject("student", studentIDs.get(fileIndex)));
            } catch(FileNotFoundException f) {
                throw new InvalidAction("Search", String.format("Student with %s : %s not found in database", key, value));
            }
            return students;
        }
        else {
            // Else, Loop all files in Student folder
            for (int i = 0; i < studentIDs.size(); i++) {
                
                try {
                    Student student = (Student) Data.writeToObject("student", studentIDs.get(i));
                
                    // Enumerate Student keys
                    Hashtable<String, String> studentData = student.getData();
                    Enumeration<String> studentKeys = studentData.keys();
                    while (studentKeys.hasMoreElements()) {
                        // If the key matches param key
                        if (key.equalsIgnoreCase(studentKeys.nextElement())) {
                            // If the value matches param value
                            if (value.equalsIgnoreCase(studentData.get(key))) {
                                // This Student Matches! Add them to the return list
                                students.add(student);
                            }
                        }
                    }
                    }
                catch(FileNotFoundException f) {
                    throw new InvalidAction("Search", String.format("Student with %s : %s not found in database", key, value));
                }
        }
        return students;
    }
    }

    /**
     *
     * @param key
     * @param value
     * @return books whose attributes and values match parameters
     * @throws FileNotFoundException
     * @throws InvalidAction
     */
    public static ArrayList<Book> getBook(String key, String value) throws FileNotFoundException, InvalidAction, IOException {
        ArrayList<String> bookIDs = Data.getDataIndices("book");
        ArrayList<Book> books = new ArrayList<>();

        // If book is searched by ID, return object with that filename
        if (key.equalsIgnoreCase("ID")) {
            try {
                books.add((Book) Data.writeToObject("book", value));
            }
            catch(FileNotFoundException f) {
                throw new InvalidAction("Search", String.format("Book with %s : %s not found in database", key, value));
            }
            return books;
        }
        // If key is "random", return a random student
        else if (key.equals("random")) {
            Random randObject = new Random();
            int fileIndex = randObject.nextInt(bookIDs.size());
            try {
                books.add((Book) Data.writeToObject("book", bookIDs.get(fileIndex)));     
            }
            catch(FileNotFoundException f) {
                throw new InvalidAction("Search", String.format("Book with %s : %s not found in database", key, value));
            }
            return books;
        }
        else{
            // Else, Loop all files in Book folder
            for (int i = 0; i < bookIDs.size(); i++) {
                try {
                    Book book = (Book) Data.writeToObject("book", bookIDs.get(i));

                    // Enumerate Book keys
                    Hashtable<String, String> bookData = book.getData();
                    Enumeration<String> bookKeys = bookData.keys();
                    while (bookKeys.hasMoreElements()) {
                        // If the key matches param key
                        String bookKey = bookKeys.nextElement();
                        if (key.equalsIgnoreCase(bookKey)) {
                            // If the value matches param value
                            String bookValue = bookData.get(bookKey);
                            if (value.equalsIgnoreCase(bookValue)) {
                                // This Book Matches! Add it to the return list
                                books.add(book);
                            }
                        }
                    }
                } catch (FileNotFoundException f) {
                    throw new InvalidAction("Search", String.format("Book with %s : %s not found in database", key, value));
                }
                
            }
        }
        
        return books;
    }
}
