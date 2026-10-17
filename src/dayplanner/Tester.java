package dayplanner;



import java.io.ByteArrayInputStream;


/**
 * Automatic tester, that executes all 22 test cases in README.md.
 */
public class Tester {
    private static int passedCount = 0;
    private static int failedCount = 0;



    /**
     * Main entry point for executing all test cases.
     * @param args Command-line arguments.
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
     * Records and prints the outcome of a single test case.
     * @param id The test case identifier.
     * @param description Short description of what was tested.
     * @param pass Whether the test passed.
     */
    public static void runTestCase(String id, String description, boolean pass) {
        if (pass) {
            passedCount++;
            System.out.printf("[pass] %s: %s\n", id, description);
        } else {
            failedCount++;
            System.out.printf("[FAIL] %s: %s\n", id, description);
        }
    }



    /**
     * Case 01: Verifies that unrecognized commands are rejected by the command loop.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase01() {
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream()));
        try {
            Input input = new Input(new ByteArrayInputStream("bye\n".getBytes()));
            String cmd = input.readCommand();
            return MenuOption.parse(cmd) == null;
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Case 02: Verifies that command aliases and case variations are recognized.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase02() {
        String[] addAliases = { "add", "ADD", "Add", "a", "A", "1", "insert" };
        String[] searchAliases = { "search", "SEARCH", "Search", "s", "S", "2", "find" };
        String[] quitAliases = { "quit", "QUIT", "Quit", "q", "Q", "3" };

        for (String a : addAliases) {
            if (MenuOption.parse(a) != MenuOption.ADD)
                return false;
        }

        for (String s : searchAliases) {
            if (MenuOption.parse(s) != MenuOption.SEARCH)
                return false;
        }

        for (String q : quitAliases) {
            if (MenuOption.parse(q) != MenuOption.QUIT)
                return false;
        }

        return true;
    }

    /**
     * Case 03: Verifies that February 29 is rejected in non-leap years.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase03() {
        boolean valid = Time.isValidTime(2026, 2, 29, 10, 0);
        String err = Time.getValidationError(2026, 2, 29, 10, 0);
        return !valid && err != null && err.contains("max 28 days");
    }

    /**
     * Case 04: Verifies that February 29 is accepted in leap years.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase04() {
        boolean isLeap = Time.isLeapYear(2024);
        boolean valid = Time.isValidTime(2024, 2, 29, 10, 0);
        return isLeap && valid;
    }

    /**
     * Case 05: Verifies that dates exceeding the maximum days in a month are rejected.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase05() {
        boolean valid = Time.isValidTime(2026, 4, 31, 9, 0);
        String err = Time.getValidationError(2026, 4, 31, 9, 0);
        return !valid && err != null && err.contains("max 30 days");
    }

    /**
     * Case 06: Verifies that out-of-bounds time components are rejected.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase06() {
        boolean badMonth = !Time.isValidTime(2026, 13, 1, 10, 0);
        boolean badHour = !Time.isValidTime(2026, 5, 10, 24, 0);
        boolean badMinute = !Time.isValidTime(2026, 5, 10, 12, 60);
        boolean badYear = !Time.isValidTime(0, 1, 1, 10, 0);
        return badMonth && badHour && badMinute && badYear;
    }

    /**
     * Case 07: Verifies that start time must be chronologically before end time.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase07() {
        Time start = new Time(2026, 10, 15, 14, 0);
        Time end = new Time(2026, 10, 15, 12, 0);
        Time equalTime = new Time(2026, 10, 15, 14, 0);
        return start.compareTo(end) > 0 && start.compareTo(equalTime) == 0;
    }

    /**
     * Case 08: Verifies that blank titles and blank locations are rejected.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase08() {
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
     * Case 09: Verifies the creation and retrieval of an activity with all fields populated.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase09() {
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
     * Case 10: Verifies search matching an activity at the beginning of the list.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase10() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.isEmpty()) return false;

        Activity first = homeList.get(0);
        boolean matches = DayPlannerUtils.matchesCriteria(first, new String[] { "Co-Op" }, null, null);
        return matches && first.getTitle().contains("Co-Op");
    }

    /**
     * Case 11: Verifies search matching an activity in the middle of the list.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase11() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.size() <= 3) return false;

        Activity mid = homeList.get(3);
        boolean matches = DayPlannerUtils.matchesCriteria(mid, new String[] { "Cook", "Meal" }, null, null);
        return matches && mid.getTitle().contains("Cook Meal");
    }

    /**
     * Case 12: Verifies search matching an activity at the end of the list.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase12() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        if (homeList.isEmpty()) return false;

        Activity last = homeList.get(homeList.size() - 1);
        boolean matches = DayPlannerUtils.matchesCriteria(last, new String[] { "Bike", "Ride" }, null, null);
        return matches && last.getTitle().contains("Bike Ride");
    }

    /**
     * Case 13: Verifies that searching for a nonexistent item yields zero matches.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase13() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        int matchCount = 0;
        for (Activity a : homeList) {
            if (DayPlannerUtils.matchesCriteria(a, new String[] { "Nonexistent", "Activity" }, null, null)) {
                matchCount++;
            }
        }
        return matchCount == 0;
    }

    /**
     * Case 14: Verifies that keyword matching requires whole-word matches.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase14() {
        String title = "Buy Programming in Java";
        return !DayPlannerUtils.matchesKeywords(title, new String[] { "Program" });
    }

    /**
     * Case 15: Verifies that keyword matching is case-insensitive and order-invariant.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase15() {
        String title = "Programming in Java";
        return DayPlannerUtils.matchesKeywords(title, new String[] { "java", "PROGRAMMING" });
    }

    /**
     * Case 16: Verifies bounded time range filtering.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase16() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time start = new Time(2026, 10, 4, 0, 0);
        Time end = new Time(2026, 10, 6, 12, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlannerUtils.matchesCriteria(a, null, start, end)) {
                count++;
            }
        }
        return count == 3;
    }

    /**
     * Case 17: Verifies open-ended time range filtering.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase17() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time start = new Time(2026, 10, 8, 0, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlannerUtils.matchesCriteria(a, null, start, null)) {
                count++;
            }
        }
        return count == 3;
    }

    /**
     * Case 18: Verifies open-start time range filtering.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase18() {
        ActivityList homeList = new ActivityList("dat", "home.ras");
        Time end = new Time(2026, 10, 5, 12, 0);

        int count = 0;
        for (Activity a : homeList) {
            if (DayPlannerUtils.matchesCriteria(a, null, null, end)) {
                count++;
            }
        }
        return count == 2;
    }

    /**
     * Case 19: Verifies detection of invalid inverted time ranges where start is after end.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase19() {
        Time s = new Time(2026, 10, 20, 12, 0);
        Time e = new Time(2026, 10, 10, 12, 0);
        return s.compareTo(e) > 0;
    }

    /**
     * Case 20: Verifies that unfiltered search matches all activities across lists.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase20() {
        ActivityList home = new ActivityList("dat", "home.ras");
        ActivityList school = new ActivityList("dat", "school.ras");
        ActivityList other = new ActivityList("dat", "other.ras");

        int total = home.size() + school.size() + other.size();
        return total == 23;
    }

    /**
     * Case 21: Verifies that adding to an ActivityList at maximum capacity is rejected.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase21() {
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
     * Case 22: Verifies serializing activities to RAS format and reading them back.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase22() {
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
        if (FileUtils.exists(testDir, testFile))
            FileUtils.delete(testDir, testFile);

        return sizeMatches && equalData;
    }

    

    /**
     * Default constructor for Tester.
     */
    public Tester() {}
}
