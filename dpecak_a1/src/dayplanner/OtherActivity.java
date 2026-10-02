package dayplanner;


public class OtherActivity extends Activity {
    private String location;

    public OtherActivity(String title, Time startTime, Time endTime, String location) {
        super(title, startTime, endTime);
        this.location = location;
    }

    public OtherActivity(String title, Time startTime, Time endTime, String location, String comment) {
        super(title, startTime, endTime, comment);
        this.location = location;
    }
}
