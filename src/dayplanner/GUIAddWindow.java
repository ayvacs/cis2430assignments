package dayplanner;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
                   if (tryCreate(activities, centerFields))
                       addMessage("Added successfully!");
                   else
                       addMessage("Error adding activity.");
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
        southPanel.add(new JLabel(
            "<html><b>Messages</b> <i>(newer ones are placed the top)</i>:</html>"));

        messages = new JTextArea();
        messages.setBackground(southPanel.getBackground());
        southPanel.add(messages);

        
        // ==============================
        // End
        // ==============================

        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Add a message to the top of the message box. Does nothing if the text box
     * hasn't been initialized yet or if the text is blank.
     * 
     * @param text Text to add.
     */
    public void addMessage(String text) {
        if (messages == null || text.isBlank())
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("> ");
        sb.append(text);
        sb.append("\n");
        sb.append(messages.getText());

        messages.setText(sb.toString());
    }

    /**
     * Input-validate and try to create an activity and add it to the list.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields All of the JTextField instances in the order: type,
     *                     title, start time, end time, comment.
     */
    private boolean tryCreate(ActivityList activities, ArrayList<JTextField> centerFields) {
        // Get inputs
        ActivityType type = ActivityType.parse(centerFields.get(0).getText());
        String title = centerFields.get(1).getText();
        Time sTime = new Time(centerFields.get(2).getText());
        Time eTime = new Time(centerFields.get(3).getText());
        String comment = centerFields.get(4).getText();

        // Input validation
        if (type == null
         || title == null || title.isBlank()
         || sTime == null
         || eTime == null || sTime.compareTo(eTime) > 0)
            return false;

        // Instantiate
        if (type.equals(ActivityType.HOME)) {
            activities.add(new HomeActivity(
                title, sTime, eTime, comment));
            return true;
        } else if (type.equals(ActivityType.SCHOOL)) {
            activities.add(new SchoolActivity(
                title, sTime, eTime, comment));
            return true;
        } else if (type.equals(ActivityType.OTHER)) {
            String location = centerFields.get(5).getText();
            activities.add(new OtherActivity(
                title, sTime, eTime, comment, location));
            return true;
        }
        
        return false;
    }
}