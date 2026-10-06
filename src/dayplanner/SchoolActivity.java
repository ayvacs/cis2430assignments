package dayplanner;


/**
 * Represents an activity that takes place at school.
 */
public class SchoolActivity extends Activity {
    /**
     * Instantiates a new <code>SchoolActivity</code> with a title and start and end time.
     * @param title The new <code>SchoolActivity</code>'s title.
     * @param startTime The new <code>SchoolActivity</code>'s start time.
     * @param endTime The new <code>SchoolActivity</code>'s end time.
     */
    public SchoolActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }



    /**
     * Instantiates a new <code>SchoolActivity</code> with a title, start and end time, and comment.
     * @param title The new <code>SchoolActivity</code>'s title.
     * @param startTime The new <code>SchoolActivity</code>'s start time.
     * @param endTime The new <code>SchoolActivity</code>'s end time.
     * @param comment The new <code>SchoolActivity</code>'s comment.
     */
    public SchoolActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
