package dayplanner;


/**
 * Represents an activity that takes place at a miscellaneous, user-defined location.
 */
public class OtherActivity extends Activity {
    private String location;    // Required



    /**
     * Instantiates a new <code>OtherActivity</code> with a title, start and end time, and location.
     * @param title The new <code>OtherActivity</code>'s title.
     * @param startTime The new <code>OtherActivity</code>'s start time.
     * @param endTime The new <code>OtherActivity</code>'s end time.
     * @param location The new <code>OtherActivity</code>'s location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String location) {
        super(title, startTime, endTime);
        this.location = location;
    }



    /**
     * Instantiates a new <code>OtherActivity</code> with a title, start and end time, comment, and location.
     * @param title The new <code>OtherActivity</code>'s title.
     * @param startTime The new <code>OtherActivity</code>'s start time.
     * @param endTime The new <code>OtherActivity</code>'s end time.
     * @param comment The new <code>OtherActivity</code>'s comment.
     * @param location The new <code>OtherActivity</code>'s location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String comment, String location) {
        super(title, startTime, endTime, comment);
        this.location = location;
    }

    /**
     * Instantiates a new <code>OtherActivity</code> from RAS format.
     * @param ras String of valid RAS format from which to populate fields.
     */
    public OtherActivity(String ras) {
        super(ras);
        System.out.println("WOOP!");
        // remove angle brackets and tokenize
        ras = ras.substring(1, ras.length() - 1);
        String[] tokens = ras.split(",");
        
        if (tokens.length >= 4 && tokens[4] != null && !tokens[4].isEmpty() && tokens[4] != "NIL")
            this.location = tokens[4];
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

    /**
     * Encodes this instance into <code>RAS</code> <b>(Readable Activity Serial)</b>, a text format that allows it to be written to text files.
     * <code>RAS</code> entries are surrounded by angle brackets, and split into columns separated by commas:
     * <ul>
     * <li>A <code>String</code> denoting the activity type.</li>
     * <li>A <code>String</code> denoting the activity title.</li>
     * <li><code>String</code> representation of the start and end times.</li>
     * <li>A <code>String</code> denoting the comment, or <code>NIL</code> if there is no comment.</li>
     * <li>For OtherActivities, a <code>String</code> denoting the location.</li>
     * </ul>
     * For more details, see <code>ActivityList.toRAS()</code> or <code>README.md</code>.
     * @return A <code>RAS</code> representation of this <code>Activity</code>.
     */
    public String toRAS() {
        String ret = super.toRAS();
        ret = ret.replaceAll("[<>]","");

        ret += ",";
        ret += getComment();

        ret = "<" + ret + ">";
        return ret;
    }
}
