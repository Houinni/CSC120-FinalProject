class Light {
    public boolean isOn;
    public final int floor;
    public final int position;

    

    public Light(int floor, int position) {
        this.isOn=true;
        this.floor=floor;
        this.position=position;
    }

    public boolean isOn() {
        return isOn;
    }

    public void turnOff() {
        this.isOn=false;
    }

    public void turnOn() {
        this.isOn=true;
    }

    public int getFloor() {
        return floor;
    }

    public int getPosition() {
        return position;
    }
}

