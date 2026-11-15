package dayplanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Abstract superclass representing a standard DayPlanner GUI window (such as
 * Add or Search).
 */
public abstract class GUIWindow extends JFrame {
    private static final Dimension DIMS = new Dimension(800, 400);
    private static final int CLOSE = JFrame.DISPOSE_ON_CLOSE;
    private static final Color BACKGROUND = new Color(250, 250, 250);
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    /**
     * Reference to the activity list.
     */
    public ActivityList activities;

    /**
     * ArrayList that contains all of the JTextField instances in the center panel.
     */
    public ArrayList<JTextField> centerFields = new ArrayList<JTextField>();

    /**
     * Message box text area.
     */
    public JTextArea messages = null;

    /**
     * Scroll pane containing the message box.
     */
    public JScrollPane scrollPane = null;

    /**
     * The reset button.
     */
    public JButton resetButton = null;

    /**
     * The primary action button (e.g. Add or Search).
     */
    public JButton actionButton = null;

    /**
     * Creates and initializes a new Swing GUI window.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUIWindow(ActivityList activities) {
        super();
        this.activities = activities;

        setSize(DIMS);
        setTitle(getWindowTitle());
        setDefaultCloseOperation(CLOSE);
        getContentPane().setBackground(BACKGROUND);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createEastPanel(), BorderLayout.EAST);
        add(createSouthPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Creates the center form panel containing input fields.
     * 
     * @return Initialized JPanel for center region.
     */
    public JPanel createCenterPanel() {
        String[] labels = getFieldLabels();
        int rows = labels.length + 1; // +1 for the header row

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(rows, 2, 8, 8));
        centerPanel.setBorder(PADDING);
        centerPanel.add(new JLabel(getHeaderText()));
        centerPanel.add(Box.createGlue()); // skip this grid cell

        centerFields.clear();
        for (String labelText : labels) {
            centerPanel.add(new JLabel(labelText));
            JTextField tf = new JTextField();
            centerPanel.add(tf);
            centerFields.add(tf);
        }

