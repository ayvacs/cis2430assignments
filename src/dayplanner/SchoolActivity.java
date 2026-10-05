package dayplanner;


public class SchoolActivity extends Activity {
    public SchoolActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }

    public SchoolActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
