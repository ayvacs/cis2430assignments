package dayplanner;



import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;



/**
 * Provides shared helpers for activity category parsing, greeting generation,
 * username storage, and search filtering. Methods are static so callers can use
 * them without creating a utility instance.
 */
public class DayPlannerUtils {
    /**
     * Retrieves the username from file, prompting the user (and storing the new
     * name to file) if no name is currently stored.
     * 
     * @param dirName  Name of the data directory.
     * @param fileName Name of the username file.
     * @param keyboard Input instance to use to enter new username if necessary.
     * @return The stored name, or a newly entered name when no usable file value
     *         exists.
     */
    public static String getUsername(String dirName, String fileName, Input keyboard) {
        String name = "exception"; // fall back name in case of error

        // Case 1: file doesn't exist
        if (!FileUtils.exists(dirName, fileName)) {
            System.out.println("\n\nHello new user, please enter your name:");
            name = keyboard.readString();

            try {
                FileUtils.writeText(dirName, fileName, name);
            } catch (Exception e) {
                System.out.println("Couldn't write name to file.");
            }

            return name;
        }

        // Case 2: File exists
        try {
            name = FileUtils.readFirstLine(dirName, fileName);
        } catch (Exception e) {
            System.out.println("Couldn't read name from file.");
        }

        return name;
    }



    /**
     * Generates a multi-line time-appropriate greeting and activity summary for the
     * user. Each line of the greeting is indented by 1 tab.
     * 
     * @param username            The user's name.
     * @param numHomeActivities   The current number of home activities.
     * @param numSchoolActivities The current number of school activities.
     * @param numOtherActivities  The current number of other activities.
     * @return Formatted greeting string indented by 1 tab.
     */
    public static String getGreeting(String username, int numHomeActivities, int numSchoolActivities, int numOtherActivities) {
        int hour = LocalTime.now().getHour();
        String greeting;

        if (hour < 12)      greeting = "Good morning";
        else if (hour < 17) greeting = "Good afternoon";
        else                greeting = "Good evening";

        String text = "\t%s, %s!\n\tYou have %d %s activities, %d %s activities,\n\tand %d %s activities currently entered.";
        return String.format(text, greeting, username,
            numHomeActivities, ActivityType.HOME.getDisplayName(),
            numSchoolActivities, ActivityType.SCHOOL.getDisplayName(),
            numOtherActivities, ActivityType.OTHER.getDisplayName());
    }



