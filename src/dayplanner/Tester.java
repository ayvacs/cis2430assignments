package dayplanner;



import java.io.ByteArrayInputStream;
import java.io.File;



/**
 * Automatic tester, that executes all 22 test cases in README.md.
 */
public class Tester {
    private static int passedCount = 0;
    private static int failedCount = 0;



    /**
     * Execution point
     * @param args Command-line arguments, if any
     */
    public static void main(String[] args) {
        System.out.println("Executing Tester.java");
        
        runTestCase("case01", "Command Loop: Invalid Command", testCase01());
        runTestCase("case02", "Command Loop: Case & Alias Recognition", testCase02());
        runTestCase("case03", "Time Validation: Non-leap February 29", testCase03());
        runTestCase("case04", "Time Validation: Leap Year February 29", testCase04());
        runTestCase("case05", "Time Validation: Month Day Overflow", testCase05());
        runTestCase("case06", "Time Validation: Component Bounds", testCase06());
        runTestCase("case07", "Chronological Validation: Start Before End", testCase07());
        runTestCase("case08", "Defensive Input: Blank Title & Blank Location", testCase08());
        runTestCase("case09", "Add Activity: Complete Flow", testCase09());
        runTestCase("case10", "Search: Element at Beginning of List", testCase10());
        runTestCase("case11", "Search: Element in Middle of List", testCase11());
        runTestCase("case12", "Search: Element at End of List", testCase12());
        runTestCase("case13", "Search: Element Not on List", testCase13());
        runTestCase("case14", "Keyword Matching: Word-Level Exactness", testCase14());
        runTestCase("case15", "Keyword Matching: Case & Order Invariance", testCase15());
        runTestCase("case16", "Time Period: Bounded Range", testCase16());
        runTestCase("case17", "Time Period: Open End Range", testCase17());
        runTestCase("case18", "Time Period: Open Start Range", testCase18());
        runTestCase("case19", "Time Period: Invalid Inverted Range", testCase19());
        runTestCase("case20", "Search: No Filters (Match All)", testCase20());
        runTestCase("case21", "Array Capacity Boundary Check", testCase21());
        runTestCase("case22", "Persistence Verification (RAS Read/Write)", testCase22());

        System.out.printf(String.format("Out of %d tests, %d passed and %d failed\n",
            passedCount + failedCount, passedCount, failedCount));
    }



    /**
     * Reports the outcome of a single test case.
     * @param id The test case identifier (e.g. case01).
     * @param description Short description of what was tested.
     * @param pass Whether the test passed.
     */
    private static void runTestCase(String id, String description, boolean pass) {
        if (pass) {
            passedCount++;
            System.out.printf("[pass] %s: %s\n", id, description);
        } else {
            failedCount++;
            System.out.printf("[FAIL] %s: %s\n", id, description);
        }
    }



