package dayplanner;


import java.util.Scanner;
import java.io.InputStream;


/**
 * Wrapper for the <code>Scanner</code> class that contains useful methods for input validation and working with the other classes in this package.
 */
public class Input {
    private final Scanner SCANNER;


    /**
     * Instantiate a new <code>Input</code> instance.
     * @param InputStream The input stream this instance will read from.
     */
    public Input(InputStream in) {
        SCANNER = new Scanner(in);
    }


    /**
     * Read and return an integer value. Continues prompting the user until a valid integer is entered. Code inspired by <b>Defensive Programming Examples</b> from this course's notes.
     */
    public int readInt(){
        int input = 0;
        String line;
        do {
            System.out.print("(integer) > ");
            line = SCANNER.nextLine();
            if (line.matches("[-+]?[0-9]+")) {
                input = Integer.parseInt(line);
                break;
            } else {
                System.out.println("Invalid integer. Try again:");
            }
        } while (true);
        
        System.out.println(" ");
        return input;
    }


    /**
     * Read and return a <code>String</code> value. Trims all whitespace from the beginning and end of the <code>String</code>.
     */
    public String readString(){
        System.out.print("(string) > ");

        String input = SCANNER.nextLine();
        input = input.trim();
        
        System.out.println(" ");
        return input;
    }


    /**
     * Read and return a boolean value. Continues prompting the user until a valid boolean is entered. Accepts any word that starts with <code>Y</code> as <code>true</code> and any word that starts with <code>N</code> as <code>false</code>.
     */
    public boolean readBool(){

        char input;
        boolean ret;
        do {
            System.out.print("(bool) > ");
            input = Character.toUpperCase(readString().charAt(0));
            if (input == 'Y') {
                ret = true;
                break;
            }
            else if (input == 'N') {
                ret = false;
                break;
            }
            else
                System.out.println("Invalid boolean. Try again; try typing Yes or No.");
        } while (true);
        
        System.out.println(" ");
        return ret;
    }


    /**
     * Read and return a <code>Time</code> value of the format <code>YYYY/MM/DD HH:MM</code>. Does not check whether the input follows the specified format; rather, relies on input validation as described in the <code>Time</code> class.
     */
    public Time readTime(){
        System.out.print("(YYYY/MM/DD HH:MM) > ");

        String input = SCANNER.nextLine();
        Time time = new Time(input);
        
        System.out.println(" ");
        return time;
    }
}