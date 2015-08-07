package cluedo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Contains the current state of the game, specifically, stores what and where
 * everthing is. Calculation of the game is all done by the board for example
 * the validity of a move and the winning condition. Is the main part of the
 * program.
 *
 * @author Shenbo Xuan 300259386
 *
 */
public class Board {

	/**
	 * Represents the state where only one player is still alive after an
	 * accusation.
	 */
	private static final int ONE_PLAYER_LEFT = 0;

	/**
	 * Represents the accusation does not match the solution.
	 */
	private static final int WRONG_ANSWER = 1;

	/**
	 * Represents the accusation matches the solution.
	 */
	private static final int RIGHT_ANSWER = 2;

	/**
	 * Represents the height of the board.
	 */
	private int height;

	/**
	 * Represents the width of the board.
	 */
	private int width;

	/**
	 * A list of Positions that forms a board
	 */
	private List<Position> board;

	/**
	 * A list of the rooms on the board.
	 */
	private List<Room> roomsList;

	/**
	 * Represents the current players in the game. That is, the player joined
	 * the game when the game starts, and did not loose because of a false
	 * accusation.
	 */
	private List<Player> alivePlayers;

	/**
	 * Represents a list of weapons in the game.
	 */
	private List<Weapon> weapons;

	/**
	 * The solution to the game.
	 */
	private Card[] solution;

