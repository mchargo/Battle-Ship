package battleShip;

/**
 * This base Player class is used as a
 * template for it's children. All children
 * need to implement
 * @author john
 *
 */

public abstract class Player
{
	/**
	 * Creates a new Player object and initializes
	 * guessBoard, pieceBoard and name.
	 * @param boardRows How many rows are on the board?
	 * @param boardColumns How many columns are on the board?
	 */
	public Player(int boardRows, int boardColumns)
	{
		guessBoard = new Board(boardRows, boardColumns);
		shipBoard = new Board(boardRows, boardColumns);
		name = "";
		ready = false;
	}
	
	/**
	 * Get the user ready for the game. This
	 * includes calling promptForName and
	 * setupBoard. The completion of this
	 * method call does NOT require the
	 * player to be ready, it signifies
	 * that the player has started to get
	 * ready.
	 * 
	 * @param player The player number.
	 * @see isReady
	 */
	public void readyPlayer(int player)
	{
		promptForName(player);
		setupBoard();
	}
	
	/**
	 * Prompt the user for the name they
	 * want to use.
	 * @param player
	 */
	public abstract void promptForName(int player);

	/**
	 * Allow the player to place their pieces
	 * on the game board.
	 */
	public abstract void setupBoard();
	
	public boolean isReady(){return ready;}
	
	public void setName(String name){this.name = name;}
	public String getName(){return name;}

	protected Board guessBoard; /**< This board is used to show the guesses for the player. */
	protected Board shipBoard;/**< This board is used to show the placement of the user's ships.*/
	protected String name; /**< The name of the player.*/
	protected boolean ready; /**< Whether or not the player is ready to play. */
}

