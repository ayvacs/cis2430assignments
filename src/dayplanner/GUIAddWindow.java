package dayplanner;

import java.util.ArrayList;
import javax.swing.JTextField;

/**
 * Represents the Swing GUI add window. Extends {@link GUIWindow} to share
 * common window configuration, layout, and component handling with other GUI
 * windows such as {@link GUISearchWindow}.
 */
public class GUIAddWindow extends GUIWindow {

    /**
     * Create a new Swing GUI add window.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUIAddWindow(ActivityList activities) {
        super(activities);
    }

    @Override
    public String getWindowTitle() {
        return "DayPlanner - Add";
    }

    @Override
    public String getHeaderText() {
        return "<html><b>Adding an activity:</b></html>";
    }

    @Override
    public String getActionButtonLabel() {
        return "Add";
    }

    @Override
    public String getActionButtonCommand() {
        return "ADD";
    }

    @Override
    public String[] getFieldLabels() {
        return new String[] {
            "<html>Type <code>('home', 'school', or 'other')</code></html>",
            "Title:",
            "<html>Starting time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Ending time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Comment <i>(optional)</i>:</html>",
            "<html>Location <i>(if creating Other Activity only)</i>:</html>"
        };
    }

    @Override
    public boolean executeAction() {
        return tryCreate(activities, centerFields);
    }

    /**
     * Input-validate and try to create an activity and add it to the list.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields All of the JTextField instances in the order: type,
     *                     title, start time, end time, comment, location.
     * @return true if activity was created and added, false otherwise.
     */
    public boolean tryCreate(ActivityList activities, ArrayList<JTextField> centerFields) {
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
