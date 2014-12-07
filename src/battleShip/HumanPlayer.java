package battleShip;

/**
 * This class represents a HumanPlayer that
 * can play from a Window.
 * 
 * @author John Detter<john@detter.com>
 *
 */

public final class HumanPlayer extends Player
{
	/**
	 * Create a human object with the given
	 * window for input, board rows and board
	 * columns.
	 * @param window The window to get input from.
	 * @param boardRows How many board rows are there?
	 * @param boardColumns How many board columns are there?
	 */
	public HumanPlayer(Window window, int boardRows, int boardColumns) 
	{
		super(boardRows, boardColumns);
		this.window = window;
	}

	/**
	 * This will use the Window to get
	 * the player's name.
	 * @author mchargo <mike.chargo@sbcglobal.net>
	 */
	@Override
	public void promptForName(int player)
	{
		window.println("Player " + player + " name: ");
		name = window.nextLine();
	}
	
	@Override
	public void myTurn()
	{
		
	}

	/**
	 * Ask the player for their ship board
	 * configuration.
	 * 
	 * @author mchargo <mike.chargo@sbcglobal.net>
	 */
	@Override
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
			shipBoard.print(window);
			window.println("Which ship would you like to place? Options: \n"
					+ word1 + word2 + word3  + word4 + word5);
			int shipType = window.nextInt() - 1;
			switch(shipType)
			{
			case Board.BATTLESHIP:
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
			case Board.AIRCRAFT_CARRIER:
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
			case Board.BOAT:
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
			case Board.SUBMARINE:
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
			case Board.DESTROYER:
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
			int row=window.nextInt() - 1;
			int column=0;
			boolean vertical=false;
			if(row >= 0 && row < shipBoard.getRows())
			{
				window.println(question2);
				String colInput = "";
				while(colInput.equals(""))
				{
					colInput = window.nextLine().toLowerCase();
					column = colInput.charAt(0) - 'a';
					if(column < 0) colInput = "";
				}
				if(column >= 0 && column < shipBoard.getColumns())
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
					window.println("Invalid column number. Please choose column between A-" 
							+ (char)('A' + shipBoard.getColumns()));
					continue;
				}
			}else{
				window.println("Invalid row number. Please choose row between 1-" + shipBoard.getRows());
				continue;
			}

			int errorCode = shipBoard.placePiece(row, column, shipType, vertical);

			switch(errorCode)
			{
			case Board.SHIP_COLLISION_ERROR:
				window.println("Already have placed ship here. Please place current ship elsewhere.");
				break;

			case Board.SHIP_OFF_OF_BOARD_ERROR:
				window.println("Oops! Looks like you tried placing your ship off the board. Please try again.");
				break;

			default:
				// There was no error, so lets mark down which ship was placed.

				switch(shipType)
				{
				case Board.BATTLESHIP:
					shipBattle = false;
					word1 = "";
					break;
				case Board.AIRCRAFT_CARRIER:
					shipAir =  false;
					word2 = "";
					break;
				case Board.BOAT:
					shipBoat = false;
					word3 = "";
					break;
				case Board.SUBMARINE:
					shipSub = false;
					word4 = "";
					break;
				case Board.DESTROYER:
					shipDest = false;
					word5 = "";
					break;
				}
				break;
			}
		}

		// we are ready to play now.
		ready = true;
		
		// hide our board configuration!
		window.clear();
	}

	private Window window; /**< Window that should be used for user io. */ 
}
