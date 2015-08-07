package cluedo;

/**
 * Represents an entrance to a Room. An entrance is in the room. An
 * entrance has its position, direction and the room it belongs to.
 * @author Shenbo Xuan 300259386
 *
 */
public class Entrance {

	/**
	 * Represents the position of the entrance.
	 */
	private Position position;

	/**
	 * Represents the direction of the entrance. There are 4 possible
	 * directions, 'n', 's', 'w' and 'e' represent North, South, West
	 * and East respectively. If the direction is north, that means only
	 * a "Move North" can lead the player to the room, otherwise player
	 * cannot move to the entrance.
	 */
	private char direction;

	/**
	 * Represents the room this entrance belongs to.
	 */
	private Room roomBelong;

	/**
	 * Construct an entrance by given position, direction and the room it belongs to.
	 * @param position
	 * @param direction
	 * @param roomBelong
	 */
	public Entrance(Position position, char direction, Room roomBelong) {
		this.position = position;
		this.direction = direction;
		this.roomBelong = roomBelong;
	}

	/**
	 * Return the Position of the entrance.
	 * @return
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Return the direction of the entrance represented by a char.
	 * 'n', 's', 'w' and 's' represent North, South, West and South
	 * respectively.
	 * @return
	 */
	public char getDirection() {
		return direction;
	}

	/**
	 * Return the Room the entrance belongs to.
	 * @return
	 */
	public Room getRoomBelong() {
		return roomBelong;
	}

	/**
	 * Return the x position of the entrance by an int.
	 * @return
	 */
	public int getX() {
		return position.getX();
	}

	/**
	 * Return the y position of the entrance by an int.
	 * @return
	 */
	public int getY() {
		return position.getY();
	}

}
