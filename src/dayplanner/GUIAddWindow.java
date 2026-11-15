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
 * Represents the Swing GUI add window.
 */
public class GUIAddWindow extends JFrame {
    private static final Dimension DIMS = new Dimension(800, 400);
    private static final String TITLE = "DayPlanner - Add";
    private static final int CLOSE = JFrame.DISPOSE_ON_CLOSE;
    private static final Color BACKGROUND = new Color(250, 250, 250);
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    /**
     * Message box
     */
    private JTextArea messages = null;

    /**
     * Scroll pane containing the message box
     */
    private JScrollPane scrollPane = null;


    /**
     * Create a new Swing GUI add window.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUIAddWindow(ActivityList activities) {
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
        // contains Text Fields.
        // ------------------------------

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(7, 2, 8, 8));
        centerPanel.setBorder(PADDING);
        centerPanel.add(new JLabel("<html><b>Adding an activity:</b></html>"));
        centerPanel.add(Box.createGlue()); // skip this grid cell

        // ArrayList that contains all of the JTextField instances in this panel.
        ArrayList<JTextField> centerFields = new ArrayList<JTextField>();

        centerPanel.add(new JLabel(
            "<html>Type <code>('home', 'school', or 'other')</code></html>"));
        JTextField typeField = new JTextField();
        centerPanel.add(typeField);
        centerFields.add(typeField);

        centerPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        centerPanel.add(titleField);
        centerFields.add(titleField);
        
        centerPanel.add(new JLabel(
            "<html>Starting time <code>(YYYY/MM/DD HH:MM)</code>:</html>"));
        JTextField sTimeField = new JTextField();
        centerPanel.add(sTimeField);
        centerFields.add(sTimeField);
        
        centerPanel.add(new JLabel(
            "<html>Ending time <code>(YYYY/MM/DD HH:MM)</code>:</html>"));
        JTextField eTimeField = new JTextField();
        centerPanel.add(eTimeField);
        centerFields.add(eTimeField);
        
        centerPanel.add(new JLabel(
            "<html>Comment <i>(optional)</i>:</html>"));
        JTextField commentField = new JTextField();
        centerPanel.add(commentField);
        centerFields.add(commentField);
        
        centerPanel.add(new JLabel(
            "<html>Location <i>(if creating Other Activity only)</i>:</html>"));
        JTextField locationField = new JTextField();
        centerPanel.add(locationField);
        centerFields.add(locationField);
        

        // ------------------------------
        // East Panel
        // contains Buttons.
        // ------------------------------

        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(PADDING);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        // This is the function that gets called when we click the RESET or ADD buttons.
        ActionListener buttonEvent = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               String action = event.getActionCommand();

               // RESET: set all text fields to NULL.
               if (action.equals("RESET"))
                   for (JTextField tf : centerFields)
                       tf.setText(null);

               // ADD: Pass all of the text field instances.
               else if (action.equals("ADD"))
                   tryCreate(activities, centerFields);
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
        // contains Message box.
        // ------------------------------

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBorder(PADDING);

        JLabel messageLabel = new JLabel(
            "<html><b>Messages:</b></html>");
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

        
        // ==============================
        // End
        // ==============================

        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Add a message to the top of the message box and scroll to the top.
     * Does nothing if the text box hasn't been initialized yet or if the text is
     * blank.
     * 
     * @param text Text to add.
     */
    public void addMessage(String text) {
        if (messages == null || text.isBlank())
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("> ");
        sb.append(text);
        if (!messages.getText().isEmpty()) {
            sb.append("\n\n");
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
     * Validates and parses a time string using {@link Input#parseTime(String)} and
     * {@link Time#getValidationError(int, int, int, int, int)}.
     * 
     * @param timeStr   Raw time string from user input.
     * @param fieldName Display name of the field (e.g. "Starting time").
     * @return Parsed {@link Time} instance, or {@code null} if invalid (with error
     *         added to messages).
     */
    private Time parseAndValidateTime(String timeStr, String fieldName) {
        if (timeStr == null || timeStr.isBlank()) {
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
     * Input-validate and try to create an activity and add it to the list.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields All of the JTextField instances in the order: type,
     *                     title, start time, end time, comment, location.
     * @return true if activity was created and added, false otherwise.
     */
    private boolean tryCreate(ActivityList activities, ArrayList<JTextField> centerFields) {
        if (activities == null || centerFields == null || centerFields.size() < 6) {
            addMessage("Internal error: missing form fields.");
            return false;
        }

        // 1. Activity type
        String typeStr = centerFields.get(0).getText().trim();
        if (typeStr.isEmpty()) {
            addMessage("Activity type cannot be empty. Please enter 'home', 'school', or 'other'.");
            return false;
        }
        ActivityType type = ActivityType.parse(typeStr);
        if (type == null) {
            addMessage("Invalid activity type: \"" + typeStr + "\". Please enter 'home', 'school', or 'other'.");
            return false;
        }

        // 2. Title
        String title = centerFields.get(1).getText().trim();
        if (title.isEmpty()) {
            addMessage("Title cannot be empty.");
            return false;
        }

        // 3. Start time
        Time sTime = parseAndValidateTime(centerFields.get(2).getText(), "Starting time");
        if (sTime == null)
            return false;

        // 4. End time
        Time eTime = parseAndValidateTime(centerFields.get(3).getText(), "Ending time");
        if (eTime == null)
            return false;

        // 5. Start vs End order
        if (sTime.compareTo(eTime) >= 0) {
            addMessage("Ending time (" + eTime + ") has to be after starting time (" + sTime + ").");
            return false;
        }

        // 6. Comment (optional)
        String comment = centerFields.get(4).getText().trim();
        if (comment.isEmpty())
            comment = null;

        // 7. Location (required for other)
        String location = null;
        if (type == ActivityType.OTHER) {
            location = centerFields.get(5).getText().trim();
            if (location.isEmpty()) {
                addMessage("Location cannot be empty for Other activities.");
                return false;
            }
        }

        // 8. Instantiate and add
        Activity act = switch (type) {
            case HOME   -> new HomeActivity(title, sTime, eTime, comment);
            case SCHOOL -> new SchoolActivity(title, sTime, eTime, comment);
            case OTHER  -> new OtherActivity(title, sTime, eTime, comment, location);
        };

        activities.add(act);
        addMessage("Added successfully: " + act.toString());
        return true;
    }
}
