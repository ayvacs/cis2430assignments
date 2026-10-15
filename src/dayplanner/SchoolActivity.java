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
     */
    public SchoolActivity(String title, Time startTime, Time endTime) {
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
     */
    public SchoolActivity(String title, Time startTime, Time endTime, String comment) {
        super(title, startTime, endTime, comment);
    }

    /**
     * Constructs a <code>SchoolActivity</code> from a RAS (Readable Activity
     * Serial) string.
     * 
     * @param ras Serialized activity entry, normally produced by {@link #toRAS()}.
     */
    public SchoolActivity(String ras) {
        super(ras);
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
