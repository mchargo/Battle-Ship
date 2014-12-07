package battleShip;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class is a template for a Board
 * object. Every board is represented by
 * a 2D array of chars. The value at each
 * cell determines which character gets
 * printed out.
 * 
 * Use printBoard() to print out the board
 * to a window.
 * 
 * Use placePiece() to place a piece on
 * the board. The return value determines
 * the success or failure of the operation.
 * 
 * @author John Detter<john@detter.com>
 *
 */

public class Board 
{
	/**
	 * Create a new Board object with the
	 * specified amount of rows and columns.
	 * @param rows How many rows the board should have.
	 * @param columns How many columns the board should have.
	 */
	public Board(int rows, int columns)
	{
		// create a new board with rows and columns
		board = new char[rows][columns];

		// make all of the squares equal to ' '
		for(int row = 0;row < rows;row++)
			Arrays.fill(board[row], bnone);

		boardRows = rows;
		boardColumns = columns;
		ships = new LinkedList<Ship>();
	}

	/**
	 * Print the board out to a window.
	 * @param window The window to print to.
	 */
	public void print(Window window)
	{
		// print the column letters
		window.print("   ");
		for(int x = 0;x < boardRows;x++)
			window.print((char)('A' + x) + " ");
		window.println();

		for(int row = 0;row < boardRows;row++)
		{
			for(int column = 0;column < boardColumns;column++)
			{
				if(column == 0) 
				{
					String extra = " ";
					if(row > 8) extra = "";
					window.print((row + 1) + extra + "|");
				}
				window.print(board[row][column] + "|");
			}

			window.println();
		}
	}

	/**
	 * Places the piece on the board. This method
	 * must be given one coordinate (starting
	 * coordinate), the ship code of the ship
	 * that is supposed to be placed and whether
	 * or not the ship is vertical.
	 * 
	 * For example, placePiece(0, 0, AIRCRAFT_CARRIER, false)
	 * would produce:
	 * 
	 * &lt * * * &gt
	 * 
	 * Where the coordinate of '&lt' is (startRow, startCol)
	 * 
	 * @param startRow The start row of the placement.
	 * @param startCol The start column of the placement.
	 * @param shipCode The code that is mapped to the ship type.
	 * @param vertical Whether or not the ship should be placed vertical.
	 * @return The error code, 0 if everything is okay.
	 * @see SHIP_COLLISION_ERROR
	 * @see SHIP_OFF_OF_BOARD_ERROR
	 */
	public int placePiece(int startRow, int startCol, 
			int shipCode, boolean vertical)
	{
		// first test to make sure placement is valid.
		int errorCode = testPiecePlacement(startRow, startCol, shipCode, vertical);
		if(errorCode != 0) return errorCode; // not valid.

		int pieceLength = SHIP_LENGTHS[shipCode];
		ships.add(new Ship(pieceLength, startRow, startCol, vertical, SHIP_NAMES[shipCode]));
		
		for(int x = 0;x < pieceLength;x++)
		{
			if(x == 0) board[startRow][startCol] = vertical ? bup : bleft;
			else if(x == pieceLength - 1) 
				board[startRow][startCol] = vertical ? bdown : bright;
			else board[startRow][startCol] = bhull;

			if(vertical) startRow++;
			else startCol++;
		}

		return 0;
	}

	/**
	 * Tests the board to see if the piece
	 * placement is valid.
	 * 
	 * @param startRow The start row of the placement.
	 * @param startCol The start column of the placement.
	 * @param shipCode The code that is mapped to the ship type.
	 * @param vertical Whether or not the ship should be placed vertical.
	 * @return The error code, 0 if everything is okay.
	 * @see SHIP_COLLISION_ERROR
	 * @see SHIP_OFF_OF_BOARD_ERROR
	 */
	private int testPiecePlacement(int startRow, int startCol, 
			int shipCode, boolean vertical)
	{
		try
		{
			int shipLength = SHIP_LENGTHS[shipCode];

			for(int x = 0;x < shipLength;x++)
			{
				// test currentPosition
				if(board[startRow][startCol] != bnone)
					return 1; // collision occurred
				if(vertical) startRow++;
				else startCol++;
			}
		}catch(IndexOutOfBoundsException e)
		{
			return 2; // There was an out of bounds error.
		}

		return 0; // no error!
	}

