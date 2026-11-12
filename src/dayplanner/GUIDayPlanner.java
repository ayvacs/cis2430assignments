package dayplanner;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Represents the Swing GUI extension.
 */
public class GUIDayPlanner extends JFrame {
    private static final Dimension DIMS = new Dimension(600, 200);
	private static final String TITLE = "DayPlanner";
	private static final int CLOSE = JFrame.EXIT_ON_CLOSE;
	private static final Color BACKGROUND = new Color(175, 175, 175);


    /**
     * Create a new Swing GUI window.
     */
    public GUIDayPlanner(ActivityList activities) {
        super();
        setSize(DIMS);
        setTitle(TITLE);
        setDefaultCloseOperation(CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setVisible(true);

        JLabel welcomeText = new JLabel();
        add(welcomeText);
        welcomeText.setText(("<html>"
            + DayPlannerUtils.getGreeting(activities.size())
            + "\nChoose a command from the menu above."
            + "</html>")
        .replaceAll("\n","<br>"));
    }
}