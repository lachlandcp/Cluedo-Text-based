package cluedo;

/**
 * A game error is a simple kind of error message which is used to terminate
 * the game when unexpected behaviour happens during runtime
 *
 * @author Shenbo Xuan 300259386
 *
 */
public class GameError extends RuntimeException {
	public GameError(String msg) {
		super(msg);
	}
}
