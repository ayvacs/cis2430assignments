# DayPlanner

*by Dom Pecak (1376696)*

The DayPlanner program is a schedule management tool that allows the user to track, schedule, and search for three categories of daily activities:

* **Home activities**: Anything done at home like chores, grocery runs, and studying at home.
* **School activities**: Anything done at university like attending lectures, scheduled labs, and campus meetings.
* **Other activities**: Anything done outside of home and school, like doctor visits, gym workouts, shopping, which additionally require a location attribute.

Activities contain a title, starting time, ending time, an optional comment, and (for **Other activities**) a mandatory location. The system stores all activities in one `ArrayList`. A `HashMap` title index maps each lower-case title keyword to the positions of activities containing that keyword. Searches intersect those positions before applying category and time filters. Data persists between sessions in a custom text based format called `RAS`.

This repository also consists of a Tester (`Tester.java`), which is an automatic tester, that executes all of the described test cases.

# File structure

| Directory | Description |
| --- | --- |
| `bin/` | Compiled Java bytecode and executable JAR |
| `dat/` | User data folder containing `.ras` data files |
| `docs/` | Generated Javadoc HTML documentation |
| `src/` | Java source code organized under the `dayplanner` package |

# User guide

All commands are automated via Makefile, so execute the proper command in the root directory (the directory that contains this `README.md`)

```shell
make               # Build everything (compile classes,
                   # generate javadoc, build JAR)

make compile       # Compile source files to bin/
make jar           # Compile and package jar
make docs          # Generate HTML javadoc in docs/

make test          # Compile and run the Tester
make zip NAME=foo  # Build everything and generate a
                   # foo.tar.gz archive for submission

make clean         # Remove generated bin/ and docs/ folders
```

## Executing

It is recommended to compile the entire project with `make all`, then an executable JAR file named `DayPlanner.jar` will be generated, you can execute the JAR from the terminal:

```shell
java -jar bin/DayPlanner.jar <data file name>
```

The data file is saved in the `dat` directory. A sample data file called `sampleData.ras` is included. To load this example file, execute the JAR from the terminal:

```shell
java -jar bin/DayPlanner.jar sampleData.ras
```

## Interface

When running the application, the command loop presents a main menu with options:

* **Add an activity**: enter `add`, `a`, or `1`.
* **Search for activities**: enter `search`, `s`, or `2`.
* **Quit application**: enter `quit`, `q`, or `3`.
* Any other input is rejected with an error message and prompts the user to re-enter.

### 1. Adding an Activity (`add`)

The following steps are presented:

1. **Activity Type**: Enter `home` (or `1`, `h`), `school` (or `2`, `s`), or `other` (or `3`, `o`).
   * The new activity is appended to the activity list and its title keywords are added to the index.
2. **Title**: Enter a non-empty descriptive title.
3. **Starting Time**: Enter a timestamp in `YYYY/MM/DD HH:MM` format (e.g. `2026/09/10 17:30`).
   * Validated defensively: year $\ge 1$, month between $1$ and $12$, days valid according to the month (including leap year rules for February: 29 days on leap years, 28 days otherwise), hour between $0$ and $23$, minute between $0$ and $59$.
4. **Ending Time**: Enter a timestamp in `YYYY/MM/DD HH:MM` format, same validation above. Must be strictly after the starting time.
5. **Comment (Optional)**: Answer `Y` / `Yes` or `N` / `No`. If `Y`, enter the comment.
6. **Location (Other activities only)**: Enter the physical location (required for `OtherActivity`).

### 2. Searching for Activities (`search`)

A search query accepts up to three optional components (press Enter on any prompt to leave that component open / unrestricted):
1. **Activity Type**: Filter by `home`, `school`, or `other`. Press Enter to search across all three categories.
2. **Title Keywords**: Enter space-separated keywords (e.g. `Java Programming`). All keywords must appear as exact whole words in the activity title (case-insensitive, any order). Press Enter to match any title.
3. **Time Period**: Enter a time range:
   * Both bounded: `2026/09/12 06:00 - 2026/09/22 11:59` (matches activities starting at/after the start time and ending at/before the end time).
   * Open end: `2026/09/12 06:00 -` (matches activities starting at/after the start time).
   * Open start: `- 2026/09/22 11:59` (matches activities ending at/before the end time).
   * Open both ends: press Enter (matches activities occurring at any time).

