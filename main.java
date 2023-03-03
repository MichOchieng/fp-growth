import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class main {

    private static HashMap<String, Integer> supportTable = new HashMap<String, Integer>();
    private static List<String> transactions = new ArrayList<String>();

    public static void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanr = new Scanner(file);

            while (scanr.hasNextLine()) {
                String line = scanr.nextLine();
                if (line.length() > 1) { // Skip the first line
                    String[] lineItems = line.trim().split("\t");
                    // If key present increment support otherwise initialize
                    for (var val : lineItems[2].split("\\s+")) {
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

    public static void main(String[] args) {
        readFile(args[0]);
        System.out.print(transactions);
    }
}