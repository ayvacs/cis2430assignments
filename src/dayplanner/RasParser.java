package dayplanner;



/**
 * <p>
 * Parses and writes RAS (Readable Activity Serial) activity records.
 * </p>
 * <p>
 * RAS records are comma-delimited and wrapped in angle brackets. Commas in
 * field values are not escaped.
 * </p>
 * <p>
 * For more information, read the <code>README.md</code> documentation.
 * </p>
 */
public final class RasParser {
    /**
     * Default constructor. Not used.
     */
    private RasParser() {}



    /**
     * <p>
     * Parses one RAS record and creates its corresponding activity.
     * </p>
     * <p>
     * Unknown type names produce a plain {@link Activity}, preserving the
     * format's existing fallback behavior.
     * </p>
     *
     * @param ras Serialized activity record.
     * @return The activity represented by the record.
     */
    public static Activity parse(String ras) {
        // extract fields
        String[] fields = fields(ras);
        String title = value(fields, 1, "raserror");
        Time start = time(fields, 2);
        Time end = time(fields, 3);
        String comment = optional(fields, 4);

        // instantiate the appropriate class
        return switch (fields[0]) {
            case "HomeActivity"
                -> new HomeActivity(title, start, end, comment);
            case "SchoolActivity"
                -> new SchoolActivity(title, start, end, comment);
            case "OtherActivity"
                -> new OtherActivity(title, start, end, comment, optional(fields, 5));
            default
                -> new Activity(title, start, end, comment);
        };
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

        String ras = String.format(
            "%s,%s,%s,%s,%s",
            type, title, sTime, eTime,
            (comment == null || comment.isBlank() ? "NIL" : comment));
        
        // add location field for Other Activities
        if (activity instanceof OtherActivity other)
            ras += "," + other.getLocation();
        
        // wrap in angled brackets
        return "<" + ras + ">";
    }

    /**
     * Returns the fields inside one RAS record. Missing fields are retained as
     * empty strings so the activity class can apply its existing defaults.
     *
     * @param ras Serialized activity record.
     * @return The record fields, including the activity type at index zero.
     */
    public static String[] fields(String ras) {
        String inner = ras.substring(1, ras.length() - 1);
        return inner.split(",", -1);
    }

    /**
     * Populate the fields of this Activity in accordance with the given RAS string.
     * 
     * @param activity The activity whose fields to populate.
     * @param ras      Serialized activity record.
     */
    public static void populate(Activity activity, String ras) {
        String[] fields = fields(ras);
        if (fields.length >= 2 && !fields[1].isEmpty())
            activity.setTitle(fields[1]);
        if (fields.length >= 3 && !fields[2].isEmpty())
            activity.setStartTime(new Time(fields[2]));
        if (fields.length >= 4 && !fields[3].isEmpty())
            activity.setEndTime(new Time(fields[3]));
        if (fields.length >= 5 && !fields[4].isEmpty() && !fields[4].equals("NIL"))
            activity.setComment(fields[4]);
    }

    /**
     * Extract the location from a RAS string.
     * 
     * @param ras Serialized activity record.
     * @return Location content.
     */
    public static String location(String ras) {
        String[] fields = fields(ras);
        return fields.length >= 6 && !fields[5].isEmpty() && !fields[5].equals("NIL")
            ? fields[5] : null;
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
        return new Time(fields[index]);
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
            ? fields[index] : fallback;
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
        return fields[index];
    }
}
