package battleShip.net;

import java.io.PrintStream;

public class NetSettings 
{
	public static final boolean NET_VER_DEBUG = true;
	public static final boolean NET_DEBUG = true;
	public static final boolean PRINT_CRITICAL_ERRORS = true;
	public static final int MAX_PACKET = 10000; // 10K
	public static final boolean PRINT_ERR_TO_VER = true;
	public static final int OVERLOADED_WAIT = 10; // 10 ms
	
	public static final void ver(String message)
	{
		if(NET_VER_DEBUG && NET_DEBUG) System.out.print(message);
	}
	
	public static final void verln(String message)
	{
		ver(message + '\n');
	}
	
	public static final void err(String message)
	{
		PrintStream print = System.err;
		if(PRINT_ERR_TO_VER) print = System.out;
		if(NET_DEBUG) print.print(message);
	}
	
	public static final void errln(String message)
	{
		err(message + '\n');
	}
	
	@SuppressWarnings("unused")
	public static final void wtf(Exception e)
	{
		if(PRINT_CRITICAL_ERRORS || NET_DEBUG)
			e.printStackTrace();
	}
}
