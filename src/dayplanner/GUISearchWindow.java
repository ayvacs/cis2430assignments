package dayplanner;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/**
 * Represents the Swing GUI search window. Extends {@link GUIWindow} to share
 * common window configuration, layout, and component handling with other GUI
 * windows such as {@link GUIAddWindow}.
 */
public class GUISearchWindow extends GUIWindow {
    /**
     * Create a new Swing GUI search window.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUISearchWindow(ActivityList activities) {
        super(activities);
    }

    @Override
    public String getWindowTitle() {
        return "DayPlanner - Search";
    }

    @Override
    public String getHeaderText() {
        return "<html><b>Searching activities:</b></html>";
    }

    @Override
    public String getActionButtonLabel() {
        return "Search";
    }

    @Override
    public String getActionButtonCommand() {
        return "SEARCH";
    }

    @Override
    public String[] getFieldLabels() {
        return new String[] {
            "<html>Type <code>('home', 'school', or 'other')</code></html>",
            "<html>Title <i>(keywords)</i>:</html>",
            "<html>Starting time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Ending time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Comment <i>(optional)</i>:</html>",
            "<html>Location <i>(if creating Other Activity only)</i>:</html>"
        };
    }

    @Override
    public boolean executeAction() {
        return trySearch(activities, centerFields);
    }

    /**
     * Input-validate and try to search for activities matching the criteria.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields All of the JTextField instances in the order: type,
     *                     title/keywords, start time, end time, comment, location.
     * @return true if search was performed, false if validation failed.
     */
    public boolean trySearch(ActivityList activities, ArrayList<JTextField> centerFields) {
        if (activities == null || centerFields == null || centerFields.size() < 4) {
            addMessage("Internal error: missing form fields.");
            return false;
        }

        // 1. Activity type (optional)
        String typeStr = centerFields.get(0).getText().trim();
        ActivityType type = null;
        if (!typeStr.isEmpty()) {
            type = ActivityType.parse(typeStr);
            if (type == null) {
                addMessage("Invalid activity type: \"" + typeStr + "\". Please enter 'home', 'school', or 'other', or leave blank.");
                return false;
            }
        }

        // 2. Title keywords (optional)
        String titleStr = centerFields.get(1).getText().trim();
        String[] keywords = null;
        if (!titleStr.isEmpty())
            keywords = titleStr.split("\\s+");

        // 3. Start time (optional)
        String sTimeStr = centerFields.get(2).getText().trim();
        Time sTime = null;
        if (!sTimeStr.isEmpty()) {
            sTime = parseAndValidateTime(sTimeStr, "Starting time", false);
            if (sTime == null)
                return false;
        }

        // 4. End time (optional)
        String eTimeStr = centerFields.get(3).getText().trim();
        Time eTime = null;
        if (!eTimeStr.isEmpty()) {
            eTime = parseAndValidateTime(eTimeStr, "Ending time", false);
            if (eTime == null)
                return false;
        }

        // 5. Start vs End order (if both are provided)
        if (sTime != null && eTime != null && sTime.compareTo(eTime) >= 0) {
            addMessage("Ending time (" + eTime + ") has to be after starting time (" + sTime + ").");
            return false;
        }

        // 6. Execute search
        List<Integer> titleMatches = activities.findTitleMatches(keywords);
        int count = 0;
        for (int index : titleMatches) {
            Activity act = activities.get(index);
            if (DayPlannerUtils.matchesCriteria(act, type, sTime, eTime)) {
                count++;
                addMessage(count + ". " + act.toString());
            }
        }

        if (count == 0)
            addMessage("No activities matched your search request.");
        else
            addMessage("Found " + count + " matching " + (count == 1 ? "activity." : "activities."));

        return true;
    }
}
