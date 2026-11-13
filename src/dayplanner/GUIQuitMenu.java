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
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Represents the Swing GUI quit menu..
 */
public class GUIQuitMenu extends JFrame {
    private static final Dimension DIMS = new Dimension(450, 250);
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
        // Warning
        // ==============================

        String warningText = "<html>"
            + "Are you sure you want to exit DayPlanner?<br>"
            + "This will save your list to disk."
            + "</html>";

        JLabel warningLabel = new JLabel(warningText);
        warningLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        warningLabel.setIconTextGap(24);
        warningLabel.setBorder(PADDING);

        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setBorder(PADDING);
        labelPanel.add(warningLabel);
        add(labelPanel);


        // ==============================
        // Buttons
        // ==============================

        ActionListener buttonEvent = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                if (event.getActionCommand().equals("CLOSE"))
                    gdp.quit();
            }
        };
        
        JButton closeButton = new JButton("YES - Save and exit");
        closeButton.setActionCommand("CLOSE");
        closeButton.addActionListener(buttonEvent);

        JButton cancelButton = new JButton("NO - Do not exit");
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(buttonEvent);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(PADDING);
        buttonPanel.add(closeButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);


        // ==============================
        // End
        // ==============================

        setVisible(true);
    }
}