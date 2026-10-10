package dayplanner;


/**
 * Superclass for all types of Activities.
 * Contains all shared attributes and methods.
 * Activities must contain titles, start and end times. Optionally, activities can contain comments.
 */
public class Activity implements Comparable<Activity> {
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

        if (comment != null)
            this.comment = comment;
    }

    /**
     * Instantiates a new <code>Activity</code> from RAS format.
     * @param ras String of valid RAS format from which to populate fields.
     */
    public Activity(String ras) {
        // default values in case of ras error
        this("raserror", new Time(), new Time());

        // remove angle brackets and tokenize
        ras = ras.substring(1, ras.length() - 1);
        String[] tokens = ras.split(",");
        
        // tokens[0] is the type tag (e.g. "HomeActivity") — skip it here
        if (tokens.length >= 2 && tokens[1] != null && !tokens[1].isEmpty())
            this.title = tokens[1];
        if (tokens.length >= 3 && tokens[2] != null && !tokens[2].isEmpty())
            this.startTime = new Time(tokens[2]);
        if (tokens.length >= 4 && tokens[3] != null && !tokens[3].isEmpty())
            this.endTime = new Time(tokens[3]);
        if (tokens.length >= 5 && tokens[4] != null && !tokens[4].isEmpty() && !tokens[4].equals("NIL"))
            this.comment = tokens[4];
    }



    /** Get this <code>Activity</code>'s <b>field</b>.
     * @return This <code>Activity</code>'s <b>title</b> field as a <code>String</code>.
     */
    public String getTitle() {
        return this.title;
    }
    
    /** Get this <code>Activity</code>'s <b>field</b>.
     * @return This <code>Activity</code>'s <b>start time</b> field as a <code>Time</code>.
     */
    public Time getStartTime() {
        return this.startTime;
    }
    
    /** Get this <code>Activity</code>'s <b>field</b>.
     * @return This <code>Activity</code>'s <b>end time</b> field as a <code>Time</code>.
     */
    public Time getEndTime() {
        return this.endTime;
    }
    
    /** Get this <code>Activity</code>'s <b>field</b>.
     * @return This <code>Activity</code>'s <b>comment</b> field as a <code>String</code>. If no comment is defined, return <code>null</code>.
     */
    public String getComment() {
        return this.comment;
    }



    /**
     * Set this <code>Activity</code>'s <b>title</b>.
     * @param title The new title. If it is <code>null</code>, do nothing.
     */
    public void setTitle(String title) {
        if (title != null)
            this.title = title;
    }

    /**
     * Set this <code>Activity</code>'s <b>start time</b>.
     * @param startTime The new start time. If it is <code>null</code>, do nothing.
     */
    public void setStartTime(Time startTime) {
        if (startTime != null)
            this.startTime = startTime;
    }

    /**
     * Set this <code>Activity</code>'s <b>end time</b>.
     * @param endTime The new end time. If it is <code>null</code>, do nothing.
     */
    public void setEndTime(Time endTime) {
        if (endTime != null)
            this.endTime = endTime;
    }

    /**
     * Set this <code>Activity</code>'s <b>comment</b>.
     * @param comment The new comment. If it is <code>null</code>, do nothing.
     */
    public void setComment(String comment) {
        if (comment != null)
            this.comment = comment;
    }



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

    /**
     * Determine of two <code>Activity</code> instances are equal.
     * @return Whether or not this <code>Activity</code> equals <code>other</code>.
     * @param other The other <code>Activity</code> to compare with.
     */
    public boolean equals(Activity other) {
        // compare the class first
        // this is necessary because HomeActivity etc. are subclasses of this one
        // and comparing activities of different classes, i.e. HomeActivity and SchoolActivity
        // they should always be treated as unequal
        if (other == null || getClass() != other.getClass())
            return false;

        // compare title
        if (getTitle() == null ? other.getTitle() != null : !getTitle().equals(other.getTitle()))
            return false;

        // compare start time
        if (getStartTime() == null ? other.getStartTime() != null : !getStartTime().equals(other.getStartTime()))
            return false;

        // compare end time
        if (getEndTime() == null ? other.getEndTime() != null : !getEndTime().equals(other.getEndTime()))
            return false;
        
        // if we get here the only difference should be the comment
        String c1 = getComment();
        String c2 = other.getComment();
        if (c1 == null)
            return c2 == null;
        else
            return c1.equals(c2);
    }

    /**
     * Determine if the instance is equal to the other object.
     * @return Whether or not this <code>Activity</code> equals <code>other</code>.
     * @param other The object to compare with.
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
     * Compare two <code>Activity</code> instances by starting time.
     * @return An integer indicating the relationship between the two instances: A value of <code>0</code> indicates they start at the same time; a negative value indicates the calling <code>Activity</code> <b>precedes</b> the argument; and a positive value indicates the calling <code>Activity</code> <b>follows</b> the argument.
     * @param other The other <code>Activity</code> to compare with.
     */
    @Override
    public int compareTo(Activity other) {
        // since we are comparing based on time, which has its own compareTo method
        // we can just piggyback off of that method
        return getStartTime().compareTo(other.getStartTime());
    }
}
