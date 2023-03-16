import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class main {

    // Tables
    private static HashMap<String, Integer> supportTable = new HashMap<String, Integer>();
    private static List<String[]> transactions = new ArrayList<String[]>();
    private static List<List<String>> orderedTransactions = new ArrayList<List<String>>();
    // Params
    private static Integer support;

    public static void readFile(String filename, Integer sup) {
        try {
            support = sup;
            File file = new File(filename);
            Scanner scanr = new Scanner(file);
            int i = 0;
            while (scanr.hasNextLine()) {
                String line = scanr.nextLine();
                if (i > 0) { // Skip the first line
                    String[] lineItems = line.trim().split("\t");
                    // If key present increment support otherwise initialize
                    for (String val : lineItems[2].split("\\s+")) {
                        supportTable.put(val, supportTable.getOrDefault(val, 0) + 1);
                    }
                    transactions.add(lineItems[2].split(" "));
                }
                i++;
            }
            scanr.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " " + "doesn't exist.");
        }

        // Sort the Suport Table
        // Creates a temp list that can use the support table supports as keys to sort
        // transactions later on in correct order
        List<Entry<String, Integer>> tempList = new LinkedList<>(supportTable.entrySet());

        Collections.sort(tempList, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                // TODO Auto-generated method stub
                return o2.getValue().compareTo(o1.getValue()); // Sorts in descending order
            }
        });

        supportTable = new LinkedHashMap();
        for (Entry<String, Integer> entry : tempList) {
            supportTable.put(entry.getKey(), entry.getValue());
        }

        // Pruning items below support
        Iterator<Entry<String, Integer>> itr = supportTable.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, Integer> entry = itr.next();
            if (entry.getValue() < support) {
                itr.remove();
            }
        }

        // Pruning and Sorting transactions
        // Uses the tempArr from before
        for (String[] string : transactions) {
            List<String> tempArr = new ArrayList<String>();
            for (String string2 : string) {
                try {
                    if (supportTable.get(string2) >= support) {
                        tempArr.add(string2);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            // Awkward lambda for sorting transactions in descending order
            Collections.sort(tempArr, (x, y) -> supportTable.get(y).compareTo(supportTable.get(x)));
            // Add ordered transaction to list of transactions
            orderedTransactions.add(tempArr);
        }

    }

    public static void main(String[] args) {
        // Error handling for inputs
        try {
            readFile(args[0], Integer.parseInt(args[1]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Make sure to add the filename and support level!");
        } catch (NumberFormatException e) {
            System.out.println("Make sure to enter a number as the second parameter!");
        }
        // Create fpTree
        fpgrowth fp = new fpgrowth(supportTable, orderedTransactions, support);
        fp.createFpTree();
        // Print patterns
        fp.viewFrequentPatterns();
    }
}