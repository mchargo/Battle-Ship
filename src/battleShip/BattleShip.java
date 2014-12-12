package battleShip;

import battleShip.netgame.Client;

/**
 * This BattleShip class will implement
 * the game BattleShip.
 * @author john
 *
 */

public final class BattleShip 
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
		playing = false;
	}

	public void playAgain()
	{
		player1.clearForNewGame();
		player2.clearForNewGame();
		setupPlayers(player1, player2);
	}

	public void forfeit(Player player)
	{
		Player otherPlayer = player1;
		if(player == player1)
			otherPlayer = player2;

		otherPlayer.opponentLeftGame(playing);
	}

	/**
	 * Play a human vs computer game.
	 */
	public void humanVScomputer(int difficulty)
	{
		setupPlayers(new HumanPlayer(this, window, rows, columns),
				new ComputerPlayer(this, rows, columns, difficulty));
	}

	/**
	 * Computer vs computer game. (testing)
	 */
	public void computerVScomputer(int difficulty1, int difficulty2)
	{
		setupPlayers(new ComputerPlayer(this, rows, columns, difficulty1),
				new ComputerPlayer(this, rows, columns, difficulty2));
	}

	/**
	 * Play a human vs human game.
	 */
	public void humanVShuman(Window window)
	{
		setupPlayers(new HumanPlayer(this, window, rows, columns),
				new HumanPlayer(this, window, rows, columns));
	}

	/**
	 * Get the players ready to play the game
	 * the completion of this method requires
	 * that all players are ready to play.
	 */
	private void setupPlayers(Player player1, Player player2)
	{
		this.player1 = player1;
		this.player2 = player2;

		System.out.print("Setting up player 1...\t\t");
		player1.readyPlayer(1);
		System.out.println("[ OK ]");

		System.out.print("Setting up player 2...\t\t");
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
		Player opponent;
		if(player == player1)
		{
			opponent = player2;
			shipBoard = player2.getShipBoard();
		}else{
			opponent = player1;
			shipBoard = player1.getShipBoard();
		}

		int code = shipBoard.guess(row, column); 

		switch(code)
		{
		case GUESS_HIT:
			player.getGuessBoard().placeGuess(row, column, Board.hit);
			String result = shipBoard.checkSunkenShips();

			if(!result.equals(""))
			{
				player1.setNotification(
						player.getName() + " sunk " 
								+ opponent.getName() + "'s " + result);
				player2.setNotification(
						player.getName() + " sunk " 
								+ opponent.getName() + "'s " + result);
			}

			opponent.opponentGuessed(row, column);
			break;
		case GUESS_MISS:
			player.getGuessBoard().placeGuess(row, column, Board.miss);
			opponent.opponentGuessed(row, column);
			break;
		}

		return code;
	}

	/**
	 * Lets play the game!
	 */
	public void play()
	{
		playing = true;
		while(playing)
		{
			player1.myTurn();
			playing=!player2.getShipBoard().gameOver();

			if(!playing)
			{
				playing = false;
				playerLost(player2, player1.getName());
				playerWon(player1, player2.getName());
			}else{
				player2.myTurn();
				playing=!player1.getShipBoard().gameOver();

				if(!playing)
				{
					playing = false;
					playerLost(player1, player2.getName());
					playerWon(player2, player1.getName());
				}
			}
		}

		try
		{
			Thread.sleep(5000);
		}catch(Exception e){}

		if(player1.isReady() && player2.isReady())
		{
			// we both want to play again!
			player1.clearForNewGame();
			player2.clearForNewGame();
			setupPlayers(player1, player2);
		}else{
			player1.donePlaying();
			player2.donePlaying();
		}
	}

	private void playerWon(final Player player, final String otherPlayer)
	{
		Runnable lost = new Runnable()
		{
			public void run()
			{
				player.youWon(otherPlayer);
			}
		};

		new Thread(lost).start();
	}

	private void playerLost(final Player player, final String otherPlayer)
	{
		Runnable lost = new Runnable()
		{
			public void run()
			{
				player.youLost(otherPlayer);
			}
		};

		new Thread(lost).start();
	}

	private Player player1;
	private Player player2;

	private final int rows;
	private final int columns;
	private boolean playing;

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
		window = new Window();

		window.println("Who do you want to play against?");
		window.println("1) Another Human Player");
		window.println("2) Computer Player");
		window.println("3) A Player across a network");

		BattleShip ship = new BattleShip(10,10);
		int selection = window.nextInt();
		boolean local = true;
		switch(selection)
		{
		case 1:
			ship.humanVShuman(window);
			break;
		case 2:
			ship.humanVScomputer(ComputerPlayer.EASY);
			break;
		case 3:
			// non localgame.
			local = false;
			break;
		default:
			return;
		}

		if(local)ship.play();
		else{
			ship = null;
			//window.println("IP Address to connect to:");
			//String address = window.nextLine();
			//window.println("Port to use (8081): ");
			//int port = window.nextInt();
			
			String address = "chetter-games.com";
			int port = 8081;
			
			window.clear();

			Client client = new Client(address, port, window);
			client.connect();
		}
	}
}
