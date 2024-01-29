/*
    Utility class:
    -   most methods take DataStorage object as argument
    -   write to files using DataStorage object methods
    -   store data in json files with key, value format

 */
package Statics;

import DataStorage.*;
import Exceptions.FileOverwrite;
import Exceptions.InvalidAction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Celia
 */
public final class Data {

    // Instance variables are final
    final static String DIR = "files/";
    final static String EXT = ".txt";
    final static String BRK = "----------------------------------------------";

    // private constructor to restrict object creation
    private Data() {

    }
     /**
     *
     * @param subtype
     * @return all objects of type
     * @throws FileNotFoundException
     */
    public static DataStorage[] getAll(String subtype) throws FileNotFoundException, InvalidAction, IOException {
        ArrayList<String> dataIDs = getDataIndices(subtype);
        DataStorage[] allObjects = new DataStorage[dataIDs.size()];

        for (int i = 0; i < dataIDs.size(); i++) {
            try {
                DataStorage d = writeToObject(subtype, dataIDs.get(i));
                allObjects[i] = d;
                }
            catch(FileNotFoundException f) {
                throw new InvalidAction("Display", String.format("%s with %s : %s not found in database", subtype, "ID", dataIDs.get(i)));
            }
            
        }
        return allObjects;
    }

    /**
     * Read data from a file / write to a DataStorage objects
     *
     * @param type: book or student
     * @param dataID: id which is the first line of data block in file
     * @return: object with file data written to it
     * @throws FileNotFoundException
     */
    public static DataStorage writeToObject(String type, String dataID) throws FileNotFoundException, IOException {

        String filePath = DIR + "/" + type + EXT;
        // create a new DataStorage object: cast as student or book
        DataStorage d;
        if (type.equals("student")) {
            d = new Student();
        } else {
            d = new Book();
        }

        // get formatted string of data from file
        File readFile = new File(filePath);
        ArrayList<String> allLines = (ArrayList<String>) Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        ArrayList<String> dataBlock = getDataBlock(dataID, allLines);
        d.reverseFormat(dataBlock);
        return d;

    }

    // Returns lines of data associated with ID number
    public static ArrayList<String> getDataBlock(String ID, ArrayList<String> lines) throws FileNotFoundException{
        
        ArrayList<String> block = new ArrayList<String>();
        boolean foundData = false;
        
        for (String line:lines){
            // Record data from part of file
            
            if (!line.equals(BRK)){
                String[] keyValue = line.split("=");
                if (keyValue.length == 2){
                    keyValue[1] = keyValue[1].trim();
                    if (keyValue[1].equalsIgnoreCase(ID)){
                        foundData = true;
                    }
                }
                if (foundData){
                    block.add(line);
                }
            } else {
                if (foundData) {
                    return block;
                }
            }
        }
        if (foundData){
            return block;
        }
        throw new FileNotFoundException();
    }
            
      
    
    /**
     *
     * @param type: student or book
     * @return: List of names in Data file
     */
    public static ArrayList<String> getDataIndices(String type) {

        String filepath = DIR + type + EXT;
        ArrayList<String> dataIDs = new ArrayList<>();

        try (Scanner reader = new Scanner(new File(filepath))) {
            while (reader.hasNextLine()) {
                
                String nextLine = reader.nextLine();
                // copy data id from ID line
                String[] keyValue = nextLine.split("=");
                keyValue[0] = keyValue[0].trim();
                if (keyValue[0].equalsIgnoreCase("ID")||keyValue[0].equalsIgnoreCase("A Number")) {
                    dataIDs.add(keyValue[1]);
                }
                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataIDs;
    }

    /**
     * create a new line entry in file from object
     *
     * @param data: DataStorage object to write to file
     * @throws FileNotFoundException
     */
    public static void addNewEntry(DataStorage data) throws FileOverwrite, FileNotFoundException, IOException {
        String filePath = DIR + "/" + data.getType() + EXT;
        FileWriter fw = new FileWriter(filePath, true);
        PrintWriter out = new PrintWriter(fw);
        String[] formatted = data.format();
        out.println(BRK);
        for (String f : formatted) {
            out.println(f.trim());
        }
        fw.close();
        out.close();
    }
    /**
     * overwrite an entry in file from data object
     * @param data: DataStorage object to write to file
     * @throws FileNotFoundException
     */
    
    public static void overwrite(DataStorage data)throws FileOverwrite, FileNotFoundException, IOException{
        String filePath = DIR + "/" + data.getType() + EXT;

        // Delete entry from file and append new entry lines to end of file
        delete(data);
        addNewEntry(data);
        return;
    }

    /**
     * delete an entry in file from data object
     * @param data: DataStorage object to write to file
     * @throws FileNotFoundException
     */        
    public static void delete(DataStorage data)throws FileOverwrite, FileNotFoundException, IOException{
        String filePath = DIR + "/" + data.getType() + EXT;
        
        // Get an array of all lines in file, and lines to remove
        ArrayList<String> allLines = (ArrayList<String>) Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        ArrayList<String> removeLines = getDataBlock(data.getId(), allLines);
        String oldEntries = String.join("&#: ", allLines);
        String removeEntry = String.join("&#: ", removeLines);
        removeEntry = BRK + "&#: " + removeEntry;
        
        String newEntries = oldEntries.replace(removeEntry, "");
        
        // Make an array of lines to keep
        ArrayList<String> keepLines = new ArrayList<String>(Arrays.asList(newEntries.split("&#: ")));
                
        // Write lines to file in overwrite mode
        FileOutputStream fos = new FileOutputStream(filePath, false);
        PrintWriter out = new PrintWriter(fos);
        for(String line:keepLines){
            out.println(line.trim());
        }
        out.close();
        return;
    }
    
    // Overwrite a file with new lines
    public static void reviseFile(String dataType, ArrayList<String[]> formatted) throws IOException {
        
        String filePath = DIR + "/" + dataType + EXT;
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        for (String[] list : formatted) {
            out.println(BRK);
            for (String l:list){
                out.println(l.trim());    
            }
        }
        fw.close();
        out.close();
    
    }

}
