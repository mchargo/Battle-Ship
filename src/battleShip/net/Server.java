package battleShip.net;

import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	public Server(int port)
	{
		this.port = port;
		server = null;
		connected = false;
		listenThread = null;
	}

	public void setServerListener(ServerListener listener)
	{
		this.listener = listener;
	}

	public void connect()
	{
		NetSettings.ver("Starting server...");
		try
		{
			server = new ServerSocket(port);
			NetSettings.verln("[ OK ]");

			connected = true;
			listen();
		}catch(Exception e)
		{
			connected = false;
			NetSettings.errln("[FAIL]");
			NetSettings.wtf(e);
		}
	}

	public void disconnect()
	{
		NetSettings.ver("Disconnecting...\t\t");

		connected = false;
		try{server.close();}catch(Exception e){}
		server = null;
		try{listenThread.interrupt();}catch(Exception e){}
		try{listenThread.join(1000);}catch(Exception e){}
		listenThread = null;

		NetSettings.verln("[ OK ]");
	}

	private void listen()
	{
		if(!connected) return;

		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				while(connected)
				{
					try
					{
						Socket socket = server.accept();
						if(listener != null) 
						{
							NetworkClient client = new NetworkClient(socket);
							client.connect();
							listener.clientConnected(client);
						}
						NetSettings.verln("Client accepted from " + socket.getLocalAddress());
					}catch(Exception e)
					{
						NetSettings.wtf(e);
						disconnect();
					}
				}
			}
		};

		listenThread = new Thread(run);
		listenThread.start();
	}

	private ServerSocket server;
	private boolean connected;
	private Thread listenThread;
	private int port;
	private ServerListener listener;
}
