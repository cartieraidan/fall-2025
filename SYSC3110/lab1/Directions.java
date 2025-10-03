public enum Directions {
    EAST("east"),
    WEST("west"),
    SOUTH("south"),
    NORTH("north"),
    UP("up"),
    DOWN("down");

    private final String name;

    Directions(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

}
