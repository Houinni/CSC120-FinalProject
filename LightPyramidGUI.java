import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The LightPyramidGUI class represents the graphical user interface for the
 * Christmas Tree Light-On Challenge.
 * This class provides a visual, interactive environment where two players take
 * turns playing the game.
 * The GUI includes:
 * - A pyramid of buttons representing the lights.
 * - Control buttons for confirming actions, restarting the game, and viewing
 * the rules.
 * - A panel for displaying game status and transitions between players.
 *
 * Game Rules:
 * - Players take turns lighting up bulbs on a pyramid-shaped Christmas tree.
 * - A player can light up one or multiple bulbs on a single floor during their
 * turn.
 * - The top bulb cannot be lit until all other bulbs on the tree are already
 * illuminated.
 * - The player who successfully lights up the top bulb wins the game.
 *
 * Key Features:
 * - Dynamic button state management for selected, unselected, and finalized
 * states.
 * - Support for restarting the game and undoing invalid moves.
 * - Interactive rule display for player guidance.
 */
public class LightPyramidGUI {
    private final ChristmasTree ChristmasTree;
    private final JFrame frame;
    private int currentPlayer; // 1 for Player 1, 2 for Player 2
    private List<JButton> selectedButtons; // Track selected lights for the current move
    private int selectedFloor; // Track the floor of the current selection

    /**
     * Constructs the LightPyramidGUI and initializes the game.
     * Sets up the building, frame, and GUI components.
     */
    public LightPyramidGUI() {
        ChristmasTree = new ChristmasTree(8); // Create an 8-floor building
        frame = new JFrame("Light Pyramid Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        currentPlayer = 1; // Player 1 starts
        selectedButtons = new ArrayList<>();
        selectedFloor = -1; // No floor selected initially
        setupGUI();
        frame.setSize(1100, 1100);
        frame.setVisible(true);
    }

    /**
     * Sets up the GUI, including the pyramid panel, middle panel, and control
     * panel.
     * Configures event listeners for buttons and initializes game state.
     */
    private void setupGUI() {
        JPanel pyramidPanel = new JPanel();
        pyramidPanel.setBackground(Color.PINK);
        pyramidPanel.setLayout(new GridLayout(8, 1, 10, 10)); // 8 rows for 8 floors with spacing

        // Iterate over the building in reverse order to render floor 7 at the top
        for (int floorIndex = ChristmasTree.getBuilding().size() - 1; floorIndex >= 0; floorIndex--) {
            JPanel floorPanel = new JPanel();
            floorPanel.setBackground(Color.PINK);
            floorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5)); // Center-align buttons with spacing
            List<Light> floor = ChristmasTree.getBuilding().get(floorIndex);

            for (int roomIndex = 0; roomIndex < floor.size(); roomIndex++) { // Adjust number of buttons per floor
                JButton lightButton = new JButton();
                isOffLightButton(lightButton);

                int roomIndexCopy = roomIndex;
                int floorIndexCopy = floorIndex;

                lightButton.addActionListener(e -> {
                    if (selectedButtons.contains(lightButton)) {
                        unselectButton(lightButton);
                        selectedButtons.remove(lightButton);
                        if (selectedButtons.isEmpty()) {
                            selectedFloor = -1;
                        }
                    } else {
                        if (selectedFloor == -1 || selectedFloor == floorIndexCopy) {
                            selectedButtons.add(lightButton);
                            isSelectedLightButton(lightButton);
                            selectedFloor = floorIndexCopy;

                            boolean success = ChristmasTree.turnOnLight(floorIndexCopy, roomIndexCopy);

                            if (!success) {
                                System.out.println("Failed to update building state for floor " + floorIndexCopy
                                        + ", room " + roomIndexCopy);
                            }

                        } else {
                            ImageIcon icon = new ImageIcon("icon.jpeg");
                            JOptionPane.showMessageDialog(frame, "You can only turn off lights on one floor per turn!",
                                    "Message", JOptionPane.INFORMATION_MESSAGE, icon);
                        }
                    }

                });

                floorPanel.add(lightButton);
            }

            pyramidPanel.add(floorPanel); // Add each floor panel to the pyramid panel
        }

