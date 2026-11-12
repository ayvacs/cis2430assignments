package dayplanner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Represents the Swing GUI quit menu..
 */
public class GUIQuitMenu extends JFrame {
    private static final Dimension DIMS = new Dimension(300, 150);
	private static final String TITLE = "Exit DayPlanner";
	private static final int CLOSE = JFrame.DO_NOTHING_ON_CLOSE;
	private static final Color BACKGROUND = new Color(222, 153, 153);
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);


    /**
     * Create a new Swing GUI quit menu.
     * 
     * @param gdp Refers to the Swing GUI menu.
     */
    public GUIQuitMenu(GUIDayPlanner gdp) {
        // ==============================
        // Window Setup
        // ==============================

        super();
        setSize(DIMS);
        setTitle(TITLE);
        setDefaultCloseOperation(CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        // ==============================
        // Warning Text
        // ==============================

        JLabel warningText = new JLabel();
        warningText.setText("<html>"
            + "Are you sure you want to exit DayPlanner?<br>"
            + "This will save your list to disk.");
        warningText.setBorder(PADDING);
        add(warningText);


        // ==============================
        // Buttons
        // ==============================
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                gdp.quit();
            }
        });
        add(closeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancelButton);


        // ==============================
        // End
        // ==============================

        setVisible(true);
    }
}