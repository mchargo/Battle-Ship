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
	public ComputerPlayer(int boardRows, int boardColumns, int difficulty) 
	{
		super(boardRows, boardColumns);
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
