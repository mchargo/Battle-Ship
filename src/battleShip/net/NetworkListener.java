package battleShip.net;

public interface NetworkListener 
{
	void messageReceived(String message);
	void connectionLost();
}
