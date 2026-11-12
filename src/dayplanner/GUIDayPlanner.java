package dayplanner;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Represents the Swing GUI extension.
 */
public class GUIDayPlanner extends JFrame {
    private static final Dimension DIMS = new Dimension(600, 200);
	private static final String TITLE = "DayPlanner";
	private static final int CLOSE = JFrame.EXIT_ON_CLOSE;
	private static final Color BACKGROUND = new Color(220, 220, 220);
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);


    /**
     * Create a new Swing GUI window.
     * @param activities Defined ActivityList.
     */
    public GUIDayPlanner(ActivityList activities) {
        // ==============================
        // Window Setup
        // ==============================

        super();
        setSize(DIMS);
        setTitle(TITLE);
        setDefaultCloseOperation(CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));


        // ==============================
        // Contents
        // ==============================

        JLabel welcomeText = new JLabel();
        welcomeText.setBorder(PADDING);
        refreshWelcomeText(welcomeText, activities);

        GUICommandMenu dropdown = new GUICommandMenu(this);
        dropdown.addActionListener(e -> {
            refreshWelcomeText(welcomeText, activities);
        });

        add(dropdown);
        add(welcomeText);
        

        // ==============================
        // End
        // ==============================

        setVisible(true);
    }

    /**
     * Saves the list and exits the GUI.
     */
    public void quit() {
        DayPlanner.quit();
        dispose();
        System.exit(0);
    }

    /**
     * Refresh the welcome text label.
     * 
     * @param welcomeText The welcome text label.
     * @param activities  The list of activities.
     */
    public void refreshWelcomeText(JLabel welcomeText, ActivityList activities) {
        welcomeText.setText(String.format("<html>%s\n%s</html>",
            DayPlannerUtils.getGreeting(activities.size()),
            "Choose a command from the menu to the left.")
                .replaceAll("\n", "<br>"));
    }
}