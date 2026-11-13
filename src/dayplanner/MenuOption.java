package dayplanner;



/**
 * Commands accepted by the DayPlanner main menu.
 */
public enum MenuOption {
    /** Option 1 */
    ADD,
    /** Option 2 */
    SEARCH,
    /** Option 3 */
    QUIT;

    /**
     * Parses a string menu command or alias.
     *
     * @param input command entered by the user
     * @return matching option enum, or {@code null} if the command is unknown
     */
    public static MenuOption parse(String input) {
        if (input == null)
            return null;

        return switch (input.trim().toLowerCase()) {
            case "add", "a", "1", "insert" -> ADD;
            case "search", "s", "2", "find" -> SEARCH;
            case "quit", "q", "3" -> QUIT;
            default -> null;
        };
    }

    /**
     * Parses an integer menu command or alias.
     * 
     * @param input command entered by the user
     * @return matching option enum, or {@code null} if the command is unknown
     */
    public static MenuOption parse(int input) {
        return parse(String.valueOf(input));
    }
}
