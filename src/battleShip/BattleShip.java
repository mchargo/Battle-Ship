package battleShip;

/**
 * This BattleShip class will implement
 * the game BattleShip.
 * @author john
 *
 */

public class BattleShip 
{
	/**
	 * Construct a new BattleShip object.
	 * @param rows How many rows are in the board?
	 * @param columns How many columns are in the board?
	 */
	public BattleShip(int rows, int columns)
	{
		// setup the board specs
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Get the players ready to play the game
	 * the completion of this method requires
	 * that all players are ready to play.
	 */
	public void setupPlayers()
	{
		System.out.print("Setting up player 1...\t\t");
		player1 = new HumanPlayer(this, window, rows, columns);
		player1.readyPlayer(1);
		System.out.println("[ OK ]");

		System.out.print("Setting up player 2...\t\t");
		player2 = new ComputerPlayer(this, rows, columns, ComputerPlayer.EASY);
		player2.readyPlayer(2);
		System.out.println("[ OK ]");

		/** 
		 * Make sure both players are ready, if they aren't
		 * then we should wait 1 second.
		 */

		System.out.print("Waiting for all player to be ready...");
		while(!(player1.isReady() && player2.isReady()))
		{
			try {
				Thread.sleep(1000); // Wait one second (1000ms = 1s)
			} catch (InterruptedException e) {}
		}
		System.out.println("[ OK ]");
	}

	/**
	 * When a player is ready to guess, they will
	 * call this method so they can either get a
	 * hit or a miss.
	 * 
	 * @param row The row we are trying to guess.
	 * @param column The column we are trying to guess
	 * @param player The player that is guessing.
	 */
	public int tryGuess(int row, int column, Player player)
	{
		Board shipBoard;
		if(player == player1)
		{
			shipBoard = player2.getShipBoard();
		}else{
			shipBoard = player1.getShipBoard();
		}

		int code = shipBoard.guess(row, column); 

		switch(code)
		{
		case GUESS_HIT:
			player.getGuessBoard().placeGuess(row, column, Board.hit);
			break;
		case GUESS_MISS:
			player.getGuessBoard().placeGuess(row, column, Board.miss);
			break;
		}

		return code;
	}

	/**
	 * Lets play the game!
	 */
	public void play()
	{
		// make sure both players are ready!
		if(!(player1.isReady() && player2.isReady()))
			setupPlayers();
		boolean gameOver=false;
		while(gameOver==false)
		{
			window.println(player1.getName() + ", it is your turn!");
			player1.myTurn();
			gameOver=player2.getShipBoard().gameOver();

			if(gameOver)
			{
				window.println("Congrats " + player1.getName() + ", you have conquered " + player2.getName());
			}else{
				window.println(player2.getName() + ", it is your turn!");
				player2.myTurn();
				gameOver=player1.getShipBoard().gameOver();
				
				if(gameOver)
				{
					window.println("Congrats " + player2.getName() + ", you have conquered " + player1.getName());
				}
			}
		}
		window.println("Would you like to play again? (Y/n)");
		if(window.prompt())
		{
			BattleShip newGame=new BattleShip(10, 10);
			newGame.setupPlayers();
			newGame.play();
		}else{
			window.println("Thanks for playing! Hope you play again!");
		}
	}

	private Player player1;
	private Player player2;

	private final int rows;
	private final int columns;

	public static Window window;

	public static final int GUESS_MISS = 0;
	public static final int GUESS_HIT = 1;
	public static final int GUESS_INVALID = 2;

	/**
	 * This is the entry point of our program!
	 * @param args Program arguments
	 */
	public static void main(String[] args) 
	{
		// initilize window
		window = new Window();

		// Create our game object from the Battleship class.
		BattleShip game = new BattleShip(10, 10);
		// Make our game setup our players.
		game.setupPlayers();
		// Lets play!
		game.play();
	}
}
