import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class main {

    private static HashMap<String, Integer> supportTable = new HashMap<String, Integer>();
    private static List<String> transactions = new ArrayList<String>();
    private static List<List<String>> orderedTransactions = new ArrayList<List<String>>();
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
                    transactions.add(lineItems[2]);
                }
            }
            scanr.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " " + "doesn't exist.");
        }
    }

    public static void orderTransactions(){
        for(String transaction : transactions){
            List<String> tempArr = new ArrayList<String>();
            // Pruning transaction items
            for (String str : transaction.split(" ")) {
                // Check to see if item meets support
                try {
                    if (supportTable.get(str) < support) {
                        continue;
                    }
                    else {
                        tempArr.add(str);
                    }
                } catch (NullPointerException e) {
                    System.out.println("Error finding '" + str + "'in hashmap.");
                }
            }
            // Sort transaction
            for (String str : tempArr) {
                // 
            }
            System.out.println(tempArr);
        }
    }

    public static void main(String[] args) {
        // Add try catch here
        readFile(args[0]);
        System.out.println(transactions);
        System.out.println(supportTable);
        orderTransactions();
    }
}