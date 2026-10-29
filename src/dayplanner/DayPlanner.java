package dayplanner;



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

    private static final String DATA_DIRECTORY = "dat";
    private static String dataFileName;
    private static ActivityList activities;

    private static final String USERNAME_FILENAME = "username.txt";
    private static final String USERNAME = DayPlannerUtils.getUsername(
        DATA_DIRECTORY,
        USERNAME_FILENAME,
        KEYBOARD);


    /**
     * Main entry point for the DayPlanner application.
     * 
     * @param args command-line arguments (currently unused).
     */
    public static void main(String[] args) {
        // Get the data file name from args
        if (args.length != 1) {
            System.out.println("\nThe program has halted because you did not specify a data file name.");
            System.out.println("A sample file named 'sampleData.ras' is included. Specify it as the first execution argument:");
            System.out.println("\tjava -jar bin/DayPlanner.jar sampleData.ras\n");
            return;
        }
        dataFileName = args[0];

        // Initialize activitylist
        try {
            activities = new ActivityList(DATA_DIRECTORY, dataFileName);
        } catch (Exception e) {
            System.out.println("\nError initializing activitylist from this data file ("+dataFileName+").\n");
            return;
        }

        // Perform the command loop until the user quits
        boolean doLoop = true;
        while (doLoop)
            doLoop = commandLoop();

        // The above loop only finishes when the user wants to quit
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
        // Print a big greeting message
        System.out.println("\n\n\t\t===-=-==-=====-==-=-===\n\n");
        System.out.println("\t『 DAY PLANNER 』  Main Menu\n");
        System.out.println(DayPlannerUtils.getGreeting(
            USERNAME,
            activities.size()));
        System.out.println("\nPlease enter a command:");
        System.out.println("\t1. add    - Insert a new activity");
        System.out.println("\t2. search - Search for an activity");
        System.out.println("\t3. quit   - Exit DayPlanner\n");

        String cmd;
        MenuOption option;
        while (true) {
            cmd = KEYBOARD.readCommand();
            option = MenuOption.parse(cmd);

            if (option == null) {
                System.out.println("Invalid command: \"" + cmd + "\".");
                System.out.println("Please enter 'add' (or 'a'), 'search' (or 's'), or 'quit' (or 'q'):");
                continue;
            } else {
                break;
            }
        }

        switch (option) {
            case ADD    -> addActivity();
            case SEARCH -> searchForActivity();
            case QUIT   -> { return false; }
            case FLUSH_USERNAME ->
                DayPlannerUtils.flushUsername(DATA_DIRECTORY, USERNAME_FILENAME);
        }

        return true;
    }



    /**
     * Saves the activity list to its RAS file.
     * 
     * @return {@code true} if the list could not be saved; {@code false}
     *         if the list was written successfully.
     */
    public static boolean saveToRAS() {
        return !activities.saveToRAS(DATA_DIRECTORY, dataFileName);
    }



    /**
     * Prompts the user for details and adds a new activity to the appropriate list.
     */
    public static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        // 1. Activity type
        ActivityType type;

        while (true) {
            System.out.println("Enter the type of this activity.");
            System.out.println("For Home, type 'home' or 1. For School, type 'school' or 2. For Other, type 'other' or 3:");
            type = ActivityType.parse(KEYBOARD.readString());

            if (type == null) {
                System.out.println("Invalid activity type. Try again:");
                continue;
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
            case HOME   -> new HomeActivity(title, startTime, endTime, comment);
            case SCHOOL -> new SchoolActivity(title, startTime, endTime, comment);
            case OTHER  -> new OtherActivity(title, startTime, endTime, comment, location);
            default     -> new Activity(title, startTime, endTime, comment);
        };

        if (act != null) {
            activities.add(act);
            System.out.println("Successfully created the new activity:\n\t" + act.toString());
        }
    }



    /**
     * Asks which activities to look for, then displays every activity that
     * matches the filters.
     */
    public static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");

        // an empty type means the search should include all three lists
        ActivityType type = null;
        while (true) {
            System.out.println("Enter activity type ('home', 'school', 'other'), or press Enter to search all types:");
            String input = KEYBOARD.readString();
            if (input.isEmpty())
                break;

            type = ActivityType.parse(input);
            if (type != null)
                break;

            System.out.println("Invalid activity type: \"" + input + "\". Please enter 'home', 'school', 'other', or press Enter to skip:");
        }

        // FILTER BY KEYWORD
        // if input is blank, do not restrict titles
        // otherwise, treat each word as its own filter
        System.out.println("Enter title keywords (separated by spaces), or press Enter to match any title:");
        String keywordInput = KEYBOARD.readString();
        String[] keywords = keywordInput.isEmpty()
            ? null
            : keywordInput.trim().split("\\s+");

        // FILTER BY TIME
        // null = unbound
        Time[] period = DayPlannerUtils.readTimePeriod(KEYBOARD);
        Time searchStart = period[0];
        Time searchEnd = period[1];

        int totalMatches = 0;
        System.out.println("\nSearch results:");

        for (int position : activities.findTitleMatches(keywords)) {
            Activity activity = activities.get(position);
            if (DayPlannerUtils.matchesCriteria(activity, type, searchStart, searchEnd)) {
                totalMatches++;
                System.out.println(String.format(
                    "\t%d. %s",
                    totalMatches,
                    activity.toString()
                ));
            }
        }

        // how many activities matched?
        if (totalMatches == 0)
            System.out.println("\tNo activities matched your search request.");
        else
            System.out.println("\nTotal matched activities: " + totalMatches);

        // press enter to continue
        KEYBOARD.pressEnterToContinue();
    }



    /**
     * Default constructor for DayPlanner, not used
     */
    private DayPlanner() {}
}