	/**
	 * Checks whether or not the game is over
	 * based on the current pieces.
	 * @return Whether or not the game is over.
	 */
	public boolean gameOver()
	{
		for(int row = 0;row < boardRows;row++)
			for(int column = 0;column < boardColumns;column++)
				if(board[row][column] != miss
				&& board[row][column] != bnone
				&& board[row][column] != hit)
					return false;
		return true;
	}
	
	/**
	 * Checks to see if any ships have been
	 * sunked since the last hit.
	 * @return The sunken ship.
	 */
	public String checkSunkenShips()
	{
		for(Ship ship : ships)
		{
			if(ship.isSunk()) continue;
			int row = ship.getRow();
			int col = ship.getCol();
			
			boolean complete = false;
			
			for(int x = 0;x < ship.getLength();x++)
			{
				complete = false;
				if(board[row][col] != hit) break;
				
				if(ship.isVertical()) row++;
				else col++;
				complete = true;
			}
			
			if(complete)
			{
				ship.setSunk(true);
				return ship.getName();
			}
		}
		
		return "";
	}

	/**
	 * Try a guess at the given row and column.
	 * @param guessRow The guess row.
	 * @param guessCol The guess column.
	 * @return The guess code.
	 * @see GUESS_MISS
	 * @see GUESS_HIT
	 * @see GUESS_INVALID
	 */
	public int guess(int guessRow, int guessCol)
	{
		try
		{
			if(board[guessRow][guessCol] != bnone &&
					board[guessRow][guessCol] != miss) 
			{
				board[guessRow][guessCol] = hit;
				return BattleShip.GUESS_HIT;
			}else {
				board[guessRow][guessCol] = miss;
				return BattleShip.GUESS_MISS;
			}
		}catch(Exception e)
		{
			return BattleShip.GUESS_INVALID;
		}
	}

	public void placeGuess(int row, int column, char piece)
	{
		board[row][column] = piece;
	}

	private LinkedList<Ship> ships;
	public int getColumns() {return boardColumns;}
	public int getRows() {return boardRows;}

	private char[][] board;/**< This is a 2D array that represents our game board. */
	private int boardRows; /**< Quick reference to how many rows there are. Same as board.length. */
	private int boardColumns; /**< Quick reference to how many columns there are. Same as board[0].length. */

	public static final char bup = '^'; /**< Up symbol for a ship. */
	public static final char bdown = 'v'; /**< Down symbol for a ship.*/
	public static final char bright = '>'; /**< Right symbol for a ship.*/
	public static final char bleft = '<'; /**< Left symbol for a ship. */
	public static final char bhull = '*'; /**< Hull symbol for a ship. */

	public static final char hit = 'X'; /**< Hit symbol. */
	public static final char miss = 'o'; /**< Miss symbol. */
	public static final char bnone = ' '; /**< Empty spot symbol. */

	/**
	 * Array that represents how many pieces each ship has.
	 * @see BATTLESHIP
	 * @see AIRCRAFT_CARRIER
	 * @see BOAT
	 * @see SUBMARINE
	 * @see DESTROYER
	 */
	public static final int[] SHIP_LENGTHS = new int[]{4, 5, 2, 3, 3};

	public static final String[] SHIP_NAMES = new String[]{
		"Battle Ship",
		"Aircraft Carrier",
		"Boat",
		"Submarine",
		"Destroyer"
	};

	// (1) Battleship, (2) Aircraft Carrier, (3) Boat, (4) Submarine, (5) Destroyer
	public static final int BATTLESHIP = 0;
	public static final int AIRCRAFT_CARRIER = 1;
	public static final int BOAT = 2;
	public static final int SUBMARINE = 3;
	public static final int DESTROYER = 4;

	public static final int SHIP_COLLISION_ERROR = 1;
	public static final int SHIP_OFF_OF_BOARD_ERROR = 2;
}
