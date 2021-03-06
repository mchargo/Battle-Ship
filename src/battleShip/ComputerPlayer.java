package battleShip;
import java.util.Random;

/**
 * This will implement an artificial
 * player that can play Battleship.
 * @author john
 *
 */

public final class ComputerPlayer extends Player 
{
	/**
	 * Create a new ComputerPlayer object.
	 * @param boardRows
	 * @param boardColumns
	 * @param difficulty What difficulty should we play at?
	 * @see EASY
	 * @see MEDIUM
	 * @see HARD
	 */
	public ComputerPlayer(BattleShip game, int boardRows, int boardColumns, int difficulty) 
	{
		super(game, boardRows, boardColumns);
		this.difficulty = difficulty;

	}

	@Override
	public void promptForName(int player) 
	{
		name = "Computer Player";
	}

	@Override
	public void myTurn()
	{
		Random random = new Random(System.nanoTime());
		boolean turnOver=false;
		switch(difficulty)
		{
		case EASY:

			while(turnOver==false)
			{
				int compGuessRow=random.nextInt(shipBoard.getRows());
				int compGuessCol=random.nextInt(shipBoard.getColumns());
				int compGuess= game.tryGuess(compGuessRow, compGuessCol, this);
				if(compGuess==BattleShip.GUESS_INVALID)
				{
					turnOver=false;
				}else if(compGuess==BattleShip.GUESS_HIT){
					if(BattleShip.window != null)
						BattleShip.window.println("Computer Player got a hit!");
					turnOver=true;
				}else if(compGuess==BattleShip.GUESS_MISS){
					if(BattleShip.window != null)
						BattleShip.window.println("Computer Player missed!");
					turnOver=true;
				}
			}
			break;
		case MEDIUM:
			break;
		case HARD:
			break;
		}
	}

	@Override
	public void setupBoard() 
	{
		Random random = new Random(System.nanoTime());
		switch(difficulty)
		{
		case EASY:
			// setup board for an easy computer opponent.
			int startShip = Board.BATTLESHIP;
			while(startShip<=4)
			{
				int randomCompColumn=random.nextInt(shipBoard.getColumns());
				int randomCompRow=random.nextInt(shipBoard.getRows());
				boolean randomVert=random.nextBoolean();

				if(shipBoard.placePiece(randomCompRow, randomCompColumn, startShip, randomVert)==0)
					startShip++;
			}
			break;
		case MEDIUM:
			break;
		case HARD:
			break;
		}

		ready = true;
	}

	private int difficulty; /**< The current difficulty of the ComputerPlayer. */

	public static final int EASY = 0;
	public static final int MEDIUM = 1;
	public static final int HARD = 2;
}
