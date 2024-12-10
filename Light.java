/**
 * The Light class represents a single light in the building.
 * Each light has a specific position and is associated with a particular floor.
 * Lights can be turned on or off and have properties to track their state and
 * location.
 */
class Light {
    public boolean isOff;
    public final int floor;
    public final int position;

    /**
     * Constructs a Light object with a specified floor and position.
     * The light is initialized in the "off" state.
     *
     * @param floor    the floor number where the light is located
     * @param position the position of the light on the floor
     */
    public Light(int floor, int position) {
        this.isOff = true;
        this.floor = floor;
        this.position = position;
    }

    /**
     * Checks whether the light is currently off.
     *
     * @return true if the light is off, false otherwise
     */
    public boolean isOff() {
        return isOff;
    }

    /**
     * Turns off the light, changing its state to "off."
     */
    public void turnOn() {
        this.isOff = false;
    }

    /**
     * Turns on the light, changing its state to "on."
     */
    public void turnOff() {
        this.isOff = true;
    }

    /**
     * Retrieves the floor number where the light is located.
     *
     * @return the floor number
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Retrieves the position of the light on its floor.
     *
     * @return the position of the light on the floor
     */
    public int getPosition() {
        return position;
    }
}
