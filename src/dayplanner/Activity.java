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

        if (comment != null)
            this.comment = comment;
    }

    /**
     * Instantiates a new <code>Activity</code> from RAS format.
     */
    public Activity(String ras) {
        // default values in case of ras error
        this("raserror", new Time(), new Time());

        // remove angle brackets and tokenize
        ras = ras.substring(1, ras.length() - 1);
        String[] tokens = ras.split(",");
        
        // string at index 0 is the classname and can be ignored
        if (tokens.length >= 1 && tokens[1] != null && !tokens[1].isEmpty())
            this.title = tokens[1];
        if (tokens.length >= 2 && tokens[2] != null && !tokens[2].isEmpty())
            this.startTime = new Time(tokens[2]);
        if (tokens.length >= 3 && tokens[3] != null && !tokens[3].isEmpty())
            this.endTime = new Time(tokens[3]);
        if (tokens.length >= 4 && tokens[4] != null && !tokens[4].isEmpty() && tokens[3] != "NIL")
            this.comment = tokens[4];
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
     * Encodes this instance into <code>RAS</code> <b>(Readable Activity Serial)</b>, a text format that allows it to be written to text files.
     * <code>RAS</code> entries are surrounded by angle brackets, and split into columns separated by commas:
     * <ul>
     * <li>A <code>String</code> denoting the activity type.</li>
     * <li>A <code>String</code> denoting the activity title.</li>
     * <li><code>String</code> representation of the start and end times.</li>
     * <li>A <code>String</code> denoting the comment, or <code>NIL</code> if there is no comment.</li>
     * </ul>
     * For more details, see <code>ActivityList.toRAS()</code> or <code>README.md</code>.
     * @return A <code>RAS</code> representation of this <code>Activity</code>.
     */
    public String toRAS() {
        String comment = getComment();
        if (comment == null)
            comment = "NIL";

        return String.format("<%s,%s,%s,%s,%s>",
            getClass().getName(),
            getTitle(),
            getStartTime().toString(),
            getEndTime().toString(),
            comment);
    }

    /**
     * @return Whether or not this <code>Activity</code> equals <code>other</code>.
     * @param other The other <code>Activity</code> to compare with.
     */
    public boolean equals(Activity other) {
        if (!getTitle().equals(other.getTitle())
            || !getStartTime().equals(other.getStartTime())
            || !getEndTime().equals(other.getEndTime()))
            return false;
        
        String c1 = getComment();
        String c2 = other.getComment();

        return c1 != null && c2 != null && c1.equals(c2);
    }
}
