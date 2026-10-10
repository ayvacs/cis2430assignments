package dayplanner;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;



/**
 * Application execution point for DayPlanner.
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("ras");

    private static final String DATA_DIRECTORY = RESOURCES.getString("dirname");
    private static final String HOME_ACTIVITIES_NAME = RESOURCES.getString("filename_home");
    private static final String SCHOOL_ACTIVITIES_NAME = RESOURCES.getString("filename_school");
    private static final String OTHER_ACTIVITIES_NAME = RESOURCES.getString("filename_other");

    private static final ActivityList HOME_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        HOME_ACTIVITIES_NAME);
    private static final ActivityList SCHOOL_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        SCHOOL_ACTIVITIES_NAME);
    private static final ActivityList OTHER_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        OTHER_ACTIVITIES_NAME);

    private static final String USERNAME_FILE = RESOURCES.getString("filename_username");
    private static final String USERNAME = getUsername();



    /**
     * Application execution point.
     * @param args Execution arguments, if any.
     */
    public static void main(String[] args) {
        // Perform the command loop until the user quits
        boolean doLoop = true;
        while (doLoop)
            doLoop = commandLoop();

        // Attempt to save the list in RAS format
        boolean errors = saveToRAS();
        if (errors)    System.out.println("\nError saving one or more lists.");
        else           System.out.println("\nAll lists saved successfully.");

        System.out.println("\nNow quitting DayPlanner. Goodbye!");
    }



    /**
     * Show the main menu one time and process user command.
     * Accepts commands: add, search, quit (and reasonable aliases/abbreviations).
     * @return Boolean indicating whether or not the user wants to continue with the command loop again.
     */
    private static boolean commandLoop() {
        System.out.println("\n\n\t\t===-=-==-=====-==-=-===\n\n");
        System.out.println("\t『 DAY PLANNER 』  Main Menu");
        System.out.println(String.format("\tHello, %s!\n", USERNAME));
        System.out.println("Please enter a command:");
        System.out.println("\t1. add    - Insert a new activity");
        System.out.println("\t2. search - Search for an activity");
        System.out.println("\t3. quit   - Exit DayPlanner\n");

        while (true) {
            String cmd = KEYBOARD.readCommand().toLowerCase();

            if (cmd.equals("add") || cmd.equals("a") || cmd.equals("1") || cmd.equals("insert")) {
                addActivity();
                return true;
            } else if (cmd.equals("search") || cmd.equals("s") || cmd.equals("2") || cmd.equals("find")) {
                searchForActivity();
                return true;
            } else if (cmd.equals("quit") || cmd.equals("q") || cmd.equals("3")) {
                return false;
            } else {
                System.out.println("Invalid command: \"" + cmd + "\".");
                System.out.println("Please enter 'add' (or 'a'), 'search' (or 's'), or 'quit' (or 'q'):");
            }
        }
    }



    /**
     * Attempt to save the three lists to their corresponding text files.
     * @return Boolean indicating if there were any errors. (<code>true</code> = success, <code>false</code> = failure)
     */
    private static boolean saveToRAS() {
        int errors = 0;

        errors += HOME_ACTIVITIES.saveToRAS(DATA_DIRECTORY, HOME_ACTIVITIES_NAME) ? 0 : 1;
        errors += SCHOOL_ACTIVITIES.saveToRAS(DATA_DIRECTORY, SCHOOL_ACTIVITIES_NAME) ? 0 : 1;
        errors += OTHER_ACTIVITIES.saveToRAS(DATA_DIRECTORY, OTHER_ACTIVITIES_NAME) ? 0 : 1;

        return errors > 0;
    }



    /**
     * Resolves an activity type input string to an integer representation (1=Home, 2=School, 3=Other).
     * @param input Raw user input string.
     * @return Integer 1, 2, 3 if recognized, or -1 if invalid.
     */
    private static int parseActivityType(String input) {
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
     * Returns the ActivityList matching the specified type code.
     * @param type Integer type code (1=Home, 2=School, 3=Other).
     * @return Corresponding <code>ActivityList</code>, or <code>null</code> if invalid.
     */
    private static ActivityList getActivityList(int type) {
        return switch (type) {
            case 1 -> HOME_ACTIVITIES;
            case 2 -> SCHOOL_ACTIVITIES;
            case 3 -> OTHER_ACTIVITIES;
            default -> null;
        };
    }

    /**
     * Returns the human-readable name of the activity category.
     * @param type Integer type code (1=Home, 2=School, 3=Other).
     * @return String name of the category.
     */
    private static String getActivityTypeName(int type) {
        return switch (type) {
            case 1 -> "Home";
            case 2 -> "School";
            case 3 -> "Other";
            default -> "Unknown";
        };
    }



    /**
     * Function 1 of the command loop: Add a new activity.
     */
    private static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        // 1. Activity type
        int type;
        ActivityList arr;

        while (true) {
            System.out.println("Enter the type of this activity.");
            System.out.println("For Home, type 'home' or 1. For School, type 'school' or 2. For Other, type 'other' or 3:");
            type = parseActivityType(KEYBOARD.readString());

            if (type == -1) {
                System.out.println("Invalid activity type. Try again:");
                continue;
            }

            arr = getActivityList(type);

            // Check first if room is available per Requirement (2)
            if (arr.isFull()) {
                System.out.println("You're pretty busy... the specified array is full, sorry!");
                return;
            }

            break;
        }

        // 2. Title
        String title;
        while (true) {
            System.out.println("Enter a title.");
            title = KEYBOARD.readString();
            if (!title.isEmpty())
                break;
            System.out.println("Title cannot be empty. Try again:");
        }

        // 3. Start time
        System.out.println("Enter a starting time.");
        Time startTime = KEYBOARD.readTime();

        // 4. End time
        Time endTime;
        while (true) {
            System.out.println("Enter an ending time.");
            endTime = KEYBOARD.readTime();

            if (startTime.compareTo(endTime) < 0)
                break;

            System.out.println("Ending time has to be after the starting time (" + startTime + "). Try again:");
        }

        // 5. Optional comment
        System.out.println("Would you like to enter a comment?");
        String comment = null;
        if (KEYBOARD.readBool()) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
            if (comment.isEmpty())
                comment = null;
        }

        // 6. Location (Other activities only)
        String location = null;
        if (type == 3) {
            while (true) {
                System.out.println("Enter a location.");
                location = KEYBOARD.readString();
                if (!location.isEmpty())
                    break;
                System.out.println("Location cannot be empty for Other activities. Try again:");
            }
        }

        // 7. Instantiate and insert activity
        Activity act = switch (type) {
            case 1 -> new HomeActivity(title, startTime, endTime, comment);
            case 2 -> new SchoolActivity(title, startTime, endTime, comment);
            case 3 -> new OtherActivity(title, startTime, endTime, comment, location);
            default -> null;
        };

        if (act != null) {
            arr.append(act);
            System.out.println("Successfully created the new activity:\n\t" + act.toString());
        }
    }



    /**
     * Prompts the user for a time period filter and parses the start and end times.
     * @return A two-element array containing <code>{ searchStart, searchEnd }</code>.
     */
    private static Time[] readTimePeriod() {
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
     * Function 2 of the command loop: Search for activities.
     */
    private static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");

        // 1. Activity type
        int type = 0;
        while (true) {
            System.out.println("Enter activity type ('home', 'school', 'other'), or press Enter to search all types:");
            String input = KEYBOARD.readString();

            if (input.isEmpty()) {
                type = 0;
                break;
            }

            type = parseActivityType(input);
            if (type != -1)
                break;

            System.out.println("Invalid activity type: \"" + input + "\". Please enter 'home', 'school', 'other', or press Enter to skip:");
        }

        // 2. Title keywords
        System.out.println("Enter title keywords (separated by spaces), or press Enter to match any title:");
        String keywordInput = KEYBOARD.readString();
        String[] keywords = keywordInput.isEmpty() ? null : keywordInput.trim().split("\\s+");

        // 3. Time period
        Time[] period = readTimePeriod();
        Time searchStart = period[0];
        Time searchEnd = period[1];

        // 4. Sequential search through target array(s)
        ActivityList[] listsToSearch = (type == 0)
            ? new ActivityList[] { HOME_ACTIVITIES, SCHOOL_ACTIVITIES, OTHER_ACTIVITIES }
            : new ActivityList[] { getActivityList(type) };

        String[] listNames = (type == 0)
            ? new String[] { "Home", "School", "Other" }
            : new String[] { getActivityTypeName(type) };

        int totalMatches = 0;
        System.out.println("\nSearch results:");

        for (int l = 0; l < listsToSearch.length; l++) {
            ActivityList currentList = listsToSearch[l];
            for (int i = 0; i < currentList.size(); i++) {
                Activity act = currentList.get(i);
                if (matchesCriteria(act, keywords, searchStart, searchEnd)) {
                    totalMatches++;
                    System.out.println("\t" + totalMatches + ". [" + listNames[l] + "] " + act.toString());
                }
            }
        }

        if (totalMatches == 0)
            System.out.println("\tNo activities matched your search request.");
        else
            System.out.println("\nTotal matched activities: " + totalMatches);
    }



    /**
     * Determines whether an activity satisfies the search criteria.
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
            if (!matchesKeywords(act.getTitle(), keywords))
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
     * Get the user's name.
     * If not defined, prompt the user for a name.
     * This persists between sessions.
     */
    public static String getUsername() {
        File file = new File(DATA_DIRECTORY, USERNAME_FILE);

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {}

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
     * Default constructor (not used).
     * This exists to suppress a Javadoc warning.
     */
    public DayPlanner() {}
}
