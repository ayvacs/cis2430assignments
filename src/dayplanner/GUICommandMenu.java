package dayplanner;

import javax.swing.JComboBox;

/**
 * Represents the Swing GUI combo box to select a command.
 */
public class GUICommandMenu extends JComboBox<String> {
    private static final String[] OPTIONS = {
        "< Select a Command >",
        "Add - Insert a new activity",
        "Search - Search for an activity",
        "Quit - Exit DayPlanner" };


    /**
     * Create a new Swing GUI picker.
     * 
     * @param gdp Refers to the Swing GUI window.
     */
    public GUICommandMenu(GUIDayPlanner gdp) {
        super(OPTIONS);

        addActionListener(e -> {
            MenuOption option = MenuOption.parse(getSelectedIndex());
            
            // Execute the desired task.
            switch (option) {
                case ADD:
                    break;
                case SEARCH:
                    break;
                case QUIT:
                    new GUIQuitMenu(gdp);
                    break;
                default:
                    break;
            }

            // Immediately show the "Select Command" item.
            setSelectedIndex(0);
        });
    }
}