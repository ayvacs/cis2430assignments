package dayplanner;



/**
 * <p>
 * This class contains methods that parse and write RAS (Readable Activity
 * Serial) activity records. RAS is the custom text format that allows data to
 * be persisted to disk.
 * </p>
 * <p>
 * All methods of this class are static, so it is not necessary to instantiate
 * this class.
 * </p>
 * <p>
 * RAS records are comma-delimited and wrapped in angle brackets. Commas in
 * field values are not escaped. Commas and angle brackets are
 * sanitized/desanitized so they can be used in Activity fields.
 * </p>
 * <p>
 * For the full RAS specification, read the <code>README.md</code>
 * documentation.
 * </p>
 */
public final class RasParser {
    /**
     * Default constructor. Not used.
     */
    private RasParser() {}



    /**
     * <p>
     * Parses one RAS record and creates its corresponding concrete activity
     * ({@link HomeActivity}, {@link SchoolActivity}, or {@link OtherActivity}).
     * </p>
     * <p>
     * Unknown type names throw a {@link RasException}.
     * </p>
     *
     * @param ras Serialized activity record.
     * @return The activity represented by the record.
     * @throws RasException when RAS input is invalid.
     */
    public static Activity parse(String ras)
    throws RasException {
        // extract fields
        String[] fields = fields(ras);
        boolean other = fields[0].equals("OtherActivity");
        if (!other && !fields[0].equals("HomeActivity") && !fields[0].equals("SchoolActivity"))
            throw new RasException("unknown activity type: " + fields[0]);
        if (fields.length != (other ? 6 : 5))
            throw new RasException("expected " + (other ? 6 : 5) + " fields but found " + fields.length);
        if (fields[1].isBlank())
            throw new RasException("title is empty");

        Time sTime = Input.parseTime(desanitize(fields[2]));
        Time eTime = Input.parseTime(desanitize(fields[3]));
        if (sTime == null || eTime == null)
            throw new RasException("invalid start or end time");
        if (sTime.compareTo(eTime) >= 0)
            throw new RasException("start is not before end");

        String title = desanitize(fields[1]);
        String comment = optional(fields, 4);

        // instantiate the appropriate class
        try {
            return switch (fields[0]) {
                case "HomeActivity"
                    -> new HomeActivity(title, sTime, eTime, comment);
                case "SchoolActivity"
                    -> new SchoolActivity(title, sTime, eTime, comment);
                default -> {
                    if (fields[5].isBlank())
                        throw new RasException("location is empty");
                    yield new OtherActivity(title, sTime, eTime, comment, desanitize(fields[5]));
                }
            };
        } catch (IllegalArgumentException e) {
            throw new RasException("invariant violation in activity data: " + e.getMessage());
        }
    }

    /**
     * Writes one activity as a RAS record.
     *
     * @param activity Activity to serialize.
     * @return Its RAS representation.
     */
    public static String serialize(Activity activity) {
        // define fields
        String type = activity.getClass().getSimpleName();
        String title = activity.getTitle();
        String sTime = activity.getStartTime().toString();
        String eTime = activity.getEndTime().toString();
        String comment = activity.getComment();

        // sanitize all fields
        type = sanitize(type);
        title = sanitize(title);
        sTime = sanitize(sTime);
        eTime = sanitize(eTime);
        comment = sanitize(comment);

        String ras = String.format(
            "%s,%s,%s,%s,%s",
            type, title, sTime, eTime,
            (comment == null || comment.isBlank() ? "NIL" : comment));
        
        // add location field for Other Activities
        if (activity instanceof OtherActivity other) {
            String location = sanitize(other.getLocation());
            ras += "," + (location == null ? "NIL" : location);
        }
        
        // wrap in angled brackets
        return "<" + ras + ">";
    }

    /**
     * Returns the fields inside one RAS record. Missing fields are retained as
     * empty strings so the activity class can apply its existing defaults.
     *
     * @param ras Serialized activity record.
     * @return The record fields, including the activity type at index zero.
     * @throws RasException when RAS input is invalid.
     */
    public static String[] fields(String ras)
    throws RasException {
        if (ras == null || !ras.startsWith("<") || !ras.endsWith(">") || ras.length() < 2)
            throw new RasException("record must be wrapped in < >");
        String inner = ras.substring(1, ras.length() - 1);
        return inner.split(",", -1);
    }

