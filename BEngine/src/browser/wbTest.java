package browser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class wbTest {
	
	protected static boolean isBrowserOpen = false;
	
	private static final int port = 5559;
	private static ServerSocket socket;
	
	protected static boolean test()
	{
			try {
				socket = new ServerSocket(port, 10, InetAddress.getLocalHost());
				return true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	}

}
