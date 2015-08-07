package cluedo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Contains the code for interfacing with the Cluedo game. It also contains much
 * of the game logic for controlling how the user can interact.
 *
 * @author Shenbo Xuan 300259386
 */
public class TextClient {

	private static final int ONE_PLAYER_LEFT = 0;
	private static final int WRONG_ANSWER = 1;
	private static final int RIGHT_ANSWER = 2;

	/**
	 * Input a number from the keyboard. The number must be between the min and
	 * max parameters.
	 *
	 * @param min
	 * @param max
	 * @param scanner
	 * @return
	 */
	private static int inputNumber(int min, int max, Scanner scanner) {
		while (true) {
			String x = scanner.nextLine();
			try {
				int answer = Integer.parseInt(x);
				if (answer >= min && answer <= max) {
					return answer;
				}
			} catch (NumberFormatException e) {
			}
			System.out.println("Invalid input!");
		}
	}

	/**
	 * Allow player to make a choice from the available options.
	 *
	 * @param player
	 * @param game
	 */
	private static String playerMakeChoice(Player player, Board board,
			Scanner scanner) {
		// System.out.println(board.toString());
		System.out.println();
		System.out.println("Dear " + player.getName() + "(uid: "
				+ player.getUid() + "):");
		System.out.println("You have " + player.getStepsRemain()
				+ " move(s) left.");
		System.out.println("Please make your choice: ");
		System.out.println();

		// print out the available options for the player
		for (int i = 0; i < optionsList(player, board).size(); i++) {
			System.out
					.println(i + 1 + ") " + optionsList(player, board).get(i));
		}

		// return the corresponding String of the player's decision
		return optionsList(player, board)
				.get((inputNumber(1, optionsList(player, board).size(), scanner)) - 1);
	}

	/**
	 * Update the game status after player's decision he/she just made
	 *
	 * @param player
	 * @param game
	 */
	private static void executePlayerDecision(Player player, Board board,
			Scanner scanner) {
		while (true) {
			String decision = playerMakeChoice(player, board, scanner);
			switch (decision) {
			case "Move North.":
				board.moveNorth(player);
				System.out.println(board.toString());
				break;
			case "Move South.":
				board.moveSouth(player);
				System.out.println(board.toString());
				break;
			case "Move West.":
				board.moveWest(player);
				System.out.println(board.toString());
				break;
			case "Move East.":
				board.moveEast(player);
				System.out.println(board.toString());
				break;
			case "Make a suggestion.":
				Card[] suggestion = makeSuggestion(player, board, scanner);
				Card refutedCard = board.makeSuggestion(player, suggestion);
				checkSuggestion(refutedCard, board);
				break;
			case "Make an accusation.":
				Card[] accusation = makeAccusation(player, board, scanner);
				int result = board.makeAccusation(player, accusation);
				if (anounceResult(result, board)) {
					// Game Over!!!
					scanner.close();
					System.exit(0);
				}
				return;
			case "End this turn.":
				return;
			case "Look at hand.":
				System.out.println(player.handToString());
				break;
			case "Print board notation guide.":
				printBoardNotationGuide();
				break;
			case "Exit Room(Multiple options including use stairwell).":
				Position exitPos = exitRoom(player, board, scanner);
				board.exitRoom(player, exitPos);
				System.out.println(board.toString());
				break;
			default:
				throw new GameError("Invalid option: " + decision);
			}
		}
	}

	private static Position exitRoom(Player player, Board board, Scanner scanner) {
		List<String> optionsList = new ArrayList<String>();
		Room room = board.inWhichRoom(player);
		for (Position pos : room.getEntrancesPositions()) {
			optionsList.add("Exit to " + pos.toString());
		}
		if (room.getStairellTo() != null) {
			optionsList.add("Go to " + room.getStairellTo().getName() + " by stairwell.");
		}
		for (int i = 0; i < optionsList.size(); i++) {
			System.out.println(i + 1 + ") " + optionsList.get(i));
		}
		int answer;
		answer = inputNumber(1, optionsList.size(), scanner);

		if (optionsList.get(answer - 1).startsWith("Exit")) {
			return room.getEntrancesPositions().get(answer - 1);
		} else {
			return room.getStairellTo().getRandomPosition();
		}
	}

