import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 7.0
 */
public class Room 
{
    public String description;
    public Map<String,Room> exits;

    //public static final String NORTH = "north";
    //public static final String EAST = "east";

   /**
     * Create a room described "description". Initially, it has no exits. 
     * "description" is something like "a kitchen" or "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
    }
  public Room getExits(String direction){
      return exits.get(direction);
    
    }
    
    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param direction is the enum value for the direction the game supports
     * @param room is the exit you want to set for object to go to
     */
    public void setExit(Directions direction, Room room)
    {
        exits.putIfAbsent(direction.getName(), room);

    }

  
    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }
    
    public void printLongDescription(){
         System.out.println("You are " + this.getDescription());
         
         System.out.print("Exits: ");
         for (String key: exits.keySet()){
                System.out.print(key + " ");
            }
            System.out.println();
}
}
