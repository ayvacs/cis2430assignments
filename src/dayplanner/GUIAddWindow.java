package dayplanner;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Represents the Swing GUI add activity panel. Extends {@link GUIWindow} to share
 * common configuration, layout, and component handling with other GUI
 * panels such as {@link GUISearchWindow}.
 */
public class GUIAddWindow extends GUIWindow {
    /**
     * Label for the location input field.
     */
    public JLabel locationLabel = null;

    /**
     * Text field for the location input.
     */
    public JTextField locationField = null;

    /**
     * Create a new Swing GUI add panel.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUIAddWindow(ActivityList activities) {
        super(activities);
        setupLocationToggle();
    }

    /**
     * Sets up the location field and label references, attaches an ItemListener
     * to the type combo box to toggle Location visibility based on whether
     * "other" is selected, and sets the initial visibility.
     */
    private void setupLocationToggle() {
        if (centerLabels.size() >= 6) {
            locationLabel = centerLabels.get(5);
        }
        if (centerFields.size() >= 5) {
            locationField = centerFields.get(4);
        }

        if (typeComboBox != null && locationLabel != null && locationField != null) {
            typeComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String type = (String) typeComboBox.getSelectedItem();
                    if (type != null) {
                        locationLabel.setVisible(type.equals("other"));
                        locationField.setVisible(type.equals("other"));
                        revalidate();
                        repaint();
                    }
                }
            });

            String type = (String) typeComboBox.getSelectedItem();
            boolean isOther = (type != null && type.equals("other"));
            locationLabel.setVisible(isOther);
            locationField.setVisible(isOther);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return Title string for the add panel.
     */
    @Override
    public String getWindowTitle() {
        return "DayPlanner - Add";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Header HTML text for the add window form.
     */
    @Override
    public String getHeaderText() {
        return "<html><b>Adding an activity:</b></html>";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Empty subheader string (no subheader needed for add window).
     */
    @Override
    public String getSubHeaderText() {
        return new String();
    }

    /**
     * {@inheritDoc}
     * 
     * @return Action button label "Add".
     */
    @Override
    public String getActionButtonLabel() {
        return "Add";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Action command string "ADD".
     */
    @Override
    public String getActionButtonCommand() {
        return "ADD";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Array of field labels for the add activity form.
     */
    @Override
    public String[] getFieldLabels() {
        return new String[] {
            "Type:",
            "Title:",
            "<html>Starting time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Ending time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Comment <i>(optional)</i>:</html>",
            "<html>Location <i>(if creating Other Activity only)</i>:</html>"
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @return true if activity creation succeeded, false otherwise.
     */
    @Override
    public boolean executeAction() {
        return tryCreate(activities, centerFields);
    }

    /**
     * Input-validate and try to create an activity and add it to the list.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields All of the JTextField instances in the order:
     *                     title, start time, end time, comment, location
     *                     (or type, title, start, end, comment, location for legacy callers).
     * @return true if activity was created and added, false otherwise.
     */
    public boolean tryCreate(ActivityList activities, ArrayList<JTextField> centerFields) {
        if (activities == null || centerFields == null) {
            addMessage("Internal error: missing form fields.");
            return false;
        }

        String typeStr;
        int titleIdx, sTimeIdx, eTimeIdx, commentIdx, locationIdx;

        if (centerFields.size() == 5) {
            typeStr = (typeComboBox != null) ? (String) typeComboBox.getSelectedItem() : "home";
            titleIdx = 0;
            sTimeIdx = 1;
            eTimeIdx = 2;
            commentIdx = 3;
            locationIdx = 4;
        } else if (centerFields.size() >= 6) {
            typeStr = centerFields.get(0).getText().trim();
            titleIdx = 1;
            sTimeIdx = 2;
            eTimeIdx = 3;
            commentIdx = 4;
            locationIdx = 5;
        } else {
            addMessage("Internal error: missing form fields.");
            return false;
        }

        // 1. Activity type
        if (typeStr == null || typeStr.isEmpty()) {
            addMessage("Activity type cannot be empty. Please enter 'home', 'school', or 'other'.");
            return false;
        }
        ActivityType type = ActivityType.parse(typeStr);
        if (type == null) {
            addMessage("Invalid activity type: \"" + typeStr + "\". Please enter 'home', 'school', or 'other'.");
            return false;
        }

        // 2. Title
        String title = centerFields.get(titleIdx).getText().trim();
        if (title.isEmpty()) {
            addMessage("Title cannot be empty.");
            return false;
        }

        // 3. Start time
        Time sTime = parseAndValidateTime(centerFields.get(sTimeIdx).getText(), "Starting time");
        if (sTime == null)
            return false;

        // 4. End time
        Time eTime = parseAndValidateTime(centerFields.get(eTimeIdx).getText(), "Ending time");
        if (eTime == null)
            return false;

        // 5. Start vs End order
        if (sTime.compareTo(eTime) >= 0) {
            addMessage("Ending time (" + eTime + ") has to be after starting time (" + sTime + ").");
            return false;
        }

        // 6. Comment (optional)
        String comment = centerFields.get(commentIdx).getText().trim();
        if (comment.isEmpty())
            comment = null;

        // 7. Location (required for other)
        String location = null;
        if (type == ActivityType.OTHER) {
            location = centerFields.get(locationIdx).getText().trim();
            if (location.isEmpty()) {
                addMessage("Location cannot be empty for Other activities.");
                return false;
            }
        }

        // 8. Instantiate and add
        try {
            Activity act = switch (type) {
                case HOME   -> new HomeActivity(title, sTime, eTime, comment);
                case SCHOOL -> new SchoolActivity(title, sTime, eTime, comment);
                case OTHER  -> new OtherActivity(title, sTime, eTime, comment, location);
            };

            activities.add(act);
            addMessage("Added successfully: " + act.toString());
            return true;
        } catch (IllegalArgumentException e) {
            addMessage("Error creating activity: " + e.getMessage());
            return false;
        }
    }
}
