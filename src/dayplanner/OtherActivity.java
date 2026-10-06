package dayplanner;


/**
 * Represents an activity that takes place at a miscellaneous, user-defined location.
 */
public class OtherActivity extends Activity {
    private String location;    // Required



    /**
     * Instantiates a new <code>OtherActivity</code> with a title, start and end time, and location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String location) {
        super(title, startTime, endTime);
        this.location = location;
    }



    /**
     * Instantiates a new <code>OtherActivity</code> with a title, start and end time, comment, and location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String comment, String location) {
        super(title, startTime, endTime, comment);
        this.location = location;
    }



    /** @return This <code>OtherActivity</code>'s <b>location</b> field as a <code>String</code>. */
    public String getLocation() { return this.location; }



    /**
     * @return A <code>String</code> representation of this <code>OtherActivity</code>.
     */
    public String toString() {
        String ret = String.format("%s (%s - %s at %s)",
            getTitle(),
            getStartTime().toString(),
            getEndTime().toString(),
            getLocation());

        String com = getComment();
        if (com != null)
            ret = ret + " (" + com + ")";

        return ret;
    }
}
