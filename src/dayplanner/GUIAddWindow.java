package dayplanner;

import java.awt.BorderLayout;
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
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * Represents the Swing GUI add window.
 */
public class GUIAddWindow extends JFrame {
    private static final Dimension DIMS = new Dimension(800, 400);
	private static final String TITLE = "DayPlanner - Add";
	private static final int CLOSE = JFrame.DISPOSE_ON_CLOSE;
	private static final Color BACKGROUND = new Color(250, 250, 250);
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);


    /**
     * Create a new Swing GUI add window.
     */
    public GUIAddWindow() {
        // ==============================
        // Window Setup
        // ==============================

        super();
        setSize(DIMS);
        setTitle(TITLE);
        setDefaultCloseOperation(CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));


        // ==============================
        // Contents
        // ==============================

        // ------------------------------
        // Center Panel
        // ------------------------------

        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Adding an activity:"));
        centerPanel.setBorder(PADDING);

        // ------------------------------
        // East Panel
        // ------------------------------

        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(PADDING);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        ActionListener buttonEvent = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               //System.out.println(event.getActionCommand());
            }
        };

        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("RESET");
        resetButton.addActionListener(buttonEvent);
        eastPanel.add(resetButton);

        JButton addButton = new JButton("Add");
        addButton.setActionCommand("ADD");
        addButton.addActionListener(buttonEvent);
        eastPanel.add(addButton);

        // ------------------------------
        // South Panel
        // ------------------------------

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBorder(PADDING);
        southPanel.add(new JLabel("Messages:"));

        JTextArea messages = new JTextArea();
        messages.setBackground(southPanel.getBackground());
        //messages.setText("Poop");
        southPanel.add(messages);

        

        // ==============================
        // End
        // ==============================

        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}