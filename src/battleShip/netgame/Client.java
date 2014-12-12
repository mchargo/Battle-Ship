package battleShip.netgame;

import battleShip.BattleShip;
import battleShip.Board;
import battleShip.Window;
import battleShip.net.NetworkClient;
import battleShip.net.NetworkListener;

public class Client implements NetworkListener
{
	public Client(String address, int port, Window window)
	{
		network = new NetworkClient(address, port);
		network.setNetworkListener(this);
		name = "";
		Client.window = window;
	}

	@Override
	public void messageReceived(String message)
	{
		StringBuilder builder = new StringBuilder(message);
		char flag = message.charAt(0);
		builder.deleteCharAt(0);

		int rows;
		int cols;
		
		switch(flag)
		{
		case FINDING_ANOTHER_PLAYER:
			window.println("Looking for another player...");
			break;
		case REQUEST_NAME:
			promptForName(1);
			break;
		case RECEIVE_BOARD_SETTINGS:
			rows = Integer.valueOf(builder.charAt(0) + "" + builder.charAt(1));
			cols = Integer.valueOf(builder.charAt(2) + "" + builder.charAt(3));

			shipBoard = new Board(rows, cols);
			guessBoard = new Board(rows, cols);

			setupBoard();
			break;
		case SEND_ME_YOUR_MOVE:
			myTurn();
			break;
		case DISPLAY_NOTIFICATION:
			notification = builder.toString();
			break;
		case RECEIVE_OPPONENTS_GUESS:
			rows = Integer.valueOf(builder.charAt(0) + "" + builder.charAt(1));
			cols = Integer.valueOf(builder.charAt(2) + "" + builder.charAt(3));
			shipBoard.guess(rows, cols);
			break;
		case OPPONENT_LEFT_GAME:
			window.println("Your opponent has left the game!");
			break;
		case YOU_HAVE_WON:
			window.println("You won!");
			playAgain();
			break;
		case YOU_HAVE_LOST:
			window.println("You lost!");
			playAgain();
			break;
		case OPPONENT_QUIT:
			window.println("You opponent has quit the game!");
			window.println("You won!");
			break;
		case DONE_PLAYING:
			window.println("This game has ended.");
			network.disconnect(null);
			break;
		}
	}

	public void playAgain()
	{
		window.println("Would you like to play again?");
		boolean yes = window.prompt();
		if(yes)network.sendMessage("" + I_WANT_TO_PLAY_AGAIN);
		else network.sendMessage("" + I_DO_NOT_WANT_TO_PLAY_AGAIN);
	}
	
	/**
	 * This will use the Window to get
	 * the player's name.
	 * @author mchargo <mike.chargo@sbcglobal.net>
	 */
	public void promptForName(int player)
	{
		window.println("Player " + player + " name: ");
		name = window.nextLine();
		network.sendMessage(RECIEVE_NAME + "" + name);
	}

	public void youWon(String otherPlayer)
	{
		window.println("Congrats " + name + ", you have conquered " + otherPlayer);

		window.println("Would you like to play again? (Y/n)");
		if(window.prompt())
		{
			// decide later
		}else{
			window.println("Thanks for playing! Hope you play again!");
		}
	}

	public void myTurn()
	{
		window.println(name + ", it is your turn!");

		boolean playerTurn=true;
		while(playerTurn==true)
		{
			shipBoard.print(window);
			guessBoard.print(window);
			if(notification != null)
				if(!notification.equals(""))
					window.println(notification);
			notification = "";
			window.println("Which row would you like to guess?");
			int playerRowGuess=window.nextInt() - 1;
			window.println("Which column would you like to guess?");
			String colInput = "";
			int playerColGuess=0;
			while(colInput.equals(""))
			{
				colInput = window.nextLine().toLowerCase();
				if(colInput.equals("")) continue;
				playerColGuess = colInput.charAt(0) - 'a';
				if(playerColGuess < 0) colInput = "";
			}
			// lets get the code here.
			String extraRow = "";
			String extraCol = "";
			if(playerRowGuess <= 9) extraRow = "0";
			if(playerColGuess <= 9) extraCol = "0";
			String rowS = extraRow + playerRowGuess ;
			String colS = extraCol + playerColGuess;

			network.sendMessage(RECIEVE_GUESS + rowS + colS);

			char result = network.blockForMessage().charAt(0);

			if(result == MOVE_INVALID)
			{
				window.println("Invalid Guess. Please try again.");
				playerTurn=true;
			}else 
			{
				guessBoard.putGuess(playerRowGuess, playerColGuess, result == MOVE_HIT);
				if(result == MOVE_HIT){
					window.println("Congrats! You got a hit!");
					playerTurn=false;
				}else{
					window.println("I'm sorry! You missed!");
					playerTurn=false;
				}
			}
		}
	}

