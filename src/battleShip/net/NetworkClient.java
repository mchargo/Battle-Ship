package battleShip.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class NetworkClient 
{
	public NetworkClient(String address, int port)
	{
		this.address = address;
		this.port = port;
		listener = null;
		blocking = false;
	}

	public NetworkClient(Socket socket)
	{
		this.socket = socket;
		listener = null;
		blocking = false;
	}

	public boolean connected()
	{
		return connected;
	}

	public void setNetworkListener(NetworkListener listener)
	{
		this.listener = listener;
	}

	public void connect()
	{
		NetSettings.ver("Connecting...\t\t\t");
		try
		{
			if(socket == null)
				socket = new Socket(address, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			NetSettings.verln("[ OK ]");

			blocking = false;
			connected = true;
			listen();
		}catch(Exception e)
		{
			NetSettings.err("[FAIL]");
			NetSettings.wtf(e);
		}
	}

	public void sendMessage(String message)
	{
		NetSettings.ver("Sending message...\t\t\t");
		if(connected)
		{
			try
			{
				out.writeInt(message.length());
				out.write(message.getBytes());
				out.flush();
			}catch(Exception e)
			{
				NetSettings.errln("[FAIL]");
				NetSettings.wtf(e);
			}
		}else NetSettings.errln("[FAIL]");
	}

	public void disconnect(String error)
	{
		NetSettings.ver("Disconnecting...\t\t\t");
		connected = false;
		try{socket.close();}catch(Exception e){}
		try{in.close();}catch(Exception e){}
		try{out.close();}catch(Exception e){}

		socket = null;
		in = null;
		out = null;

		try{listenThread.interrupt();}catch(Exception e){}
		try{listenThread.join(1000);}catch(Exception e){}
		listenThread = null;
		NetSettings.verln("[ OK ]");

		if(error == null) return;
		if(error.equals("")) return;

		NetSettings.errln("Disconnect Message: " + error);
	}

	private void listen()
	{
		NetSettings.ver("Starting to listen...\t\t");
		if(!connected) 
		{
			NetSettings.errln("[FAIL]");
			return;
		}

		Runnable run = new Runnable()
		{
			public void run()
			{
				while(connected)
				{
					try
					{
						// first read packet length
						int packetLength = in.readInt();
						if(!connected) break;

						if(packetLength <= 0 || packetLength > NetSettings.MAX_PACKET)
							disconnect("Malicious Packet Received");

						byte[] data = new byte[packetLength];
						int packetRead = 0;
						while(packetRead < packetLength)
						{
							if(packetRead != 0) Thread.sleep(NetSettings.OVERLOADED_WAIT);
							packetRead = in.read(data, packetRead, packetLength);
						}

						String message = new String(data);
						NetSettings.verln("Message received: " + message);

						if(message.equals("")) continue;
						receiveMessage(message);

						// cleanup
						data = null;
						message = null;
					}catch(Exception e)
					{
						NetSettings.wtf(e);
						disconnect(e.toString());
					}
				}
			}
		};

		listenThread = new Thread(run);
		listenThread.start();
		NetSettings.verln("[ OK ]");
	}
	
	private void receiveMessage(final String message)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				if(listener != null)
				{
					if(blocking)
					{
						blockMessage = message;
						latch.countDown();
					}else listener.messageReceived(message);
				}
			}
		};
		
		new Thread(run).start();
	}
	
	public String blockForMessage()
	{
		if(blocking) return null;
		
		try
		{
			blocking = true;
			latch = new CountDownLatch(1);
			latch.await();
			blocking = false;
		}catch(Exception e)
		{
			NetSettings.wtf(e);
			return null;
		}
		
		return blockMessage;
	}

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private String address;
	private int port;

	private NetworkListener listener;
	private boolean connected;
	private Thread listenThread;
	
	private boolean blocking;
	private CountDownLatch latch;
	private String blockMessage;
}
