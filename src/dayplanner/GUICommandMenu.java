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
            int selected = getSelectedIndex();
            if (selected == 0)
                return;
            
            MenuOption option = MenuOption.parse(String.valueOf(selected));

            System.out.println(option);

            if (option.equals(MenuOption.QUIT)) {
                new GUIQuitMenu(gdp);
            }
        });
    }
}