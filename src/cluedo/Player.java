package cluedo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the a user-controlled player in the game.
 *
 * @author Shenbo Xuan 300259386
 *
 */
public class Player implements Token {

	/**
	 * Represent a unique identifier of a player, should be no duplications.
	 */
	private int uid;

	/**
	 * Represent a name of a Player
	 */
	private String name;

	/**
	 * Represent a hand of cards. Is dealt in the beginning of the game evenly
	 * to each player.
	 */
	private List<Card> hand;

	/**
	 * Represent the x position of the player.
	 */
	private int posX;

	/**
	 * Represent the y position of the player.
	 */
	private int posY;

	/**
	 * Represent the number of steps remains in the current turn.
	 */
	private int stepsRemain;

	/**
	 * Represent if the player has already made a suggestion after the last move.
	 */
	private boolean suggested;

	/**
	 * Construct a player by the given uid.
	 * @param uid
	 */
	public Player(int uid) {
		this.uid = uid;
		this.name = initName(uid);
		this.hand = new ArrayList<Card>();
		suggested = false;
	}

	/**
	 * Provide a human read-able hand of cards
	 * @return
	 */
	public String handToString() {
		String string = "[";
		for (Card card : hand) {
			string = string + card.toString() + ", ";
		}
		string = string.substring(0, string.length() - 2);
		string += "]";
		return string;
	}

	/**
	 * Set suggested field to false.
	 */
	public void resetSuggeted() {
		suggested = false;
	}

	/**
	 * Set suggested field to true.
	 */
	public void setSuggested() {
		suggested = true;
	}

	/**
	 * Return true if the player has suggested after the last move,
	 * return false otherwise.
	 * @return
	 */
	public boolean hasSuggested() {
		return suggested;
	}

	/**
	 * Return the number of steps remain in the current turn.
	 * @return
	 */
	public int getStepsRemain() {
		return stepsRemain;
	}

	/**
	 * Set the number of steps remain in the current turn.
	 * @param roll
	 */
	public void setStepsRemain(int roll) {
		stepsRemain = roll;
	}

	/**
	 * Return a list a Card that is possessed by the player.
	 * @return
	 */
	public List<Card> getHand() {
		return hand;
	}

	/**
	 * Return true if the player has the given card. false otherwise.
	 * @param card
	 * @return
	 */
	public boolean hasCard(Card card) {
		return hand.contains(card);
	}

	/**
	 * Return the uid of the player.
	 * @return
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Add the given card to the player's hand.
	 * @param card
	 */
	public void addCard(Card card) {
		hand.add(card);
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
	public String getName() {
		return name;
	}

	@Override
	public Card getCard() {
		switch (uid) {
		case 1:
			return CluedoGame.CharacterEnum.MISS_SCARLETT;
		case 2:
			return CluedoGame.CharacterEnum.COLONEL_MUSTARD;
		case 3:
			return CluedoGame.CharacterEnum.MRS_WHITE;
		case 4:
			return CluedoGame.CharacterEnum.THE_REVEREND_GREEN;
		case 5:
			return CluedoGame.CharacterEnum.MRS_PEACOCK;
		case 6:
			return CluedoGame.CharacterEnum.PROFESSOR_PLUM;
		default:
			throw new GameError("Invalid uid: " + uid);
		}
	}

	/**
	 * Initialise the name of the player by the given uid.
	 * @param uid
	 * @return
	 */
	private String initName(int uid) {
		switch (uid) {
		case 1:
			return "MISS SCARLETT";
		case 2:
			return "COLONEL MUSTARD";
		case 3:
			return "MRS WHITE";
		case 4:
			return "THE REVEREND GREEN";
		case 5:
			return "MRS PEACOCK";
		case 6:
			return "PROFESSOR PLUM";
		default:
			throw new GameError("Invalid uid: " + uid);
		}
	}
}