	/**
	 * If game over return true otherwise return false
	 *
	 * @param result
	 * @param board
	 * @param scanner
	 * @return
	 */
	private static boolean anounceResult(int result, Board board) {
		switch (result) {
		case ONE_PLAYER_LEFT:
			System.out.println("Wrong answer! Only one player left!!!");
			System.out.println(board.getAlivePlayers().get(0).getName()
					+ " WON!!!");
			return true;
		case WRONG_ANSWER:
			System.out.println("Wrong answer! YOU ARE OUT!!!");
			return false;
		case RIGHT_ANSWER:
			System.out.println("RIGHT ANSWER! YOU WON!!!");
			return true;
		default:
			throw new GameError("Unrecognised accusation result.");
		}
	}

	private static Card[] makeAccusation(Player player, Board board,
			Scanner scanner) {
		Card[] accusation = new Card[3];
		List<Card> optionList;
		int answer;

		// ask player to choose a Character
		optionList = new ArrayList<Card>();
		System.out.println("Choose a Character:");
		for (CluedoGame.CharacterEnum c : CluedoGame.CharacterEnum.values()) {
			optionList.add(c);
		}
		// print out the available options for the player
		for (int i = 0; i < optionList.size(); i++) {
			System.out.println(i + 1 + ") " + optionList.get(i).toString());
		}

		// get the answer and put it as the first element of the accusation
		// array
		answer = inputNumber(1, optionList.size(), scanner);
		accusation[0] = optionList.get(answer - 1);

		// ask player to choose a Weapon
		optionList.clear();
		;
		System.out.println("Choose a Weapon:");
		for (CluedoGame.WeaponEnum w : CluedoGame.WeaponEnum.values()) {
			optionList.add(w);
		}
		// print out the available options for the player
		for (int i = 0; i < optionList.size(); i++) {
			System.out.println(i + 1 + ") " + optionList.get(i).toString());
		}

		// get the answer and put it as the second element of the accusation
		// array
		answer = inputNumber(1, optionList.size(), scanner);
		accusation[1] = optionList.get(answer - 1);

		// ask player to choose a Room
		optionList.clear();

		System.out.println("Choose a Room:");
		for (CluedoGame.RoomEnum r : CluedoGame.RoomEnum.values()) {
			optionList.add(r);
		}
		// print out the available options for the player
		for (int i = 0; i < optionList.size(); i++) {
			System.out.println(i + 1 + ") " + optionList.get(i).toString());
		}

		// get the answer and put it as the third element of the accusation
		// array
		answer = inputNumber(1, optionList.size(), scanner);
		accusation[2] = optionList.get(answer - 1);

		return accusation;
	}

	private static void checkSuggestion(Card refutedCard, Board board) {
		if (refutedCard == null) {
			System.out.println("No one can refuse the suggestion!!!");
			return;
		} else {
			for (Player p : board.getAlivePlayers()) {
				if (p.hasCard(refutedCard)) {
					System.out.println(p.getName() + " has the card "
							+ refutedCard.toString());
					return;
				}
			}
		}
		throw new GameError(
				"No one has the refuted card but suggestion has been refuted.");
	}

	private static Card[] makeSuggestion(Player player, Board board,
			Scanner scanner) {
		Card[] suggestion = new Card[3];
		List<Card> optionList;
		int answer;

		// ask player to choose a Character
		optionList = new ArrayList<Card>();
		System.out.println("Choose a Character:");
		for (CluedoGame.CharacterEnum c : CluedoGame.CharacterEnum.values()) {
			optionList.add(c);
		}
		// print out the available options for the player
		for (int i = 0; i < optionList.size(); i++) {
			System.out.println(i + 1 + ") " + optionList.get(i).toString());
		}

		// get the answer and put it as the first element of the suggestion
		// array
		answer = inputNumber(1, optionList.size(), scanner);
		suggestion[0] = optionList.get(answer - 1);

		// ask player to choose a Weapon
		optionList.clear();
		;
		System.out.println("Choose a Weapon:");
		for (CluedoGame.WeaponEnum w : CluedoGame.WeaponEnum.values()) {
			optionList.add(w);
		}
		// print out the available options for the player
		for (int i = 0; i < optionList.size(); i++) {
			System.out.println(i + 1 + ") " + optionList.get(i).toString());
		}

		// get the answer and put it as the second element of the suggestion
		// array
		answer = inputNumber(1, optionList.size(), scanner);
		suggestion[1] = optionList.get(answer - 1);

		// set the player's current room as the Room
		suggestion[2] = board.getRoomByToken(player).getRoomCard();

		return suggestion;
	}

