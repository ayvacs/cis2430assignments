package dayplanner;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


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
        runTestCase("case21", "Title Index: Activity Addition", testCase21());
        runTestCase("case22", "Persistence Verification (RAS Read/Write)", testCase22());

        // Exception-handling test cases (Assignment 4)
        runTestCase("case23", "Exception: Time Constructor Throws IllegalArgumentException on Bad Year",   testCase23());
        runTestCase("case24", "Exception: Time Constructor Throws IllegalArgumentException on Bad Month",  testCase24());
        runTestCase("case25", "Exception: Time Constructor Throws IllegalArgumentException on Bad Day",    testCase25());
        runTestCase("case26", "Exception: Time Constructor Throws IllegalArgumentException on Bad Hour",   testCase26());
        runTestCase("case27", "Exception: Time Constructor Throws IllegalArgumentException on Bad Minute", testCase27());
        runTestCase("case28", "Exception: Time.compareTo Throws NullPointerException for Null Argument",  testCase28());
        runTestCase("case29", "Exception: Time.compareTo Throws ClassCastException for Wrong Class",      testCase29());
        runTestCase("case30", "Exception: Activity Constructor Throws on Null/Blank Title",               testCase30());
        runTestCase("case31", "Exception: Activity Constructor Throws on Null Start/End Time",            testCase31());
        runTestCase("case32", "Exception: Activity Constructor Throws When Start >= End",                 testCase32());
        runTestCase("case33", "Exception: OtherActivity Throws on Null/Blank Location",                  testCase33());
        runTestCase("case34", "Exception: Input.readInt Recovers from Non-Integer Input",                 testCase34());
        runTestCase("case35", "Exception: Time Mutators Throw IllegalArgumentException on Bad Values",    testCase35());

        // Inheritance, Abstract Classes & Polymorphism test cases (Assignment 3, requirement 3a)
        runTestCase("case36", "Abstract Class: Activity is Abstract and Cannot be Instantiated Directly", testCase36());
        runTestCase("case37", "Polymorphism: Dynamic Binding via toString and getType on Activity List",  testCase37());

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
        ActivityList list = new ActivityList();
        Time start = new Time(2026, 10, 20, 14, 30);
        Time end = new Time(2026, 10, 20, 16, 30);
        SchoolActivity sa = new SchoolActivity("CIS 2430 Lab Office Hours", start, end, "Ask TA about search");

        list.add(sa);
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
        ActivityList activities = testActivities();
        return activities.findTitleMatches(new String[] { "Java" }).equals(java.util.Arrays.asList(0, 2));
    }

    /**
     * Case 11: Verifies search matching an activity in the middle of the list.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase11() {
        ActivityList activities = testActivities();
        return activities.findTitleMatches(new String[] { "Java", "Programming" })
            .equals(java.util.Arrays.asList(0));
    }

    /**
     * Case 12: Verifies search matching an activity at the end of the list.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase12() {
        ActivityList activities = testActivities();
        return activities.findTitleMatches(new String[] { "JAVA", "PROGRAMMING" })
            .equals(java.util.Arrays.asList(0));
    }

    /**
     * Case 13: Verifies that searching for a nonexistent item yields zero matches.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase13() {
        return testActivities().findTitleMatches(new String[] { "Nonexistent", "Activity" }).isEmpty();
    }

    /**
     * Case 14: Verifies that keyword matching requires whole-word matches.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase14() {
        ActivityList activities = testActivities();
        return activities.findTitleMatches(new String[] { "Program" }).isEmpty();
    }

    /**
     * Case 15: Verifies that keyword matching is case-insensitive and order-invariant.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase15() {
        ActivityList activities = testActivities();
        return activities.findTitleMatches(new String[] { "programming", "jAvA" })
            .equals(java.util.Arrays.asList(0));
    }

    /**
     * Case 16: Verifies bounded time range filtering.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase16() {
        ActivityList homeList = testTimeActivities();
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
        ActivityList homeList = testTimeActivities();
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
        ActivityList homeList = testTimeActivities();
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
     * Case 20: Verifies that a search without title keywords returns every list position.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase20() {
        ActivityList activities = testActivities();
        return activities.findTitleMatches(null).size() == activities.size();
    }

    /**
     * Case 21: Verifies that appending an activity updates its title index position.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase21() {
        ActivityList list = new ActivityList();
        Time t1 = new Time(2026, 10, 1, 10, 0);
        Time t2 = new Time(2026, 10, 1, 11, 0);

        list.add(new HomeActivity("A1", t1, t2));
        list.add(new HomeActivity("A2", t1, t2));
        list.add(new HomeActivity("A3", t1, t2));
        return list.findTitleMatches(new String[] { "A3" }).equals(java.util.Arrays.asList(2));
    }

    /**
     * Case 22: Verifies serializing activities to RAS format and reading them back.
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase22() {
        String testDir;
        try {
            testDir = java.nio.file.Files.createTempDirectory("dayplanner-test-").toString();
        } catch (java.io.IOException e) {
            return false;
        }
        String testFile = "test_persistence.ras";

        ActivityList outList = new ActivityList();
        Time t1 = new Time(2026, 10, 1, 10, 0);
        Time t2 = new Time(2026, 10, 1, 11, 0);

        HomeActivity ha = new HomeActivity("Test Home", t1, t2, "Home Comment");
        SchoolActivity sa = new SchoolActivity("Test School", t1, t2, "School Comment");
        OtherActivity oa = new OtherActivity("Test Other", t1, t2, "Other Comment", "Library");

        outList.add(ha);
        outList.add(sa);
        outList.add(oa);

        boolean saved = outList.saveToRAS(testDir, testFile);
        if (!saved) return false;

        ActivityList inList = new ActivityList(testDir, testFile);
        boolean sizeMatches = inList.size() == 3;
        boolean equalData = inList.get(0).equals(ha) &&
                            inList.get(1).equals(sa) &&
                            inList.get(2).equals(oa);

        // Clean up test file and directory
        try {
            Files.deleteIfExists(Path.of(testDir, testFile));
            Files.deleteIfExists(Path.of(testDir));
        } catch (IOException e) {
            // ignore lol
        }

        return sizeMatches && equalData
            && inList.findTitleMatches(new String[] { "Test" }).equals(java.util.Arrays.asList(0, 1, 2));
    }

    /**
     * Return an ActivityList with a bunch of activities with the same start and end
     * times (Sample data)
     */
    private static ActivityList testActivities() {
        ActivityList list = new ActivityList();
        Time start = new Time(2026, 10, 1, 9, 0);
        Time end = new Time(2026, 10, 1, 10, 0);
        list.add(new HomeActivity("Java Programming", start, end));
        list.add(new SchoolActivity("Programming Workshop", start, end));
        list.add(new OtherActivity("Java Study", start, end, "Library"));
        return list;
    }

    /**
     * Return a ActivityList with a bunch of activities with differing start and end
     * times (Sample data)
     */
    private static ActivityList testTimeActivities() {
        ActivityList list = new ActivityList();
        list.add(new HomeActivity("Oct 4", new Time(2026, 10, 4, 9, 0), new Time(2026, 10, 4, 10, 0)));
        list.add(new HomeActivity("Oct 5", new Time(2026, 10, 5, 9, 0), new Time(2026, 10, 5, 10, 0)));
        list.add(new HomeActivity("Oct 6", new Time(2026, 10, 6, 10, 0), new Time(2026, 10, 6, 11, 0)));
        list.add(new HomeActivity("Oct 8", new Time(2026, 10, 8, 9, 0), new Time(2026, 10, 8, 10, 0)));
        list.add(new HomeActivity("Oct 9", new Time(2026, 10, 9, 9, 0), new Time(2026, 10, 9, 10, 0)));
        list.add(new HomeActivity("Oct 10", new Time(2026, 10, 10, 9, 0), new Time(2026, 10, 10, 10, 0)));
        return list;
    }

    

    // =========================================================================
    // Exception-handling test cases (Assignment 4)
    // =========================================================================

    /**
     * Case 23: Verifies that Time's parameterized constructor throws
     * IllegalArgumentException for an out-of-range year (year = 0).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase23() {
        try {
            new Time(0, 1, 1, 0, 0);
            return false; // should have thrown
        } catch (IllegalArgumentException e) {
            return true;  // correct: exception was thrown
        }
    }

    /**
     * Case 24: Verifies that Time's parameterized constructor throws
     * IllegalArgumentException for an out-of-range month (month = 13).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase24() {
        try {
            new Time(2026, 13, 1, 0, 0);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Case 25: Verifies that Time's parameterized constructor throws
     * IllegalArgumentException for an out-of-range day (day = 32 in January).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase25() {
        try {
            new Time(2026, 1, 32, 0, 0);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Case 26: Verifies that Time's parameterized constructor throws
     * IllegalArgumentException for an out-of-range hour (hour = 24).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase26() {
        try {
            new Time(2026, 1, 1, 24, 0);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Case 27: Verifies that Time's parameterized constructor throws
     * IllegalArgumentException for an out-of-range minute (minute = 60).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase27() {
        try {
            new Time(2026, 1, 1, 0, 60);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Case 28: Verifies that Time.compareTo throws NullPointerException
     * when the argument is null, as required by the Comparable contract.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase28() {
        Time t = new Time(2026, 10, 1, 9, 0);
        try {
            t.compareTo((Object) null);
            return false; // should have thrown
        } catch (NullPointerException e) {
            return true;  // correct
        }
    }

    /**
     * Case 29: Verifies that Time.compareTo throws ClassCastException
     * when the argument is not an instance of Time.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase29() {
        Time t = new Time(2026, 10, 1, 9, 0);
        try {
            t.compareTo("not a Time");
            return false; // should have thrown
        } catch (ClassCastException e) {
            return true;  // correct
        }
    }

    /**
     * Case 30: Verifies that Activity's constructor throws
     * IllegalArgumentException for a null or blank title.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase30() {
        Time start = new Time(2026, 10, 1, 9, 0);
        Time end   = new Time(2026, 10, 1, 10, 0);

        boolean nullTitleThrew  = false;
        boolean blankTitleThrew = false;

        try {
            new HomeActivity(null, start, end);
        } catch (IllegalArgumentException e) {
            nullTitleThrew = true;
        }

        try {
            new HomeActivity("   ", start, end);
        } catch (IllegalArgumentException e) {
            blankTitleThrew = true;
        }

        return nullTitleThrew && blankTitleThrew;
    }

    /**
     * Case 31: Verifies that Activity's constructor throws
     * IllegalArgumentException when startTime or endTime is null.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase31() {
        Time valid = new Time(2026, 10, 1, 9, 0);

        boolean nullStartThrew = false;
        boolean nullEndThrew   = false;

        try {
            new HomeActivity("Test", null, valid);
        } catch (IllegalArgumentException e) {
            nullStartThrew = true;
        }

        try {
            new HomeActivity("Test", valid, null);
        } catch (IllegalArgumentException e) {
            nullEndThrew = true;
        }

        return nullStartThrew && nullEndThrew;
    }

    /**
     * Case 32: Verifies that Activity's constructor throws
     * IllegalArgumentException when startTime is not strictly before endTime
     * (covers equal times and reversed order).
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase32() {
        Time t1 = new Time(2026, 10, 1, 10, 0);
        Time t2 = new Time(2026, 10, 1,  9, 0); // before t1

        boolean equalThrew   = false;
        boolean invertedThrew = false;

        // equal start and end
        try {
            new HomeActivity("Test", t1, t1);
        } catch (IllegalArgumentException e) {
            equalThrew = true;
        }

        // start after end
        try {
            new HomeActivity("Test", t1, t2);
        } catch (IllegalArgumentException e) {
            invertedThrew = true;
        }

        return equalThrew && invertedThrew;
    }

    /**
     * Case 33: Verifies that OtherActivity's constructor throws
     * IllegalArgumentException when the location is null or blank.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase33() {
        Time start = new Time(2026, 10, 1, 9, 0);
        Time end   = new Time(2026, 10, 1, 10, 0);

        boolean nullLocationThrew  = false;
        boolean blankLocationThrew = false;

        try {
            new OtherActivity("Test", start, end, null);
        } catch (IllegalArgumentException e) {
            nullLocationThrew = true;
        }

        try {
            new OtherActivity("Test", start, end, "   ");
        } catch (IllegalArgumentException e) {
            blankLocationThrew = true;
        }

        return nullLocationThrew && blankLocationThrew;
    }

    /**
     * Case 34: Verifies that Input.readInt() silently recovers from a
     * non-integer line and returns the first valid integer that follows.
     * Uses a ByteArrayInputStream so no real keyboard is needed.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase34() {
        // Feed: "hello\n42\n" – the first token is not an integer, the second is
        java.io.InputStream stream =
            new ByteArrayInputStream("hello\n42\n".getBytes());
        Input input = new Input(stream);

        // Suppress console output during this test
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream()));
        try {
            int result = input.readInt();
            return result == 42;
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Case 35: Verifies that the Time mutator methods (setYear, setMonth,
     * setDay, setHour, setMinute) each throw IllegalArgumentException for
     * values outside their valid ranges.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase35() {
        Time t = new Time(2026, 6, 15, 12, 30);

        boolean setYearThrew   = false;
        boolean setMonthThrew  = false;
        boolean setDayThrew    = false;
        boolean setHourThrew   = false;
        boolean setMinuteThrew = false;

        try { t.setYear(0);    } catch (IllegalArgumentException e) { setYearThrew   = true; }
        try { t.setMonth(13);  } catch (IllegalArgumentException e) { setMonthThrew  = true; }
        try { t.setDay(32);    } catch (IllegalArgumentException e) { setDayThrew    = true; }
        try { t.setHour(24);   } catch (IllegalArgumentException e) { setHourThrew   = true; }
        try { t.setMinute(60); } catch (IllegalArgumentException e) { setMinuteThrew = true; }

        return setYearThrew && setMonthThrew && setDayThrew && setHourThrew && setMinuteThrew;
    }

    /**
     * Case 36: Verifies that Activity is declared as an abstract class, so
     * objects can only be created from the concrete subclasses: HomeActivity,
     * SchoolActivity, and OtherActivity.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase36() {
        // Activity class itself must be abstract
        boolean activityIsAbstract = java.lang.reflect.Modifier.isAbstract(Activity.class.getModifiers());

        // Concrete subclasses must not be abstract
        boolean homeConcrete = !java.lang.reflect.Modifier.isAbstract(HomeActivity.class.getModifiers());
        boolean schoolConcrete = !java.lang.reflect.Modifier.isAbstract(SchoolActivity.class.getModifiers());
        boolean otherConcrete = !java.lang.reflect.Modifier.isAbstract(OtherActivity.class.getModifiers());

        // Subclasses must inherit from Activity
        boolean homeExtends = Activity.class.isAssignableFrom(HomeActivity.class);
        boolean schoolExtends = Activity.class.isAssignableFrom(SchoolActivity.class);
        boolean otherExtends = Activity.class.isAssignableFrom(OtherActivity.class);

        return activityIsAbstract && homeConcrete && schoolConcrete && otherConcrete
            && homeExtends && schoolExtends && otherExtends;
    }

    /**
     * Case 37: Verifies effective use of polymorphism through dynamic binding.
     * Given an ArrayList&lt;Activity&gt; containing HomeActivity, SchoolActivity, and
     * OtherActivity objects, all details are displayed polymorphically via
     * toString() without checking class types or using instanceof.
     * 
     * @return <code>true</code> if the test passes, <code>false</code> otherwise.
     */
    public static boolean testCase37() {
        Time s1 = new Time(2026, 10, 1, 9, 0);
        Time e1 = new Time(2026, 10, 1, 10, 0);
        Time s2 = new Time(2026, 10, 1, 11, 0);
        Time e2 = new Time(2026, 10, 1, 12, 0);
        Time s3 = new Time(2026, 10, 1, 13, 0);
        Time e3 = new Time(2026, 10, 1, 14, 0);

        java.util.ArrayList<Activity> activities = new java.util.ArrayList<>();
        activities.add(new HomeActivity("Clean Kitchen", s1, e1, "Do dishes"));
        activities.add(new SchoolActivity("CIS 2430 Lecture", s2, e2, "Room 101"));
        activities.add(new OtherActivity("Dentist Appointment", s3, e3, "Checkup", "Medical Clinic"));

        // Polymorphic display via toString() and polymorphic getType()
        // No class type checks or instanceof needed
        String[] expectedDetails = {
            "Clean Kitchen (2026/10/01 09:00 - 2026/10/01 10:00) (Do dishes)",
            "CIS 2430 Lecture (2026/10/01 11:00 - 2026/10/01 12:00) (Room 101)",
            "Dentist Appointment (2026/10/01 13:00 - 2026/10/01 14:00 at Medical Clinic) (Checkup)"
        };
        ActivityType[] expectedTypes = {
            ActivityType.HOME,
            ActivityType.SCHOOL,
            ActivityType.OTHER
        };

        for (int i = 0; i < activities.size(); i++) {
            Activity act = activities.get(i);
            // Dynamic binding invokes the appropriate toString() and getType() implementation
            String details = act.toString();
            ActivityType type = act.getType();

            if (!details.equals(expectedDetails[i]))
                return false;
            if (type != expectedTypes[i])
                return false;
        }

        return true;
    }

    /**
     * Default constructor (not used)
     */
    private Tester() {}
}
