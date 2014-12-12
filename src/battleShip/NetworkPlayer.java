package battleShip;

import java.util.Arrays;

import battleShip.net.NetSettings;
import battleShip.net.NetworkClient;
import battleShip.net.NetworkListener;

public class NetworkPlayer extends Player implements NetworkListener
{
	public NetworkPlayer(BattleShip game, int boardRows, int boardColumns,
			NetworkClient client) 
	{
		super(game, boardRows, boardColumns);
		this.client = client;
		client.setNetworkListener(this);
		gotName = false;
		clearForNewGame();
	}
	
	public void connectionLost()
	{
		game.forfeit(this);
	}

	@Override
	public void messageReceived(String message) 
	{
		StringBuilder builder = new StringBuilder(message);
		char flag = builder.charAt(0);
		builder.deleteCharAt(0);

		switch(flag)
		{
		case I_WANT_TO_PLAY_AGAIN:
			gotName = gotBoard = true;
			break;
		case I_DO_NOT_WANT_TO_PLAY_AGAIN:
			gotName = false;
			gotBoard = false;
			break;
		case RECEIVE_BOARD_CONFIG:
			if(gotBoard) return;

			// format <flag = 1><row = 2><col = 2><code = 1><vertical = 1>

			int startRow = Integer.valueOf(builder.charAt(0) + "" + builder.charAt(1));
			int startCol = Integer.valueOf(builder.charAt(2) + "" + builder.charAt(3));
			int shipCode = Integer.valueOf(builder.charAt(4) + "");
			boolean vertical = builder.charAt(5) == 'T';

			boolean alreadyPlaced = false;
			for(int x = 0;x < placedShips.length;x++)
				if(placedShips[x] == shipCode)
					alreadyPlaced = true;
			if(alreadyPlaced) 
			{
				client.sendMessage("" + SHIP_ALREADY_PLACED);
				return;
			}
			int err;
			if((err = shipBoard.placePiece(startRow, startCol, shipCode, vertical)) != 0)
			{
				System.out.println(name + " tried to place a piece wrongly. " + startRow +
						"  " + startCol + "  " + shipCode + "  " + vertical + "  E=" + err);
				client.sendMessage(SHIP_PLACEMENT_ERROR_CODE + "" + err);
			}else{
				System.out.println(name + " placed a piece...");
				client.sendMessage(SHIP_PLACEMENT_SUCCESS + "");
				placedShipCount++;

				if(placedShipCount == Board.SHIP_LENGTHS.length)
				{
					// we are ready!!
					gotBoard = true;
					System.out.println(name + " is ready to play!!");
					return;
				}else placedShips[placedShipCount] = shipCode;
			}			

			break;
		case RECIEVE_NAME:
			if(gotName) return;
			name = builder.toString();
			System.out.println("Received player name: " + name);
			gotName = true;

			setupBoard();
			break;
		default:
			System.out.println("Did nothing with message.");
			break;
		}
	}
	
	@Override
	public void donePlaying()
	{
		client.sendMessage("" + DONE_PLAYING);
		client.disconnect(null);
	}
	
	@Override
	public void opponentLeftGame(boolean playing)
	{
		if(playing)client.sendMessage("" + OPPONENT_QUIT);
		else client.sendMessage("" + OPPONENT_LEFT_GAME);
	}

	@Override
	public boolean isReady()
	{
		return gotName && gotBoard;
	}

	@Override
	public void opponentGuessed(int row, int col)
	{
		String extraRow = "0";
		String extraCol = "0";

		if(row > 9) extraRow = "";
		if(col > 9) extraCol = "";

		client.sendMessage(RECEIVE_OPPONENTS_GUESS + extraRow + row + extraCol + col);
	}

	@Override
	public void myTurn() 
	{
		client.sendMessage("" + SEND_ME_YOUR_MOVE);

		boolean moved = false;
		while(!moved)
		{
			try
			{
				String message = client.blockForMessage();
				StringBuilder builder = new StringBuilder(message);
				if(builder.charAt(0) != RECIEVE_GUESS) continue;
				builder.deleteCharAt(0);

				// format <flag><row = 2><column = 2>
				int row = Integer.valueOf(builder.charAt(0) + "" + builder.charAt(1));
				int column = Integer.valueOf(builder.charAt(2) + "" + builder.charAt(3));

				System.out.println(name + " is guessing " + row + "  " + column);

				notification = null;
				int err;
				if((err = game.tryGuess(row, column, this)) != BattleShip.GUESS_INVALID)
				{
					System.out.println(name + " had a good move.");
					if(err == BattleShip.GUESS_MISS)
						client.sendMessage("" + MOVE_MISS);
					else client.sendMessage("" + MOVE_HIT);
					moved = true;

					if(notification != null)
					{
						client.sendMessage(DISPLAY_NOTIFICATION + "" + notification);
						notification = null;
					}
				}else{
					System.out.println(name + " had a bad move.  E = " + err);
					client.sendMessage("" + MOVE_INVALID);
				}
			}catch(Exception e)
			{
				System.out.println(name + " had a bad move.");
				NetSettings.wtf(e);
				client.sendMessage("" + MOVE_INVALID);
			}
		}
	}

	@Override
	public void clearForNewGame()
	{
		super.clearForNewGame();
		gotBoard = false;
		ready = false;
		placedShips = new int[Board.SHIP_LENGTHS.length];
		placedShipCount = 0;
		Arrays.fill(placedShips, -1);
	}

	@Override 
	public void promptForName(int player) 
	{
		if(!gotName) client.sendMessage("" + REQUEST_NAME);
	}
	@Override 
	public void setupBoard() 
	{
		if(gotName)
		{
			if(!gotBoard) client.sendMessage("" + RECEIVE_BOARD_SETTINGS + 
					shipBoard.getRows() + shipBoard.getColumns());
		}
	}

	private NetworkClient client;

	private boolean gotName;
	private boolean gotBoard;
	private int placedShips[];
	private int placedShipCount;

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
