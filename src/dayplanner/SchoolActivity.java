package dayplanner;


/**
 * <p>
 * Represents an activity categorized as taking place at school, such as a
 * lecture or lab. Common fields and behavior are inherited from
 * {@link Activity}.
 * </p>
 * <p>
 * Example:
 * </p>
 * <code>new SchoolActivity("CIS 2430 Lecture", start, end)</code>
 */
public class SchoolActivity extends Activity {
    /*
     * Constructors
     */

    /**
     * Constructs a <code>SchoolActivity</code> with a title, start time, and end
     * time.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public SchoolActivity(String title, Time startTime, Time endTime)
    throws IllegalArgumentException {
        super(title, startTime, endTime);
    }

    /**
     * Constructs a <code>SchoolActivity</code> with a title, start time, end time,
     * and comment.
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param comment   Optional note; null or blank values are treated as no
     *                  comment.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public SchoolActivity(String title, Time startTime, Time endTime, String comment)
    throws IllegalArgumentException {
        super(title, startTime, endTime, comment);
    }

    /**
     * Constructs a <code>SchoolActivity</code> from a RAS (Readable Activity
     * Serial) string.
     * 
     * @param ras Serialized activity entry, normally produced by {@link #toRAS()}.
     * @throws RasException when RAS input is invalid.
     */
    public SchoolActivity(String ras)
    throws RasException {
        super(ras);
    }



    /*
     * Activity Overrides
     */


    
    /**
     * Returns the {@link dayplanner.ActivityType} that corresponds to this
     * Activity.
     * 
     * @return Corresponding ActivityType. For SchoolActivities, this is
     *         <code>SCHOOL</code>.
     */
    @Override
    public ActivityType getType() {
        return ActivityType.SCHOOL;
    }

    

    /*
     * Object Overrides
     */

    /**
     * Determines whether this school activity is equal to another object.
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
