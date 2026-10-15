package dayplanner;



import java.util.ResourceBundle;



/**
 * <p>
 * Main application class and execution entry point for DayPlanner.
 * </p>
 * <p>
 * Loads the configured activity files, runs the interactive menu, and saves
 * changes when the user quits.
 * </p>
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("ras");

    private static final String DATA_DIRECTORY = RESOURCES.getString("dirname");
    private static final String HOME_ACTIVITIES_FILENAME = RESOURCES.getString("filename_home");
    private static final String SCHOOL_ACTIVITIES_FILENAME = RESOURCES.getString("filename_school");
    private static final String OTHER_ACTIVITIES_FILENAME = RESOURCES.getString("filename_other");

    private static final ActivityList HOME_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        HOME_ACTIVITIES_FILENAME);
    private static final ActivityList SCHOOL_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        SCHOOL_ACTIVITIES_FILENAME);
    private static final ActivityList OTHER_ACTIVITIES = new ActivityList(
        DATA_DIRECTORY,
        OTHER_ACTIVITIES_FILENAME);

    private static final String USERNAME_FILENAME = RESOURCES.getString("filename_username");
    private static final String USERNAME = DayPlannerUtils.getUsername(DATA_DIRECTORY, USERNAME_FILENAME);


    /**
     * Main entry point for the DayPlanner application.
     * 
     * @param args command-line arguments (currently unused).
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
     * Displays the main menu and processes a single user command.
     * 
     * @return {@code true} to show the menu again, or {@code false} after
     *         processing quit.
     */
    public static boolean commandLoop() {
        System.out.println("\n\n\t\t===-=-==-=====-==-=-===\n\n");
        System.out.println("\t『 DAY PLANNER 』  Main Menu\n");
        System.out.println(DayPlannerUtils.getGreeting(
            USERNAME,
            HOME_ACTIVITIES.size(),
            SCHOOL_ACTIVITIES.size(),
            OTHER_ACTIVITIES.size()));
        System.out.println("\nPlease enter a command:");
        System.out.println("\t1. add    - Insert a new activity");
        System.out.println("\t2. search - Search for an activity");
        System.out.println("\t3. quit   - Exit DayPlanner\n");

        while (true) {
            String cmd = KEYBOARD.readCommand().toLowerCase();

            // Function 1
            if (cmd.equals("add") || cmd.equals("a") || cmd.equals("1") || cmd.equals("insert")) {
                addActivity();
                return true;
            }
            
            // Function 2
            else if (cmd.equals("search") || cmd.equals("s") || cmd.equals("2") || cmd.equals("find")) {
                searchForActivity();
                return true;
            }
            
            // Function 3
            else if (cmd.equals("quit") || cmd.equals("q") || cmd.equals("3")) {
                return false;
            }
            
            // Hidden function 1
            else if (cmd.equals("flush_username")) {
                DayPlannerUtils.flushUsername(DATA_DIRECTORY, USERNAME_FILENAME);
                return true;
            }
            
            // Invalid input
            else {
                System.out.println("Invalid command: \"" + cmd + "\".");
                System.out.println("Please enter 'add' (or 'a'), 'search' (or 's'), or 'quit' (or 'q'):");
            }
        }
    }



    /**
     * Returns the activity list corresponding to a category.
     * 
     * @param type Category to look up.
     * @return Corresponding <code>ActivityList</code>, or <code>null</code> if
     *         invalid.
     */
    private static ActivityList getActivityList(ActivityType type) {
        if (type == null)
            return null;
        return switch (type) {
            case HOME -> HOME_ACTIVITIES;
            case SCHOOL -> SCHOOL_ACTIVITIES;
            case OTHER -> OTHER_ACTIVITIES;
        };
    }



    /**
     * Saves all activity lists to their respective RAS files.
     * 
     * @return {@code true} if one or more lists could not be saved; {@code false}
     *         if every configured list was written successfully.
     */
    public static boolean saveToRAS() {
        int errors = 0;

        errors += HOME_ACTIVITIES.saveToRAS(DATA_DIRECTORY, HOME_ACTIVITIES_FILENAME) ? 0 : 1;
        errors += SCHOOL_ACTIVITIES.saveToRAS(DATA_DIRECTORY, SCHOOL_ACTIVITIES_FILENAME) ? 0 : 1;
        errors += OTHER_ACTIVITIES.saveToRAS(DATA_DIRECTORY, OTHER_ACTIVITIES_FILENAME) ? 0 : 1;

        return errors > 0;
    }



    /**
     * Prompts the user for details and adds a new activity to the appropriate list.
     */
    public static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        // 1. Activity type
        ActivityType type;
        ActivityList arr;

        while (true) {
            System.out.println("Enter the type of this activity.");
            System.out.println("For Home, type 'home' or 1. For School, type 'school' or 2. For Other, type 'other' or 3:");
            type = ActivityType.parse(KEYBOARD.readString());

            if (type == null) {
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
        if (type == ActivityType.OTHER) {
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
            case HOME -> new HomeActivity(title, startTime, endTime, comment);
            case SCHOOL -> new SchoolActivity(title, startTime, endTime, comment);
            case OTHER -> new OtherActivity(title, startTime, endTime, comment, location);
        };

        if (act != null) {
            arr.append(act);
            System.out.println("Successfully created the new activity:\n\t" + act.toString());
        }
    }



    /**
     * Prompts the user for search filters and displays matching activities across
     * lists.
     */
    public static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");

        // 1. Activity type
        ActivityType type = null;
        while (true) {
            System.out.println("Enter activity type ('home', 'school', 'other'), or press Enter to search all types:");
            String input = KEYBOARD.readString();

            if (input.isEmpty()) {
                break;
            }

            type = ActivityType.parse(input);
            if (type != null)
                break;

            System.out.println("Invalid activity type: \"" + input + "\". Please enter 'home', 'school', 'other', or press Enter to skip:");
        }

        // 2. Title keywords
        System.out.println("Enter title keywords (separated by spaces), or press Enter to match any title:");
        String keywordInput = KEYBOARD.readString();
        String[] keywords = keywordInput.isEmpty() ? null : keywordInput.trim().split("\\s+");

        // 3. Time period
        Time[] period = DayPlannerUtils.readTimePeriod();
        Time searchStart = period[0];
        Time searchEnd = period[1];

        // 4. Sequential search through target array(s)
        ActivityList[] listsToSearch = (type == null)
            ? new ActivityList[] { HOME_ACTIVITIES, SCHOOL_ACTIVITIES, OTHER_ACTIVITIES }
            : new ActivityList[] { getActivityList(type) };

        String[] listNames = (type == null)
            ? new String[] { "Home", "School", "Other" }
            : new String[] { type.getDisplayName() };

        int totalMatches = 0;
        System.out.println("\nSearch results:");

        for (int l = 0; l < listsToSearch.length; l++) {
            ActivityList currentList = listsToSearch[l];
            for (int i = 0; i < currentList.size(); i++) {
                Activity act = currentList.get(i);
                if (DayPlannerUtils.matchesCriteria(act, keywords, searchStart, searchEnd)) {
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
     * Default constructor for DayPlanner.
     */
    public DayPlanner() {}
}
