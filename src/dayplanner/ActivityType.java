package dayplanner;



/**
 * <p>
 * Identifies one of the activity categories managed by the planner. Each value
 * has a numeric identifier and a display name.
 * </p>
 * <p>
 * The numeric identifiers for each type are as follows:
 * </p>
 * <ol>
 * <li>Home</li>
 * <li>School</li>
 * <li>Other</li>
 * </ol>
 * <p>
 * Use {@link #parse(String)} to convert a menu response, for example:
 * </p>
 * <code>ActivityType.parse("h") == ActivityType.HOME</code>
 */
public enum ActivityType {
    /**
     * Defined enums
     */

    /**
     * Activities categorized as taking place at home.
     */
    HOME(1, "Home"),
    /**
     * Activities categorized as taking place at school.
     */
    SCHOOL(2, "School"),
    /**
     * Activities categorized as taking place elsewhere and carrying a location. In
     * other words, a fallback type for activities that don't fit into any of the
     * other categories.
     */
    OTHER(3, "Other");



    private final int code;
    private final String displayName;



    /**
     * Define a new enum
     */
    private ActivityType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }



    /**
     * Returns the numeric menu choice associated with this category.
     * @return The choice number (see above for reference).
     */
    public int getCode() {
        return code;
    }



    /**
     * Returns the category name.
     * @return {@code Home}, {@code School}, or {@code Other}.
     */
    public String getDisplayName() {
        return displayName;
    }



    /**
     * <p>
     * Parses a category name, first-letter abbreviation, or numeric choice.
     * </p>
     * <p>
     * Matching ignores case and leading or trailing whitespace.
     * </p>
     * 
     * @param input User input such as {@code home}, {@code H}, or {@code 1}.
     * @return The matching category, or {@code null} when input is null or unknown.
     */
    public static ActivityType parse(String input) {
        if (input == null)
            return null;

        return switch (input.trim().toLowerCase()) {
            case "1", "home", "h" -> HOME;
            case "2", "school", "s" -> SCHOOL;
            case "3", "other", "o" -> OTHER;
            default -> null;
        };
    }
}
