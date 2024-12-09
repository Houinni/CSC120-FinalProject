import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightPyramidGUI {
    private final Building building;
    private final JFrame frame;
    private int currentPlayer; // 1 for Player 1, 2 for Player 2
    private List<JButton> selectedButtons; // Track selected lights for the current move
    private int selectedFloor; // Track the floor of the current selection

    public LightPyramidGUI() {
        building = new Building(8); // Create an 8-floor building
        frame = new JFrame("Light Pyramid Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        currentPlayer = 1; // Player 1 starts
        selectedButtons = new ArrayList<>();
        selectedFloor = -1; // No floor selected initially
        setupGUI();
        frame.setSize(1500, 1500);
        frame.setVisible(true);
    }

    private void setupGUI() {
        JPanel pyramidPanel = new JPanel();
        pyramidPanel.setBackground(Color.BLACK);
        pyramidPanel.setLayout(new GridLayout(8, 1, 10, 10)); // 8 rows for 8 floors with spacing
    
        // Iterate over the building in reverse order to render floor 7 at the top
        for (int floorIndex = building.getBuilding().size() - 1; floorIndex>=0; floorIndex--) {
            JPanel floorPanel=new JPanel();
            floorPanel.setBackground(Color.BLACK);
            floorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center-align buttons with spacing
            List<Light> floor=building.getBuilding().get(floorIndex);
    
            for (int roomIndex=0; roomIndex<floor.size(); roomIndex++) { // Adjust number of buttons per floor
                JButton lightButton=new JButton();
                lightButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Set white border
                lightButton.setBackground(Color.BLACK); // Set black background
                lightButton.setForeground(Color.WHITE);
                lightButton.setPreferredSize(new Dimension(50, 70)); // Set uniform button size
    
                int roomIndexCopy= roomIndex;
                int floorIndexCopy= floorIndex;
    
                lightButton.addActionListener(e -> {
                    if (selectedButtons.contains(lightButton)) {
                        unselectButton(lightButton);
                    } else {
                        if (selectedFloor==-1 || selectedFloor==floorIndexCopy) {
                            selectedButtons.add(lightButton);
                            lightButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                            lightButton.setForeground(Color.ORANGE);
                            selectedFloor=floorIndexCopy;

                            boolean success= building.turnOffLight(floorIndexCopy, roomIndexCopy);
                           
                            if (!success) {
                                System.out.println("Failed to update building state for floor " + floorIndexCopy + ", room " + roomIndexCopy);
                            }
                            
                        } else {
                            ImageIcon icon= new ImageIcon("icon.jpeg");
                            JOptionPane.showMessageDialog(frame, "You can only turn off lights on one floor per turn!","Message",JOptionPane.INFORMATION_MESSAGE, icon);
                        }
                    }
                });
    
                floorPanel.add(lightButton);
            }
    
            pyramidPanel.add(floorPanel); // Add each floor panel to the pyramid panel
        }
    
        frame.add(pyramidPanel, BorderLayout.CENTER);
    
        // Add Control Panel
        JPanel controlPanel=new JPanel();
        JButton confirmButton=new JButton("Confirm");
        JLabel turnLabel=new JLabel("Player 1's Turn");
        JButton startOverButton=new JButton("Start Over");
    
        confirmButton.addActionListener(e -> {
            
            if (selectedButtons.isEmpty()){
                ImageIcon icon=new ImageIcon("icon.jpeg");
                JOptionPane.showMessageDialog(frame, "No lights selected. Make a move!","Message",JOptionPane.INFORMATION_MESSAGE, icon);
                return;
            }

            // Check if the player is attempting to turn off the top floor (floor 7)
            if (selectedFloor==7){
            // Temporarily turn off the selected buttons to check if the top floor is valid
                if (!building.isAllOffExceptTop()) {
                    ImageIcon icon=new ImageIcon("icon.jpeg");
                    JOptionPane.showMessageDialog(frame, "You cannot turn off the top floor while other lights are still on! Move undone.","Message",JOptionPane.INFORMATION_MESSAGE, icon);
                    for (JButton button : selectedButtons) {
                        button.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Set white border
                        button.setForeground(Color.WHITE);
                        button.setEnabled(true); // Re-enable the button
                    }
                    selectedButtons.clear();
                    selectedFloor=-1;
                    
                } else {
                    for (JButton button : selectedButtons) {
                        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        button.setForeground(Color.GRAY);
                    button.setEnabled(false); // Simulate turning off the light
                    }   
                }
            }

            // Finalize the selected buttons
            for (JButton button : selectedButtons){
                button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                button.setForeground(Color.GRAY);
                button.setEnabled(false); // Disable button after turning off
            }
            selectedButtons.clear();
            selectedFloor=-1; // Reset selected floor after confirmation

            // Check for win
            if (building.isAllOff()){
                JOptionPane.showMessageDialog(frame, "YIPPEEE! Player " + currentPlayer + " wins by turning off the top floor!");
                
            }

            // Switch to the next player
            currentPlayer= currentPlayer== 1 ? 2 : 1;
            turnLabel.setText("Player " + currentPlayer + "'s Turn");
        });
        
        startOverButton.addActionListener(e -> {
            // Reset the game state
            selectedButtons.clear();
            selectedFloor= -1;
            currentPlayer= 1;
    
            // Recreate the building
            building.reset();
    
            // Clear the frame and reinitialize the GUI
            frame.getContentPane().removeAll();
            setupGUI();
    
            // Refresh the frame
            frame.revalidate();
            frame.repaint();
        });

        controlPanel.add(turnLabel);
        controlPanel.add(confirmButton);
        controlPanel.add(startOverButton);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }
          
    private void unselectButton(JButton lightButton) {
        selectedButtons.remove(lightButton); // Remove the button from the selected list
        lightButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Set white border
        lightButton.setForeground(Color.WHITE);
    
        // Debugging message
        System.out.println("Button unselected. Remaining selected buttons: " + selectedButtons.size());
    
        // Reset the selected floor if no buttons are selected
        if (selectedButtons.isEmpty()) {
            selectedFloor= -1;
        }
    }
    
    
}
