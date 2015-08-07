package cluedo.tests;

import org.junit.*;

import cluedo.Board;
import cluedo.Card;
import cluedo.CluedoGame;
import cluedo.Position;
import cluedo.Weapon;
import static cluedo.CluedoGame.CharacterEnum.*;
import static cluedo.CluedoGame.WeaponEnum.*;
import static cluedo.CluedoGame.RoomEnum.*;
import cluedo.GameError;
import cluedo.Player;
import static org.junit.Assert.*;

public class CluedoTests {

	// trying to create a game with only one player
	@Test
	public void invalidNumberOfPlayers() {
		String boardName = "gameBoard.txt";
		try {
			CluedoGame game = new CluedoGame(boardName, 1);
			fail("Invalid number of players.");
		} catch (GameError e) {
		}
	}

	@Test
	public void validMove() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(6);
		int initX = player.getX();
		int initY = player.getY();
		board.moveSouth(player);
		assertTrue(player.getX() == initX + 1);
		assertTrue(player.getY() == initY);
	}

	// trying to move outside the board
	@Test
	public void invalidMove_1() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(6);
		try {
			board.moveNorth(player);
			fail("Cannot move outside the board.");
		} catch (GameError e) {
		}
	}

	// trying to move to a wall
	@Test
	public void invalidMove_2() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(6);
		try {
			board.moveEast(player);
			fail("Cannot move to a wall.");
		} catch (GameError e) {
		}
	}

	// trying to move when the steps have run out
	@Test
	public void invalidMove_3() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(0);
		try {
			board.moveSouth(player);
			fail("Cannot move when stepsRemain is 0.");
		} catch (GameError e) {
		}
	}

	// trying to move to an entrance from a wrong direction
	@Test
	public void invalidMove_4() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setX(4);
		player.setY(17);
		player.setStepsRemain(2);
		try {
			board.moveEast(player);
			fail("Cannot move when stepsRemain is 0.");
		} catch (GameError e) {
		}
	}

	// trying to move to make an ordinary move inside a room
	@Test
	public void invalidMove_5() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setX(4);
		player.setY(19);
		player.setStepsRemain(2);
		try {
			board.moveEast(player);
			fail("Cannot make ordinary move when inside a room.");
		} catch (GameError e) {
		}
	}

	@Test
	public void validSuggestion() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(2);
		player.setX(1);
		player.setY(1);
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = HALL;
		Card[] suggestion = new Card[] { charCard, weaponCard, roomCard };
		try {
			board.makeSuggestion(player, suggestion);
		} catch (GameError e) {
			fail();
		}
	}

	// trying to make a suggestion outside a room
	@Test
	public void invalidSuggestion() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = HALL;
		Card[] suggestion = new Card[] { charCard, weaponCard, roomCard };
		try {
			board.makeSuggestion(player, suggestion);
			fail("Cannot make a suggestion outside a room.");
		} catch (GameError e) {
		}
	}

	// trying to make a suggestion when already made one
	@Test
	public void invalidSuggestion_2() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setSuggested();
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = HALL;
		Card[] suggestion = new Card[] { charCard, weaponCard, roomCard };
		try {
			board.makeSuggestion(player, suggestion);
			fail("Cannot make a suggestion outside a room.");
		} catch (GameError e) {
		}
	}

	@Test
	public void validExitRoom() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setX(11);
		player.setY(1);
		player.setStepsRemain(6);
		try {
			board.exitRoom(player, new Position(12, 7, 'w'));
		} catch (GameError e) {
			fail();
		}
	}

	// trying to exit a room when not in a room
	@Test
	public void invalidExitRoom() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(6);
		try {
			board.exitRoom(player, new Position(12, 7, 'w'));
			fail("Cannot exit a room when not in a room.");
		} catch (GameError e) {
		}
	}

	// trying to exit to a position that is not an entrance or stairwell
	@Test
	public void invalidExitRoom_2() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(6);
		try {
			board.exitRoom(player, new Position(7, 0, ' '));
			fail("Cannot exit a room to a position that's not an entrance or stairwell.");
		} catch (GameError e) {
		}
	}

	// trying to exit when there is no more steps remianing
	@Test
	public void invalidExitRoom_3() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setStepsRemain(0);
		try {
			board.exitRoom(player, new Position(12, 7, 'w'));
			fail("Cannot exit a room when there is no more steps remaining.");
		} catch (GameError e) {
		}
	}

	@Test
	public void validUseStairwell() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		player.setX(1);
		player.setY(1);
		player.setStepsRemain(6);
		try {
			board.exitRoom(player, new Position(1, 5, 'S'));
		} catch (GameError e) {
			fail();
		}
		assertTrue(board.inWhichRoom(player).getName().equals("STUDY"));
	}

	@Test
	public void accusationWrong() {
		String boardName = "gameBoard.txt";
		CluedoGame game = new CluedoGame(boardName, 6);
		Board board = game.getBoard();
		Player player = board.getAlivePlayers().get(0);
		Card[] solution = game.getSolution();
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = HALL;
		if (solution[0].equals(charCard)) {
			charCard = COLONEL_MUSTARD;
		}
		Card[] accusation = new Card[]{charCard, weaponCard, roomCard};
		board.makeAccusation(player, accusation);

		// after wrong accusation, player should be killed
		assertTrue(!board.getAlivePlayers().contains(player));
	}

	// each player should have same number of cards in hand
	@Test
	public void evenNumOfCard() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(0);
		int numOfCards = player.getHand().size();
		for (Player p : board.getAlivePlayers()) {
			assertTrue(p.getHand().size() == numOfCards);
		}
	}

	// the suspect should be put in the room after suggestion
	@Test
	public void playerPosAfterSuggestion() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(2);
		player.setX(1);
		player.setY(1);
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = KITCHEN;
		Card[] suggestion = new Card[] { charCard, weaponCard, roomCard };
		try {
			board.makeSuggestion(player, suggestion);
		} catch (GameError e) {
			fail();
		}
		Player scarlett = board.getAlivePlayers().get(0);
		assertTrue(board.inWhichRoom(scarlett).getName().equals("KITCHEN"));
	}

	// the suspect weapon should be put in the room after suggestion
	@Test
	public void weaponPosAfterSuggestion() {
		Board board = gameBoard();
		Player player = board.getAlivePlayers().get(2);
		player.setX(1);
		player.setY(1);
		CluedoGame.CharacterEnum charCard = MISS_SCARLETT;
		CluedoGame.WeaponEnum weaponCard = DAGGER;
		CluedoGame.RoomEnum roomCard = KITCHEN;
		Card[] suggestion = new Card[] { charCard, weaponCard, roomCard };
		try {
			board.makeSuggestion(player, suggestion);
		} catch (GameError e) {
			fail();
		}
		Weapon weapon = null;
		for (Weapon w : board.getWeapons()) {
			if (w.getName().equals("DAGGER")) {
				weapon = w;
			}
		}
		assertTrue(board.inWhichRoom(weapon).getName().equals("KITCHEN"));
	}

	// if there are 5 initial players in the game, there should be 3 cards not used
	@Test
	public void unusedCardsTest() {
		String boardName = "gameBoard.txt";
		CluedoGame game = new CluedoGame(boardName, 5);
		assertTrue(game.getUnusedCards().size() == 3);
	}

	/**
	 * helper method build a board with 6 players
	 * @return
	 */
	private Board gameBoard() {
		String boardName = "gameBoard.txt";
		CluedoGame game = new CluedoGame(boardName, 6);
		Board board = game.getBoard();
		return board;
	}
}
