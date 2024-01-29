/*
    This is the superclass for all classes which contain data to be stored:
    - student
    - book
    sets and returns associated values
    implements Withdrawable
 */
package DataStorage;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Celia
 */
public class DataStorage implements Withdrawable {

    // Type: student or book
    private String type;

    // public constructor: specify subclass type
    /**
     *
     * @param type: type of subclass
     */
    public DataStorage(String type) {
        this.type = type;
    }

    /**
     *
     * @param type: subclass name
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return name of subclass
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return all keys and values formatted as array of strings
     */
    public String[] format() {
        Hashtable data = getData();
        String strData = data.toString();
        strData = strData.substring(1, strData.length() - 1);
        String[] arrData = strData.split(",");
        // Format array of data so ID is first entry
        ArrayList<String> formatArray = new ArrayList<String>(); 
        for (String entry:arrData){
            String[] keyValue = entry.split("=");
            keyValue[0] = keyValue[0].trim();
            if(keyValue[0].equals("ID")||keyValue[0].equals("A Number")){
                formatArray.add(entry);
                formatArray.addAll(removeEntry(entry, arrData));
            }
        }
        String[] stringformatArray = formatArray.toArray(new String[formatArray.size()]);
        return stringformatArray;
    }

    // return array of elements with one entry removed
    public ArrayList<String> removeEntry(String entry, String[] Array){
        ArrayList<String> newArray = new ArrayList<String>();
       
        for (String element:Array){
            if (!element.equals(entry)){
                newArray.add(element);
            }
        }
        return newArray;
    }
    
    /**
     * set all keys and values formatted as a hashtable
     *
     * @param lines specifies data received from files
     */
    public void reverseFormat(ArrayList<String> lines) {
        Hashtable data = new Hashtable();
        for (String line : lines) {
            String[] entry = line.split("=");
            if (entry.length > 1) {
                data.put(entry[0], entry[1]);
            }

        }
        setData(data);
    }

    /**
     *
     * @return all keys in data
     */
    public String[] getKeys() {
        String[] string = {""};
        return string;
    }

    /**
     *
     * @return ID (filename) as string
     */
    public String getId() {
        return "";
    }

    /**
     *
     * @return Name or Title as String
     */
    public String getName() {
        return "";
    }

    /**
     *
     * @param name: set name or title
     */
    public void setName(String name) {

    }

    /**
     *
     * @return hashtable of instance variables and their values
     */
    public Hashtable getData() {
        Hashtable empty = new Hashtable();
        return empty;
    }

    /**
     *
     * @param data: hashtable of keys and values, Set instance variables =
     * values
     */
    public void setData(Hashtable data) {

    }

    /**
     *
     * @return bool true if can withdraw
     */
    @Override
    public boolean canWithdraw() {
        return false;
    }

}
