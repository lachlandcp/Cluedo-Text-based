package cluedo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Contains the useful information of a game of Cluedo. In particular, the game holds the
 * number of players in the game, alive players and solution etc. Initialise the game but
 * not the board.
 *
 * @author Shenbo Xuan 300259386
 *
 */
public class CluedoGame {

	/**
	 * Represents one of the six characters in the game
	 */
	public enum CharacterEnum implements Card {
		MISS_SCARLETT,
		COLONEL_MUSTARD,
		MRS_WHITE,
		THE_REVEREND_GREEN,
		MRS_PEACOCK,
		PROFESSOR_PLUM;

		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}

	/**
	 * Represents one of the six weapons in the game
	 */
	public enum WeaponEnum implements Card {
		CANDLESTICK,
		DAGGER,
		LEAD_PIPE,
		REVOLVER,
		ROPE,
		SPANNER;

		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}

	/**
	 * Represents one of the nine rooms in the game
	 */
	public enum RoomEnum implements Card {
		KITCHEN,
		BALL_ROOM,
		CONSERVATORY,
		BILLIARD_ROOM,
		LIBRARY,
		STUDY,
		HALL,
		LOUNGE,
		DINING_ROOM;

		public static Card getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}

		@Override
		public String toString() {
			return this.name().replaceAll("_", " ");
		}
	}

	/**
	 * A 2-dimenional array representing the board itself.
	 */
	private Board gameBoard;

	/**
	 * A deck of all 21 cards(All CharacterCard, WeaponCard and RoomCard)
	 * takes away the 3 solution cards.
	 */
	private List<Card> deck;

	/**
	 * Total number of players. Specifically, number of players when the game starts.
	 */
	private int numPlayers;

	/**
	 * The solution of the game. Three cards in total, exactly one card from each card
	 * type (CharacterCard, WeaponCard and RoomCard).
	 */
	private Card[] solution;

	/**
	 * The cards those will not be used in the game. Because each player should have the
	 * same number of cards there might be left-over.
	 */
	private List<Card> unusedCards;

	/**
	 * All the players that are still alive in the game.
	 */
	private List<Player> alivePlayers;

	/**
	 * All the weapons in the game.
	 */
	private List<Weapon> weapons;

	/**
	 * Construct a game of Cluedo.
	 *
	 * @param numPlayers
	 *            --- the number of players. Decided by the user.
	 */
	public CluedoGame(String boardFile, int numPlayers) {
		if (numPlayers < 3 || numPlayers > 6) {
			throw new GameError("Invalid number of players: " + numPlayers);
		}
		this.numPlayers = numPlayers;
		alivePlayers = initPlayers();
		weapons = initWeapons();
		solution = initSolution();
		gameBoard = new Board(this, boardFile);
		deck = initDeck();
		dealCards();
	}

	/**
	 * Return a list of cards that are not used in the game, due to each
	 * player should have the same number of cards.
	 * @return
	 */
	public List<Card> getUnusedCards() {
		return unusedCards;
	}

	/**
	 * Get the list of current alive players in the game.
	 * @return
	 */
	public List<Player> getAlivePlayers() {
		return this.alivePlayers;
	}

	/**
	 * Get the game board of the game.
	 * @return
	 */
	public Board getBoard() {
		return gameBoard;
	}

	/**
	 * Get a list of weapons in the game.
	 * @return
	 */
	public List<Weapon> getWeapons() {
		return weapons;
	}

	/**
	 * Return the solution of the game which is an array of 3 cards, where the
	 * first card is a character, the second is a weapon and the third is a room.
	 * @return
	 */
	public Card[] getSolution() {
		return solution;
	}

	/**
	 * Initialise all the weapons in the game
	 * @return
	 */
	private List<Weapon> initWeapons() {
		List<Weapon> weapons = new ArrayList<Weapon>();
		for (WeaponEnum w : WeaponEnum.values()) {
			Weapon weapon = new Weapon(w.toString());
			weapon.setCard(w);
			weapons.add(weapon);
		}
		return weapons;
	}

	/**
	 * Deal the cards in the game. Specifically, use only the number of cards that can
	 * be evenly distributed to each player. Other cards are taken out from the game.
	 */
	private void dealCards() {
		// take out some cards so each player can have the same number of cards
		int numUnusedCards = deck.size() % numPlayers;
		unusedCards = new ArrayList<Card>();
		for (int i = 0; i < numUnusedCards; i++) {
			unusedCards.add(takeOneFromDeck());
		}

		// deal cards to each player evenly
		int numCardEachPlayer = deck.size() / numPlayers;
		for (Player player : alivePlayers) {
			for (int i = 0; i < numCardEachPlayer; i++) {
				player.addCard(takeOneFromDeck());
			}
		}
	}

	/**
	 * Randomly take one card from the current deck, remove the taken card
	 * from the deck.
	 * @return
	 */
	private Card takeOneFromDeck() {
		Card toBeTaken = deck.get(new Random().nextInt(deck.size()));
		deck.remove(toBeTaken);
		return toBeTaken;
	}

	/**
	 * Create a list of players in the game, the number of players is determained
	 * by use's input.
	 * @return
	 */
	private List<Player> initPlayers() {
		List<Player> playersList = new ArrayList<Player>();
		for (int i = 0; i < numPlayers; i++) {
			playersList.add(new Player(i + 1));
		}
		return playersList;
	}

	/**
	 * Randomly choose exactly one card from each type of card to
	 * form a solution.
	 * @return
	 */
	private Card[] initSolution() {
		solution = new Card[3];
		solution[0] = CharacterEnum.getRandom();
		solution[1] = WeaponEnum.getRandom();
		solution[2] = RoomEnum.getRandom();
		return solution;
	}

	/**
	 * Initialise a deck of cards which is the total 21 cards(CharacterCard,
	 * WeaponCard and RoomCard) taking out the solution. Therefore 18 cards
	 * in total.
	 * @return
	 */
	private List<Card> initDeck() {
		deck = new ArrayList<>();
		deck.addAll(Arrays.asList(CharacterEnum.values()));
		deck.addAll(Arrays.asList(WeaponEnum.values()));
		deck.addAll(Arrays.asList(RoomEnum.values()));
		deck.removeAll(Arrays.asList(solution));
		return deck;
	}
}
