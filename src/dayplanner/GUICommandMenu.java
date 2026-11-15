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
     * @param gdp        Refers to the Swing GUI window.
     * @param activities Instantiated ActivityList.
     */
    public GUICommandMenu(GUIDayPlanner gdp, ActivityList activities) {
        super(OPTIONS);

        addActionListener(e -> {
            int index = getSelectedIndex();
            if (index <= 0)
                return;

            MenuOption option = MenuOption.parse(index);
            if (option == null)
                return;
            
            // Execute the desired task.
            switch (option) {
                case ADD:
                    new GUIAddWindow(activities);
                    break;
                case SEARCH:
                    new GUISearchWindow(activities);
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