    /**
     * Deletes the stored username file if it exists. This is used by the hidden
     * maintenance command; it does not prompt for or create a replacement name.
     * 
     * @param dirName  Name of the data directory.
     * @param fileName Name of the username file.
     */
    public static void flushUsername(String dirName, String fileName) {
        try {
            FileUtils.delete(dirName, fileName);
            System.out.println("Done, although username isn't refreshed until program is relaunched");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * <p>
     * Checks if all keywords appear as individual words in the title
     * (case-insensitive, any order).
     * </p>
     * <p>
     * A keyword matches only if it appears as a complete word in the title; partial
     * word matches are rejected. For example, keyword <code>"Cook"</code> matches
     * title <code>"Cook Meal"</code> but does <em>not</em> match
     * <code>"Cooking"</code> because <code>"cooking"</code> is a different word.
     * Multiple-word keywords are split on non-alphanumeric characters and each
     * resulting token must appear as its own word in the title.
     * </p>
     * 
     * @param title    The activity title.
     * @param keywords Search terms; every non-empty term must match a whole title
     *                 word, with case ignored. An empty or null keyword array
     *                 imposes no filter.
     * @return <code>true</code> if all keywords match words in the title,
     *         <code>false</code> otherwise.
     */
    public static boolean matchesKeywords(String title, String[] keywords) {
        // no keywords means the caller did not ask to filter by title
        if (keywords == null || keywords.length == 0)
            return true;

        // if title doesnt exist, reject
        if (title == null)
            return false;

        // i used a hashset so that the order doesnt matter and so partial words dont match
        HashSet<String> titleWords = new HashSet<>(
            Arrays.asList(title.toLowerCase().split("[^a-zA-Z0-9]+")));

        // just check if the keywords match!
        for (String keyword : keywords)
            for (String token : keyword.toLowerCase().split("[^a-zA-Z0-9]+"))
                if (!token.isEmpty() && !titleWords.contains(token))
                    return false;

        return true;
    }



    /**
     * <p>
     * Determines whether an activity satisfies the specified search criteria.
     * </p>
     * <p>
     * All non-null criteria must be satisfied simultaneously:
     * </p>
     * <ul>
     * <li><b>keywords</b>: every keyword must be a whole-word match in the title
     * (see {@link #matchesKeywords}).</li>
     * <li><b>searchStart</b>: the activity's start time must be &gt;=
     * <code>searchStart</code>.</li>
     * <li><b>searchEnd</b>: the activity's end time must be &lt;=
     * <code>searchEnd</code>.</li>
     * </ul>
     * <p>
     * Passing <code>null</code> for any filter disables that filter entirely.
     * </p>
     * 
     * @param act         The activity to test.
     * @param keywords    Array of title keywords that must all match words in the
     *                    title, or <code>null</code> to match all.
     * @param searchStart Minimum start time, or <code>null</code> for open start.
     * @param searchEnd   Maximum end time, or <code>null</code> for open end.
     * @return <code>true</code> if the activity satisfies all given criteria,
     *         <code>false</code> otherwise.
     */
    public static boolean matchesCriteria(Activity act, String[] keywords, Time searchStart, Time searchEnd) {
        // if no activity was given, there is nothing to check
        if (act == null)
            return false;

        // check title
        if (keywords != null && keywords.length > 0)
            if (!matchesKeywords(act.getTitle(), keywords))
                return false;

        // check start time
        // a must start on or after the lower bound
        if (searchStart != null && act.getStartTime().compareTo(searchStart) < 0)
            return false;

        // check end time
        // a must end on or before the upper bound
        if (searchEnd != null && act.getEndTime().compareTo(searchEnd) > 0)
            return false;

        return true;
    }
    

    
    /**
     * Prompts the user for a time period filter and parses the start and end times.
     * A blank response means both bounds are open; a single hyphen may introduce
     * either one-sided range, for example {@code 2026/09/12 06:00 -}.
     * 
     * @param keyboard Input instance to use to prompt the user
     * @return A two-element array {@code {searchStart, searchEnd}}; either element
     *         may be null to represent an open bound.
     */
    public static Time[] readTimePeriod(Input keyboard) {
        while (true) {
            System.out.println("Enter a time period (e.g. '2026/9/12, 6:00 - 2026/9/22, 11:59', '2026/9/12, 6:00 -', '- 2026/9/22, 11:59'), or press Enter for any time:");
            String line = keyboard.readString();

            // if the user typed nothing, both sides of the range should be unrestricted
            if (line.isEmpty())
                return new Time[] { null, null };

            // find at what index the dash is typed
            // all common dash characters are accepted
            int dashIndex = -1;
            char[] dashes = { '-', '–', '—' };
            for (char d : dashes) {
                dashIndex = line.indexOf(d);
                if (dashIndex != -1)
                    break;
            }

            // if the user actually did type a dash...
            if (dashIndex != -1) {
                // convert the input into two time instances
                String part1 = line.substring(0, dashIndex).trim();
                String part2 = line.substring(dashIndex + 1).trim();
                Time start = part1.isEmpty() ? null : Input.parseTime(part1);
                Time end = part2.isEmpty() ? null : Input.parseTime(part2);

                // and only parse a boundary if the user typed one properly
                if (!part1.isEmpty() && start == null) {
                    System.out.println("Invalid starting time. Use format YYYY/MM/DD HH:MM. Try again:");
                    continue;
                }
                if (!part2.isEmpty() && end == null) {
                    System.out.println("Invalid ending time. Use format YYYY/MM/DD HH:MM. Try again:");
                    continue;
                }

                // ensure that start comes first
                if (start != null && end != null && start.compareTo(end) >= 0) {
                    System.out.println("Starting time must be before ending time. Try again:");
                    continue;
                }

                return new Time[] { start, end };
            }

            // if the user didn't input a dash...
            // then a single valid time is treated as the lower bound with no upper bound
            Time single = Input.parseTime(line);
            if (single != null)
                return new Time[] { single, null };

            // if we got here then nothing else worked
            System.out.println("Invalid time period. Expected 'start - end', 'start -', '- end', or press Enter to skip. Try again:");
        }
    }



    /**
     * Default constructor for DayPlannerUtils.
     */
    private DayPlannerUtils() {}
}
