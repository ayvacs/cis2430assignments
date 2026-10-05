package dayplanner;


public class HomeActivity extends Activity {
    public HomeActivity(String title, Time startTime, Time endTime) {
        super(title, startTime, endTime);
    }

    public HomeActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }
}