### 3. Quitting (`quit`)

Saves the activity list to disk in RAS format and exits.

# Implementation

The DayPlanner consists of the following classes in the `dayplanner` package:

* `Time`: Represents an instant in time with minute precision (`year`, `month`, `day`, `hour`, `minute`). Implements the `Comparable` interface (`compareTo(Object)`) for chronological ordering and time period validation, properly throwing `NullPointerException` when passed `null` and `ClassCastException` for mismatched object classes. Constructors and mutator methods strictly enforce valid ranges and leap year calendar rules, throwing `IllegalArgumentException` whenever invariants are violated.
* `Activity`: Abstract base class holding common attributes: `title`, `startTime`, `endTime`, `comment`. Enforces class invariants (non-null and non-blank title, non-null timestamps, and start time strictly preceding end time), throwing `IllegalArgumentException` in constructors and mutator methods on invariant violations. Implements `Comparable<Activity>` to sort by starting time, accessors, mutators, and also overrides `equals` and `toString`.
    * `HomeActivity`: Subclass of `Activity` representing home activities.
    * `SchoolActivity`: Subclass of `Activity` representing school activities.
    * `OtherActivity`: Subclass of `Activity` representing miscellaneous activities, adding the `location` attribute (enforced non-null and non-blank via `IllegalArgumentException` in constructors and mutators), accessors, mutators, and custom `toString`.
* `ActivityList`: Wrapper around an `ArrayList<Activity>`, extending `AbstractList<Activity>`. Enforces non-null activities in mutators and insertion methods with `IllegalArgumentException`. Maintains a `HashMap` title index whose values are activity positions.
* `ActivityType`: Enum denoting all valid types of Activities.
* `Input`: Wrapper around `Scanner` utilizing exception handling (`NumberFormatException`, `IllegalArgumentException`) to catch invalid input and re-prompt the user to retry entering valid data.
* `DayPlanner`: The main application class which manages the user menu and handles command/creation exceptions with recovery loops.
* `DayPlannerUtils`: Utility methods for `DayPlanner`.
* `MenuOption`: Enum denoting all valid menu options.
* `RasParser`: Utility methods for serialization/deserialization.
* `RasException`: Exception denoting something went wrong with serialization/deserialization.

## Exception Handling & Class Invariants

The application handles errors and invariant violations via structured Java exceptions:

1. **Class Invariants**:
   * `Time`: Enforces year (1–9999), month (1–12), day (1 to month maximum, accounting for leap years), hour (0–23), and minute (0–59). Any attempt to construct or mutate a `Time` instance with out-of-range values throws an `IllegalArgumentException`.
   * `Time.compareTo`: Implements the `Comparable` interface. Compares chronologically, throwing `NullPointerException` if the argument is `null` and `ClassCastException` if the argument is not an instance of `Time`.
   * `Activity` and subclasses: Enforce mandatory non-blank `title`, non-null `startTime` and `endTime`, and chronological ordering (`startTime` must be strictly before `endTime`). Mutator methods (`setTitle`, `setStartTime`, `setEndTime`) and constructors throw `IllegalArgumentException` on violation.
   * `OtherActivity`: Enforces mandatory non-blank `location`, throwing `IllegalArgumentException` in constructors and `setLocation`.
   * `ActivityList`: Enforces non-null elements in `add` and `set` methods, throwing `IllegalArgumentException`.

2. **Input Validation & Recovery**:
   * `Input.readInt()`: Catches `NumberFormatException` when parsing integers, re-prompting the user until a valid integer is supplied.
   * `Input.readTime()`: Uses `Time` constructors and mutators inside `try-catch` blocks, catching `NumberFormatException` (for non-integer components) and `IllegalArgumentException` (for invalid dates or times), allowing the user to re-enter the timestamp.
   * `Input.readBool()`: Catches invalid boolean selections and re-prompts.
   * `DayPlanner`: Wraps menu command parsing and activity field input in `try-catch` loops so incorrect entries are reported with clear messages and the user is prompted to try again.

The Tester consists of one class:

* `Tester.java`: Automatic tester that executes test cases for DayPlanner features, invariants, and exceptions.

## Assumptions

My app assumes:

* The user is comfortable executing Java code from a terminal.
* The three activity categories (Home, School, Other) are sufficient to classify all scheduled items.
* Time values follow the standard Gregorian calendar.
* The user is fine with accessing their schedule on one device without syncing between devices