	/**
	 * A Cluedo game board is created by a CluedoGame and a board file which
	 * must be a txt file.
	 *
	 * @param game
	 * @param boardFile
	 */
	public Board(CluedoGame game, String boardFile) {
		alivePlayers = game.getAlivePlayers();
		weapons = game.getWeapons();
		board = new ArrayList<Position>();
		roomsList = new ArrayList<Room>();
		solution = game.getSolution();

		// read the board file and initialise the board one char each time
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(boardFile));
			for (height = 0; scanner.hasNextLine(); height++) {
				char[] lineArray = scanner.nextLine().toCharArray();
				for (width = 0; width < lineArray.length; width++) {
					Position pos;
					// if it's a digit, make it as the initial position of the
					// corresponding character, also make the board consider the
					// position as a blank space
					if (Character.isDigit(lineArray[width])) {
						for (Player p : alivePlayers) {
							if (p.getUid() == Character
									.getNumericValue(lineArray[width])) {
								p.setX(height);
								p.setY(width);
							}
						}
						pos = new Position(height, width, ' ');
					} else {
						pos = new Position(height, width, lineArray[width]);
					}
					board.add(pos);
				}
			}
		} catch (IOException ex) {
			System.out.println("Error processing file: " + ex);
		} finally {
			scanner.close();
		}

		creatRooms();
	}

	/**
	 * Get the room by the given position. If the position if not in a room,
	 * return null.
	 *
	 * @param pos
	 * @return
	 */
	public Room getRoomByPosition(Position pos) {
		for (Room r : roomsList) {
			if (r.contains(pos)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Get the room that the token is currently in.
	 *
	 * @param player
	 */
	public Room getRoomByToken(Token token) {
		if (!inRoom(token)) {
			throw new GameError("Given token is not in a room:" + token);
		}

		int x = token.getX();
		int y = token.getY();
		for (Position pos : board) {
			if (pos.getX() == x && pos.getY() == y
					&& Character.isUpperCase(pos.type)) {
				return getRoomByPosition(pos);
			}
		}

		// should not happen
		throw new GameError("Given token is not in a room:" + token);
	}

	/**
	 * Check if a given token is in a room
	 *
	 * @param token
	 * @return
	 */
	public boolean inRoom(Token token) {
		int x = token.getX();
		int y = token.getY();
		for (Position pos : board) {
			if (pos.getX() == x && pos.getY() == y
					&& Character.isUpperCase(pos.type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check which room the given token is currently in. null if not in a room.
	 *
	 * @param token
	 * @return
	 */
	public Room inWhichRoom(Token token) {
		if (inRoom(token)) {
			int x = token.getX();
			int y = token.getY();
			for (Position pos : board) {
				if (pos.getX() == x && pos.getY() == y
						&& Character.isUpperCase(pos.type)) {
					for (Room room : roomsList) {
						if (room.getShortName() == pos.type) {
							return room;
						}
					}
				}
			}
		}
		// if the token is not in a room, return null
		return null;
	}

	/**
	 * true if the given player is able to move to north, false otherwise.
	 *
	 * @param player
	 * @return
	 */
	public boolean canGoNorth(Player player) {
		// can only exit or go to stairwell if in room
		if (inRoom(player)) {
			return false;
		}

		// cannot go north if at northest on board
		if (player.getX() - 1 < 0) {
			return false;
		}

		// cannot go north if north is room or invalid place
		// or it is an entrance which the direction is not north
		int x = player.getX();
		int y = player.getY();
		for (Position pos : board) {
			if (pos.getX() == x - 1
					&& pos.getY() == y
					&& (Character.isUpperCase(pos.type) || pos.type == 'x'
							|| pos.type == 'e' || pos.type == 's' || pos.type == 'w')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * true if the given player is able to move to south, false otherwise.
	 *
	 * @param player
	 * @return
	 */
	public boolean canGoSouth(Player player) {
		// can only exit or go to stairwell if in room
		if (inRoom(player)) {
			return false;
		}

		// cannot go south if at southest on board
		if (player.getX() + 1 >= height) {
			return false;
		}

		// cannot go south if south is room or invalid place
		// or it is an entrance which the direction is not south
		int x = player.getX();
		int y = player.getY();
		for (Position pos : board) {
			if (pos.getX() == x + 1
					&& pos.getY() == y
					&& (Character.isUpperCase(pos.type) || pos.type == 'x'
							|| pos.type == 'n' || pos.type == 'e' || pos.type == 'w')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * true if the given player is able to move to west, false otherwise.
	 *
	 * @param player
	 * @return
	 */
	public boolean canGoWest(Player player) {
		// can only exit or go to stairwell if in room
		if (inRoom(player)) {
			return false;
		}

		// cannot go west if at westest on board
		if (player.getY() - 1 < 0) {
			return false;
		}

		// cannot go west if west is room or invalid place
		// or it is an entrance which the direction is not west
		int x = player.getX();
		int y = player.getY();
		for (Position pos : board) {
			if (pos.getX() == x
					&& pos.getY() == y - 1
					&& (Character.isUpperCase(pos.type) || pos.type == 'x'
							|| pos.type == 'n' || pos.type == 's' || pos.type == 'e')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * true if the given player is able to move to east, false otherwise.
	 *
	 * @param player
	 * @return
	 */
	public boolean canGoEast(Player player) {
		// can only exit or go to stairwell if in room
		if (inRoom(player)) {
			return false;
		}

		// cannot go east if at eastest on board
		if (player.getY() + 1 >= width) {
			return false;
		}

		// cannot go east if east is room or invalid place
		// or it is an entrance which the direction is not east
		int x = player.getX();
		int y = player.getY();
		for (Position pos : board) {
			if (pos.getX() == x
					&& pos.getY() == y + 1
					&& (Character.isUpperCase(pos.type) || pos.type == 'x'
							|| pos.type == 'n' || pos.type == 's' || pos.type == 'w')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Make the given player's position 1 step to the north. Decrease the
	 * remaining steps by one. Reset the suggested field of the player. If at an
	 * entrance after the move, randomly put the player at a position inside the
	 * corresponding room
	 *
	 * @param player
	 */
	public void moveNorth(Player player) {
		if (player.getStepsRemain() <= 0) {
			throw new GameError("Player has run out the steps.");
		}

		if (!canGoNorth(player)) {
			throw new GameError("Player cannot go north.");
		}

		// decrease the steps remain by 1
		player.setStepsRemain(player.getStepsRemain() - 1);

		// decrease the x coordinate by 1
		player.setX(player.getX() - 1);

		// every time a player has made a move, suggested field should be reset.
		player.resetSuggeted();

		// if player is at an entrance of a room, put the player in the room
		if (isAtEntranceOf(player) != null) {
			Room room = isAtEntranceOf(player);
			Position randomPositionInRoom = room.getRandomPosition();
			player.setX(randomPositionInRoom.getX());
			player.setY(randomPositionInRoom.getY());
		}
	}

	/**
	 * Make the given player's position 1 step to the south. Decrease the
	 * remaining steps by one. Reset the suggested field of the player. If at an
	 * entrance after the move, randomly put the player at a position inside the
	 * corresponding room
	 *
	 * @param player
	 */
	public void moveSouth(Player player) {
		if (player.getStepsRemain() <= 0) {
			throw new GameError("Player has run out the steps.");
		}

		if (!canGoSouth(player)) {
			throw new GameError("Player cannot go south.");
		}

		// decrease the steps remain by 1
		player.setStepsRemain(player.getStepsRemain() - 1);

		// increase the x coordinate by 1
		player.setX(player.getX() + 1);

		// every time a player has made a move, suggested field should be reset.
		player.resetSuggeted();

		// if player is at an entrance of a room, put the player in the room
		if (isAtEntranceOf(player) != null) {
			Room room = isAtEntranceOf(player);
			Position randomPositionInRoom = room.getRandomPosition();
			player.setX(randomPositionInRoom.getX());
			player.setY(randomPositionInRoom.getY());
		}
	}

	/**
	 * Make the given player's position 1 step to the west. Decrease the
	 * remaining steps by one. Reset the suggested field of the player. If at an
	 * entrance after the move, randomly put the player at a position inside the
	 * corresponding room
	 *
	 * @param player
	 */
	public void moveWest(Player player) {
		if (player.getStepsRemain() <= 0) {
			throw new GameError("Player has run out the steps.");
		}

		if (!canGoWest(player)) {
			throw new GameError("Player cannot go west.");
		}

		// decrease the steps remain by 1
		player.setStepsRemain(player.getStepsRemain() - 1);

		// decrease the y coordinate by 1
		player.setY(player.getY() - 1);

		// every time a player has made a move, suggested field should be reset.
		player.resetSuggeted();

		// if player is at an entrance of a room, put the player in the room
		if (isAtEntranceOf(player) != null) {
			Room room = isAtEntranceOf(player);
			Position randomPositionInRoom = room.getRandomPosition();
			player.setX(randomPositionInRoom.getX());
			player.setY(randomPositionInRoom.getY());
		}
	}

	/**
	 * Make the given player's position 1 step to the east. Decrease the
	 * remaining steps by one. Reset the suggested field of the player. If at an
	 * entrance after the move, randomly put the player at a position inside the
	 * corresponding room
	 *
	 * @param player
	 */
	public void moveEast(Player player) {
		if (player.getStepsRemain() <= 0) {
			throw new GameError("Player has run out the steps.");
		}

		if (!canGoEast(player)) {
			throw new GameError("Player cannot go east.");
		}

		// decrease the steps remain by 1
		player.setStepsRemain(player.getStepsRemain() - 1);

		// increase the y coordinate by 1
		player.setY(player.getY() + 1);

		// every time a player has made a move, suggested field should be reset.
		player.resetSuggeted();

		// if player is at an entrance of a room, put the player in the room
		if (isAtEntranceOf(player) != null) {
			Room room = isAtEntranceOf(player);
			Position randomPositionInRoom = room.getRandomPosition();
			player.setX(randomPositionInRoom.getX());
			player.setY(randomPositionInRoom.getY());
		}
	}

	/**
	 * Make the given player exit a room to the given position. Player's
	 * suggested field is also reset.
	 *
	 * @param player
	 * @param pos
	 */
	public void exitRoom(Player player, Position pos) {
		if (!inRoom(player)) {
			throw new GameError("Cannot exit a room if not in a room.");
		}

		if (player.getStepsRemain() <= 0) {
			throw new GameError("Cannot exit a room if there if no steps remainning.");
		}

		player.setX(pos.getX());
		player.setY(pos.getY());
		player.resetSuggeted();
	}

	/**
	 * The given player make a suggestion, where the first card is a character,
	 * the second card is a weapon and the third card is the room where the
	 * player is currently in. Suspected character (if in the game) and weapon
	 * should be put in the current room.
	 *
	 * @param player
	 * @param suggestion
	 * @return
	 */
	public Card makeSuggestion(Player player, Card[] suggestion) {
		if (!inRoom(player)) {
			throw new GameError(
					"Cannot make a suggestion if plyer not in a room.");
		}

		if (player.hasSuggested()) {
			throw new GameError(player.getName() + " has already suggested.");
		}

		dragSuspectIn(player, suggestion);
		dragWeaponIn(player, suggestion);
		player.setSuggested();

		Card refutedCard = checkSuggestion(player, suggestion);

		return refutedCard;
	}

	/**
	 * Make an accusation. There are three posible outcomes, if the accusation
	 * matches the solution, the player wins the game. If the accusation does
	 * not match the solution, the player should be kicked out from the game.
	 * Under this situation, if there is only one player is still alive, the
	 * alive player wins the game.
	 *
	 * @param player
	 * @param accusation
	 * @return
	 */
	public int makeAccusation(Player player, Card[] accusation) {
		for (Card accuCard : accusation) {
			for (Card solCard : solution) {
				if (!accuCard.equals(solCard)) {
					// if it's wrong, kill the player
					// create a new list of alive players as .remove method
					// cause problems
					List<Player> temp = new ArrayList<Player>();
					for (Player p : alivePlayers) {
						if (p != player) {
							temp.add(p);
						}
					}
					alivePlayers = temp;

					// if only one player left, we have a winner
					if (alivePlayers.size() == 1) {
						return ONE_PLAYER_LEFT;
					}

					return WRONG_ANSWER;
				}
			}
		}
		return RIGHT_ANSWER;

	}

	/**
	 * Return all the players that are still in the game.
	 *
	 * @return
	 */
	public List<Player> getAlivePlayers() {
		return alivePlayers;
	}

	/**
	 * Retrun all the weapons in the game
	 * @return
	 */
	public List<Weapon> getWeapons() {
		return weapons;
	}

	@Override
	public String toString() {
		// create the board as a string first
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < board.size(); i++) {
			if (i % width == 0) {
				sb.append("\n");
			}
			sb.append(board.get(i).type);
		}
		String boardString = sb.toString().substring(0, sb.length());

		boardString = boardString.replace("\n", "");

		// make the string a 2d char array so we can manipulate with it more
		// easily
		char[][] board2dArray = stringTo2dArray(boardString, width, height);

		// put all alive players on board
		for (Player p : alivePlayers) {
			int x = p.getX();
			int y = p.getY();
			board2dArray[x][y] = Character.forDigit(p.getUid(), 10);
		}

		// put the weapon on board if they are put in a room
		for (Weapon w : weapons) {
			if (inRoom(w)) {
				int x = w.getX();
				int y = w.getY();
				board2dArray[x][y] = w.getShortName();
			}
		}

		sb = new StringBuffer();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(board2dArray[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/*
	 * ============================================================ Below are
	 * some useful helper methods
	 * ============================================================
	 */

	/**
	 * Convert a rectangle-liked string to a 2d array of chars so we can
	 * manipulate with it more easily.
	 *
	 * @param args
	 * @param width
	 * @return
	 */
	private char[][] stringTo2dArray(String string, int width, int height) {
		char[] stringArray = string.toCharArray();
		char[][] matrix = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = stringArray[i * width + j];
			}
		}
		return matrix;
	}

	/**
	 * Create Room objects by the given board. A group of adjacent CAPITAL
	 * letters form a Room.
	 */
	private void creatRooms() {
		// put all CAPITAL chars on the board in a set
		Set<Character> charSet = new HashSet<Character>();
		for (Position pos : board) {
			char c = pos.type;
			if (Character.isUpperCase(c)) {
				charSet.add(c);
			}
		}

		// for each CAPITAL char creat a room
		for (Character shortName : charSet) {
			Room room = new Room(shortName);
			roomsList.add(room);
		}

		addPositionToRooms();
		addEntranceToRooms();
		addStairwells();
	}

	/**
	 * If a position belongs to a room, add it to the room's position list.
	 */
	private void addPositionToRooms() {
		// add positions to the corresponding rooms
		for (Position pos : board) {
			char c = pos.type;
			if (Character.isUpperCase(c)) {
				for (Room r : roomsList) {
					if (r.getShortName() == c) {
						r.addPosition(pos);
					}
				}
			}
		}
	}

	/**
	 * If an entrance belongs to a room, add it to the room's entrance list.
	 */
	private void addEntranceToRooms() {
		// add entrances to the corresponding rooms
		for (Position pos : board) {
			char c = pos.type;
			if (isEntrance(c)) {
				Room room = getRoomByEntrance(pos);
				for (Room r : roomsList) {
					if (room.equals(r)) {
						r.addEntrance(new Entrance(pos, c, room));
					}
				}
			}
		}
	}

	/**
	 * If a room has a stairwell, add it to the both two rooms.
	 */
	private void addStairwells() {
		// add stairwells to the corresponding rooms
		for (Position pos : board) {
			char c = pos.type;
			if (Character.isUpperCase(c)) {
				// find the current room that this position represents to
				Room curRoom = null;
				for (Room r : roomsList) {
					if (r.getShortName() == c) {
						curRoom = r;
					}
				}
				// set each other's stairwellToRoom if there are adjacent
				// different rooms
				Room adjRoom = getAdjacentRoom(pos);
				if (adjRoom != null && !curRoom.equals(adjRoom)) {
					adjRoom.setStairwellTo(curRoom);
					curRoom.setStairwellTo(adjRoom);
				}
			}
		}
	}

	/**
	 * Get the room that the given entrance belongs to.
	 *
	 * @param pos
	 * @return
	 */
	private Room getRoomByEntrance(Position pos) {
		for (Room r : roomsList) {
			if (r.contains(new Position(pos.getX() - 1, pos.getY(), r
					.getShortName()))
					|| r.contains(new Position(pos.getX() + 1, pos.getY(), r
							.getShortName()))
					|| r.contains(new Position(pos.getX(), pos.getY() - 1, r
							.getShortName()))
					|| r.contains(new Position(pos.getX(), pos.getY() + 1, r
							.getShortName()))) {
				return r;
			}
		}
		// this method should only be called when the given position is an
		// entrance
		throw new GameError("This given position " + pos.toString()
				+ " is not an entrance.");
	}

	/**
	 * Get the room that the given position is adjacent to.
	 *
	 * @param pos
	 * @return
	 */
	private Room getAdjacentRoom(Position pos) {
		for (Room r : roomsList) {
			if (r.contains(new Position(pos.getX() - 1, pos.getY(), r
					.getShortName()))
					|| r.contains(new Position(pos.getX() + 1, pos.getY(), r
							.getShortName()))
					|| r.contains(new Position(pos.getX(), pos.getY() - 1, r
							.getShortName()))
					|| r.contains(new Position(pos.getX(), pos.getY() + 1, r
							.getShortName()))) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Check if the char represents an entrance
	 *
	 * @param c
	 * @return
	 */
	private boolean isEntrance(char c) {
		return c == 'n' || c == 's' || c == 'w' || c == 'e';
	}

	/**
	 * Get which room's entrance the given player is currently at, null if not
	 * at any entrances.
	 *
	 * @param player
	 * @return
	 */
	private Room isAtEntranceOf(Player player) {
		for (Room room : roomsList) {
			for (Entrance en : room.getEntrances()) {
				if (en.getX() == player.getX() && en.getY() == player.getY()) {
					return room;
				}
			}
		}
		return null;
	}

	/**
	 * Put the suggested weapon in the room where suggestion is made.
	 *
	 * @param player
	 * @param suggestion
	 */
	private void dragWeaponIn(Player player, Card[] suggestion) {
		Card weaponCard = suggestion[1];
		String weaponName = weaponCard.toString();
		for (Weapon weapon : weapons) {
			if (weapon.getName().equals(weaponName)) {
				Position newPos = inWhichRoom(player).getRandomPosition();
				weapon.setX(newPos.getX());
				weapon.setY(newPos.getY());
			}
		}
	}

	/**
	 * Put the suspect in the room where suggestion is made if the suspect is in
	 * the game.
	 *
	 * @param player
	 * @param suggestion
	 */
	private void dragSuspectIn(Player player, Card[] suggestion) {
		Card suspectCard = suggestion[0];
		String suspectName = suspectCard.toString();
		// check if the suspect is in the game, if so, drag him/her in the room
		for (Player p : alivePlayers) {
			if (p.getName().equals(suspectName)) {
				Position newPos = inWhichRoom(player).getRandomPosition();
				p.setX(newPos.getX());
				p.setY(newPos.getY());
			}
		}
	}

	/**
	 * Once a player has made a suggestion, should check each player's hand see
	 * if the suggestion could be refuted. Each player should respond in a
	 * clock-wise fashion. i.e. if the player who made the suggestion has a uid
	 * of 1, should check in the order of 2, 3, 4, 5, 6. if the player who made
	 * the suggestion has a uid of 2, should check in the order of 3, 4, 5, 6,
	 * 1. Return the refuted card if can refute the suggestion, otherwise return
	 * null.
	 *
	 * @return
	 */
	private Card checkSuggestion(Player player, Card[] suggestion) {
		// check the players whose uid is greater than the suggestion maker's
		// uid first
		for (Player p : alivePlayers) {
			if (player.getUid() > p.getUid()) {
				for (Card card : suggestion) {
					if (p.hasCard(card)) {
						return card;
					}
				}
			}
		}

		// then check whose uid is smaller than the suggestion maker's uid
		for (Player p : alivePlayers) {
			if (player.getUid() < p.getUid()) {
				for (Card card : suggestion) {
					if (p.hasCard(card)) {
						return card;
					}
				}
			}
		}

		// no one can refute the suggestion
		return null;
	}
}
