package dayplanner;



/**
 * Application execution point.
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input(System.in);
    private static final int ACTIVITY_ARRAY_SIZE = 256;
    private static final Activity[] HOME_ACTIVITIES = new HomeActivity[ACTIVITY_ARRAY_SIZE];
    private static final Activity[] SCHOOL_ACTIVITIES = new SchoolActivity[ACTIVITY_ARRAY_SIZE];
    private static final Activity[] OTHER_ACTIVITIES = new OtherActivity[ACTIVITY_ARRAY_SIZE];
    // For the arrays, the superclass is used instead of the appropriate subclass.
    // This is intentional as it simplifies code later on.



    private static int homeActivitiesLength = 0;
    private static int schoolActivitiesLength = 0;
    private static int otherActivitiesLength = 0;



    /**
     * Function 1 of the command loop.
     */
    private static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        System.out.println("Enter the type of this activity.");
        System.out.println("For Home, type 1. For School, type 2. For Other, type 3.");
        int type = KEYBOARD.readInt();

        // If the specified array is full, exit now
        if (type == 1 && homeActivitiesLength >= ACTIVITY_ARRAY_SIZE
        || type == 2 && schoolActivitiesLength >= ACTIVITY_ARRAY_SIZE
        || type == 3 && otherActivitiesLength >= ACTIVITY_ARRAY_SIZE) {
            System.out.println("You're pretty busy... the specified array is full, sorry!");
            return;
        }


        // Ask the user to input all fields

        System.out.println("Enter a title.");
        String title = KEYBOARD.readString();

        System.out.println("Enter a starting time.");
        Time startTime = KEYBOARD.readTime();

        System.out.println("Enter an ending time.");
        Time endTime = KEYBOARD.readTime();

        System.out.println("Would you like to enter a comment?");
        boolean doComment = KEYBOARD.readBool();
        String comment = "empty";
        if (doComment) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
        }

        String location = "empty";
        if (type == 3) {
            System.out.println("Enter a location.");
            location = KEYBOARD.readString();
        }


        // Instantiate the activity
        Activity activity;
        if (doComment) {
            if (type == 1) {
                activity = new HomeActivity(title, startTime, endTime, comment);
            } else if (type == 2) {
                activity = new SchoolActivity(title, startTime, endTime, comment);
            } else {
                activity = new OtherActivity(title, startTime, endTime, comment, location);
            }
        } else {
            if (type == 1) {
                activity = new HomeActivity(title, startTime, endTime);
            } else if (type == 2) {
                activity = new SchoolActivity(title, startTime, endTime);
            } else {
                activity = new OtherActivity(title, startTime, endTime, location);
            }
        }


        // Append the activity to the appropriate list
        if (type == 1) {
            HOME_ACTIVITIES[homeActivitiesLength] = activity;
            homeActivitiesLength++;
        }
        else if (type == 2) {
            SCHOOL_ACTIVITIES[schoolActivitiesLength] = activity;
            schoolActivitiesLength++;
        }
        else {
            OTHER_ACTIVITIES[otherActivitiesLength] = activity;
            otherActivitiesLength++;
        }


        System.out.println("Successfully created the new activity:");
        System.out.println(activity.toString());
    }



    /**
     * Function 2 of the command loop.
     */
    private static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");
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

        System.out.println("Now quitting DayPlanner. Goodbye!");
    }
}
