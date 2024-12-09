import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Building{
    private final List<List<Light>> building; // List of lights organized by floors
    private final Stack<int[]> history;       // History stack for undo functionality

    public Building(int totalFloors) {
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

    public List<List<Light>> getBuilding() {
        return building;
    }

    public boolean turnOffLight(int floor, int position) {
        if (floor < 0 || floor >= building.size()) {
            return false;
        }
        List<Light> selectedFloor = building.get(floor);
        if (position < 0 || position >= selectedFloor.size()) {
            return false;
        }
        Light light = selectedFloor.get(position);
        if (!light.isOn()) {
            return false;
        }
        light.turnOff();
        history.push(new int[]{floor, position}); // Record the move
        return true;
    }

    public boolean turnOffAllOnFloor(int floor) {
        if (floor < 0 || floor >= building.size()) {
            return false;
        }
        List<Light> selectedFloor = building.get(floor);
        boolean anyTurnedOff = false;
        for (int i = 0; i < selectedFloor.size(); i++) {
            Light light = selectedFloor.get(i);
            if (light.isOn()) {
                light.turnOff();
                history.push(new int[]{floor, i}); // Record the move
                anyTurnedOff = true;
            }
        }
        return anyTurnedOff;
    }

    public boolean undoLastMove() {
        if (history.isEmpty()) {
            return false;
        }
        int[] lastMove = history.pop();
        int floor = lastMove[0];
        int position = lastMove[1];
        Light light = building.get(floor).get(position);
        light.turnOn(); // Reverse the move by turning the light back on
        return true;
    }

    public boolean isAllOffExceptTop() {
        for (int i = 0; i < building.size() - 1; i++) { // Skip the top floor
            for (Light light : building.get(i)) {
                if (light.isOn()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isAllOff() {
        for (List<Light> floor : building) {
            for (Light light : floor) {
                if (light.isOn()) {
                    return false;
                }
            }
        }
        return true;
    }

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
