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
	public static final int battleship = 4;
	public static final int aircraftCarrier = 5;
	public static final int boat = 2;
	public static final int submarine = 3;
	public static final int destroyer = 3;

	// (1) Battleship, (2) Aircraft Carrier, (3) Boat, (4) Submarine, (5) Destroyer
	public static final int BATTLESHIP = 0;
	public static final int AIRCRAFT_CARRIER = 1;
	public static final int BOAT = 2;
	public static final int SUBMARINE = 3;
	public static final int DESTROYER = 4;

	public static final int[] lengths = new int[]{4, 5, 2, 3, 3};

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
			window.println("   A B C D E F G H I J");
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
				int piece, boolean vertical)
		{
			int pieceLength = lengths[piece];

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

		public void promptForName(int player)
		{
			window.println("Player " + player + " name: ");
			name = window.nextLine();
		}

		public void setupBoard()
		{
			// lets print the blank board:

			String question1 = "";
			String question2 = "";

			boolean shipBattle = true;
			boolean shipAir = true;
			boolean shipBoat = true;
			boolean shipSub = true;
			boolean shipDest = true;

			String word1 = "(1) Battleship, ";
			String word2 = "(2) Aircraft Carrier, ";
			String word3 = "(3) Boat, ";
			String word4 = "(4) Submarine, ";
			String word5 = "(5) Destroyer";

			while((shipBattle==true||shipAir==true||shipBoat==true||shipSub==true||shipDest==true))
			{
				window.println("Player " + name + " blank board:");
				printboard();
				window.println("Which ship would you like to place? Options: \n"
						+ word1 + word2 + word3  + word4 + word5);
				int shipType = window.nextInt() - 1;
				switch(shipType)
				{
				case BATTLESHIP:
					if(shipBattle==true)
					{
						question1 = "Which row would you like to place your battleship in?";
						question2 = "Which column would you like to place your battleship in?"; 
					}else{
						window.println("You have already placed your battleship. \n"
								+ "Please select a different ship to place.");
						continue;
					}
					break;
				case AIRCRAFT_CARRIER:
					if(shipAir==true)
					{
						question1 = "Which row would you like to place your aircraft carrier in?";
						question2 = "Which column would you like to place your aircraft carrier in?";
					}else{
						window.println("You have already placed your aircaft carrier. \n"
								+ "Please select a different ship to place.");
						continue;
					}
					break;
				case BOAT:
					if(shipBoat==true)
					{
						question1 = "Which row would you like to place your boat in?";
						question2 = "Which column would you like to place your boat in?";
					}else{
						window.println("You have already placed your boat. \n"
								+ "Please select a different ship to place");
						continue;
					}
					break;
				case SUBMARINE:
					if(shipSub==true)
					{
						question1 = "Which row would you like to place your submarine in?";
						question2 = "Which column would you like to place your submarine in?";
					}else{
						window.println("You have already placed your submarine. \n"
								+ "Please select a different ship to place");
						continue;
					}
					break;
				case DESTROYER:
					if(shipDest==true)
					{
						question1 = "Which row would you like to place your destroyer in?";
						question2 = "Which column would you like to place your destroyer in?";
					}else{
						window.println("You have already placed your destroyer. \n"
								+ "Please select a different ship to place");
						continue;
					}
					break;
				default:
					if(!(shipType>=1||shipType<=5))
					{
						window.println("Invalid ship selection. Please enter a number 1-5.");
					}
				}

				window.println(question1);
				int row=window.nextInt();
				int column=0;
				boolean vertical=false;
				if(row>=1||row<=10)
				{
					window.println(question2);
					column =window.nextInt();
					if(column>=1||column<=10)
					{
						window.println("Would you like the piece to be vertical? (Y/n)");
						String orientation=window.nextLine();
						if(orientation.equals("Y")||orientation.equals("y"))
						{
							vertical=true;
						}else if(orientation.equals("N")||orientation.equals("n"))
						{
							vertical=false;
						}else{
							window.println("Invalid input. Please enter Y/y or N/n");
							continue;
						}
					}else{
						window.println("Invalid column number. Please choose column between 1-10");
					}
				}else{
					window.println("Invalid row number. Please choose row between 1-10.");
				}

				switch(shipType)
				{
				case BATTLESHIP:
					shipBattle = false;
					word1 = "";
					break;
				case AIRCRAFT_CARRIER:
					shipAir =  false;
					word2 = "";
					break;
				case BOAT:
					shipBoat = false;
					word3 = "";
					break;
				case SUBMARINE:
					shipSub = false;
					word4 = "";
					break;
				case DESTROYER:
					shipDest = false;
					word5 = "";
					break;
				}
				simplePlacePiece(row, column, shipType, vertical);
				printboard();
			}
		}

		private int boardRows;
		private int boardColumns;
		public String[][] board; // our board for our player
		private String name;
	}

	public static Window window;

	public static void main(String[] args) 
	{
		// initilize window
		window = new Window();

		// setup player1 so that their board is empty
		Player player1 = new Player();
		player1.promptForName(1);
		player1.initilizeBoard(10, 10);
		player1.setupBoard();

		// setup player2 so that their board is empty
		Player player2 = new Player();
		player2.promptForName(2);
		player2.initilizeBoard(10, 10);
		player2.setupBoard();


	}
}
