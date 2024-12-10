import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The Building class represents a pyramid-shaped structure with multiple floors
 * of lights.
 * Each floor contains a decreasing number of lights as you move up the pyramid.
 * The class provides functionality to manage the lights, such as turning them
 * on or off,
 * undoing the last move, and checking the state of the lights.
 * It is used in the Christmas Tree Light-On game to manage the game logic.
 */
class ChristmasTree {
    private final List<List<Light>> building; // List of lights organized by floors
    private final Stack<int[]> history; // History stack for undo functionality

    /**
     * Constructs a Building with a specified number of floors.
     * The bottom floor has the most lights, decreasing by one light per floor as
     * you go up.
     *
     * @param totalFloors the total number of floors in the building
     */
    public ChristmasTree(int totalFloors) {
        building = new ArrayList<>();
        history = new Stack<>();
        for (int floorNumber = 0; floorNumber < totalFloors; floorNumber++) {
            int roomsOnFloor = totalFloors - floorNumber; // Bottom floor has the most rooms
            List<Light> floor = new ArrayList<>();
            for (int roomNumber = 0; roomNumber < roomsOnFloor; roomNumber++) {
                floor.add(new Light(floorNumber, roomNumber)); // Zero-based indexing
            }
            building.add(floor);
        }
    }

    /**
     * Retrieves the list of floors in the building.
     *
     * @return a list of floors, each floor containing a list of Light objects
     */
    public List<List<Light>> getBuilding() {
        return building;
    }

    /**
     * Turns on a specific light on a specified floor.
     *
     * @param floor    the floor number (0-based indexing)
     * @param position the position of the light on the floor (0-based indexing)
     * @return true if the light was successfully turned on, false otherwise
     */
    public boolean turnOnLight(int floor, int position) {
        if (floor < 0 || floor >= building.size()) {
            return false;
        }
        List<Light> selectedFloor = building.get(floor);
        if (position < 0 || position >= selectedFloor.size()) {
            return false;
        }
        Light light = selectedFloor.get(position);
        if (!light.isOff()) {
            return false;
        }
        light.turnOn();
        history.push(new int[] { floor, position }); // Record the move
        return true;
    }

    /**
     * Turns off all lights on a specified floor.
     *
     * @param floor the floor number (0-based indexing)
     * @return true if any lights were turned off, false otherwise
     */
    public boolean undoLastMove() {
        if (history.isEmpty()) {
            return false;
        }
        int[] lastMove = history.pop();
        int floor = lastMove[0];
        int position = lastMove[1];
        Light light = building.get(floor).get(position);
        light.turnOff(); // Reverse the move by turning the light back on
        return true;
    }

    /**
     * Turns off all lights on a specified floor.
     *
     * @param floor the floor number (0-based indexing)
     * @return true if any lights were turned off, false otherwise
     */
    public boolean isAllOnExceptTop() {
        for (int i = 0; i < building.size() - 1; i++) { // Skip the top floor
            for (Light light : building.get(i)) {
                if (light.isOff()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Turns on all lights on a specified floor.
     *
     * @param floor the floor number (0-based indexing)
     * @return true if any lights were turned on, false otherwise
     */
    public boolean isAllOn() {
        for (List<Light> floor : building) {
            for (Light light : floor) {
                if (light.isOff()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Resets the building to its initial state, with all lights turned on.
     */
    public void reset() {
        building.clear(); // Clear the existing structure

        for (int floor = 0; floor < 8; floor++) { // Create 8 floors (0 to 7)
            List<Light> floorLights = new ArrayList<>();
            int numLights = 8 - floor; // Start with 8 lights on floor 0, reduce by 1 each floor up
            for (int position = 0; position < numLights; position++) {
                floorLights.add(new Light(floor, position)); // Pass floor and position to the constructor
            }
            building.add(floorLights); // Add the floor to the building
        }
    }

}
