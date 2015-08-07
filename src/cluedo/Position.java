package cluedo;

/**
 * Represents a position on a game board. Contains the x and y coordinates also the
 * type of the Position. (Type could be a blank space or room or invalid place, each
 * represented by a char).
 * @author Shenbo Xuan 300259386
 *
 */
public class Position {

	/**
	 * Represents the x coordinate, ie row number.
	 */
	private int x;

	/**
	 * Represents the y coordinate, ie column number.
	 */
	private int y;

	/**
	 * Represents the type of the position, can be a blank space, room or invalid place.
	 * Cannot be changed once initialised.
	 */
	public final char type;

	public Position(int x, int y, char type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	/**
	 * Set the x coordinate of the Position.
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the y coordinate of the Position.
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Return the x coordinate of the Position.
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the y coordinate of the Position.
	 * @return
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + x + ", "+ y +"]";
	}
}
