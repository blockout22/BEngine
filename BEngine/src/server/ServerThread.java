package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.lwjgl.util.vector.Vector3f;

public class ServerThread extends Thread{
	
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String userName;
	
	private Vector3f playerPosition;
	
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			send();
			recieve();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void send() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						//send player positions
					} catch (Exception e) {

					}
				}
			}
		}).start();
	}

	private void recieve() {
		new Thread(new Runnable() {
			public void run() {
				String input;
				try {
					while ((input = in.readLine()) != null) {
						if(input.startsWith("#request-"))
						{
							//TODO request & send Mesh data
						}
						if(input.startsWith("#user"))
						{
							userName = input.replace("#user", "");
							input.replace("#user", "");
						}
						
						if(input.startsWith("#position"))
						{
							String value = input.replace("#position", "");
							Server.print(value);
						}
						Server.print("[" + userName + "] " + input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
