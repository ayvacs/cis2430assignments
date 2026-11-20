package dayplanner;


/**
 * <p>
 * Represents an activity categorized as taking place at home, such as a chore
 * or a study session. It inherits the common title, times, optional comment,
 * serialization, and comparison behavior from {@link Activity}.
 * </p>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * new HomeActivity("Laundry", start, end, "Wash bedding")
 * </pre>
 */
public class HomeActivity extends Activity {
    /**
     * Constructors
     */

    /**
     * Constructs a <code>HomeActivity</code> with a title, start time, and end
     * time.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public HomeActivity(String title, Time startTime, Time endTime)
    throws IllegalArgumentException {
        super(title, startTime, endTime);
    }

    /**
     * Constructs a <code>HomeActivity</code> with a title, start time, end time,
     * and comment.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param comment   Optional note associated with the activity; null or blank
     *                  values are treated as no comment by {@link Activity}.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public HomeActivity(String title, Time startTime, Time endTime, String comment)
    throws IllegalArgumentException {
        super(title, startTime, endTime, comment);
    }

    /**
     * Constructs a <code>HomeActivity</code> from a RAS (Readable Activity Serial)
     * string.
     * 
     * @param ras Serialized activity entry, normally produced by {@link #toRAS()}.
     * @throws RasException when RAS input is invalid.
     */
    public HomeActivity(String ras)
    throws RasException {
        super(ras);
    }

    /**
     * <p>
     * Copy constructor. Constructs a new <code>HomeActivity</code> that is a
     * duplicate of the given <code>HomeActivity</code>, preventing co-references
     * that could cause privacy leaks.
     * </p>
     *
     * @param other The <code>HomeActivity</code> to copy.
     * @throws IllegalArgumentException If {@code other} is {@code null}.
     */
    public HomeActivity(HomeActivity other)
    throws IllegalArgumentException {
        super(other);
    }



    /*
     * Activity Overrides
     */

    
    
    /**
     * Returns the {@link dayplanner.ActivityType} that corresponds to this Activity.
     * @return Corresponding ActivityType. For HomeActivities, this is <code>HOME</code>.
     */
    @Override
    public ActivityType getType() {
        return ActivityType.HOME;
    }

    

    /*
     * Object Overrides
     */
    
    /**
     * Determines whether this home activity is equal to another object.
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
        return super.equals(other);
    }
}
