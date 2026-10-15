package dayplanner;


import java.util.Scanner;
import java.io.InputStream;


/**
 * <p>
 * Console input helper backed by a {@link Scanner}.
 * </p>
 * <p>
 * Read methods display prompts and generally retry invalid input. The stream
 * constructor also supports scripted input, such as a
 * {@code ByteArrayInputStream} in a small example program, which is actually
 * used in {@link Tester} for scripted input.
 * </p>
 * <p>
 * The goal of this class is to make user input as easy as possible to deal with
 * in application classes. So, this class does a lot of the heavy lifting for
 * you.
 * </p>
 */
public class Input {
    private final Scanner SCANNER;


    /**
     * Constructs an <code>Input</code> instance that reads from the specified
     * stream.
     * 
     * @param stream Source of input.
     */
    public Input(InputStream stream) {
        SCANNER = new Scanner(stream);
    }

    /**
     * Constructs an <code>Input</code> instance that reads from standard input
     * (<code>System.in</code>).
     */
    public Input() {
        this(System.in);
    }



    /**
     * Reads and returns an integer from user input, prompting repeatedly until
     * valid.
     * 
     * @return The next parseable integer. Invalid lines are rejected and requested
     *         again.
     */
    public int readInt() {
        int input = 0;

        do {
            System.out.print("(integer) > ");

            String line = null;
            if (SCANNER.hasNextLine())
                line = SCANNER.nextLine().trim();

            if (line != null && line.matches("[-+]?[0-9]+")) {
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
     * Reads and returns a trimmed string from user input.
     * 
     * @return User input as a <code>String</code>, or <code>null</code> if end of
     *         input.
     */
    public String readString() {
        System.out.print("(string) > ");

        String input = null;
        if (SCANNER.hasNextLine())
            input = SCANNER.nextLine().trim();
        
        System.out.print("\n");
        return input;
    }



    /**
     * <p>
     * Reads and returns a boolean from user input, prompting repeatedly until
     * valid.
     * </p>
     * <p>
     * Accepts responses beginning with {@code Y}/{@code y} or {@code N}/{@code n};
     * characters after the first non-space character do not affect the answer.
     * </p>
     * 
     * @return User input as a boolean.
     */
    public boolean readBool() {
        char input;
        boolean ret;

        do {
            System.out.print("(Y/N) > ");
            
            String line = null;
            if (SCANNER.hasNextLine())
                line = SCANNER.nextLine().trim();

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
     * Parses a string representation of time in YYYY/MM/DD HH:MM format.
     * 
     * @param str The string representing a time in YYYY/MM/DD HH:MM format.
     * @return A valid {@link Time}, or {@code null} for empty or invalid input. For
     *         example, {@code 2026/09/15 14:30} produces a time value.
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
     * Reads and returns a valid <code>Time</code> from user input, prompting
     * repeatedly until valid.
     * 
     * @return A valid {@link Time}; malformed values are explained and requested
     *         again.
     */
    public Time readTime() {
        do {
            System.out.print("(YYYY/MM/DD HH:MM) > ");
            String line = null;
            if (SCANNER.hasNextLine())
                line = SCANNER.nextLine().trim();

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
     * Reads and returns a command string from user input.
     * 
     * @return The next line with leading and trailing whitespace removed.
     */
    public String readCommand() {
        System.out.print("(command) > ");

        String input = null;
        if (SCANNER.hasNextLine())
            input = SCANNER.nextLine().trim();

        System.out.print("\n");
        return input;
    }

    /**
     * Prompts the user to press Enter and halts until input is received.
     */
    public void pressEnterToContinue() {
        System.out.println("\nPress enter to continue.");
        SCANNER.nextLine();
    }
}
