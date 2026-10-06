package dayplanner;


/**
 * Represents an activity that takes place at school.
 */
public class SchoolActivity extends Activity {
    /**
     * Instantiates a new <code>SchoolActivity</code> with a title and start and end time.
     */
    public SchoolActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }



    /**
     * Instantiates a new <code>SchoolActivity</code> with a title, start and end time, and comment.
     */
    public SchoolActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
