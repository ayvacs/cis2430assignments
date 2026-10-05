package dayplanner;


public class DayPlanner {
    private static final int ACTIVITY_ARRAY_SIZE = 256;
    private static final Input KEYBOARD = new Input(System.in);
    private static final HomeActivity[] HOME_ACTIVITIES = new HomeActivity[ACTIVITY_ARRAY_SIZE];
    private static final SchoolActivity[] SCHOOL_ACTIVITIES = new SchoolActivity[ACTIVITY_ARRAY_SIZE];
    private static final OtherActivity[] OTHER_ACTIVITIES = new OtherActivity[ACTIVITY_ARRAY_SIZE];



    private static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        System.out.println("Enter the type of this activity.");
        System.out.println("For Home, type 1. For School, type 2. For Other, type 3.");
        int type = KEYBOARD.readInt();

        System.out.println("Enter a title.");
        String title = KEYBOARD.readString();

        System.out.println("Enter a starting time.");
        Time startTime = KEYBOARD.readTime();

        System.out.println("Enter an ending time.");
        Time endTime = KEYBOARD.readTime();

        System.out.println("Would you like to enter a comment?");
        boolean doComment = KEYBOARD.readBool();
        String comment = "-1";
        if (doComment) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
        }

        String location = "-1";
        if (type == 3) {
            System.out.println("Enter a location.");
            location = KEYBOARD.readString();
        }


        // Create the activity
        Activity activity;
        if (doComment) {
            if (type == 1) {
                activity = new HomeActivity(title, startTime, endTime);
            } else if (type == 2) {
                activity = new SchoolActivity(title, startTime, endTime);
            } else if (type == 3) {
                activity = new OtherActivity(title, startTime, endTime, location);
            }
        } else {
            if (type == 1) {
                activity = new HomeActivity(title, startTime, endTime, comment);
            } else if (type == 2) {
                activity = new SchoolActivity(title, startTime, endTime, comment);
            } else if (type == 3) {
                activity = new OtherActivity(title, startTime, endTime, location, comment);
            }
        }
    }


    private static void searchForActivity() {
        System.out.println("Let's search for activities in the DayPlanner.");
    }



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
