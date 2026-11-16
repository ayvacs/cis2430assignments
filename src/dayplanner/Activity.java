package dayplanner;


/**
 * <p>
 * Represents a scheduled activity with a title, start time, end time, and
 * optional comment.
 * </p>
 * <p>
 * Serves as the base class for {@link HomeActivity}, {@link SchoolActivity},
 * and {@link OtherActivity}.
 * </p>
 * <p>
 * The class stores its {@link Time} references directly and enforces
 * that the title is non-null and non-blank, start and end times are non-null,
 * and the start time is strictly before the end time. Any violation of these
 * invariant conditions causes an {@link IllegalArgumentException} to be thrown.
 * For example:
 * </p>
 * <pre>
 * Time start = new Time(2026, 10, 20, 14, 30);
 * Time end = new Time(2026, 10, 20, 16, 30);
 * Activity lab = new SchoolActivity("CIS 2430 Lab", start, end, "Bring laptop");
 * </pre>
 */
public class Activity implements Comparable<Activity> {
    private String title;   // Required
    private Time startTime; // Required
    private Time endTime;   // Required
    private String comment; // Optional



    /*
     * Constructors
     */

    /**
     * Protected default constructor for serialization and subclass initialization.
     */
    protected Activity() {}
   
    /**
     * <p>
     * Constructs an <code>Activity</code> with a title, start time, and end time.
     * </p>
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public Activity(String title, Time startTime, Time endTime)
    throws IllegalArgumentException {
        // Input validation is managed by the setters.
        setTitle(title);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    /**
     * <p>
     * Constructs an <code>Activity</code> with a title, start time, end time, and
     * comment.
     * </p>
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param comment   The activity comment.
     * @throws IllegalArgumentException If title is null or blank, if startTime or
     *                                  endTime is null, or if startTime is not
     *                                  strictly before endTime.
     */
    public Activity(String title, Time startTime, Time endTime, String comment)
    throws IllegalArgumentException {
        // Input validation is managed by the setters.
        this(title, startTime, endTime);
        setComment(comment);
    }

    /**
     * <p>
     * Constructs an <code>Activity</code> from a RAS (Readable Activity Serial)
     * string. Tokens are parsed in the following order:
     * </p>
     * <ol>
     * <li>class name (ignored),</li>
     * <li>title,</li>
     * <li>start time,</li>
     * <li>end time,</li>
     * <li>comment (<code>NIL</code> is treated as no comment).</li>
     * </ol>
     * 
     * @param ras RAS entry, such as
     *            {@code <HomeActivity,Laundry,2026/10/20 09:00,2026/10/20 10:00,NIL>}.
     * @throws RasException when RAS input is invalid.
     */
    public Activity(String ras)
    throws RasException {
        RasParser.populate(this, ras);
    }



    /*
     * Accessors
     */
   
    /**
     * Gets the title of this activity.
     * 
     * @return The activity title.
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Gets the start time of this activity.
     * 
     * @return The activity start time.
     */
    public Time getStartTime() {
        return this.startTime;
    }
  
    /**
     * Gets the end time of this activity.
     * 
     * @return The activity end time.
     */
    public Time getEndTime() {
        return this.endTime;
    }
    
    /**
     * Gets the comment for this activity.
     * 
     * @return The activity comment, or <code>null</code> if none is defined.
     */
    public String getComment() {
        return this.comment;
    }



    /**
     * Returns the {@link dayplanner.ActivityType} that corresponds to this
     * Activity.
     * 
     * @return Corresponding ActivityType. For regular Activities, this is
     *         <code>null</code>.
     */
    public ActivityType getType() {
        return null;
    }



    /*
     * Mutators
     */
    
    /**
     * Sets the title of this activity.
     * 
     * @param title The new title.
     * @throws IllegalArgumentException If title is null or blank.
     */
    public void setTitle(String title)
    throws IllegalArgumentException {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be null or blank.");
        
        this.title = title;
    }

    /**
     * Sets the start time of this activity.
     * 
     * @param startTime The new start time.
     * @throws IllegalArgumentException If startTime is null, or if an endTime is
     *                                  already set and startTime is not strictly
     *                                  before endTime.
     */
    public void setStartTime(Time startTime)
    throws IllegalArgumentException {
        if (startTime == null)
            throw new IllegalArgumentException("Start time cannot be null.");
        if (this.endTime != null
            && startTime.compareTo(this.endTime) >= 0)
            throw new IllegalArgumentException("Start time (" + startTime + ") must be before end time (" + this.endTime + ").");
        
        this.startTime = startTime;
    }

