import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class main {

    public static void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanr = new Scanner(file);
            
            while(scanr.hasNextLine()){
                String line = scanr.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(filename + " " + "doesn't exist.");
        }
    }

    public static void main(String[] args) {
        readFile(args[0]);
    }
}