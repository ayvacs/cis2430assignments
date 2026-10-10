package dayplanner;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;



/**
 * Contains utility methods for the application class.
 * Contains extra code that is not required for the main functions of the command loop.
 */
public class DayPlannerUtils {
    private static final Input KEYBOARD = new Input();

    /**
     * Get the user's name.
     * If not defined, prompt the user for a name.
     * This persists between sessions.
     * @param dirName Name of the data directory
     * @param fileName Name of the username file
     * @return User's username
     */
    public static String getUsername(String dirName, String fileName) {
        File file = new File(dirName, fileName);

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine().trim();

            br.close();
            return line;
        } catch (FileNotFoundException e) {

            try {
                if (!file.exists()) {
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);

                    System.out.println("\n\nHello new user, please enter your name:");
                    String name = KEYBOARD.readString();

                    fw.write(name);
                    fw.close();

                    return name;
                }
            } catch (Exception f) {}

        } catch (Exception e) {}
        
        return "exception";
    }

    /**
     * Get a greeting for the user.
     * @param username User's name
     * @return Greeting (i.e. Good morning, John!)
     */
    public static String getGreeting(String username) {
        int hour = LocalTime.now().getHour();
        String greeting;

        if (hour < 12)
            greeting = "Good morning";
        else if (hour < 17)
            greeting = "Good afternoon";
        else
            greeting = "Good evening";

        String text = "%s, %s!";
        return String.format(text, greeting, username);
    }

    /**
     * Delete the user's name and the username file, without prompting for a new name
     * @param dirName Name of the data directory
     * @param fileName Name of the username file
     */
    public static void flushUsername(String dirName, String fileName) {
        try {
            File file = new File(dirName, fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done, although username isn't refreshed until program is relaunched");
    }

    /**
     * Default constructor (not used).
     * This exists to suppress a Javadoc warning.
     */
    public DayPlannerUtils() {}
}
