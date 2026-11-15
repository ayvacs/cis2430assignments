package dayplanner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

/**
 * Represents the Swing GUI extension.
 */
public class GUIDayPlanner extends JFrame {
    private static final Dimension DIMS = new Dimension(600, 200);
	private static final String TITLE = "DayPlanner";
	private static final int CLOSE = JFrame.DO_NOTHING_ON_CLOSE;
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showGuiQuitMenu();
            }
        });


        // ==============================
        // Contents
        // ==============================

        JLabel welcomeText = new JLabel();
        welcomeText.setBorder(PADDING);
        refreshWelcomeText(welcomeText, activities);

        GUICommandMenu dropdown = new GUICommandMenu(this, activities);
        dropdown.setBorder(PADDING);
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
     * Saves the list and exits the GUI. Shows an error message if there was an
     * error saving the list.
     */
    public void quit() {
        boolean errorOccurred = DayPlanner.quit(false);

        String msg = errorOccurred
            ? "<html>There was an error saving the list.<br>Click \"OK\" to exit DayPlanner.</html>"
            : "<html>The list was saved successfully.<br>Now quitting DayPlanner. Goodbye!</html>";
        int mode = errorOccurred 
            ? JOptionPane.ERROR_MESSAGE
            : JOptionPane.INFORMATION_MESSAGE;

        JOptionPane.showMessageDialog(null, msg, null, mode);
        
        dispose();
        System.exit(0);
    }

    /**
     * Shows the GUI quit menu.
     */
    public void showGuiQuitMenu() {
        new GUIQuitMenu(this);
    }

    /**
     * Refresh the welcome text label.
     * 
     * @param welcomeText The welcome text label.
     * @param activities  The list of activities.
     */
    public void refreshWelcomeText(JLabel welcomeText, ActivityList activities) {
        String text = ("<html>"
                     + DayPlannerUtils.getGreeting(activities.size())
                     + "\n"
                     + "Choose a command from the menu to the left."
                     + "</html>").replaceAll("\n", "<br>");
                     
        welcomeText.setText(text);
    }
}