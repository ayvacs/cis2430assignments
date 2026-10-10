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
            String line = SCANNER.nextLine().trim();

            if (line.matches("[-+]?[0-9]+")) {
                try {
                    input = Integer.parseInt(line);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\nInteger value out of range. Try again:");
                }
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
            String line = SCANNER.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("\nInput cannot be empty. Try typing Yes or No.");
                continue;
            }

            input = Character.toUpperCase(line.charAt(0));

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
     * Safely parse a Time from a string representation.
     * @param str The string representing a time in YYYY/MM/DD HH:MM format.
     * @return A valid <code>Time</code> instance, or <code>null</code> if invalid or empty.
     */
    public static Time parseTime(String str) {
        if (str == null || str.trim().isEmpty())
            return null;

        String[] components = str.trim().split("[/:,\\s]+");
        if (components.length < 5)
            return null;

        try {
            int y = Integer.parseInt(components[0]);
            int m = Integer.parseInt(components[1]);
            int d = Integer.parseInt(components[2]);
            int h = Integer.parseInt(components[3]);
            int min = Integer.parseInt(components[4]);

            if (Time.isValidTime(y, m, d, h, min))
                return new Time(y, m, d, h, min);
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }

    /**
     * Read and return a <code>Time</code> value of the format <code>YYYY/MM/DD HH:MM</code>.
     * Defensively validates year, month, day, hour, and minute ranges.
     * Continues prompting the user until a valid time is entered.
     * @return User input as a valid <code>Time</code>.
     */
    public Time readTime() {
        do {
            System.out.print("(YYYY/MM/DD HH:MM) > ");
            String line = SCANNER.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("\nTime cannot be empty. Please enter format YYYY/MM/DD HH:MM (e.g. 2026/9/10 17:30). Try again:");
                continue;
            }

            Time parsed = parseTime(line);
            if (parsed != null) {
                System.out.print("\n");
                return parsed;
            }

            // Detailed validation error feedback
            String[] components = line.split("[/:,\\s]+");
            if (components.length < 5) {
                System.out.println("\nInvalid format. Expected: YYYY/MM/DD HH:MM (e.g. 2026/9/10 17:30). Try again:");
                continue;
            }

            try {
                int y = Integer.parseInt(components[0]);
                int m = Integer.parseInt(components[1]);
                int d = Integer.parseInt(components[2]);
                int h = Integer.parseInt(components[3]);
                int min = Integer.parseInt(components[4]);

                String err = Time.getValidationError(y, m, d, h, min);
                if (err != null) {
                    System.out.println("\nInvalid time: " + err + " Try again:");
                } else {
                    System.out.println("\nInvalid time values. Try again:");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nTime components must be integers. Try again:");
            }
        } while (true);
    }

    /**
     * Read and return a command string from the user.
     * @return User command input trimmed of whitespace.
     */
    public String readCommand() {
        System.out.print("(command) > ");
        String input = SCANNER.nextLine().trim();
        System.out.print("\n");
        return input;
    }



    /**
     * Prints out a list of options and return's the user's choice.
     * Options and choices are both indexed at 1, meaning that the first item in the array of options corresponds to option number 1.
     * @param options An array of option labels
     * @return An integer that corresponds to the user's choice (indexed at 1 as described above). It is always guaranteed that the return value will be between 1 and the number of options.
     * @throws IllegalArgumentException When the array of options is empty.
     */
    public int fromOptions(String[] options) {
        if (options.length < 1)
            throw new IllegalArgumentException("Options array is empty.");

        // print out all of the options
        System.out.println("\nPlease use your keyboard to select one of the following options:");
        for (int i = 0; i < options.length; i++)
            System.out.println("\t" + (i+1) + ": " + options[i]);

        int input = readInt();

        // validate info
        if (input < 1 || input > options.length) {
            System.out.println("\nYou selected an invalid option; try typing a number between 1 and " + options.length + ".");
            return fromOptions(options);
        }

        System.out.print("\n");
        return input;
    }

    /**
     * Prompts the user to click 'enter' to continue, then continues after the user presses 'enter'.
     */
    public void pressEnterToContinue() {
        System.out.println("\nPress enter to continue.");
        SCANNER.nextLine();
    }
}