package dayplanner;



/**
 * Application execution point.
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input(System.in);

    private static final String DATA_DIRECTORY = "dat";
    private static final String HOME_ACTIVITIES_NAME = "home.ras";
    private static final String SCHOOL_ACTIVITIES_NAME = "school.ras";
    private static final String OTHER_ACTIVITIES_NAME = "other.ras";

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
        switch (type) {
            // cant use arr here because it is a wildcard.
            case 1:
                HOME_ACTIVITIES.append(
                    new HomeActivity(title, startTime, endTime, comment)
                );
                break;
            case 2:
                SCHOOL_ACTIVITIES.append(
                    new SchoolActivity(title, startTime, endTime, comment)
                );
                break;
            case 3:
                OTHER_ACTIVITIES.append(
                    new OtherActivity(title, startTime, endTime, comment, location)
                );
                break;
        }

        System.out.println("Successfully created the new activity:");
    }



    /**
     * Function 2 of the command loop.
     */
    private static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");
    }



    /**
     * Attempt to save the three lists to their corresponding text files.
     * @return Boolean indicating if there were any errors.
     */
    private static boolean saveToRAS() {
        int errors = 0;

        errors += HOME_ACTIVITIES.saveToRAS(DATA_DIRECTORY, HOME_ACTIVITIES_NAME) ? 0 : 1;
        errors += SCHOOL_ACTIVITIES.saveToRAS(DATA_DIRECTORY, SCHOOL_ACTIVITIES_NAME) ? 0 : 1;
        errors += OTHER_ACTIVITIES.saveToRAS(DATA_DIRECTORY, OTHER_ACTIVITIES_NAME) ? 0 : 1;

        return errors == 0;
    }



    /**
     * Application execution point.
     * @param args Command-line arguments, if any.
     */
    public static void main(String[] args) {
        boolean doLoop = true;
        int option;

        while (doLoop) {
            System.out.println("\nDayPlanner Main Menu; select an option:");
            System.out.println("1) Insert a new activity");
            System.out.println("2) Search for an activity");
            System.out.println("3) Quit");
            option = KEYBOARD.readInt();

            switch (option) {
                case 1:
                    addActivity();
                    break;
                case 2:
                    searchForActivity();
                    break;
                case 3:
                    doLoop = false;
                    break;
                default:
                    System.out.println("Invalid option; type an integer between 1 and 3 inclusive.");
                    break;
            }
        }

        if (saveToRAS())
            System.out.println("\nAll lists saved successfully.");
        else
            System.out.println("\nError saving one or more lists.");

        System.out.println("\nNow quitting DayPlanner. Goodbye!");
    }
}
