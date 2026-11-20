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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Abstract superclass representing a standard DayPlanner GUI panel (such as
 * Add or Search) used inside a CardLayout.
 */
public abstract class GUIWindow extends JPanel {
    /**
     * Default dimensions of the GUI window.
     */
    public static final Dimension DIMS = new Dimension(800, 600);

    /**
     * Background color for the GUI window panel.
     */
    private static final Color BACKGROUND = new Color(250, 250, 250);

    /**
     * Padding border applied around panels in the window.
     */
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    /**
     * Reference to the activity list.
     */
    public ActivityList activities;

    /**
     * Combo box for selecting activity type ("home", "school", "other").
     */
    public JComboBox<String> typeComboBox;

    /**
     * List of JLabels in the center form panel.
     */
    public ArrayList<JLabel> centerLabels = new ArrayList<JLabel>();

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
     * Creates and initializes a new Swing GUI panel.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUIWindow(ActivityList activities) {
        super();
        this.activities = activities;

        setPreferredSize(DIMS);
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(12, 12));
        setBorder(PADDING);

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createEastPanel(), BorderLayout.EAST);
        add(createSouthPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the center form panel containing input fields.
     * 
     * @return Initialized JPanel for center region.
     */
    public JPanel createCenterPanel() {
        String[] labels = getFieldLabels();
        int rows = labels.length + 2; // +2 for the header rows

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(rows, 2, 8, 8));
        centerPanel.setBorder(PADDING);
        centerPanel.add(new JLabel(getHeaderText()));
        centerPanel.add(Box.createGlue()); // skip this grid cell
        centerPanel.add(new JLabel(getSubHeaderText()));
        centerPanel.add(Box.createGlue()); // skip this grid cell

        centerLabels.clear();
        centerFields.clear();

        for (int i = 0; i < labels.length; i++) {
            String labelText = labels[i];
            JLabel label = new JLabel(labelText);
            centerLabels.add(label);
            centerPanel.add(label);

            if (i == 0) {
                String[] options = getTypeOptions();
                typeComboBox = new JComboBox<>(options);
                if (options.length > 0) {
                    typeComboBox.setSelectedItem(options[0]);
                }
                centerPanel.add(typeComboBox);
            } else {
                JTextField tf = new JTextField();
                centerPanel.add(tf);
                centerFields.add(tf);
            }
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

        JLabel messageLabel = new JLabel("<html><b>" + getMessageBoxLabel() + "</b></html>");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        southPanel.add(messageLabel);
        southPanel.add(Box.createVerticalStrut(6));

        messages = new JTextArea(5, 40);
        messages.setEditable(false);
        messages.setLineWrap(false);
        messages.setWrapStyleWord(false);
        messages.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(messages);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(760, 100));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        southPanel.add(scrollPane);

        return southPanel;
    }

    /**
     * Resets all text input fields in the center panel without resetting the combo box.
     */
    public void resetFields() {
        if (centerFields != null) {
            for (JTextField tf : centerFields) {
                tf.setText(null);
            }
        }
    }

    /**
     * Returns the options for the activity type combo box.
     * 
     * @return Array of type option strings, defaulting to "home", "school", and "other".
     */
    protected String[] getTypeOptions() {
        return new String[]{"home", "school", "other"};
    }

    /**
     * Clears all messages from the message box.
     */
    public void clearMessages() {
        if (messages != null) {
            messages.setText("");
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
     * Appends a message to the end of the message box and scrolls to the top.
     * Does nothing if the text box hasn't been initialized yet or if the text is blank.
     * 
     * @param text Text to append.
     */
    public void appendMessage(String text) {
        if (messages == null || text == null || text.isBlank())
            return;

        StringBuilder sb = new StringBuilder();
        if (!messages.getText().isEmpty()) {
            sb.append(messages.getText());
            sb.append("\n");
        }
        sb.append("> ");
        sb.append(text);

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
            if (required)
                addMessage(fieldName + " cannot be empty. Expected format: YYYY/MM/DD HH:MM.");
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

            return new Time(y, m, d, h, min);
        } catch (NumberFormatException e) {
            addMessage(fieldName + " components must be integers.");
        } catch (IllegalArgumentException e) {
            addMessage("Invalid " + fieldName.toLowerCase() + ": " + e.getMessage());
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
     * Gets the combo box used to select activity type.
     * 
     * @return JComboBox instance for activity type.
     */
    public JComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    /**
     * Gets the list of labels in the center panel.
     * 
     * @return ArrayList of JLabels.
     */
    public ArrayList<JLabel> getCenterLabels() {
        return centerLabels;
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
     * Returns the label text to display above the south message area.
     * 
     * @return Message box header string, defaulting to "Messages:".
     */
    public String getMessageBoxLabel() {
        return "Messages:";
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
     * Returns the subheader HTML text shown under the header.
     * 
     * @return Subheader label text.
     */
    public abstract String getSubHeaderText();

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
