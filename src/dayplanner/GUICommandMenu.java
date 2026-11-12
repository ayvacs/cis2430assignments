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
     */
    public GUICommandMenu() {
        super(OPTIONS);

        addActionListener(e -> {
            int selected = getSelectedIndex();
            if (selected == 0)
                return;
            
            MenuOption option = MenuOption.parse(String.valueOf(selected));

            System.out.println(option);
        });
    }
}