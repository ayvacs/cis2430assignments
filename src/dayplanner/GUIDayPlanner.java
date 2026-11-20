package dayplanner;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Main application window for the DayPlanner Swing GUI.
 * Uses a {@link CardLayout} to switch between views (welcome, add, search)
 * within a single main {@link JFrame}, navigated via a {@link JMenuBar}.
 */
public class GUIDayPlanner extends JFrame {
    /**
     * Default dimensions of the main GUI window.
     */
    private static final Dimension DIMS = new Dimension(800, 600);

    /**
     * Title of the main GUI window.
     */
    private static final String TITLE = "DayPlanner";

    /**
     * Window close operation: do nothing on default close so custom quit flow is used.
     */
    private static final int CLOSE = JFrame.DO_NOTHING_ON_CLOSE;

    /**
     * Background color for the window.
     */
    private static final Color BACKGROUND = new Color(220, 220, 220);

    /**
     * Padding border applied around components.
     */
    private static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    /**
     * Reference to the activity list.
     */
    private ActivityList activities;

    /**
     * CardLayout managing in-place panel switching.
     */
    private CardLayout cardLayout;

    /**
     * Panel container holding the cards for CardLayout.
     */
    private JPanel cardsPanel;

    /**
     * Welcome text label on the initial welcome card.
     */
    private JLabel welcomeText;

    /**
     * Add activity view panel.
     */
    private GUIAddWindow addWindow;

    /**
     * Search activity view panel.
     */
    private GUISearchWindow searchWindow;

    /**
     * Main menu bar.
     */
    private JMenuBar menuBar;

    /**
     * Commands menu.
     */
    private JMenu commandsMenu;

    /**
     * Add command menu item.
     */
    private JMenuItem addMenuItem;

    /**
     * Search command menu item.
     */
    private JMenuItem searchMenuItem;

    /**
     * Quit command menu item.
     */
    private JMenuItem quitMenuItem;

    /**
     * Create a new DayPlanner Swing GUI window.
     * 
     * @param activities Defined ActivityList.
     */
    public GUIDayPlanner(ActivityList activities) {
        // ==============================
        // Window Setup
        // ==============================

        super();
        this.activities = activities;

        setSize(DIMS);
        setTitle(TITLE);
        setDefaultCloseOperation(CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showGuiQuitMenu();
            }
        });

        // ==============================
        // Menu Bar
        // ==============================

        menuBar = new JMenuBar();
        commandsMenu = new JMenu("Commands");

        addMenuItem = new JMenuItem("Add");
        addMenuItem.addActionListener(e -> showAdd());
        commandsMenu.add(addMenuItem);

        searchMenuItem = new JMenuItem("Search");
        searchMenuItem.addActionListener(e -> showSearch());
        commandsMenu.add(searchMenuItem);

        quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(e -> showGuiQuitMenu());
        commandsMenu.add(quitMenuItem);

        menuBar.add(commandsMenu);
        setJMenuBar(menuBar);

        // ==============================
        // CardLayout & Views
        // ==============================

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Card 1: Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(BACKGROUND);
        welcomePanel.setBorder(PADDING);

        welcomeText = new JLabel();
        welcomeText.setHorizontalAlignment(JLabel.CENTER);
        refreshWelcomeText();
        welcomePanel.add(welcomeText, BorderLayout.CENTER);

        // Card 2: Add window (panel)
        addWindow = new GUIAddWindow(activities);

        // Card 3: Search window (panel)
        searchWindow = new GUISearchWindow(activities);

        cardsPanel.add(welcomePanel, "welcome");
        cardsPanel.add(addWindow, "add");
        cardsPanel.add(searchWindow, "search");

        getContentPane().add(cardsPanel);

        // Display the initial welcome view
        cardLayout.show(cardsPanel, "welcome");

        setVisible(true);
    }

    /**
     * Switches the active card to the welcome panel and refreshes welcome text.
     */
    public void showWelcome() {
        refreshWelcomeText();
        cardLayout.show(cardsPanel, "welcome");
    }

    /**
     * Switches the active card to the add activity view.
     */
    public void showAdd() {
        cardLayout.show(cardsPanel, "add");
    }

    /**
     * Switches the active card to the search activities view.
     */
    public void showSearch() {
        cardLayout.show(cardsPanel, "search");
    }

    /**
     * Switches the active card to the specified card name ("welcome", "add", "search").
     * 
     * @param name Name of the card to display.
     */
    public void showCard(String name) {
        if ("welcome".equalsIgnoreCase(name)) {
            showWelcome();
        } else {
            cardLayout.show(cardsPanel, name);
        }
    }

    /**
     * Saves the list and exits the GUI. Shows an error message if there was an
     * error saving the list.
     */
    public void quit() {
        boolean errorOccurred = DayPlanner.quit(false);

        String msg = errorOccurred
            ? "<html>There was an error saving the list.<br>Click \"OK\" to exit DayPlanner.</html>"
            : "<html>The list was saved successfully.<br>Now quitting DayPlanner. Goodbye!</html>";
        int mode = errorOccurred 
            ? JOptionPane.ERROR_MESSAGE
            : JOptionPane.INFORMATION_MESSAGE;

        JOptionPane.showMessageDialog(null, msg, null, mode);
        
        dispose();
        System.exit(0);
    }

    /**
     * Shows the GUI quit confirmation menu.
     */
    public void showGuiQuitMenu() {
        new GUIQuitMenu(this);
    }

    /**
     * Refresh the welcome text label to display the current greeting and activity count.
     */
    public void refreshWelcomeText() {
        if (welcomeText != null && activities != null) {
            String greeting = DayPlannerUtils.getGreeting(activities.size()).trim();
            String text = ("<html><div style='text-align: center; font-size: 14pt;'>"
                         + "<b>Welcome to DayPlanner!</b><br><br>"
                         + greeting
                         + "<br><br>"
                         + "Choose a command from the Commands menu above."
                         + "</div></html>").replaceAll("\n", "<br>");
            welcomeText.setText(text);
        }
    }

    /**
     * Gets the CardLayout instance used for switching views.
     * 
     * @return CardLayout instance.
     */
    public CardLayout getCardLayout() {
        return cardLayout;
    }

    /**
     * Gets the JPanel container holding the cards.
     * 
     * @return JPanel cards panel.
     */
    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    /**
     * Gets the GUIAddWindow view panel.
     * 
     * @return GUIAddWindow instance.
     */
    public GUIAddWindow getAddWindow() {
        return addWindow;
    }

    /**
     * Gets the GUISearchWindow view panel.
     * 
     * @return GUISearchWindow instance.
     */
    public GUISearchWindow getSearchWindow() {
        return searchWindow;
    }

    /**
     * Gets the ActivityList instance.
     * 
     * @return ActivityList instance.
     */
    public ActivityList getActivities() {
        return activities;
    }

    /**
     * Gets the JMenuBar instance.
     * 
     * @return JMenuBar instance.
     */
    public JMenuBar getCommandsMenuBar() {
        return menuBar;
    }

    /**
     * Gets the Commands JMenu instance.
     * 
     * @return JMenu instance.
     */
    public JMenu getCommandsMenu() {
        return commandsMenu;
    }
}
