package dayplanner;


public class Activity {
    String title;
    Time startTime;
    Time endTime;
    String comment;

    public Activity(String title, Time startTime, Time endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Activity(String title, Time startTime, Time endTime, String comment) {
        this(title, startTime, endTime);
        this.comment = comment;
    }
}
