package dayplanner;


import java.util.Scanner;
import java.io.InputStream;


/**
 * Wrapper for the <code>Scanner</code> class that contains useful methods for input validation and working with the other classes in this package.
 */
public class Input {
    private final Scanner SCANNER;


    /**
     * Instantiate a new <code>Input</code> instance from a given <code>InputStream</code>.
     * @param stream The input stream this instance will read from.
     */
    public Input(InputStream stream) {
        SCANNER = new Scanner(stream);
    }

    /**
     * Instantiate a new <code>Input</code> instance for the keyboard input (<code>System.in</code>).
     */
    public Input() {
        this(System.in);
    }



    /**
     * Read and return an integer value.
     * Continues prompting the user until a valid integer is entered.
     * Code inspired by <b>Defensive Programming Examples</b> from this course's notes.
     * @return User input as an integer.
     */
    public int readInt() {
        int input = 0;

        do {
            System.out.print("(integer) > ");
            String line = SCANNER.nextLine();

            if (line.matches("[-+]?[0-9]+")) {
                input = Integer.parseInt(line);
                break;
            } else {
                System.out.println("\nInvalid integer. Try again:");
            }
        } while (true);
        
        System.out.print("\n");
        return input;
    }


    
    /**
     * Read and return a <code>String</code> value.
     * Trims all whitespace from the beginning and end of the <code>String</code>.
     * @return User input as a <code>String</code>.
     */
    public String readString() {
        System.out.print("(string) > ");

        String input = SCANNER.nextLine();
        input = input.trim();
        
        System.out.print("\n");
        return input;
    }



    /**
     * Read and return a boolean value.
     * Continues prompting the user until a valid boolean is entered.
     * Accepts any word that starts with <code>Y</code> as <code>true</code> and any word that starts with <code>N</code> as <code>false</code>.
     * @return User input as a boolean.
     */
    public boolean readBool() {
        char input;
        boolean ret;

        do {
            System.out.print("(Y/N) > ");
            input = Character.toUpperCase(SCANNER.nextLine().trim().charAt(0));

            if (input == 'Y') {
                ret = true;
                break;
            }
            else if (input == 'N') {
                ret = false;
                break;
            }
            else
                System.out.println("\nInvalid boolean. Try again; try typing Yes or No.");
        } while (true);
        
        System.out.println(" ");
        return ret;
    }



    /**
     * Read and return a <code>Time</code> value of the format <code>YYYY/MM/DD HH:MM</code>.
     * Does not check whether the input follows the specified format; rather, relies on input validation as described in the <code>Time</code> class.
     * @return User input as a <code>Time</code>.
     */
    public Time readTime() {
        System.out.print("(YYYY/MM/DD HH:MM) > ");

        String input = SCANNER.nextLine();
        Time time = new Time(input);
        // new Time(input) never fails when input is a string
        
        System.out.println(" ");
        return time;
    }
}