        frame.add(pyramidPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel();
        middlePanel.setPreferredSize(new Dimension(800, 50));
        middlePanel.setBackground(Color.PINK);
        frame.add(middlePanel, BorderLayout.CENTER);

        JLabel rectangleLabel = new JLabel();
        rectangleLabel.setOpaque(true);
        rectangleLabel.setBackground(new Color(139, 69, 19)); // Brown color
        rectangleLabel.setPreferredSize(new Dimension(150, 120)); // Rectangle size
        middlePanel.add(rectangleLabel, BorderLayout.CENTER);

        // Add Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(800, 50));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.BLACK);

        JButton confirmButton = new JButton("Confirm");
        styleButton(confirmButton);
        JLabel turnLabel = new JLabel("Player 1's Turn");
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton startOverButton = new JButton("Start Over");
        styleButton(startOverButton);
        JButton ruleButton = new JButton("Rule");
        styleButton(ruleButton);

        controlPanel.add(turnLabel);
        controlPanel.add(confirmButton);
        controlPanel.add(startOverButton);
        controlPanel.add(ruleButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> {

            if (selectedButtons.isEmpty()) {
                ImageIcon icon = new ImageIcon("icon.jpeg");
                JOptionPane.showMessageDialog(frame, "No lights selected. Make a move!", "Message",
                        JOptionPane.INFORMATION_MESSAGE, icon);
                return;
            }

            // Check if the player is attempting to turn off the top floor (floor 7)
            if (selectedFloor == 7) {
                if (!ChristmasTree.isAllOnExceptTop()) {
                    ImageIcon icon = new ImageIcon("icon.jpeg");
                    JOptionPane.showMessageDialog(frame,
                            "You cannot turn on the top floor while other lights are still on! Move undone.",
                            "Message", JOptionPane.INFORMATION_MESSAGE, icon);
                    for (JButton button : selectedButtons) {
                        isOffLightButton(button);
                    }
                    selectedButtons.clear();
                    selectedFloor = -1;

                } else {
                    for (JButton button : selectedButtons) {
                        isOnLightButton(button);
                    }
                }
            }

            // Finalize the selected buttons
            for (JButton button : selectedButtons) {
                isOnLightButton(button);
            }
            selectedButtons.clear();
            selectedFloor = -1; // Reset selected floor after confirmation

            // Check for win
            if (ChristmasTree.isAllOn()) {
                JOptionPane.showMessageDialog(frame,
                        "YIPPEEE! Player " + currentPlayer + " wins by turning off the top floor!");

            }

            // Switch to the next player
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            turnLabel.setText("Player " + currentPlayer + "'s Turn");
        });

        startOverButton.addActionListener(e -> {
            // Reset the game state
            selectedButtons.clear();
            selectedFloor = -1;
            currentPlayer = 1;

            // Recreate the building
            ChristmasTree.reset();

            // Clear the frame and reinitialize the GUI
            frame.getContentPane().removeAll();
            setupGUI();

            // Refresh the frame
            frame.revalidate();
            frame.repaint();
        });

        ruleButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "<html>The Christmas Tree Light-On Challenge is a strategic two-player game where<br>" +
                            "players take turns lighting up bulbs on a pyramid-shaped Christmas tree.<br>" +
                            "Each turn, a player can light up one or multiple bulbs, but only from a single floor.<br>"
                            +
                            "The top bulb cannot be lit until all other bulbs on the tree are illuminated.<br>" +
                            "The player who successfully lights up the top bulb wins the game!</html>",
                    "Game Rules",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Resets the button to the unselected state.
     * Updates the visual appearance and clears the selected buttons list if empty.
     *
     * @param lightButton the JButton to unselect
     */
    private void unselectButton(JButton lightButton) {
        isOffLightButton(lightButton);

        // Reset the selected floor if no buttons are selected
        if (selectedButtons.isEmpty()) {
            selectedFloor = -1;
        }
    }

    /**
     * Styles a light button to represent the "on" state.
     *
     * @param lightButton the JButton to style
     */
    private void isOffLightButton(JButton lightButton) {
        lightButton.setBackground(new Color(0, 179, 44)); // Set green background
        lightButton.setForeground(Color.BLACK);
        lightButton.setOpaque(true);
        lightButton.setContentAreaFilled(true);
        lightButton.setBorderPainted(false);
        lightButton.setPreferredSize(new Dimension(60, 70));

    }

    /**
     * Styles a light button to represent the "off" state.
     *
     * @param button the JButton to style
     */
    private void isOnLightButton(JButton button) {
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.GRAY);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setEnabled(false);
    }

    /**
     * Styles a light button to represent the "selected" state.
     *
     * @param lightButton the JButton to style
     */
    private void isSelectedLightButton(JButton lightButton) {
        lightButton.setForeground(Color.ORANGE);
        lightButton.setBackground(Color.ORANGE);
        lightButton.setOpaque(true);
        lightButton.setContentAreaFilled(true);
        lightButton.setBorderPainted(false);
        lightButton.setEnabled(true);
    }

    /**
     * Applies consistent styling to control buttons (e.g., Confirm, Start Over,
     * Rule).
     *
     * @param button the JButton to style
     */
    private void styleButton(JButton button) {
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

}
