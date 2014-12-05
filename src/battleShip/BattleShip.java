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
		/**
		 * Put blank tiles into the player's board.
		 * @param rows How many rows are in the game board?
		 * @param columns How many cols are in the game board?
		 */
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
		
		/**
		 * Print out the player's board. This
		 * is a very simple implementation and
		 * can be changed later.
		 */
		public void printboard()
		{
			for(int row = 0;row < boardRows;row++)
			{
				for(int column = 0;column < boardColumns;column++)
				{
					if(column == 0) window.print("|");
					window.print(board[row][column] + "|");
				}
				
				window.println();
			}
		}
		
		/**
		 * Place the given piece onto the board at row,col
		 * @param row The row to place the piece at.
		 * @param col The column to place the piece at.
		 * @param piece The piece type to place.
		 */
		public void placeShipPiece(int row, int col, String piece)
		{
			board[row][col] = piece;
		}
		
		/**
		 * This method will place a piece on the board
		 * for you so that you don't have to worry about
		 * where the pieces are supposed to go.
		 * 
		 * @param startRow Start row for the piece.
		 * @param startCol Start column for the piece.
		 * @param pieceLength How long is the piece?
		 * @param vertical Is the ship vertical or horizontal?
		 * @return Whether or not the ship was placed.
		 */
		public boolean simplePlacePiece(int startRow, int startCol, 
				int pieceLength, boolean vertical)
		{
			boolean success = true;
			int row = startRow;
			int col = startCol;

			try
			{
				for(int x = 0;x < pieceLength;x++)
				{
					if(!board[row][col].equals(bnone))
						throw new Exception("Ship Collision Error");
					
					if(x == 0) board[row][col] = vertical ? bup : bleft;
					else if(x == pieceLength - 1) 
						board[row][col] = vertical ? bdown : bright;
					else board[row][col] = bhull;
					
					if(vertical) row++;
					else col++;
				}
			}catch(Exception e)
			{
				// something went wrong?
				window.println("Error placing piece: " + e);
				success = false;
			}
			
			return success;
		}
		
		private int boardRows;
		private int boardColumns;
		public String[][] board; // our board for our player
	}
	
	public static Window window;
	
	public static void main(String[] args) 
	{
		// initilize window
		window = new Window();
		
		// setup player1 so that their board is empty
		Player player1 = new Player();
		player1.initilizeBoard(10, 10);
		
		// setup player2 so that their board is empty
		Player player2 = new Player();
		player2.initilizeBoard(10, 10);
		
		
		// lets print the blank board:
		window.println("Player 1 blank board:");
		player1.printboard();
		window.println("Which ship would you like to place? Options: (1) Battleship, (2) Aircraft Carrier, (3) Boat, (4) Submarine, (5) Destroyer");
		int shipType = window.nextInt();
		String orientation=window.nextLine();
		if(shipType==1)
		{
			window.println("What row would you like to place your battleship in?");
			int row=window.nextInt();
			if(row>=1||row<=10)
			{
				window.println("What column would you like to place your battleship in?");
				int column =window.nextInt();
				if(column>=1||column<=10)
				{
					window.println("Would you like the piece to be vertical? (Y/n)");
					if(orientation.equals("Y")||orientation.equals("y"))
					{
						boolean vertical=true;
					}else if(orientation.equals("N")||orientation.equals("n"))
					{
						boolean vertical=false;
					}else{
						window.println("Invalid input. Please enter Y/y or N/n");
					}
				}
			}else{
				window.println("Invalid row number. Please choose row between 1-10.");
			}
		}
		if(shipType==2)
		{
			window.println("What row would you like to place your aircraft carrier in?");
		}
		if(shipType==3)
		{
			window.println("What row would you like to place your boat in?");
		}
		if(shipType==4)
		{
			window.println("What row would you like to place your submarine in?");
		}
		if(shipType==5)
		{
			window.println("What row would you like to place your destroyer in?");
		}
		if(!(shipType==1||shipType==2||shipType==3||shipType==4||shipType==5))
		{
			window.println("Invalid ship selection. Please enter a number 1-5.");
		}
		// now for example, I will add a destroyer for you.
		// Use simplePlacePiece instead of placeShipPiece
		window.println("\nAdding a bunch of ships:");
		player1.simplePlacePiece(3, 0, battleship, true);
		player1.simplePlacePiece(5, 5, aircraftCarrier, false);
		player1.simplePlacePiece(9, 5, boat, false);
		player1.simplePlacePiece(5, 2, submarine, true);
		player1.simplePlacePiece(8, 2, destroyer, false);
		player1.printboard();
		
	}
}