        return centerPanel;
    }

    /**
     * Creates the east panel containing action buttons.
     * 
     * @return Initialized JPanel for east region.
     */
    public JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(PADDING);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        ActionListener buttonEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String action = event.getActionCommand();
                if ("RESET".equals(action)) {
                    resetFields();
                } else if (getActionButtonCommand().equals(action)) {
                    executeAction();
                }
            }
        };

        resetButton = new JButton("Reset");
        resetButton.setActionCommand("RESET");
        resetButton.addActionListener(buttonEvent);
        eastPanel.add(resetButton);

        actionButton = new JButton(getActionButtonLabel());
        actionButton.setActionCommand(getActionButtonCommand());
        actionButton.addActionListener(buttonEvent);
        eastPanel.add(actionButton);

        return eastPanel;
    }

    /**
     * Creates the south panel containing the message log display.
     * 
     * @return Initialized JPanel for south region.
     */
    public JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBorder(PADDING);

        JLabel messageLabel = new JLabel("<html><b>Messages:</b></html>");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        southPanel.add(messageLabel);
        southPanel.add(Box.createVerticalStrut(6));

        messages = new JTextArea(5, 40);
        messages.setEditable(false);
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        messages.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(messages);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(760, 100));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        southPanel.add(scrollPane);

        return southPanel;
    }

    /**
     * Resets all text input fields in the center panel.
     */
    public void resetFields() {
        if (centerFields != null) {
            for (JTextField tf : centerFields) {
                tf.setText(null);
            }
        }
    }

    /**
     * Add a message to the top of the message box and scroll to the top.
     * Does nothing if the text box hasn't been initialized yet or if the text is blank.
     * 
     * @param text Text to add.
     */
    public void addMessage(String text) {
        if (messages == null || text == null || text.isBlank())
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("> ");
        sb.append(text);
        if (!messages.getText().isEmpty()) {
            sb.append("\n");
            sb.append(messages.getText());
        }

        messages.setText(sb.toString());
        messages.setCaretPosition(0);
        if (scrollPane != null) {
            SwingUtilities.invokeLater(() -> {
                scrollPane.getVerticalScrollBar().setValue(0);
            });
        }
    }

    /**
     * Validates and parses a required time string using
     * {@link Input#parseTime(String)} and
     * {@link Time#getValidationError(int, int, int, int, int)}.
     * 
     * @param timeStr   Raw time string from user input.
     * @param fieldName Display name of the field (e.g. "Starting time").
     * @return Parsed {@link Time} instance, or {@code null} if invalid (with error
     *         added to messages).
     */
    public Time parseAndValidateTime(String timeStr, String fieldName) {
        return parseAndValidateTime(timeStr, fieldName, true);
    }

    /**
     * Validates and parses a time string with optional requirement check.
     * 
     * @param timeStr   Raw time string from user input.
     * @param fieldName Display name of the field (e.g. "Starting time").
     * @param required  Whether the field is required. If false and input is blank,
     *                  returns null without error.
     * @return Parsed {@link Time} instance, or {@code null} if invalid or empty.
     */
    public Time parseAndValidateTime(String timeStr, String fieldName, boolean required) {
        if (timeStr == null || timeStr.isBlank()) {
            if (required) {
                addMessage(fieldName + " cannot be empty. Expected format: YYYY/MM/DD HH:MM.");
            }
            return null;
        }

        Time time = Input.parseTime(timeStr);
        if (time != null)
            return time;

        String[] components = timeStr.trim().split("[/:,\\s]+");
        if (components.length < 5) {
            addMessage("Invalid " + fieldName.toLowerCase() + " format. Expected: YYYY/MM/DD HH:MM (e.g. 2026/09/10 17:30).");
            return null;
        }

        try {
            int y = Integer.parseInt(components[0]);
            int m = Integer.parseInt(components[1]);
            int d = Integer.parseInt(components[2]);
            int h = Integer.parseInt(components[3]);
            int min = Integer.parseInt(components[4]);

            String err = Time.getValidationError(y, m, d, h, min);
            if (err != null)
                addMessage("Invalid " + fieldName.toLowerCase() + ": " + err);
            else
                addMessage("Invalid " + fieldName.toLowerCase() + " values.");

        } catch (NumberFormatException e) {
            addMessage(fieldName + " components must be integers.");
        }

        return null;
    }

    /**
     * Gets the associated ActivityList.
     * 
     * @return The ActivityList instance.
     */
    public ActivityList getActivities() {
        return activities;
    }

    /**
     * Gets the list of text fields in the center panel.
     * 
     * @return ArrayList of JTextFields.
     */
    public ArrayList<JTextField> getCenterFields() {
        return centerFields;
    }

    /**
     * Gets the messages text area.
     * 
     * @return JTextArea instance.
     */
    public JTextArea getMessages() {
        return messages;
    }

    /**
     * Gets the scroll pane for messages.
     * 
     * @return JScrollPane instance.
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Gets the reset button.
     * 
     * @return JButton instance.
     */
    public JButton getResetButton() {
        return resetButton;
    }

    /**
     * Gets the action button.
     * 
     * @return JButton instance.
     */
    public JButton getActionButton() {
        return actionButton;
    }

    /**
     * Returns the title for this window.
     * 
     * @return Window title string.
     */
    public abstract String getWindowTitle();

    /**
     * Returns the header HTML text shown at the top of the form.
     * 
     * @return Header label text.
     */
    public abstract String getHeaderText();

    /**
     * Returns the display text for the primary action button.
     * 
     * @return Action button label.
     */
    public abstract String getActionButtonLabel();

    /**
     * Returns the action command string for the primary action button.
     * 
     * @return Action button command string.
     */
    public abstract String getActionButtonCommand();

    /**
     * Returns the labels for each form field in the center panel.
     * 
     * @return Array of label strings.
     */
    public abstract String[] getFieldLabels();

    /**
     * Executes the primary action when the action button is clicked.
     * 
     * @return true if action succeeded, false otherwise.
     */
    public abstract boolean executeAction();
}
