package cluedo;

/**
 * Represents a token on a board. A token is an object in the game that can be
 * moved.
 * @author Shenbo Xuan 300259386
 *
 */
public interface Token {

	/**
	 * Set the x position of the Token
	 * @param x
	 */
	public void setX(int x);

	/**
	 * Set the y position of the Token
	 * @param y
	 */
	public void setY(int y);

	/**
	 * Return the x position of the Token
	 * @return
	 */
	public int getX();

	/**
	 * Return the y position of the Token
	 * @return
	 */
	public int getY();

	/**
	 * Return the corresponding Card of the Token
	 * @return
	 */
	public Card getCard();

	/**
	 * Return the full name of the Token
	 * @return
	 */
	public String getName();
}
