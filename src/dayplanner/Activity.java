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
 * The class stores its {@link Time} references directly and does not enforce
 * that the end follows the start; callers creating activities directly should
 * validate that rule themselves. For example:
 * </p>
 * 
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
     * <p>
     * Constructs an <code>Activity</code> with a title, start time, and end time.
     * </p>
     * <p>
     * Null or blank required values are ignored by their setters; construction
     * therefore does not throw for those values.
     * </p>
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     */
    public Activity(String title, Time startTime, Time endTime) {
        setTitle(title);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    /**
     * <p>
     * Constructs an <code>Activity</code> with a title, start time, end time, and
     * comment.
     * </p>
     * <p>
     * A null or blank comment is ignored, leaving the comment unset.
     * </p>
     * 
     * @param title     The activity title.
     * @param startTime The start time.
     * @param endTime   The end time.
     * @param comment   The activity comment.
     */
    public Activity(String title, Time startTime, Time endTime, String comment) {
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
     * <p>
     * Missing or empty fields retain defaults; malformed time fields may likewise
     * leave their defaults in place. This is a parser intended for data loading.
     * </p>
     * 
     * @param ras RAS entry, such as
     *            {@code <HomeActivity,Laundry,2026/10/20 09:00,2026/10/20 10:00,NIL>}.
     */
    public Activity(String ras) {
        // declare default values in case of ras error
        this("raserror", new Time(), new Time());

        // remove angle brackets and tokenize
        ras = ras.substring(1, ras.length() - 1);
        String[] tokens = ras.split(",");
        
        // tokens[0] is the type tag (e.g. "HomeActivity") — skip it here
        if (tokens.length >= 2 && tokens[1] != null && !tokens[1].isEmpty())
            setTitle(tokens[1]);
        if (tokens.length >= 3 && tokens[2] != null && !tokens[2].isEmpty())
            setStartTime(new Time(tokens[2]));
        if (tokens.length >= 4 && tokens[3] != null && !tokens[3].isEmpty())
            setEndTime(new Time(tokens[3]));
        if (tokens.length >= 5 && tokens[4] != null && !tokens[4].isEmpty() && !tokens[4].equals("NIL"))
            setComment(tokens[4]);
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



    /*
     * Mutators
     */
    
    /**
     * Sets the title of this activity.
     * 
     * @param title The new title; ignored if {@code null} or blank, leaving the old
     *              title unchanged.
     */
    public void setTitle(String title) {
        if (title != null && !title.isBlank())
            this.title = title;
    }

    /**
     * Sets the start time of this activity.
     * 
     * @param startTime The new start time; ignored if {@code null}. The reference
     *                  is stored directly.
     */
    public void setStartTime(Time startTime) {
        if (startTime != null)
            this.startTime = startTime;
    }

    /**
     * Sets the end time of this activity.
     * 
     * @param endTime The new end time; ignored if {@code null}; no ordering check
     *                is performed.
     */
    public void setEndTime(Time endTime) {
        if (endTime != null)
            this.endTime = endTime;
    }

    /**
     * Sets the comment for this activity.
     * 
     * @param comment The new comment; ignored if {@code null} or blank. A blank
     *                value does not clear an existing comment.
     */
    public void setComment(String comment) {
        if (comment != null && !comment.isBlank())
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
        String comment = getComment();
        if (comment == null)
            comment = "NIL";

        return String.format("<%s,%s,%s,%s,%s>",
            getClass().getSimpleName(),
            getTitle(),
            getStartTime().toString(),
            getEndTime().toString(),
            comment);
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
        // compare the class first
        // this is necessary because HomeActivity etc. are subclasses of this one
        // and when comparing activities of different classes (i.e. HomeActivity and SchoolActivity),
        // the two should always be treated as unequal
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
        
        // if we get here the only difference should be the comment
        String c1 = getComment();
        String c2 = other.getComment();
        if (c1 == null || c1.isBlank())
            return c2 == null || c2.isBlank();
        else
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
     */
    @Override
    public int compareTo(Activity other) {
        // since we are comparing based on a Time,
        // which has its own compareTo method,
        // we can just piggyback off of that method :)
        return getStartTime().compareTo(other.getStartTime());
    }
}
