# DayPlanner

by Dom Pecak (1376696)

Complete commit history is available on [GitHub](https://github.com/ayvacs/cis2430assignments).

# General Problem

The DayPlanner program is a schedule management tool built for CIS*2430 Assignment 1. It allows users to track, schedule, and search for three distinct categories of daily activities:
* **Home activities**: Anything done at home (e.g. chores, family calls, studying at home).
* **School activities**: Anything done at school or university (e.g. attending lectures, scheduled labs, campus meetings).
* **Other activities**: Anything done outside of home and school (e.g. doctor visits, gym workouts, shopping), which additionally require a physical location attribute.

Activities contain a title, starting time, ending time, an optional comment, and (for other activities) a mandatory location. The system stores each category in a dedicated array of fixed capacity, supports sequential search queries across multiple optional filters (category, title keywords, and time periods), and persists data to disk between runs.

# File structure

| Directory / File | Description |
| --- | --- |
| `bin/` | Compiled Java bytecode and executable JAR |
| `dat/` | User data folder containing `.ras` persistence files |
| `docs/` | Generated Javadoc HTML documentation |
| `src/` | Java source code organized under the `dayplanner` package |
| `Makefile` | Build automation targets |
| `README.md` | System documentation and test plan |

# User guide

To compile, build documentation, or run the application, run the appropriate `make` target in the project root directory (the directory containing `Makefile` and `README.md`):

```shell
make            # Build everything (compile classes, generate javadoc, build JAR)

make compile    # Compile source files to bin/
make run        # Compile and execute via JVM
make jar        # Compile and package executable bin/DayPlanner.jar
make docs       # Generate HTML javadoc in docs/

make clean      # Remove generated bin/ and docs/ folders
```

You can also run the packaged JAR directly:
```shell
java -jar bin/DayPlanner.jar
```

## Interactive Commands

When running the application, the command loop presents a main menu:

```
          ===-=-==-=====-==-=-===

  『 DAY PLANNER 』  Main Menu

Please enter a command:
	1. add    - Insert a new activity
	2. search - Search for an activity
	3. quit   - Exit DayPlanner
```

The system defends against case variation and common aliases:
* **Add an activity**: enter `add`, `a`, or `1`.
* **Search for activities**: enter `search`, `s`, or `2`.
* **Quit application**: enter `quit`, `q`, or `3`.
* Any other input (e.g., `bye`, `exit`) is rejected with an error message and prompts the user to re-enter.

### 1. Adding an Activity (`add`)
Prompts guide you through each attribute:
1. **Activity Type**: Enter `home` (or `1`, `h`), `school` (or `2`, `s`), or `other` (or `3`, `o`).
   * If the chosen array is full (capacity reached), the system rejects the addition immediately with feedback and returns to the menu.
2. **Title**: Enter a non-empty descriptive title.
3. **Starting Time**: Enter a timestamp in `YYYY/MM/DD HH:MM` format (e.g. `2026/09/10 17:30`).
   * Validated defensively: year $\ge 1$, month between $1$ and $12$, days valid according to the month (including leap year rules for February: 29 days on leap years, 28 days otherwise), hour between $0$ and $23$, minute between $0$ and $59$.
4. **Ending Time**: Enter a timestamp in `YYYY/MM/DD HH:MM` format. Must be strictly after the starting time.
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
Saves all activity lists to disk in RAS format and exits cleanly.

# Implementation

The DayPlanner architecture consists of eight primary classes in the `dayplanner` package:
* `Time`: Models timestamps with minute precision (`year`, `month`, `day`, `hour`, `minute`). Implements `Comparable<Time>` for chronological ordering and defensive range validation (including Gregorian leap year calculations).
* `Activity`: Abstract base class holding common attributes: `title`, `startTime`, `endTime`, `comment`. Implements `Comparable<Activity>` to sort by starting time, accessors, mutators, `equals`, and `toString`.
* `HomeActivity`: Subclass of `Activity` representing home activities.
* `SchoolActivity`: Subclass of `Activity` representing school activities.
* `OtherActivity`: Subclass of `Activity` representing miscellaneous activities, adding the `location` attribute, accessors, mutators, and custom `toString`.
* `ActivityList`: Wrapper around a fixed-size Java array (`DEFAULT_CAPACITY = 256`), extending `AbstractList<Activity>`. Implements bounds checking, `append`, `sort`, `isFull`, `isEmpty`, and RAS persistence.
* `Input`: Wrapper around `Scanner` with defensive validation methods for integers, booleans, commands, and timestamps.
* `DayPlanner`: The main driver class managing the user menu, activity arrays, add workflow, sequential search filtering, and serialization.

## Assumptions

My solution assumes:
* The user executes Java code via a terminal or command-line environment.
* The three activity categories (Home, School, Other) are sufficient to classify all scheduled items.
* Time values follow the standard Gregorian calendar with minute precision.
* The fixed array capacity of 256 entries per list is adequate for single-user daily planning needs.

## Limitations

My solution has several limitations:
* Fixed-size arrays impose an upper limit on the number of activities stored in each category before resizing or reallocation would be needed.
* Sequential search runs in $O(N)$ time per list; with larger datasets, an inverted index or hash map on keywords would provide faster lookups.
* The text-based CLI does not visually represent overlapping events on a calendar grid or timetable.
* No collision detection warning is given when two activities occupy overlapping time slots.

## Serialization

RAS (**Readable Activity Serial**) is a custom human-readable text format used to persist Activities and ActivityLists between program executions. RAS files follow the format:

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
6. `String` denoting the location (only present for `OtherActivity`).

File paths are configured via `src/dayplanner/ras.properties`:
* `directory_data=dat`
* `filename_home=home.ras`
* `filename_school=school.ras`
* `filename_other=other.ras`

# Test Plan

## 1. Testing Strategy and Conditions

Testing of the DayPlanner system follows defensive programming principles, exercising both normal operational flows and boundary/edge conditions across all functional requirements:

1. **User Interface and Command Loop**:
   * Standard commands: `add`, `search`, `quit`.
   * Case variations: `Add`, `SEARCH`, `Quit`, `QUIT`.
   * Single-character abbreviations: `a`, `s`, `q`.
   * Numerical selections: `1`, `2`, `3`.
   * Invalid commands: irrelevant strings such as `bye`, `exit`, `help`, empty inputs, random numbers. Expected behavior: display helpful feedback message and re-prompt.

2. **Input Validation and Boundary Conditions**:
   * **Leap Years**: Century leap years (2000 divisible by 400 $\rightarrow$ leap), standard century years (1900 not leap $\rightarrow$ 28 days), standard leap years (2024 $\rightarrow$ 29 days), standard non-leap years (2023, 2026 $\rightarrow$ 28 days).
   * **Month Boundaries**: Months with 31 days (Jan 31 valid, Jan 32 rejected), months with 30 days (Apr 30 valid, Apr 31 rejected), February (Feb 28/29 boundary checks).
   * **Time Ranges**: Month $\notin [1, 12]$, day $\notin [1, \text{maxDays}]$, hour $\notin [0, 23]$, minute $\notin [0, 59]$, year $< 1$.
   * **Chronological Ordering**: Starting time must be strictly before ending time. End time before or equal to start time must be rejected with feedback.
   * **Mandatory vs Optional Fields**: Title cannot be blank. Location cannot be blank for `OtherActivity`. Comment is optional.
   * **Array Capacity Boundary**: Attempting to add an activity when the corresponding array is at capacity must be rejected before prompting for attributes.

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

---

## 2. Concrete Test Cases

| Case ID | Feature / Condition | Input Data / Steps | Expected Output | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Command Loop: Invalid Command | Enter `bye` at main menu prompt. | Rejects with message: `Invalid command: "bye". Please enter 'add' (or 'a'), 'search' (or 's'), or 'quit' (or 'q'):` and re-prompts. | **PASS** |
| **TC-02** | Command Loop: Case & Alias | Enter `a`, then `ADD`, then `s`, then `q`. | All recognized as valid commands (`add`, `search`, `quit`). | **PASS** |
| **TC-03** | Time Validation: Non-leap Feb 29 | When adding activity, enter start time `2026/02/29 10:00`. | Rejects with message: `Invalid day (29): Month 2 has max 28 days. Try again:` and re-prompts. | **PASS** |
| **TC-04** | Time Validation: Leap Year Feb 29 | Enter start time `2024/02/29 10:00`. | Accepted as valid leap year timestamp. | **PASS** |
| **TC-05** | Time Validation: Month Day Overflow | Enter start time `2026/04/31 09:00` (April has 30 days). | Rejects with message: `Invalid day (31): Month 4 has max 30 days. Try again:` and re-prompts. | **PASS** |
| **TC-06** | Time Validation: Component Bounds | Enter start times `2026/13/01 10:00`, `2026/05/10 24:00`, `2026/05/10 12:60`, `0/01/01 10:00`. | Each rejected with specific feedback identifying the out-of-range field (month, hour, minute, year). | **PASS** |
| **TC-07** | Chronological Validation | Start time: `2026/10/15 14:00`<br>End time: `2026/10/15 12:00`. | Rejects with message: `Ending time has to be after the starting time (2026/10/15 14:00). Try again:` | **PASS** |
| **TC-08** | Defensive Title & Location | Enter blank title on add; enter blank location for OtherActivity. | Rejects blanks: `Title cannot be empty. Try again:`, `Location cannot be empty for Other activities. Try again:` | **PASS** |
| **TC-09** | Add Activity: Complete Flow | Add `SchoolActivity`:<br>Type: `school`<br>Title: `CIS 2430 Lab Office Hours`<br>Start: `2026/10/20 14:30`<br>End: `2026/10/20 16:30`<br>Comment: `Y` $\rightarrow$ `Ask TA about search` | Output: `Successfully created the new activity:` followed by formatted activity details. | **PASS** |
| **TC-10** | Search: Element at Beginning | Search type `home`, keywords `Co-Op`, time period `[Enter]`. | Finds `1. [Home] Co-Op Interview (2026/10/04 14:00 - 2026/10/04 15:00) (For Software Developer)`. | **PASS** |
| **TC-11** | Search: Element in Middle | Search type `home`, keywords `Cook Meal`, time period `[Enter]`. | Finds `1. [Home] Cook Meal Prep (2026/10/07 18:00 - 2026/10/07 19:30) (Prep for the rest of the week)`. | **PASS** |
| **TC-12** | Search: Element at End | Search type `home`, keywords `Bike Ride`, time period `[Enter]`. | Finds `1. [Home] Bike Ride (2026/10/10 10:00 - 2026/10/10 11:00)`. | **PASS** |
| **TC-13** | Search: Element Not on List | Search type `home`, keywords `Nonexistent Activity`, time period `[Enter]`. | Displays message: `No activities matched your search request.` | **PASS** |
| **TC-14** | Keyword Word-Level Exactness | Search title `Buy Programming in Java` with keyword `Program`. | Zero matches: `Program` does not match word `Programming`. | **PASS** |
| **TC-15** | Keyword Case & Order Invariance | Search keywords `java PROGRAMMING` against title `Programming in Java`. | Matched successfully: case ignored, order independent. | **PASS** |
| **TC-16** | Time Period: Bounded Range | Search range `2026/10/04 00:00 - 2026/10/06 12:00` in `home`. | Matches only 3 activities occurring within that window (`Co-Op Interview`, `Groceries`, `Laundry`). | **PASS** |
| **TC-17** | Time Period: Open End | Search range `2026/10/08 00:00 -` in `home`. | Matches 3 activities starting at or after Oct 8 (`Clean Apartment`, `Call Parents`, `Bike Ride`). | **PASS** |
| **TC-18** | Time Period: Open Start | Search range `- 2026/10/05 12:00` in `home`. | Matches 2 activities ending at or before Oct 5 12:00 (`Co-Op Interview`, `Groceries`). | **PASS** |
| **TC-19** | Time Period: Invalid Inverted Range | Search range `2026/10/20 12:00 - 2026/10/10 12:00`. | Rejects: `Starting time must be before ending time. Try again:` and re-prompts. | **PASS** |
| **TC-20** | Search: No Filters (Match All) | Type `[Enter]`, Keywords `[Enter]`, Time `[Enter]`. | Returns all 23 activities sequentially across Home, School, and Other categories. | **PASS** |
| **TC-21** | Array Capacity Check | Array at capacity (`length == capacity`). | Rejects add request immediately: `You're pretty busy... the specified array is full, sorry!` | **PASS** |
| **TC-22** | Persistence Verification | Add activity, quit program, relaunch program and search for added item. | Activity loaded from disk and found in search results. | **PASS** |

# Future Improvements

If I had additional time to extend the project further, I would implement:
1. **Dynamic Collections (`ArrayList`)**: Transition from fixed arrays to `ArrayList<Activity>` to eliminate arbitrary capacity caps and support dynamic resizing and natural sorting.
2. **Graphical User Interface (GUI)**: Implement a JavaFX or Swing desktop application presenting a weekly/monthly timetable with drag-and-drop scheduling.
3. **Collision / Overlap Detection**: Warn the user when an activity's time range conflicts with an existing activity in any category.
4. **Keyword Indexing**: Build an inverted index (`HashMap<String, ArrayList<Integer>>`) mapping keywords directly to activity indices for sub-millisecond search lookups.
5. **Recurring Activities**: Support recurrence rules (e.g., weekly lectures or daily gym sessions) without manual repetition.