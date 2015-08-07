package cluedo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represent a room on a board. When a player in a game, he/she can
 * make suggestions. A Room may have one or more entrances. A Room may
 * be connected to another Room if there is a stairwell.
 *
 * @author Shenbo Xuan 300259386
 *
 */
public class Room {

	/**
	 * Represent a short name of the room by a char.
	 */
	private char shortName;

	/**
	 * Represent all the Positions the room contains.
	 */
	private List<Position> positions;

	/**
	 * Represent all the Entrances the room has.
	 */
	private List<Entrance> entrances;

	/**
	 * Represent a full name of a room.
	 */
	private String name;

	/**
	 * A room that is connected by a stairwell with this room.
	 */
	private Room stairwellTo;

	/**
	 * Construct a Room by a give shorName
	 * @param shortName
	 */
	public Room(char shortName) {
		this.shortName = shortName;
		positions = new ArrayList<Position>();
		entrances = new ArrayList<Entrance>();
		setName();
	}

	/**
	 * initialise room names by their short names (a char on board.txt)
	 */
	private void setName() {
		switch (shortName) {
		case 'K':
			name = "KITCHEN";
			break;
		case 'B':
			name = "BALL ROOM";
			break;
		case 'C':
			name = "CONSERVATORY";
			break;
		case 'I':
			name = "BILLIARD ROOM";
			break;
		case 'L':
			name = "LIBRARY";
			break;
		case 'S':
			name = "STUDY";
			break;
		case 'H':
			name = "HALL";
			break;
		case 'O':
			name = "LOUNGE";
			break;
		case 'N':
			name = "DINING ROOM";
			break;
		default:
			throw new GameError("Invalid room short name: " + shortName);
		}
	}

	/**
	 * Return the short name of the room.
	 * @return
	 */
	public char getShortName() {
		return shortName;
	}

	/**
	 * Return the full name of the room.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the list of all Positions contained in the room.
	 * @return
	 */
	public List<Position> getPositions() {
		return positions;
	}

	/**
	 * Return the room that this room is connected to by the stairwell,
	 * return null if there is no such a room.
	 * @return
	 */
	public Room getStairellTo() {
		return stairwellTo;
	}

	/**
	 * Add a Position to the Room.
	 * @param pos
	 */
	public void addPosition(Position pos) {
		positions.add(pos);
	}

	/**
	 * Add an entrance to the Room.
	 * @param entrance
	 */
	public void addEntrance(Entrance entrance) {
		entrances.add(entrance);
	}

	/**
	 * Return a list of all entrances in the room.
	 * @return
	 */
	public List<Entrance> getEntrances() {
		return entrances;
	}

	/**
	 * Return a list of all Entrances' Positions in the Room.
	 * @return
	 */
	public List<Position> getEntrancesPositions() {
		List<Position> positions = new ArrayList<Position>();
		for (Entrance entrance : entrances) {
			positions.add(entrance.getPosition());
		}
		return positions;
	}

	/**
	 * Set the Room connected to this Room by the stairwell.
	 * @param stairwellTo
	 */
	public void setStairwellTo(Room stairwellTo) {
		this.stairwellTo = stairwellTo;
	}

	/**
	 * Return true if the given position is in the room,
	 * return false otherwise.
	 * @param pos
	 * @return
	 */
	public boolean contains(Position pos) {
		return positions.contains(pos);
	}

	/**
	 * A Room should have a corresponding RoomCard which a player can olny
	 * suggest this card when in a room.
	 * @return
	 */
	public CluedoGame.RoomEnum getRoomCard() {
		switch (shortName) {
		case 'K':
			return CluedoGame.RoomEnum.KITCHEN;
		case 'B':
			return CluedoGame.RoomEnum.BALL_ROOM;
		case 'C':
			return CluedoGame.RoomEnum.CONSERVATORY;
		case 'I':
			return CluedoGame.RoomEnum.BILLIARD_ROOM;
		case 'L':
			return CluedoGame.RoomEnum.LIBRARY;
		case 'S':
			return CluedoGame.RoomEnum.STUDY;
		case 'H':
			return CluedoGame.RoomEnum.HALL;
		case 'O':
			return CluedoGame.RoomEnum.LOUNGE;
		case 'N':
			return CluedoGame.RoomEnum.DINING_ROOM;
		default:
			throw new GameError("Invalid room short name: " + shortName);
		}
	}

	/**
	 * Get a random position in a room, every time a player enters a room,
	 * he/she is put in a random position in the room.
	 * @return
	 */
	public Position getRandomPosition() {
		Random random = new Random();
		int index = random.nextInt(positions.size());
		return positions.get(index);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((entrances == null) ? 0 : entrances.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((positions == null) ? 0 : positions.hashCode());
		result = prime * result + shortName;
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
		Room other = (Room) obj;
		if (entrances == null) {
			if (other.entrances != null)
				return false;
		} else if (!entrances.equals(other.entrances))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		if (shortName != other.shortName)
			return false;
		return true;
	}
}