	/**
	 * Ask the player for their ship board
	 * configuration.
	 * 
	 * @author mchargo <mike.chargo@sbcglobal.net>
	 */
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
					if(colInput.equals("")) continue;
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

			// lets get the code here.
			String extraRow = "";
			String extraCol = "";
			if(row < 9) extraRow = "0";
			if(column < 9) extraCol = "0";
			String vert = vertical ? "T" : "F";

			network.sendMessage(RECEIVE_BOARD_CONFIG
					+ (extraRow + row) + (extraCol +column) +  ("" + shipType) + vert);

			String err = network.blockForMessage();
			int flag;

			if(err.length() == 1)
				flag = 0;
			else if(err.charAt(1) == '1')
				flag = 1;
			else flag = 2;

			switch(flag)
			{
			case 1:
				window.println("Already have placed ship here. Please place current ship elsewhere.");
				break;

			case 2:
				window.println("Oops! Looks like you tried placing your ship off the board. Please try again.");
				break;

			default:
				window.println("Unknown error, please try again.");
				break;

			case 0:
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

				shipBoard.placePiece(row, column, shipType, vertical);
				break;
			}
		}

		// we are ready to play now.
		ready = true;

		// hide our board configuration!
		window.clear();
		window.println("Waiting for other player...");
	}

	public void connect()
	{
		window.println("Connecting to host...");
		network.connect();
	}

	private NetworkClient network;

	private String name;

	public boolean isReady(){return ready;}
	public void setName(String name){this.name = name;}
	public String getName(){return name;}

	protected BattleShip game; /**< This is a reference back to the BattleShip game. */
	protected Board guessBoard; /**< This board is used to show the guesses for the player. */
	protected Board shipBoard;/**< This board is used to show the placement of the user's ships.*/
	protected boolean ready; /**< Whether or not the player is ready to play. */
	protected String notification; /**< A message that should be printed at the beginning of a player's turn.*/
	protected int boardRows;
	protected int boardColumns;

	private static Window window;

	// to client flags
	public static final char REQUEST_NAME 				= 'a';
	public static final char RECEIVE_BOARD_SETTINGS		= 'b';
	public static final char SHIP_PLACEMENT_ERROR_CODE 	= 'd';
	public static final char SHIP_PLACEMENT_SUCCESS 	= 'e';
	public static final char SEND_ME_YOUR_MOVE 			= 'f';
	public static final char MOVE_INVALID 				= 'g';
	public static final char MOVE_HIT 					= 'i';
	public static final char MOVE_MISS 					= 'j';
	public static final char DISPLAY_NOTIFICATION		= 'k';
	public static final char SHIP_ALREADY_PLACED		= 'l';
	public static final char RECEIVE_OPPONENTS_GUESS	= 'm';
	public static final char OPPONENT_LEFT_GAME			= 'n';
	public static final char YOU_HAVE_WON				= 'o';
	public static final char YOU_HAVE_LOST				= 'p';
	public static final char OPPONENT_QUIT				= 'q';
	public static final char DONE_PLAYING				= 'r';
	public static final char FINDING_ANOTHER_PLAYER		= 's';
	
	// to server flag
	public static final char RECEIVE_BOARD_CONFIG 		= 'a';
	public static final char RECIEVE_NAME 				= 'b';
	public static final char RECIEVE_GUESS 				= 'c';
	public static final char I_WANT_TO_PLAY_AGAIN		= 'd';
	public static final char I_DO_NOT_WANT_TO_PLAY_AGAIN= 'e';
}
