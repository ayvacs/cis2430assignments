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
        // remove angle brackets and tokenize
        ras = ras.substring(1, ras.length() - 1);
        String[] tokens = ras.split(",");

        // tokens[0]=type, [1]=title, [2]=start, [3]=end, [4]=comment, [5]=location
        if (tokens.length >= 6 && tokens[5] != null && !tokens[5].isEmpty() && !tokens[5].equals("NIL"))
            this.location = tokens[5];
    }



    /**
     * Get this <code>OtherActivity</code>'s <b>location</b> field
     * @return This <code>OtherActivity</code>'s <b>location</b> field as a <code>String</code>.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Set this <code>OtherActivity</code>'s <b>location</b>.
     * @param location The new location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Compare two OtherActivities.
     * @return Whether or not this <code>OtherActivity</code> equals <code>other</code>.
     * @param other The other <code>OtherActivity</code> to compare with.
     */
    public boolean equals(OtherActivity other) {
        if (other == null)
            return false;
        if (!super.equals(other))
            return false;
        if (getLocation() == null)
            return other.getLocation() == null;
        return getLocation().equals(other.getLocation());
    }

    /**
     * Compare this OtherActivity with another Object.
     * @return Whether or not this <code>OtherActivity</code> equals <code>other</code>.
     * @param other The object to compare with.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return equals((OtherActivity) other);
    }



    /**
     * Get a <code>String</code> representation.
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
        // super.toRAS() gives us <OtherActivity,title,start,end,comment>
        // strip the brackets and append the location field then re-close the brackets.
        String ret = super.toRAS();
        ret = ret.substring(0, ret.length() - 1);

        ret += "," + getLocation() + ">";
        return ret;
    }
}
