/**
 * <p>
 * This is where all the classes for the DayPlanner app live.
 * </p>
 * <p>
 * Full documentation is available in the <code>README.md</code> file and it is
 * necessary that you read it entirely.
 * </p>
 * <p>
 * To build and execute the program, run the following terminal commands in
 * order in the root directory:
 * </p>
 * 
 * <pre>
 * make jar
 * java -jar bin/DayPlanner.jar
 * </pre>
 *
 * <p>
 * The package models scheduled activities, collects them into lists by
 * category, accepts menu input, searches activities, and persists the lists
 * between runs. {@link dayplanner.DayPlanner} is the main application class.
 * {@link dayplanner.Activity} is the common activity model, with
 * {@link dayplanner.HomeActivity}, {@link dayplanner.SchoolActivity}, and
 * {@link dayplanner.OtherActivity} representing the specialized categories of
 * activities. Other activities include a location.
 * {@link dayplanner.ActivityList} stores and orders activities, while
 * {@link dayplanner.DayPlannerUtils} provides utility methods.
 * </p>
 *
 * <p>
 * {@link dayplanner.Time} represents a date and time to minute precision in
 * {@code YYYY/MM/DD HH:MM} form. It validates calendar fields, including month
 * lengths and leap years, and supports comparison.
 * </p>
 *
 * <p>
 * Activity data is persisted in custom text format called RAS (Readable
 * Activity Serial). The full RAS specialization is avaialble in the
 * <code>README.md</code>. In a RAS list, each activity is encoded on one line
 * with its category, title, start time, end time, optional comment, and any
 * category-specific fields. {@link dayplanner.FileUtils} and
 * {@link dayplanner.Input} are wrapper classes to handle text file access, and
 * console input respectively. The {@link dayplanner.Tester} class provides the
 * project's automated test cases.
 * </p>
 * <p>
 * Some cool special features of the program include:
 * </p>
 * <ul>
 * <li>The program remembers your name and persists it to file in addition to
 * your lists.</li>
 * <li>The program splits longer methods into utility methods in order to keep
 * the code as readable and maintainable as possible.</li>
 * <li>The program has an automatic makefile rule that builds a
 * <code>tar.gz</code> file for submission.</li>
 * <li>Every method in the program is fully documented regardless of being
 * public or private</li>
 * </ul>
 * @see dayplanner.DayPlanner
 * @see dayplanner.Tester
 */
package dayplanner;
