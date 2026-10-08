package dayplanner;



import java.util.ResourceBundle;



/**
 * Application execution point.
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("dayplanner");

    private static final String DATA_DIRECTORY = RESOURCES.getString("directory_data");
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
     * Show the main menu one time.
     * @return Boolean indicating whether or not the user wants to continue with the command loop again.
     */
    private static boolean commandLoop() {
        System.out.println("\n\n          ===-=-==-=====-==-=-===\n\n");
        System.out.println("  『 DAY PLANNER 』  Main Menu\n");

        String[] operationList = {
            "Insert a new activity",
            "Search for an activity",
            "Quit"};
            
        int operation = KEYBOARD.fromOptions(operationList);
        switch (operation) {
            case 1:    addActivity();          break;
            case 2:    searchForActivity();    break;
            case 3:    return false; }

        return true;
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
     * Function 1 of the command loop.
     */
    private static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        // User input: activity type
        int type;
        ActivityList arr;

        while (true) {
            System.out.println("Enter the type of this activity.");
            System.out.println("For Home, type 1. For School, type 2. For Other, type 3.");
            type = KEYBOARD.readInt();

            if (type < 1 || type > 3) {
                System.out.println("Type has to be between 1 and 3. Try again:");
                continue;
            }

            // Determine proper array
            if (type == 1)
                arr = HOME_ACTIVITIES;
            else if (type == 2)
                arr = SCHOOL_ACTIVITIES;
            else
                arr = OTHER_ACTIVITIES;

            // If the array array is full, exit now
            if (arr.isFull()) {
                System.out.println("You're pretty busy... the specified array is full, sorry!");
                return;
            }

            break;
        }

        // User input: title
        System.out.println("Enter a title.");
        String title = KEYBOARD.readString();

        // User input: start time
        System.out.println("Enter a starting time.");
        Time startTime = KEYBOARD.readTime();

        // User input: end time
        Time endTime;
        while (true) {
            System.out.println("Enter an ending time.");
            endTime = KEYBOARD.readTime();

            if (startTime.compareTo(endTime) < 0)
                break;

            System.out.println("Ending time has to be after the starting time. Try again:");
        }

        // User input: comment
        System.out.println("Would you like to enter a comment?");
        String comment = null;
        if (KEYBOARD.readBool()) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
        }

        // User input: location
        String location = null;
        if (type == 3) {
            System.out.println("Enter a location.");
            location = KEYBOARD.readString();
        }


        // Instantiate the activity
        String string;
        switch (type) {
            case 1:
                HomeActivity hact = new HomeActivity(title, startTime, endTime, comment);
                HOME_ACTIVITIES.append(hact);
                string = hact.toString();
                break;
            case 2:
                SchoolActivity sact = new SchoolActivity(title, startTime, endTime, comment);
                SCHOOL_ACTIVITIES.append(sact);
                string = sact.toString();
                break;
            case 3:
                OtherActivity oact = new OtherActivity(title, startTime, endTime, comment, location);
                OTHER_ACTIVITIES.append(oact);
                string = oact.toString();
                break;
            default:
                string = "foo";
        }

        System.out.println("Successfully created the new activity:\n\t" + string);
    }



    /**
     * Function 2 of the command loop.
     */
    private static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");
    }
}