## Limitations

My app has several limitations:

* Does not offer all CRUD (create, remove, update, delete) operations which are standard for working with data.
* Activities cannot currently be removed or edited through the application.
* The text-based interface does not visually represent events on a calendar grid or timetable which makes it more difficult to detect overlapping events.
* No warning is given when two activities overlap or occupy the same time slot.
* The schedule doesn't sync between devices which is a very helpful feature in other scheduling apps.

# Future Improvements

If I had additional time to extend the project further, I would implement several features to resolve the downsides listed above:

1. Add the ability to remove and edit existing activities.
1. Add a gui with a weekly/monthly timetable with drag-and-drop scheduling.
1. Warn the user when an activity's time range conflicts with an existing activity in any category.
1. Support recurrence rules (e.g., weekly lectures or daily gym sessions) without manual repetition like other scheduling apps.

# Serialization

RAS (**Readable Activity Serial**) is a custom human-readable text format used to persist Activities and ActivityLists between program executions. There are several aspects to the RAS format:

<ul>
<li>Methods in each instance class (Activities, Time, etc.) that convert the class into a RAS representation string.</li>
<li>Methods in each instance class that construct a new instance from a RAS representation string.</li>
<li>String sanitization/desanitization so that commas and angled brackets can be used in String fields.
<li><code>.RAS</code> files, which follow the format:

```
<entry...>
<entry...>
<entry...>
```

where each `<entry...>` is enclosed in angle brackets with comma-separated fields:

1. `String` denoting the activity type (`HomeActivity`, `SchoolActivity`, or `OtherActivity`).
2. `String` denoting the activity title.
3. `String` denoting the starting time (`YYYY/MM/DD HH:MM`).
4. `String` denoting the ending time (`YYYY/MM/DD HH:MM`).
5. `String` denoting the comment (or `NIL` if no comment was provided).
6. `String` denoting the location (only present for `OtherActivity`).</li>
</ul>

# Test Plan

## 1. Testing Strategy and Conditions

1. **User Interface and Command Loop**:

   * Standard commands: `add`, `search`, `quit`.
   * Case variations: `Add`, `SEARCH`, `Quit`, `QUIT`.
   * Single-character abbreviations: `a`, `s`, `q`.
   * Numerical selections: `1`, `2`, `3`.
   * Invalid commands: irrelevant strings such as `bye`, `exit`, `help`, empty inputs, random numbers. Expected behavior: display helpful feedback message and re-prompt.

2. **Input Validation and Boundary Conditions**:

   * **Leap Years**: Century leap years, standard century years, standard leap years, standard non-leap years.
   * **Month Boundaries**: Months with 31 days, months with 30 days, February special case.
   * **Time Ranges**: Checking valid month, day, hour, minute, year.
   * **Chronological Ordering**: Starting time must be strictly before ending time. End time before or equal to start time is rejected with feedback.
   * **Mandatory vs Optional Fields**: Title cannot be blank. Location cannot be blank for `OtherActivity`. Comment is optional.
   * **Title Index Maintenance**: Loading and adding activities must populate the index with their list positions; indexed insertions, replacements, and removals must keep those positions correct.

3. **Search Matching Conditions**:
   * **Activity Category**: Filter by Home only, School only, Other only, or all categories (empty input).
   * **Keyword Matching**:
     * Word-level exactness: keyword `Program` must **not** match title `Programming in Java`.
     * Case insensitivity: keyword `java` must match title `Work on Java Programming`.
     * Order independence: keywords `Java Programming` must match title `Programming in Java`.
     * Multiple keywords: all keywords must appear in title; partial keyword match must be excluded.
     * Empty keyword query: matches all activities regardless of title.
   * **Element Position in List**:
     * Target activity is located at the **beginning** of the list (index 0).
     * Target activity is located in the **middle** of the list.
     * Target activity is located at the **end** of the list.
     * Target activity is **not on the list** (zero matches).
   * **Time Period Filtering**:
     * Both bounds open (empty input): matches all times.
     * Start time only (`YYYY/MM/DD HH:MM -`): matches activities starting at or after the timestamp.
     * End time only (`- YYYY/MM/DD HH:MM`): matches activities ending at or before the timestamp.
     * Both bounds specified: matches activities whose start and end both fall within the range.
     * Activity outside range: excluded.
     * Inverted time period (`start > end`): rejected with error message and re-prompted.
   * **Combined Queries**: Category + keywords + time period simultaneously filtered.