    /**
     * Populate the fields of this Activity in accordance with the given RAS string.
     * 
     * @param activity The activity whose fields to populate.
     * @param ras      Serialized activity record.
     * @throws RasException when RAS input is invalid.
     */
    public static void populate(Activity activity, String ras)
    throws RasException {
        String[] f = fields(ras);
        boolean other = f[0].equals("OtherActivity");
        if (!other && !f[0].equals("HomeActivity") && !f[0].equals("SchoolActivity"))
            throw new RasException("unknown activity type: " + f[0]);
        if (f.length != (other ? 6 : 5))
            throw new RasException("expected " + (other ? 6 : 5) + " fields but found " + f.length);
        if (f[1].isBlank())
            throw new RasException("title is empty");

        Time start = Input.parseTime(desanitize(f[2]));
        Time end = Input.parseTime(desanitize(f[3]));
        if (start == null || end == null)
            throw new RasException("invalid start or end time");
        if (start.compareTo(end) >= 0)
            throw new RasException("start is not before end");

        String comment = optional(f, 4);

        try {
            if (activity != null) {
                activity.setTitle(desanitize(f[1]));
                activity.setStartTime(start);
                activity.setEndTime(end);
                activity.setComment(comment);
                if (other) {
                    if (f[5].isBlank())
                        throw new RasException("location is empty");
                    if (activity instanceof OtherActivity o)
                        o.setLocation(desanitize(f[5]));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RasException("invariant violation in activity data: " + e.getMessage());
        }
    }

    /**
     * Extract the location from a RAS string.
     * 
     * @param ras Serialized activity record.
     * @return Location content.
     * @throws RasException when RAS input is invalid.
     */
    public static String location(String ras)
    throws RasException {
        String[] fields = fields(ras);
        return fields.length >= 6 && !fields[5].isEmpty() && !fields[5].equals("NIL")
            ? desanitize(fields[5]) : null;
    }

    /**
     * Extract the time from a RAS string.
     * 
     * @param fields Array of fields.
     * @param index  Index where the time is located.
     * @return Time content.
     */
    public static Time time(String[] fields, int index) {
        if (index >= fields.length || fields[index].isEmpty())
            return new Time();
        return new Time(desanitize(fields[index]));
    }

    /**
     * Extract any required field at any index from a RAS string.
     * 
     * @param fields   Array of fields.
     * @param index    Index.
     * @param fallback Fallback value in case the value at index does not exist or
     *                 is empty.
     * @return Value content.
     */
    public static String value(String[] fields, int index, String fallback) {
        return index < fields.length && !fields[index].isEmpty()
            ? desanitize(fields[index])
            : fallback;
    }

    
    /**
     * Extract any optional field at any index from a RAS string.
     * 
     * @param fields Array of fields.
     * @param index  Index.
     * @return Value content if it exists, null otherwise.
     */
    public static String optional(String[] fields, int index) {
        if (index >= fields.length || fields[index].isEmpty()
         || fields[index].equals("NIL"))
            return null;
        return desanitize(fields[index]);
    }

    /**
     * Sanitize input for serialization using HTML codes.
     * HTML codes referenced from <a href=
     * "https://www.rapidtables.com/web/html/html-codes.html">RapidTables</a>.
     * 
     * @param input Input to sanitize.
     * @return Sanitized input, or <code>null</code> if input is null.
     * @see #desanitize(String)
     */
    public static String sanitize(String input) {
        if (input == null)
            return null;
        return input
            .replace(",", "&#44;")
            .replace("<", "&#60;")
            .replace(">", "&#62;");
    }

    /**
     * Desanitize input for serialization using HTML codes.
     * HTML codes referenced from <a href=
     * "https://www.rapidtables.com/web/html/html-codes.html">RapidTables</a>.
     * 
     * @param input Input to desanitize.
     * @return Desanitized input, or <code>null</code> if input is null.
     * @see #sanitize(String)
     */
    public static String desanitize(String input) {
        if (input == null)
            return null;
        return input
            .replace("&#62;", ">")
            .replace("&#60;", "<")
            .replace("&#44;", ",");
    }
}
