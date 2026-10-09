package dayplanner;



import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



/**
 * Application execution point.
 */
public class DayPlanner {
    private static final Input KEYBOARD = new Input();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("ras");

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
     * Show the main menu one time and process user command.
     * Accepts commands: add, search, quit (and reasonable aliases/abbreviations).
     * @return Boolean indicating whether or not the user wants to continue with the command loop again.
     */
    private static boolean commandLoop() {
        System.out.println("\n\n          ===-=-==-=====-==-=-===\n\n");
        System.out.println("  『 DAY PLANNER 』  Main Menu\n");
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
     * Function 1 of the command loop: Add a new activity.
     */
    private static void addActivity() {
        System.out.println("Let's add a new activity to the DayPlanner.");

        // User input: activity type
        int type;
        ActivityList arr;

        while (true) {
            System.out.println("Enter the type of this activity.");
            System.out.println("For Home, type 'home' or 1. For School, type 'school' or 2. For Other, type 'other' or 3:");
            String input = KEYBOARD.readString().toLowerCase();

            if (input.equals("1") || input.equals("home") || input.equals("h")) {
                type = 1;
                arr = HOME_ACTIVITIES;
            } else if (input.equals("2") || input.equals("school") || input.equals("s")) {
                type = 2;
                arr = SCHOOL_ACTIVITIES;
            } else if (input.equals("3") || input.equals("other") || input.equals("o")) {
                type = 3;
                arr = OTHER_ACTIVITIES;
            } else {
                System.out.println("Invalid activity type: \"" + input + "\". Try again:");
                continue;
            }

            // Check first if there are rooms available in the corresponding array per Requirement (2)
            if (arr.isFull()) {
                System.out.println("You're pretty busy... the specified array is full, sorry!");
                return;
            }

            break;
        }

        // User input: title
        String title;
        while (true) {
            System.out.println("Enter a title.");
            title = KEYBOARD.readString();
            if (!title.isEmpty())
                break;
            System.out.println("Title cannot be empty. Try again:");
        }

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

            System.out.println("Ending time has to be after the starting time (" + startTime + "). Try again:");
        }

        // User input: comment
        System.out.println("Would you like to enter a comment?");
        String comment = null;
        if (KEYBOARD.readBool()) {
            System.out.println("Enter a comment.");
            comment = KEYBOARD.readString();
            if (comment.isEmpty())
                comment = null;
        }

        // User input: location
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

        // Instantiate the activity
        Activity act;
        switch (type) {
            case 1:
                act = new HomeActivity(title, startTime, endTime, comment);
                HOME_ACTIVITIES.append(act);
                break;
            case 2:
                act = new SchoolActivity(title, startTime, endTime, comment);
                SCHOOL_ACTIVITIES.append(act);
                break;
            case 3:
                act = new OtherActivity(title, startTime, endTime, comment, location);
                OTHER_ACTIVITIES.append(act);
                break;
            default:
                act = null;
        }

        if (act != null)
            System.out.println("Successfully created the new activity:\n\t" + act.toString());
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
            String input = KEYBOARD.readString().toLowerCase();

            if (input.isEmpty()) {
                type = 0; // all types
                break;
            } else if (input.equals("1") || input.equals("home") || input.equals("h")) {
                type = 1;
                break;
            } else if (input.equals("2") || input.equals("school") || input.equals("s")) {
                type = 2;
                break;
            } else if (input.equals("3") || input.equals("other") || input.equals("o")) {
                type = 3;
                break;
            } else {
                System.out.println("Invalid activity type: \"" + input + "\". Please enter 'home', 'school', 'other', or press Enter to skip:");
            }
        }

        // 2. Title keywords
        System.out.println("Enter title keywords (separated by spaces), or press Enter to match any title:");
        String keywordInput = KEYBOARD.readString();
        String[] keywords = null;
        if (!keywordInput.isEmpty()) {
            keywords = keywordInput.trim().split("\\s+");
        }

        // 3. Time period
        Time searchStart = null;
        Time searchEnd = null;

        while (true) {
            System.out.println("Enter a time period (e.g. '2026/9/12, 6:00 - 2026/9/22, 11:59', '2026/9/12, 6:00 -', '- 2026/9/22, 11:59'), or press Enter for any time:");
            String periodInput = KEYBOARD.readString();

            if (periodInput.isEmpty()) {
                searchStart = null;
                searchEnd = null;
                break;
            }

            // Check if delimiter exists (- or – or —)
            int dashIndex = -1;
            char[] dashes = { '-', '–', '—' };
            for (char d : dashes) {
                dashIndex = periodInput.indexOf(d);
                if (dashIndex != -1)
                    break;
            }

            if (dashIndex != -1) {
                String part1 = periodInput.substring(0, dashIndex).trim();
                String part2 = periodInput.substring(dashIndex + 1).trim();

                Time sStart = null;
                Time sEnd = null;
                boolean valid = true;

                if (!part1.isEmpty()) {
                    sStart = Input.parseTime(part1);
                    if (sStart == null) {
                        System.out.println("Invalid starting time in time period. Please use format YYYY/MM/DD HH:MM. Try again:");
                        valid = false;
                    }
                }

                if (valid && !part2.isEmpty()) {
                    sEnd = Input.parseTime(part2);
                    if (sEnd == null) {
                        System.out.println("Invalid ending time in time period. Please use format YYYY/MM/DD HH:MM. Try again:");
                        valid = false;
                    }
                }

                if (valid && sStart != null && sEnd != null) {
                    if (sStart.compareTo(sEnd) >= 0) {
                        System.out.println("Starting time must be before ending time. Try again:");
                        valid = false;
                    }
                }

                if (valid) {
                    searchStart = sStart;
                    searchEnd = sEnd;
                    break;
                }
            } else {
                // Single time entered without dash: treat as starting time with open end
                Time single = Input.parseTime(periodInput);
                if (single != null) {
                    searchStart = single;
                    searchEnd = null;
                    break;
                } else {
                    System.out.println("Invalid time period. Expected 'start - end', 'start -', '- end', or press Enter to skip. Try again:");
                }
            }
        }

        // Sequential search through appropriate array(s)
        ActivityList[] listsToSearch;
        String[] listNames;

        if (type == 1) {
            listsToSearch = new ActivityList[] { HOME_ACTIVITIES };
            listNames = new String[] { "Home" };
        } else if (type == 2) {
            listsToSearch = new ActivityList[] { SCHOOL_ACTIVITIES };
            listNames = new String[] { "School" };
        } else if (type == 3) {
            listsToSearch = new ActivityList[] { OTHER_ACTIVITIES };
            listNames = new String[] { "Other" };
        } else {
            listsToSearch = new ActivityList[] { HOME_ACTIVITIES, SCHOOL_ACTIVITIES, OTHER_ACTIVITIES };
            listNames = new String[] { "Home", "School", "Other" };
        }

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

        // Split title into word tokens (removing punctuation and whitespace)
        String[] rawWords = title.toLowerCase().split("[^a-zA-Z0-9]+");
        List<String> titleWords = new ArrayList<>();
        for (String w : rawWords) {
            if (!w.isEmpty())
                titleWords.add(w);
        }

        for (String kw : keywords) {
            String cleanKw = kw.toLowerCase().replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
            if (cleanKw.isEmpty())
                continue;

            boolean found = false;
            for (String tw : titleWords) {
                if (tw.equals(cleanKw)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }

        return true;
    }
}
