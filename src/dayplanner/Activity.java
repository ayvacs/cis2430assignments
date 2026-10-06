package dayplanner;


/**
 * Superclass for all types of Activities.
 * Contains all shared attributes and methods.
 * Activities must contain titles, start and end times. Optionally, activities can contain comments.
 */
public class Activity {
    private String title;   // Required
    private Time startTime; // Required
    private Time endTime;   // Required
    private String comment; // Optional



    /**
     * Instantiates a new <code>Activity</code> with a title and start and end time.
     * @param title The new <code>Activity</code>'s title.
     * @param startTime The new <code>Activity</code>'s start time.
     * @param endTime The new <code>Activity</code>'s end time.
     */
    public Activity(String title, Time startTime, Time endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }



    /**
     * Instantiates a new <code>Activity</code> with a title, start and end time, and comment.
     * @param title The new <code>Activity</code>'s title.
     * @param startTime The new <code>Activity</code>'s start time.
     * @param endTime The new <code>Activity</code>'s end time.
     * @param comment The new <code>Activity</code>'s comment.
     */
    public Activity(String title, Time startTime, Time endTime, String comment) {
        this(title, startTime, endTime);
        this.comment = comment;
    }



    /** @return This <code>Activity</code>'s <b>title</b> field as a <code>String</code>. */
    public String getTitle() { return this.title; }
    
    /** @return This <code>Activity</code>'s <b>start time</b> field as a <code>Time</code>. */
    public Time getStartTime() { return this.startTime; }
    
    /** @return This <code>Activity</code>'s <b>end time</b> field as a <code>Time</code>. */
    public Time getEndTime() { return this.endTime; }
    
    /** @return This <code>Activity</code>'s <b>comment</b> field as a <code>String</code>. If no comment is defined, return <code>null</code>.*/
    public String getComment() { return this.comment; }



    /**
     * @return A <code>String</code> representation of this <code>Activity</code>.
     */
    public String toString() {
        String ret = String.format("%s (%s - %s)",
            getTitle(),
            getStartTime().toString(),
            getEndTime().toString());

        String com = getComment();
        if (com != null)
            ret = ret + " (" + com + ")";

        return ret;
    }

    /**
     * @return Whether or not this <code>Activity</code> equals <code>other</code>.
     * @param other The other <code>Activity</code> to compare with.
     */
    public boolean equals(Activity other) {
        return toString().equals(other.toString());
    }
}
