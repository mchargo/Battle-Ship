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
	public Player(BattleShip game, int boardRows, int boardColumns)
	{
		this.game = game;
		this.boardRows = boardRows;
		this.boardColumns = boardColumns;
		clearForNewGame();
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
	 * Notify the player that we can no
	 * longer play =(
	 */
	public void donePlaying(){}
	
	/**
	 * Returns the board with all of the ships on it.
	 * @return The ship board.
	 */
	public Board getShipBoard(){return shipBoard;}
	
	/**
	 * Notify the player that they have won.
	 */
	public void youWon(String otherPlayer){}
	
	/**
	 * Notify the player that the opponent has
	 * left the game.
	 * @param playing Whether or not we were playing
	 * when the opponent left the game.
	 */
	public void opponentLeftGame(boolean playing){}
	
	/**
	 * Notify the player that they lost.
	 */
	public void youLost(String otherPlayer){}
	
	/**
	 * Ask the player if they want to play again
	 * @return Whether or not the player wants to play again
	 */
	public boolean wantsToPlayAgain(){return true;}
	
	/**
	 * Notify the player that the opponent has
	 * guessed here.
	 * @param row The row where the opponent guessed.
	 * @param col The column where the opponent guessed.
	 */
	public void opponentGuessed(int row, int col){}
	
	/**
	 * Clear settings to prepare for a new game.
	 */
	public void clearForNewGame()
	{
		guessBoard = new Board(boardRows, boardColumns);
		shipBoard = new Board(boardRows, boardColumns);
		name = "";
		ready = false;
		notification = "";
	}
	
	/**
	 * Returns the board with all of the player's
	 * guesses on it.
	 * @return
	 */
	public Board getGuessBoard()
	{
		return guessBoard;
	}
	
	/**
	 * Call this when it is the player's turn
	 * to go in the game
	 */
	public abstract void myTurn();
	
	/**
	 * Prompt the user for the name they
	 * want to use.
	 * @param player
	 */
	public abstract void promptForName(int player);

	/**
	 * Set's the player's current notification.
	 * "" means no notification.
	 * @param message the message to set.
	 */
	public void setNotification(String message)
	{
		this.notification = message;
	}
	
	/**
	 * Allow the player to place their pieces
	 * on the game board.
	 */
	public abstract void setupBoard();
	public boolean isReady(){return ready;}
	public void setName(String name){this.name = name;}
	public String getName(){return name;}

	protected BattleShip game; /**< This is a reference back to the BattleShip game. */
	protected Board guessBoard; /**< This board is used to show the guesses for the player. */
	protected Board shipBoard;/**< This board is used to show the placement of the user's ships.*/
	protected String name; /**< The name of the player.*/
	protected boolean ready; /**< Whether or not the player is ready to play. */
	protected String notification; /**< A message that should be printed at the beginning of a player's turn.*/
	protected int boardRows;
	protected int boardColumns;
}

