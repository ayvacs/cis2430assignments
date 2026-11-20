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
    /**
     * The physical location where this activity takes place. Must be non-null and non-blank.
     */
    private String location;



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
     * @param location  The activity location.
     * @throws IllegalArgumentException If title or location is null or blank,
     *                                  if startTime or endTime is null, or if
     *                                  startTime is not strictly before endTime.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String location)
    throws IllegalArgumentException {
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
     * @param location  The activity location.
     * @throws IllegalArgumentException If title or location is null or blank,
     *                                  if startTime or endTime is null, or if
     *                                  startTime is not strictly before endTime.
     */
    public OtherActivity(String title, Time startTime, Time endTime, String comment, String location)
    throws IllegalArgumentException {
        super(title, startTime, endTime, comment);
        setLocation(location);
    }

    /**
     * Constructs an <code>OtherActivity</code> from a RAS (Readable Activity
     * Serial) string.
     * 
     * @param ras Serialized entry.
     * @throws RasException when RAS input is invalid.
     */
    public OtherActivity(String ras)
    throws RasException {
        super(ras);
    }

    /**
     * <p>
     * Copy constructor. Constructs a new <code>OtherActivity</code> that is a
     * duplicate of the given <code>OtherActivity</code>, preventing co-references
     * that could cause privacy leaks.
     * </p>
     *
     * @param other The <code>OtherActivity</code> to copy.
     * @throws IllegalArgumentException If {@code other} is {@code null}.
     */
    public OtherActivity(OtherActivity other)
    throws IllegalArgumentException {
        super(other);
        setLocation(other.getLocation()); // String is immutable; no copy needed
    }

    /**
     * Creates and returns a copy of this {@code OtherActivity}.
     * 
     * @return A duplicate copy of this OtherActivity.
     */
    @Override
    public Activity copy() {
        return new OtherActivity(this);
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
     * @throws IllegalArgumentException If location is null or blank.
     */
    public void setLocation(String location)
    throws IllegalArgumentException {
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be null or blank.");
        this.location = location;
    }



    /*
     * Activity Overrides
     */


    
    /**
     * Returns the {@link dayplanner.ActivityType} that corresponds to this
     * Activity.
     * 
     * @return Corresponding ActivityType. For OtherActivities, this is
     *         <code>OTHER</code>.
     */
    @Override
    public ActivityType getType() {
        return ActivityType.OTHER;
    }



    /*
     * Object Overrides
     */

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
        if (!super.equals(other))
            return false;
        OtherActivity that = (OtherActivity) other;
        if (getLocation() == null)
            return that.getLocation() == null;
        return getLocation().equals(that.getLocation());
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
