package dayplanner;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;



/**
 * Provides utility methods for the DayPlanner application.
 * Methods include activity type parsing, greeting generation, username storage, and search filtering.
 */
public class DayPlannerUtils {
    private static final Input KEYBOARD = new Input();



    /**
     * Resolves an activity type input string to an integer representation (1=Home, 2=School, 3=Other).
     * @param input Raw user input string.
     * @return Integer 1, 2, or 3 if recognized, or -1 if invalid.
     */
    public static int parseActivityType(String input) {
        if (input == null)
            return -1;

        String s = input.trim().toLowerCase();
        if (s.equals("1") || s.equals("home") || s.equals("h"))
            return 1;
        if (s.equals("2") || s.equals("school") || s.equals("s"))
            return 2;
        if (s.equals("3") || s.equals("other") || s.equals("o"))
            return 3;

        return -1;
    }



    /**
     * Returns the human-readable name of an activity category.
     * @param type Integer type code (1=Home, 2=School, 3=Other).
     * @return Name of the category, or "Unknown" if unrecognized.
     */
    public static String getActivityTypeName(int type) {
        return switch (type) {
            case 1 -> "Home";
            case 2 -> "School";
            case 3 -> "Other";
            default -> "Unknown";
        };
    }



    /**
     * Retrieves the username from file, prompting the user (and storing the new name to file) if no name is currently stored.
     * @param dirName Name of the data directory.
     * @param fileName Name of the username file.
     * @return The user's username.
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
     * Generates a time-appropriate greeting and activity summary for the user.
     * @param username The user's name.
     * @param numHomeActivities The current number of home activities.
     * @param numSchoolActivities The current number of school activities.
     * @param numOtherActivities The current number of other activities.
     * @return Formatted greeting string.
     */
    public static String getGreeting(String username, int numHomeActivities, int numSchoolActivities, int numOtherActivities) {
        int hour = LocalTime.now().getHour();
        String greeting;

        if (hour < 12)
            greeting = "Good morning";
        else if (hour < 17)
            greeting = "Good afternoon";
        else
            greeting = "Good evening";

        String text = "\t%s, %s!\n\tYou have %d home activities, %d school activities,\n\tand %d other activities currently entered.";
        return String.format(text, greeting, username, numHomeActivities, numSchoolActivities, numOtherActivities);
    }



    /**
     * Deletes the stored username file.
     * @param dirName Name of the data directory.
     * @param fileName Name of the username file.
     */
    public static void flushUsername(String dirName, String fileName) {
        try {
            File file = new File(dirName, fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done, although username isn't refreshed until program is relaunched");
        KEYBOARD.pressEnterToContinue();
    }



    /**
     * Checks if all keywords appear as individual words in the title (case-insensitive, any order).
     * @param title The activity title.
     * @param keywords The search keywords.
     * @return <code>true</code> if all keywords match words in the title, <code>false</code> otherwise.
     */
    public static boolean matchesKeywords(String title, String[] keywords) {
        if (keywords == null || keywords.length == 0)
            return true;
        if (title == null)
            return false;

        HashSet<String> titleWords = new HashSet<>(
            Arrays.asList(title.toLowerCase().split("[^a-zA-Z0-9]+")));

        for (String kw : keywords) {
            String[] tokens = kw.toLowerCase().split("[^a-zA-Z0-9]+");
            for (String token : tokens)
                if (!token.isEmpty() && !titleWords.contains(token))
                    return false;
        }

        return true;
    }



    /**
     * Determines whether an activity satisfies the specified search criteria.
     * @param act The activity to test.
     * @param keywords Array of title keywords that must all match words in the title, or <code>null</code> to match all.
     * @param searchStart Minimum start time, or <code>null</code> for open start.
     * @param searchEnd Maximum end time, or <code>null</code> for open end.
     * @return <code>true</code> if the activity satisfies all given criteria, <code>false</code> otherwise.
     */
    public static boolean matchesCriteria(Activity act, String[] keywords, Time searchStart, Time searchEnd) {
        if (act == null)
            return false;

        // Check keywords: every keyword must match a word in the activity title (case-insensitive)
        if (keywords != null && keywords.length > 0) {
            if (!DayPlannerUtils.matchesKeywords(act.getTitle(), keywords))
                return false;
        }

        // Check time period
        if (searchStart != null && act.getStartTime().compareTo(searchStart) < 0)
            return false;

        if (searchEnd != null && act.getEndTime().compareTo(searchEnd) > 0)
            return false;

        return true;
    }
    

    
    /**
     * Prompts the user for a time period filter and parses the start and end times.
     * @return A two-element array containing <code>{ searchStart, searchEnd }</code>.
     */
    public static Time[] readTimePeriod() {
        while (true) {
            System.out.println("Enter a time period (e.g. '2026/9/12, 6:00 - 2026/9/22, 11:59', '2026/9/12, 6:00 -', '- 2026/9/22, 11:59'), or press Enter for any time:");
            String line = KEYBOARD.readString();

            if (line.isEmpty())
                return new Time[] { null, null };

            // Find dash delimiter (hyphen, en-dash, em-dash)
            int dashIndex = -1;
            char[] dashes = { '-', '–', '—' };
            for (char d : dashes) {
                dashIndex = line.indexOf(d);
                if (dashIndex != -1)
                    break;
            }

            if (dashIndex != -1) {
                String part1 = line.substring(0, dashIndex).trim();
                String part2 = line.substring(dashIndex + 1).trim();

                Time start = part1.isEmpty() ? null : Input.parseTime(part1);
                Time end = part2.isEmpty() ? null : Input.parseTime(part2);

                if (!part1.isEmpty() && start == null) {
                    System.out.println("Invalid starting time. Use format YYYY/MM/DD HH:MM. Try again:");
                    continue;
                }
                if (!part2.isEmpty() && end == null) {
                    System.out.println("Invalid ending time. Use format YYYY/MM/DD HH:MM. Try again:");
                    continue;
                }
                if (start != null && end != null && start.compareTo(end) >= 0) {
                    System.out.println("Starting time must be before ending time. Try again:");
                    continue;
                }

                return new Time[] { start, end };
            }

            // Single time entered without dash
            Time single = Input.parseTime(line);
            if (single != null)
                return new Time[] { single, null };

            System.out.println("Invalid time period. Expected 'start - end', 'start -', '- end', or press Enter to skip. Try again:");
        }
    }



    /**
     * Default constructor for DayPlannerUtils.
     */
    public DayPlannerUtils() {}
}