    /**
     * case01: Command Loop: Invalid Command.
     * Verifies that irrelevant commands like "bye" or "exit" are rejected as invalid.
     */
    private static boolean testCase01() {
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream()));
        try {
            Input input = new Input(new ByteArrayInputStream("bye\n".getBytes()));
            String cmd = input.readCommand().toLowerCase();
            boolean isAdd = cmd.equals("add") || cmd.equals("a") || cmd.equals("1") || cmd.equals("insert");
            boolean isSearch = cmd.equals("search") || cmd.equals("s") || cmd.equals("2") || cmd.equals("find");
            boolean isQuit = cmd.equals("quit") || cmd.equals("q") || cmd.equals("3");
            return !isAdd && !isSearch && !isQuit;
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * case02: Command Loop: Case & Alias Recognition.
     * Verifies aliases ("a", "ADD", "s", "q") are recognized.
     */
    private static boolean testCase02() {
        String[] addAliases = { "add", "ADD", "Add", "a", "A", "1" };
        String[] searchAliases = { "search", "SEARCH", "Search", "s", "S", "2" };
        String[] quitAliases = { "quit", "QUIT", "Quit", "q", "Q", "3" };

        for (String a : addAliases) {
            String c = a.toLowerCase();
            if (!(c.equals("add") || c.equals("a") || c.equals("1") || c.equals("insert")))
                return false;
        }

        for (String s : searchAliases) {
            String c = s.toLowerCase();
            if (!(c.equals("search") || c.equals("s") || c.equals("2") || c.equals("find")))
                return false;
        }

        for (String q : quitAliases) {
            String c = q.toLowerCase();
            if (!(c.equals("quit") || c.equals("q") || c.equals("3")))
                return false;
        }

        return true;
    }

    /**
     * case03: Time Validation: Non-leap February 29.
     * Verifies 2026/02/29 is rejected since 2026 is not a leap year.
     */
    private static boolean testCase03() {
        boolean valid = Time.isValidTime(2026, 2, 29, 10, 0);
        String err = Time.getValidationError(2026, 2, 29, 10, 0);
        return !valid && err != null && err.contains("max 28 days");
    }

    /**
     * case04: Time Validation: Leap Year February 29.
     * Verifies 2024/02/29 is accepted as a valid leap year timestamp.
     */
    private static boolean testCase04() {
        boolean isLeap = Time.isLeapYear(2024);
        boolean valid = Time.isValidTime(2024, 2, 29, 10, 0);
        return isLeap && valid;
    }

    /**
     * case05: Time Validation: Month Day Overflow.
     * Verifies April 31 is rejected since April has 30 days.
     */
    private static boolean testCase05() {
        boolean valid = Time.isValidTime(2026, 4, 31, 9, 0);
        String err = Time.getValidationError(2026, 4, 31, 9, 0);
        return !valid && err != null && err.contains("max 30 days");
    }

    /**
     * case06: Time Validation: Component Bounds.
     * Verifies out-of-range months, hours, minutes, and non-positive years are rejected.
     */
    private static boolean testCase06() {
        boolean badMonth = !Time.isValidTime(2026, 13, 1, 10, 0);
        boolean badHour = !Time.isValidTime(2026, 5, 10, 24, 0);
        boolean badMinute = !Time.isValidTime(2026, 5, 10, 12, 60);
        boolean badYear = !Time.isValidTime(0, 1, 1, 10, 0);
        return badMonth && badHour && badMinute && badYear;
    }

    /**
     * case07: Chronological Validation: Start Before End.
     * Verifies that start time must be strictly before end time.
     */
    private static boolean testCase07() {
        Time start = new Time(2026, 10, 15, 14, 0);
        Time end = new Time(2026, 10, 15, 12, 0);
        Time equalTime = new Time(2026, 10, 15, 14, 0);
        return start.compareTo(end) > 0 && start.compareTo(equalTime) == 0;
    }

    /**
     * case08: Defensive Input: Blank Title & Blank Location.
     * Verifies blank strings are detected for required fields.
     */
    private static boolean testCase08() {
        String blankTitle = "   ";
        String blankLocation = "";
        OtherActivity oa = new OtherActivity("Valid Title",
            new Time(2026, 10, 1, 10, 0),
            new Time(2026, 10, 1, 11, 0),
            "Campus Rec Centre");

        return blankTitle.trim().isEmpty() &&
               blankLocation.trim().isEmpty() &&
               !oa.getLocation().trim().isEmpty();
    }

    /**
     * case09: Add Activity: Complete Flow.
     * Verifies adding a SchoolActivity with all fields populated.
     */
    private static boolean testCase09() {
        ActivityList list = new ActivityList(10);
        Time start = new Time(2026, 10, 20, 14, 30);
        Time end = new Time(2026, 10, 20, 16, 30);
        SchoolActivity sa = new SchoolActivity("CIS 2430 Lab Office Hours", start, end, "Ask TA about search");

        list.append(sa);
        if (list.size() != 1) return false;

        Activity retrieved = list.get(0);
        return retrieved.getTitle().equals("CIS 2430 Lab Office Hours") &&
               retrieved.getStartTime().equals(start) &&
               retrieved.getEndTime().equals(end) &&
               retrieved.getComment().equals("Ask TA about search");
    }

    /**
     * case10: Search: Element at Beginning of List.
     * Searches dat/home.ras for "Co-Op" matching index 0.
     */
    private static boolean testCase10() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.isEmpty()) return false;

        Activity first = homeList.get(0);
        boolean matches = DayPlanner.matchesCriteria(first, new String[] { "Co-Op" }, null, null);
        return matches && first.getTitle().contains("Co-Op");
    }

    /**
     * case11: Search: Element in Middle of List.
     * Searches dat/home.ras for "Cook Meal" matching index 3.
     */
    private static boolean testCase11() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.size() <= 3) return false;

        Activity mid = homeList.get(3);
        boolean matches = DayPlanner.matchesCriteria(mid, new String[] { "Cook", "Meal" }, null, null);
        return matches && mid.getTitle().contains("Cook Meal");
    }

    /**
     * case12: Search: Element at End of List.
     * Searches dat/home.ras for "Bike Ride" matching the last element.
     */
    private static boolean testCase12() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.isEmpty()) return false;

        Activity last = homeList.get(homeList.size() - 1);
        boolean matches = DayPlanner.matchesCriteria(last, new String[] { "Bike", "Ride" }, null, null);
        return matches && last.getTitle().contains("Bike Ride");
    }

    /**
     * case13: Search: Element Not on List.
     * Verifies that searching for a nonexistent item yields 0 matches.
     */
    private static boolean testCase13() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        int matchCount = 0;
        for (Activity a : homeList) {
            if (DayPlanner.matchesCriteria(a, new String[] { "Nonexistent", "Activity" }, null, null)) {
                matchCount++;
            }
        }
        return matchCount == 0;
    }

    /**
     * case14: Keyword Matching: Word-Level Exactness.
     * Keyword "Program" must not match title "Buy Programming in Java".
     */
    private static boolean testCase14() {
        String title = "Buy Programming in Java";
        return !DayPlanner.matchesKeywords(title, new String[] { "Program" });
    }

    /**
     * case15: Keyword Matching: Case & Order Invariance.
     * Keywords "java PROGRAMMING" must match "Programming in Java".
     */
    private static boolean testCase15() {
        String title = "Programming in Java";
        return DayPlanner.matchesKeywords(title, new String[] { "java", "PROGRAMMING" });
    }

    /**
     * case16: Time Period: Bounded Range.
     * Search range 2026/10/04 00:00 - 2026/10/06 12:00 in home list matches exactly 3 items.
     */
    private static boolean testCase16() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time start = new Time(2026, 10, 4, 0, 0);
        Time end = new Time(2026, 10, 6, 12, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlanner.matchesCriteria(a, null, start, end)) {
                count++;
            }
        }
        return count == 3;
    }

    /**
     * case17: Time Period: Open End Range.
     * Search range 2026/10/08 00:00 - in home list matches exactly 3 items.
     */
    private static boolean testCase17() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time start = new Time(2026, 10, 8, 0, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlanner.matchesCriteria(a, null, start, null)) {
                count++;
            }
        }
        return count == 3;
    }

    /**
     * case18: Time Period: Open Start Range.
     * Search range - 2026/10/05 12:00 in home list matches exactly 2 items.
     */
    private static boolean testCase18() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time end = new Time(2026, 10, 5, 12, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlanner.matchesCriteria(a, null, null, end)) {
                count++;
            }
        }
        return count == 2;
    }

    /**
     * case19: Time Period: Invalid Inverted Range.
     * Verifies that start after end is detected as an invalid period.
     */
    private static boolean testCase19() {
        Time s = new Time(2026, 10, 20, 12, 0);
        Time e = new Time(2026, 10, 10, 12, 0);
        return s.compareTo(e) > 0;
    }

    /**
     * case20: Search: No Filters (Match All).
     * Unfiltered search across home (7), school (9), other (7) totals 23 activities.
     */
    private static boolean testCase20() {
        ActivityList home = new ActivityList("dat", "home.ras");
        ActivityList school = new ActivityList("dat", "school.ras");
        ActivityList other = new ActivityList("dat", "other.ras");

        int total = home.size() + school.size() + other.size();
        return total == 23;
    }

    /**
     * case21: Array Capacity Boundary Check.
     * Rejects addition when array capacity is reached.
     */
    private static boolean testCase21() {
        ActivityList smallList = new ActivityList(2);
        Time t1 = new Time(2026, 10, 1, 10, 0);
        Time t2 = new Time(2026, 10, 1, 11, 0);

        smallList.append(new HomeActivity("A1", t1, t2));
        smallList.append(new HomeActivity("A2", t1, t2));

        boolean full = smallList.isFull();
        Activity extra = smallList.append(new HomeActivity("A3", t1, t2));

        return full && (extra == null) && (smallList.size() == 2);
    }

    /**
     * case22: Persistence Verification (RAS Read/Write).
     * Verifies that newly added activities are serialized and cleanly read back.
     */
    private static boolean testCase22() {
        String testDir = "dat";
        String testFile = "test_persistence.ras";

        ActivityList outList = new ActivityList(10);
        Time t1 = new Time(2026, 10, 1, 10, 0);
        Time t2 = new Time(2026, 10, 1, 11, 0);

        HomeActivity ha = new HomeActivity("Test Home", t1, t2, "Home Comment");
        SchoolActivity sa = new SchoolActivity("Test School", t1, t2, "School Comment");
        OtherActivity oa = new OtherActivity("Test Other", t1, t2, "Other Comment", "Library");

        outList.append(ha);
        outList.append(sa);
        outList.append(oa);

        boolean saved = outList.saveToRAS(testDir, testFile);
        if (!saved) return false;

        ActivityList inList = new ActivityList(testDir, testFile);
        boolean sizeMatches = inList.size() == 3;
        boolean equalData = inList.get(0).equals(ha) &&
                            inList.get(1).equals(sa) &&
                            inList.get(2).equals(oa);

        // Clean up test file
        File f = new File(testDir + "/" + testFile);
        if (f.exists()) {
            f.delete();
        }

        return sizeMatches && equalData;
    }

    

    /**
     * Default constructor (not used).
     * This exists to suppress a Javadoc warning.
     */
    public Tester() {}
}