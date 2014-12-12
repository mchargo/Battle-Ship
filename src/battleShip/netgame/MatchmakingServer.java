package battleShip.netgame;

import battleShip.BattleShip;
import battleShip.ComputerPlayer;
import battleShip.NetworkPlayer;
import battleShip.net.NetworkClient;
import battleShip.net.Server;
import battleShip.net.ServerListener;

public class MatchmakingServer implements ServerListener
{
	public MatchmakingServer(int port, boolean allowSinglePlayer)
	{
		server = new Server(port);
		server.setServerListener(this);
		this.allowSinglePlayer = allowSinglePlayer;
	}

	public void startMatchmaking()
	{
		server.connect();
	}

	@Override
	public void clientConnected(NetworkClient client) 
	{
		// check to see if we have a first client
		if(client1 == null)
		{
			client1 = client;
			System.out.println("We have one client that we need.");
			if(allowSinglePlayer) startGame();
			else client.sendMessage("" + NetworkPlayer.FINDING_ANOTHER_PLAYER);
		}else{
			if(!client1.connected())
			{
				client1 = client;
				System.out.println("Client 1 has been replaced.");
				if(allowSinglePlayer) startGame();
				else client.sendMessage("" + NetworkPlayer.FINDING_ANOTHER_PLAYER);
			}else if(client2 == null)
			{
				client2 = client;
				System.out.println("We should be ready to play now.");
				startGame();
			} else if(!client2.connected())
			{
				client2 = client;
				System.out.println("We are replacing client 2...");
				startGame();
			}else{
				startGame();
				System.out.println("This game should already have been started!!");
			}
		}
	}

	public void startGame()
	{
		System.out.print("Starting a new game...\t\t");

		boolean valid = false;
		if(allowSinglePlayer && client1 != null && client1.connected())
			valid = true;
		if(client1 != null && client1.connected()
				&& client2 != null && client2.connected())
			valid = true;

		if(valid)
		{
			System.out.println("[ OK ]");

			if(allowSinglePlayer) launchGame(client1);
			else launchGame(client1, client2);

			client1 = null;
			client2 = null;
			System.out.println("Ready for more players.");
		}else{
			System.out.println("[FAIL]");
			System.out.println("Not enough players!!");
		}
	}

	private void launchGame(final NetworkClient client)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				BattleShip ship = new BattleShip(10, 10);
				ship.computerVSnetwork(client1, ComputerPlayer.EASY);
				ship.play();
			}
		};
		new Thread(run).start();
	}

	private void launchGame(final NetworkClient client1, final NetworkClient client2)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				BattleShip ship = new BattleShip(10, 10);
				ship.networkVSnetwork(client1, client2);
				ship.play();
			}
		};
		new Thread(run).start();
	}

	private Server server;

	// we need 2 clients to start a game
	private NetworkClient client1;
	private NetworkClient client2;
	private boolean allowSinglePlayer;
}
