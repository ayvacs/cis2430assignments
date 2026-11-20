package dayplanner;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/**
 * Represents the Swing GUI search activities panel. Extends {@link GUIWindow} to share
 * common configuration, layout, and component handling with other GUI
 * panels such as {@link GUIAddWindow}.
 */
public class GUISearchWindow extends GUIWindow {
    /**
     * Create a new Swing GUI search panel.
     * 
     * @param activities Instantiated ActivityList.
     */
    public GUISearchWindow(ActivityList activities) {
        super(activities);
    }

    /**
     * {@inheritDoc}
     * 
     * @return Title string for the search window.
     */
    @Override
    public String getWindowTitle() {
        return "DayPlanner - Search";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Header HTML text for the search window form.
     */
    @Override
    public String getHeaderText() {
        return "<html><b>Searching activities:</b></html>";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Subheader HTML text explaining search parameters are optional.
     */
    @Override
    public String getSubHeaderText() {
        return "<html>Fields are optional; leaving a text field blank means the search will ignore that parameter.</html>";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Action button label "Search".
     */
    @Override
    public String getActionButtonLabel() {
        return "Search";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Action command string "SEARCH".
     */
    @Override
    public String getActionButtonCommand() {
        return "SEARCH";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Array of type options with "any" first, followed by "home", "school", and "other".
     */
    @Override
    protected String[] getTypeOptions() {
        return new String[]{"any", "home", "school", "other"};
    }

    /**
     * {@inheritDoc}
     * 
     * @return Label for the south message area "Search Results:".
     */
    @Override
    public String getMessageBoxLabel() {
        return "Search Results:";
    }

    /**
     * {@inheritDoc}
     * 
     * @return Array of field labels for the search form. Comment and Location
     *         fields are omitted as the search function ignores them.
     */
    @Override
    public String[] getFieldLabels() {
        return new String[] {
            "Type:",
            "<html>Title <i>(keywords)</i>:</html>",
            "<html>Starting time <code>(YYYY/MM/DD HH:MM)</code>:</html>",
            "<html>Ending time <code>(YYYY/MM/DD HH:MM)</code>:</html>"
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @return true if search was performed, false if validation failed.
     */
    @Override
    public boolean executeAction() {
        return trySearch(activities, centerFields);
    }

    /**
     * Input-validate and try to search for activities matching the criteria.
     * 
     * @param activities   Instantiated ActivityList.
     * @param centerFields List of JTextField instances in the center panel.
     * @return true if search was performed, false if validation failed.
     */
    public boolean trySearch(ActivityList activities, ArrayList<JTextField> centerFields) {
        clearMessages();

        if (activities == null || centerFields == null || centerFields.size() < 3) {
            addMessage("Internal error: missing form fields.");
            return false;
        }

        // 1. Activity type
        String typeStr = (typeComboBox != null) ? (String) typeComboBox.getSelectedItem() : "";
        ActivityType type = "any".equals(typeStr) ? null : ActivityType.parse(typeStr);

        // 2. Title keywords (optional)
        String titleStr = centerFields.get(0).getText().trim();
        String[] keywords = null;
        if (!titleStr.isEmpty())
            keywords = titleStr.split("\\s+");

        // 3. Start time (optional)
        String sTimeStr = centerFields.get(1).getText().trim();
        Time sTime = null;
        if (!sTimeStr.isEmpty()) {
            sTime = parseAndValidateTime(sTimeStr, "Starting time", false);
            if (sTime == null)
                return false;
        }

        // 4. End time (optional)
        String eTimeStr = centerFields.get(2).getText().trim();
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
                appendMessage(count + ". " + act.toString());
            }
        }

        if (count == 0)
            appendMessage("No activities matched your search request.");
        else
            appendMessage("Found " + count + " matching " + (count == 1 ? "activity." : "activities."));

        return true;
    }
}