    /**
     * Sets the end time of this activity.
     * 
     * @param endTime The new end time.
     * @throws IllegalArgumentException If endTime is null, or if a startTime is
     *                                  already set and endTime is not strictly after startTime.
     */
    public void setEndTime(Time endTime)
    throws IllegalArgumentException {
        if (endTime == null)
            throw new IllegalArgumentException("End time cannot be null.");
        if (this.startTime != null
            && this.startTime.compareTo(endTime) >= 0)
            throw new IllegalArgumentException("End time (" + endTime + ") must be after start time (" + this.startTime + ").");
        
        this.endTime = endTime;
    }

    /**
     * Sets the comment for this activity.
     * 
     * @param comment The new comment; null or blank values clear the comment.
     */
    public void setComment(String comment) {
        if (comment == null || comment.isBlank())
            this.comment = null;
        else
            this.comment = comment;
    }



    /*
     * Serialization
     */

    /**
     * <p>
     * Encodes this activity into RAS (Readable Activity Serial) format.
     * </p>
     * <p>
     * The RAS format is a comma-separated list wrapped in angle brackets, for
     * example:
     * </p>
     * 
     * <pre>
     * &lt;ClassName,title,YYYY/MM/DD HH:MM,YYYY/MM/DD HH:MM,comment&gt;
     * </pre>
     * <p>
     * If no comment is set, <code>NIL</code> is used as a placeholder. For example,
     * a <code>HomeActivity</code> titled "Morning Run" with no comment might
     * produce:
     * </p>
     * 
     * <pre>
     * &lt;HomeActivity,Morning Run,2026/01/01 07:00,2026/01/01 08:00,NIL&gt;
     * </pre>
     * 
     * @return A single-line RAS representation; commas in field values are not
     *         escaped.
     */
    public String toRAS() {
        return RasParser.serialize(this);
    }



    /*
     * Object Overrides
     */

    /**
     * Returns a string representation of this activity.
     * 
     * @return A string representation of this activity.
     */
    @Override
    public String toString() {
        String str = String.format("%s (%s - %s)",
            getTitle(),
            getStartTime().toString(),
            getEndTime().toString());

        String comment = getComment();
        if (comment != null && !comment.isBlank())
            str = str + " (" + comment + ")";

        return str;
    }

    /**
     * Determines whether this activity is equal to another activity.
     * 
     * @param other The other activity to compare with.
     * @return <code>true</code> if both activities are equal, <code>false</code>
     *         otherwise.
     */
    public boolean equals(Activity other) {
        /*
         * compare the class first. this is necessary because HomeActivity etc. are
         * subclasses of this one and when comparing activities of different classes
         * (i.e. HomeActivity and SchoolActivity), the two should always be treated as
         * unequal.
         */
        if (other == null || (getClass() != other.getClass()))
            return false;

        // compare title
        if (getTitle() == null
            ? other.getTitle() != null
            : !getTitle().equals(other.getTitle()))
            return false;

        // compare start time
        if (getStartTime() == null
            ? other.getStartTime() != null
            : !getStartTime().equals(other.getStartTime()))
            return false;

        // compare end time
        if (getEndTime() == null
            ? other.getEndTime() != null
            : !getEndTime().equals(other.getEndTime()))
            return false;
        
        // compare comment
        String c1 = getComment();
        String c2 = other.getComment();
        if (c1 == null || c1.isBlank())
            return c2 == null || c2.isBlank();
        return c1.equals(c2);
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

        return equals((Activity) other);
    }

    /**
     * Compares this activity with another activity based on starting time.
     * 
     * @param other The other activity to compare with.
     * @return A negative integer, zero, or a positive integer as this activity's
     *         start time precedes, equals, or follows the other activity's start
     *         time. Equal start times compare as zero even if other fields differ.
     * @throws NullPointerException If {@code other} is {@code null}.
     */
    @Override
    public int compareTo(Activity other)
    throws NullPointerException {
        if (other == null)
            throw new NullPointerException("Cannot compare Activity to null");

        /*
         * since we are comparing based on a Time,
         * which has its own compareTo method,
         * we can just use that method :)
         */
        return getStartTime().compareTo(
            other.getStartTime());
    }
}
