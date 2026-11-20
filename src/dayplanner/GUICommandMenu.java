package dayplanner;

import javax.swing.JComboBox;

/**
 * Represents the Swing GUI combo box to select a command.
 * Main navigation is now handled by the menu bar in {@link GUIDayPlanner};
 * this class switches cards on the main window for backward compatibility.
 */
public class GUICommandMenu extends JComboBox<String> {
    /**
     * Menu options displayed in the command selection combo box.
     */
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
            
            // Execute the desired task by switching in-place cards on the main frame
            switch (option) {
                case ADD:
                    gdp.showAdd();
                    break;
                case SEARCH:
                    gdp.showSearch();
                    break;
                case QUIT:
                    gdp.showGuiQuitMenu();
                    break;
                default:
                    break;
            }

            // Immediately show the "Select Command" item.
            setSelectedIndex(0);
        });
    }
}
