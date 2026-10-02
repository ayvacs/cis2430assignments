package dayplanner;


public class OtherActivity extends Activity {
    public OtherActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }

    public OtherActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
