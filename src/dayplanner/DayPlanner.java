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
    /**
     * Input instance used for reading console user input.
     */
    private static final Input KEYBOARD = new Input();

    /**
     * Directory path where data files are stored.
     */
    private static final String DATA_DIRECTORY = "dat";

    /**
     * File name of the RAS data file loaded and persisted.
     */
    private static String dataFileName;

    /**
     * The master list of activities currently loaded into the planner.
     */
    private static ActivityList activities;


    /**
     * Main entry point for the DayPlanner application.
     * 
     * @param args Command-line arguments; expects exactly one argument specifying the data file name.
     */
    public static void main(String[] args) {
        // Get the data file name from args
        if (args.length != 1) {
            StringBuilder error = new StringBuilder();
            error.append("The program has halted because you did not specify a data file name.");
            error.append("\nA sample file named 'sampleData.ras' is included. Specify it as the first execution argument:");
            error.append("\n\tjava -jar bin/DayPlanner.jar sampleData.ras");

            System.out.println("\n"+error.toString()+"\n");
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

        // Now the the ActivityList is initialized we can Show the GUI
        System.out.println("Showing GUI. Ctrl+C on this terminal window to quit immediately without saving.");
        new GUIDayPlanner(activities);

        /*
        // This is Legacy Code that executes the old, terminal-based command loop.
        // Perform the command loop until the user quits
        boolean doLoop = true;
        while (doLoop)
            doLoop = commandLoop();

        // The above loop only finishes when the user wants to quit
        quit(true);
        */
    }


    /**
     * Saves the list and exits the program.
     * 
     * @param print Whether to print output.
     * @return True if there was a RAS save failure, false otherwise.
     */
    public static boolean quit(boolean print) {
        // Attempt to save the list in RAS format
        boolean errors = saveToRAS();

        if (print) {
            if (errors)    System.out.println("\nError saving the list.");
            else           System.out.println("\nThe list was saved successfully.");
            System.out.println("\nNow quitting DayPlanner. Goodbye!");
        }

        return errors;
    }



    /**
     * Get a large greeting message.
     * 
     * @return Greeting message.
     */
    public static String getGreetingMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("\n\n\t\t===-=-==-=====-==-=-===\n\n");
        msg.append("\t『 DAY PLANNER 』  Main Menu\n");
        msg.append(DayPlannerUtils.getGreeting(activities.size()));
        msg.append("\nPlease enter a command:");
        msg.append("\t1. add    - Insert a new activity");
        msg.append("\t2. search - Search for an activity");
        msg.append("\t3. quit   - Exit DayPlanner\n");
        return msg.toString();
    }



    /**
     * Displays the main menu and processes a single user command.
     * 
     * @return {@code true} to show the menu again, or {@code false} after
     *         processing quit.
     */
    public static boolean commandLoop() {
        // Print a big greeting message
        System.out.println(getGreetingMessage());

        String cmd;
        MenuOption option;
        while (true) {
            cmd = KEYBOARD.readCommand();
            try {
                option = MenuOption.parse(cmd);
                if (option == null)
                    throw new IllegalArgumentException("Invalid command: \"" + cmd + "\".");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter 'add' (or 'a'), 'search' (or 's'), or 'quit' (or 'q'):");
            }
        }

        switch (option) {
            case ADD    -> addActivity();
            case SEARCH -> searchForActivity();
            case QUIT   -> { return false; }
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
            try {
                type = ActivityType.parse(KEYBOARD.readString());
                if (type == null)
                    throw new IllegalArgumentException("Invalid activity type.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try again:");
            }
        }

        // 2. Title
        String title;
        while (true) {
            System.out.println("Enter a title.");
            try {
                title = KEYBOARD.readString();
                if (title == null || title.isBlank())
                    throw new IllegalArgumentException("Title cannot be empty.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try again:");
            }
        }

        // 3. Start time
        System.out.println("Enter a starting time.");
        Time startTime = KEYBOARD.readTime();

        // 4. End time
        Time endTime;
        while (true) {
            System.out.println("Enter an ending time.");
            try {
                endTime = KEYBOARD.readTime();
                if (startTime.compareTo(endTime) >= 0)
                    throw new IllegalArgumentException("Ending time has to be after the starting time (" + startTime + ").");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try again:");
            }
        }

        // 5. Optional comment
        System.out.println("Would you like to enter a comment?");
        String comment = null;
        if (KEYBOARD.readBool()) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
            if (comment != null && comment.isEmpty())
                comment = null;
        }

        // 6. Location (Other activities only)
        String location = null;
        if (type == ActivityType.OTHER) {
            while (true) {
                System.out.println("Enter a location.");
                try {
                    location = KEYBOARD.readString();
                    if (location == null || location.isBlank())
                        throw new IllegalArgumentException("Location cannot be empty for Other activities.");
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + " Try again:");
                }
            }
        }

        // 7. Instantiate and insert activity
        try {
            Activity act = switch (type) {
                case HOME   -> new HomeActivity(title, startTime, endTime, comment);
                case SCHOOL -> new SchoolActivity(title, startTime, endTime, comment);
                case OTHER  -> new OtherActivity(title, startTime, endTime, comment, location);
            };

            if (act != null) {
                activities.add(act);
                System.out.println("Successfully created the new activity:\n\t" + act.toString());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating activity: " + e.getMessage());
        }
    }



    /**
     * Asks which activities to look for, then displays every activity that
     * matches the filters.
     */
    public static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");

        // an empty type means the search should include every activity
        ActivityType type = null;
        while (true) {
            System.out.println("Enter activity type ('home', 'school', 'other'), or press Enter to search all types:");
            String input = KEYBOARD.readString();
            if (input == null || input.isEmpty())
                break;

            try {
                type = ActivityType.parse(input);
                if (type == null)
                    throw new IllegalArgumentException("Invalid activity type: \"" + input + "\".");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Please enter 'home', 'school', 'other', or press Enter to skip:");
            }
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
