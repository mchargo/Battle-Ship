package battleShip;

public class BattleShip 
{
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
		player1 = new HumanPlayer(window, rows, columns);
		player1.readyPlayer(1);
		System.out.println("[ OK ]");
		
		System.out.print("Setting up player 2...\t\t");
		player2 = new HumanPlayer(window, rows, columns);
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
	
	public void play()
	{
		// make sure both players are ready!
		if(!(player1.isReady() && player2.isReady()))
			setupPlayers();
	}
	
	private Player player1;
	private Player player2;
	
	private final int rows;
	private final int columns;
	
	private static Window window;

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
