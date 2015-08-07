package cluedo;

public class Weapon implements Token {

	/**
	 * Represent the x position of the Weapon.
	 */
	private int posX;

	/**
	 * Represent the y position of the Weapon.
	 */
	private int posY;

	/**
	 * Represent the full name of the Weapon.
	 */
	private String name;

	/**
	 * Represent the short name of the Weapon.
	 */
	private char shortName;

	/**
	 * Represent the corresponding Card of the Weapon.
	 */
	private Card correspondingCard;

	public Weapon(String name) {
		this.name = name;
		posX = posY = 0;
		setShortName();
	}

	/**
	 * Return the short name of the Weapon.
	 * @return
	 */
	public char getShortName() {
		return shortName;
	}

	/**
	 * Set the corresponding Card of the Weapon.
	 * @param card
	 */
	public void setCard(Card card) {
		this.correspondingCard = card;
	}

	@Override
	public void setX(int x) {
		this.posX = x;
	}

	@Override
	public void setY(int y) {
		this.posY = y;
	}

	@Override
	public int getX() {
		return posX;
	}

	@Override
	public int getY() {
		return posY;
	}

	@Override
	public Card getCard() {
		return correspondingCard;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the short name of the Weapon so it can be shown in the text based UI.
	 */
	private void setShortName() {
		switch (name) {
		case "CANDLESTICK":
			shortName = '+';
			break;
		case "DAGGER":
			shortName = '-';
			break;
		case "LEAD PIPE":
			shortName = '*';
			break;
		case "REVOLVER":
			shortName = '/';
			break;
		case "ROPE":
			shortName = '=';
			break;
		case "SPANNER":
			shortName = '?';
			break;
		default:
			throw new GameError("No such a weapon in the game: " + name);
		}
	}

}
