package battleShip;

public class BattleShip 
{
	// Make the ships with these pieces!
	// For example:
	// Destroyer (4 pieces):  < * * >
	// Little boat (2 pieces): < >
	
	// or vertically:
	// Destroyer (4 pieces):
	
	// ^
	// *
	// *
	// v
	
	// Little boat (2 pieces):
	
	// ^
	// v
	public static final String bup = "^";
	public static final String bdown = "v";
	public static final String bright = ">";
	public static final String bleft = "<";
	public static final String bhull = "*";
	
	public static final String hit = "X";
	public static final String miss = "m";
	public static final String bnone = " ";
	
	// specific ship lengths:
	public static final int aircraftCarrier = 5;
	public static final int battleship = 4;
	public static final int submarine = 3;
	public static final int destroyer = 3;
	public static final int boat = 2;
	
	static class Player
	{
		public void initilizeBoard(int rows, int columns)
		{
			// create a new board with rows and columns
			board = new String[rows][columns];
			
			// make all of the squares equal to " "
			for(int row = 0;row < rows;row++)
				for(int column = 0;column < columns;column++)
					board[row][column] = bnone;
			
			boardRows = rows;
			boardColumns = columns;
		}
		
		public void printboard()
		{
			for(int row = 0;row < boardRows;row++)
			{
				for(int column = 0;column < boardColumns;column++)
				{
					if(column == 0) System.out.print("|");
					System.out.print(board[row][column] + "|");
				}
				
				System.out.println();
			}
		}
		
		public void placeShipPiece(int row, int col, String piece)
		{
			board[row][col] = piece;
		}
		
		private int boardRows;
		private int boardColumns;
		public String[][] board; // our board for our player
	}
	
	public static void main(String[] args) 
	{
		// setup player1 so that their board is empty
		Player player1 = new Player();
		player1.initilizeBoard(10, 10);
		
		// setup player2 so that their board is empty
		Player player2 = new Player();
		player2.initilizeBoard(10, 10);
		
		
		// lets print the blank board:
		System.out.println("Player 1 blank board:");
		player1.printboard();
		
		
		// now for example, I will add a destroyer for you.
		System.out.println("\nPlaced destroyer:");
		player1.placeShipPiece(0, 0, bup);
		player1.placeShipPiece(1, 0, bhull);
		player1.placeShipPiece(2, 0, bhull);
		player1.placeShipPiece(3, 0, bhull);
		player1.placeShipPiece(4, 0, bdown);
		player1.printboard();
	}
}