	/**
	 * Create a list of options that the player currently has.
	 *
	 * @param player
	 * @return
	 */
	private static List<String> optionsList(Player player, Board board) {
		List<String> optionsList = new ArrayList<String>();
		if (player.getStepsRemain() > 0) {
			if (board.canGoNorth(player)) {
				optionsList.add("Move North.");
			}
			if (board.canGoSouth(player)) {
				optionsList.add("Move South.");
			}
			if (board.canGoWest(player)) {
				optionsList.add("Move West.");
			}
			if (board.canGoEast(player)) {
				optionsList.add("Move East.");
			}
		}
		if (board.inRoom(player)) {
			if (player.getStepsRemain() > 0) {
				optionsList.add("Exit Room(Multiple options including use stairwell).");
			}
			if (!player.hasSuggested()) {
				optionsList.add("Make a suggestion.");
			}
		}
		optionsList.add("Make an accusation.");
		if (player.getStepsRemain() == 0) {
			optionsList.add("End this turn.");
		}
		optionsList.add("Look at hand.");
		optionsList.add("Print board notation guide.");
		return optionsList;
	}

	/**
	 * Print out the board notation guide.
	 */
	private static void printBoardNotationGuide() {
		System.out
				.println("------------------------------------------------------");
		System.out.println("digits represent players' tokens by UIDs");
		System.out.println();
		System.out.println("'x' represents an invalid place, "
				+ "no players can go to an invalid place");
		System.out.println();
		System.out.println("'n', 's', 'w' and 'e' represent a door to a room"
				+ ", where: ");
		System.out
				.println("- 'n' means only a \"Move North\" can enter the room");
		System.out
				.println("- 's' means only a \"Move South\" can enter the room");
		System.out
				.println("- 'w' means only a \"Move West\" can enter the room");
		System.out
				.println("- 'e' means only a \"Move East\" can enter the room");
		System.out.println();
		System.out.println("CAPITAL LETTERS represent a room, where: ");
		System.out.println("- 'K' represents (K)itchen");
		System.out.println("- 'B' represents (B)all Room");
		System.out.println("- 'C' represents (C)onservatory");
		System.out.println("- 'I' represents B(I)lliard Room");
		System.out.println("- 'L' represents (L)ibrary");
		System.out.println("- 'S' represents (S)tudy");
		System.out.println("- 'H' represents (H)all");
		System.out.println("- 'O' represents L(O)unge");
		System.out.println("- 'N' represents Di(N)ing Room");
		System.out.println();
		System.out.println("operators represents a weapon, where: ");
		System.out.println("- '+' represents Candlestick");
		System.out.println("- '-' represents Dagger");
		System.out.println("- '*' represents Lead Pipe");
		System.out.println("- '/' represents Revolver");
		System.out.println("- '=' represents Rope");
		System.out.println("- '?' represents Spanner");
		System.out.println("Note weapons are only shown when they are in rooms.");
		System.out.println();
		System.out.println("Note that a door is in a room.");
		System.out.println("Note that a CAPITAL LETTER appears in a corner of "
				+ "another room is a stairwell. For example: ");
		System.out.println("- an 'S' in a (K)itchen is a stairwell to (S)tudy");
		System.out
				.println("------------------------------------------------------");
		System.out.println();
	}

	public static void main(String[] args) {
		// check number of arguments
		if (args.length != 1) {
			System.out.println("Usage: java TextClient boardName.txt");
			System.exit(1);
		}

		String boardName = args[0];
		File file = new File("./" + boardName);

		// check file exsitance
		if (!file.exists()) {
			System.out.println(boardName + " not exist in current directory.");
			System.exit(2);
		}

		// check file type
		if (!boardName.toLowerCase().endsWith(".txt")) {
			System.out.println("Only txt file is accepted.");
			System.exit(3);
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("WELCOME TO THE CLUEDO GAME!!!");
		System.out.println("Please enter the number of players(3-6):");
		int nbOfPlayer = inputNumber(3, 6, scanner);
		CluedoGame game = new CluedoGame(boardName, nbOfPlayer);
		Board board = game.getBoard();

		// now, game starts
		System.out.println("Cards have been dealt!!!");
		System.out.println("You can look your hand at your turn!!!");
		if (!game.getUnusedCards().isEmpty()) {
			System.out.println();
			System.out.println("!!!NOTE THERE ARE UNUSED CARDS IN THIS GAME:");
			for (Card card : game.getUnusedCards()) {
				System.out.println(card.toString());
			}
		}
		System.out.println();

		Random die = new Random();
		while (true) {
			for (Player player : board.getAlivePlayers()) {
				int roll = die.nextInt(6) + 1;
				player.setStepsRemain(roll);
				System.out.println(board.toString());
				System.out
						.println(player.getName() + "(uid: " + player.getUid()
								+ "" + ")" + " rolls a " + roll + ".");
				executePlayerDecision(player, board, scanner);
			}
		}
	}
}
