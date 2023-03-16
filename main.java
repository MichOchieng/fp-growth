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
    private static double support = 2.0;
    private static double confidence;

    public static void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanr = new Scanner(file);
            while (scanr.hasNextLine()) {
                String line = scanr.nextLine();
                if (line.length() > 1) { // Skip the first line
                    String[] lineItems = line.trim().split("\t");
                    // If key present increment support otherwise initialize
                    for (String val : lineItems[2].split("\\s+")) {
                        supportTable.put(val, supportTable.getOrDefault(val, 0) + 1);
                    }
                    transactions.add(lineItems[2].split(" "));
                }
            }
            scanr.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " " + "doesn't exist.");
        }

        // Sort the Suport Table
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
            orderedTransactions.add(tempArr);
        }

    }

    public static void main(String[] args) {
        // Add try catch here
        readFile(args[0]);

        System.out.println(supportTable);
        System.out.println(orderedTransactions);
        fpgrowth fp = new fpgrowth(supportTable, orderedTransactions);
        fp.createFpTree();
        // fp.viewTree();
        // orderTransactions();
    }
}