4. **Persistence (RAS)**:
   * Verify newly added activities are written to disk upon `quit`.
   * Verify saved activities are restored properly when restarting the application.

5. **Exception Handling and Class Invariants**:
   * **Time Component Bounds**: Parameterized constructor and mutator methods throw `IllegalArgumentException` on invalid year, month, day, hour, or minute values.
   * **Comparable Contract**: `Time.compareTo` throws `NullPointerException` when passed `null` and `ClassCastException` when passed an object of mismatched type.
   * **Activity Invariants**: Constructors throw `IllegalArgumentException` for null or blank titles, null start or end timestamps, and when start time is not strictly before end time.
   * **OtherActivity Location**: Constructor throws `IllegalArgumentException` for null or blank locations.
   * **Input Recovery**: `Input.readInt()` catches `NumberFormatException` on non-integer input and re-prompts until a valid integer is entered.

6. **Inheritance, Abstract Classes & Polymorphism**:
   * **Abstract Activity Class**: `Activity` is declared as an abstract class and cannot be instantiated directly; concrete subclasses (`HomeActivity`, `SchoolActivity`, `OtherActivity`) are instantiated.
   * **Dynamic Binding**: Iterating over an `ArrayList<Activity>` polymorphically invokes `toString()` and `getType()` on each element without explicit type checks or `instanceof`.

---

## 2. Tester

A tester program is included to verify all of the test cases. To compile and run the test suite run

```shell
make test
```

The tester will automatically run all test cases, and report results.

---

## 3. Test Cases

**Notes:**

* The test cases use self-contained activity lists and do not depend on user data files.
* The persistence test creates and removes a temporary directory and file.

