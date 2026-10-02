package dayplanner;


public class DayPlanner {
    private static final int ACTIVITY_ARRAY_SIZE = 256;
    private static final Input KEYBOARD = new Input(System.in);


    private static void addActivity() {

    }


    private static void searchForActivity() {
        
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
