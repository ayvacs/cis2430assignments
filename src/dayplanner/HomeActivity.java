package dayplanner;


/**
 * Represents an activity that takes place at home.
 */
public class HomeActivity extends Activity {
    /**
     * Instantiates a new <code>HomeActivity</code> with a title and start and end time.
     */
    public HomeActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }



    /**
     * Instantiates a new <code>HomeActivity</code> with a title, start and end time, and comment.
     */
    public HomeActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
