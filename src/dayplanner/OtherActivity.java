package dayplanner;


/**
 * <p>
 * Represents an activity outside the home and school categories, with an
 * associated location such as a clinic or gym. Common fields and behavior are
 * inherited from {@link Activity}.
 * </p>
 * <p>
 * Example:
 * </p>
 * <code>new OtherActivity("Appointment", start, end, "Clinic")</code>
 */
public class OtherActivity extends Activity {
    private String location;    // Required



    /*
     * Constructors
     */
    
    /**
     * Constructs an <code>OtherActivity</code> with a title, start time, end time,
     * and location.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param location  The activity location; this constructor does not reject null
     *                  or blank values, although the app requires a nonblank
     *                  location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String location) {
        super(title, startTime, endTime);
        setLocation(location);
    }

    /**
     * Constructs an <code>OtherActivity</code> with a title, start time, end time,
     * comment, and location.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param comment   The activity comment.
     * @param location  The activity location; this constructor does not reject null
     *                  or blank values, although the app requires a nonblank
     *                  location.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String comment, String location) {
        super(title, startTime, endTime, comment);
        setLocation(location);
    }

    /**
     * Constructs an <code>OtherActivity</code> from a RAS (Readable Activity
     * Serial) string.
     * 
     * @param ras Serialized entry.
     */
    public OtherActivity(String ras) {
        super(ras);
        setLocation(RasParser.location(ras));
    }



    /*
     * Accessors and Mutators
     */

    /**
     * Gets the location of this activity.
     * 
     * @return The activity location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the location of this activity.
     * 
     * @param location The new location.
     */
    public void setLocation(String location) {
        this.location = location;
    }



    /*
     * Object Overrides
     */

    /**
     * Determines whether this activity is equal to another
     * <code>OtherActivity</code>.
     * 
     * @param other The other <code>OtherActivity</code> to compare with.
     * @return <code>true</code> if both activities are equal, <code>false</code>
     *         otherwise.
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
     * Determines whether this activity is equal to another object.
     * 
     * @param other The object to compare with.
     * @return <code>true</code> if the objects are equal, <code>false</code>
     *         otherwise.
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
     * Returns a string representation of this activity.
     * 
     * @return A string representation of this activity.
     */
    @Override
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


    
    /*
     * Serialization
     */

    /**
     * Encodes this activity into RAS (Readable Activity Serial) format.
     * 
     * @return One-line RAS entry including the location after the inherited fields.
     */
    public String toRAS() {
        return RasParser.serialize(this);
    }
}
