package server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Server {

	private JFrame frame;
	private JButton start;
	private static JTextArea console;

	private ServerSocket serverSocket;

	private int port;

	public Server(int port) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		this.port = port;

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 250);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		start = new JButton("Start Server");
		console = new JTextArea();

		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				try {
					close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						startServer();
					}
				}).start();
			}
		});

		frame.add(start, BorderLayout.SOUTH);
		frame.add(new JScrollPane(console), BorderLayout.CENTER);

		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	private void startServer() {
		System.out.println("Starting Server...");
		print("Starting Server...");
		
		try {
			serverSocket = new ServerSocket(port);
			
			while(true)
			{
				new ServerThread(serverSocket.accept()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close() throws Exception {
		serverSocket.close();
	}

	protected static void print(String text) {
		console.setText(console.getText() + text + "\n");
	}
}