| Case ID | Feature / Condition | Input Data / Steps | Expected Output | Status |
| --- | --- | --- | --- | --- |
| **case01** | Command Parsing: Invalid Command | Parse command `bye`. | Returns no menu option for the unrecognized command. | **PASS** |
| **case02** | Command Parsing: Case & Alias | Parse `ADD`, `s`, and `3`. | Recognizes the add, search, and quit options. | **PASS** |
| **case03** | Time Validation: Non-leap Feb 29 | When adding activity, enter start time `2026/02/29 10:00`. | Rejects with message: `Invalid day (29): Month 2 has max 28 days. Try again:` and re-prompts. | **PASS** |
| **case04** | Time Validation: Leap Year Feb 29 | Enter start time `2024/02/29 10:00`. | Accepted as valid leap year timestamp. | **PASS** |
| **case05** | Time Validation: Month Day Overflow | Enter start time `2026/04/31 09:00` (April has 30 days). | Rejects with message: `Invalid day (31): Month 4 has max 30 days. Try again:` and re-prompts. | **PASS** |
| **case06** | Time Validation: Component Bounds | Enter start times `2026/13/01 10:00`, `2026/05/10 24:00`, `2026/05/10 12:60`, `0/01/01 10:00`. | Each rejected with specific feedback identifying the out-of-range field (month, hour, minute, year). | **PASS** |
| **case07** | Chronological Comparison | Compare `2026/10/15 14:00` with an earlier and equal time. | Confirms the start is later than the earlier time and equal to the matching time. | **PASS** |
| **case08** | Blank Title & Location Values | Check blank title and location strings, plus a valid Other activity location. | Identifies blank strings and confirms the activity has a nonblank location. | **PASS** |
| **case09** | Activity Creation and Retrieval | Add a `SchoolActivity` with title, times, and comment to an `ActivityList`. | Retrieves the same activity with its fields preserved. | **PASS** |
| **case10** | Title Index: Beginning Position | Search local activities with keyword `Java`. | Returns positions 0 and 2. | **PASS** |
| **case11** | Title Index: Keyword Intersection | Search local activities with keywords `Java Programming`. | Returns only position 0, which contains both keywords. | **PASS** |
| **case12** | Title Index: Case Normalization | Search with `JAVA PROGRAMMING`. | Returns the same result as lower-case keywords. | **PASS** |
| **case13** | Title Index: Missing Keyword | Search local activities for `Nonexistent Activity`. | Returns no candidate positions. | **PASS** |
| **case14** | Title Index: Whole-Word Matching | Search for `Program` when the title contains `Programming`. | Returns no matches. | **PASS** |
| **case15** | Title Index: Case & Order Invariance | Search keywords `programming Java`. | Returns position 0. | **PASS** |
| **case16** | Time Period: Bounded Range | Search local activities from `2026/10/04 00:00` to `2026/10/06 12:00`. | Matches 3 activities. | **PASS** |
| **case17** | Time Period: Open End | Search local activities from `2026/10/08 00:00`. | Matches 3 activities. | **PASS** |
| **case18** | Time Period: Open Start | Search local activities through `2026/10/05 12:00`. | Matches 2 activities. | **PASS** |
| **case19** | Time Period: Inverted Range | Compare `2026/10/20 12:00` with `2026/10/10 12:00`. | Confirms that the proposed start time comes after the end time. | **PASS** |
| **case20** | Search: No Title Keywords | Request all title candidates with no keywords. | Returns every position in the activity list. | **PASS** |
| **case21** | Title Index: Activity Addition | Add three activities, then search for the title of the last activity. | Returns its updated position, 2. | **PASS** |
| **case22** | Persistence Verification | Write three activities to a temporary RAS file and load them again. | Activities are restored in order and the title index returns the expected positions. | **PASS** |
| **case23** | Exception: Bad Year in Time Constructor | Construct `Time(0, 1, 1, 0, 0)` with invalid year (0). | Throws `IllegalArgumentException`. | **PASS** |
| **case24** | Exception: Bad Month in Time Constructor | Construct `Time(2026, 13, 1, 0, 0)` with invalid month (13). | Throws `IllegalArgumentException`. | **PASS** |
| **case25** | Exception: Bad Day in Time Constructor | Construct `Time(2026, 1, 32, 0, 0)` with invalid day (32 in January). | Throws `IllegalArgumentException`. | **PASS** |
| **case26** | Exception: Bad Hour in Time Constructor | Construct `Time(2026, 1, 1, 24, 0)` with invalid hour (24). | Throws `IllegalArgumentException`. | **PASS** |
| **case27** | Exception: Bad Minute in Time Constructor | Construct `Time(2026, 1, 1, 0, 60)` with invalid minute (60). | Throws `IllegalArgumentException`. | **PASS** |
| **case28** | Exception: Time.compareTo Null Argument | Call `compareTo((Object) null)` with `null` argument. | Throws `NullPointerException` per `Comparable` contract. | **PASS** |
| **case29** | Exception: Time.compareTo Wrong Class | Call `compareTo("not a Time")` with a non-`Time` object. | Throws `ClassCastException`. | **PASS** |
| **case30** | Exception: Null or Blank Activity Title | Construct `HomeActivity` with `null` and blank (`"   "`) titles. | Throws `IllegalArgumentException` in both cases. | **PASS** |
| **case31** | Exception: Null Activity Timestamps | Construct `HomeActivity` with `null` start time and `null` end time. | Throws `IllegalArgumentException` in both cases. | **PASS** |
| **case32** | Exception: Start Time Not Before End Time | Construct `HomeActivity` with equal start/end times and start time after end time. | Throws `IllegalArgumentException` in both cases. | **PASS** |
| **case33** | Exception: Null or Blank Location | Construct `OtherActivity` with `null` and blank (`"   "`) locations. | Throws `IllegalArgumentException` in both cases. | **PASS** |
| **case34** | Exception: Input.readInt Non-Integer Input | Feed non-integer input (`"hello\n42\n"`) to `Input.readInt()`. | Silently recovers from `NumberFormatException` and returns `42`. | **PASS** |
| **case35** | Exception: Time Mutators Component Bounds | Call `setYear(0)`, `setMonth(13)`, `setDay(32)`, `setHour(24)`, `setMinute(60)`. | Each mutator throws `IllegalArgumentException`. | **PASS** |
| **case36** | Abstract Class: Activity Direct Instantiation | Inspect `Activity` class modifiers and concrete subclasses via reflection. | Confirms `Activity` is abstract and subclasses are concrete. | **PASS** |
| **case37** | Polymorphism: Dynamic Binding via toString & getType | Invoke `toString()` and `getType()` on an `ArrayList<Activity>` containing all three subclasses. | Dynamic binding invokes correct subclass implementation without `instanceof`. | **PASS** |
