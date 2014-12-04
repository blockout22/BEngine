package browser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WebBrowser {

	private static JFrame frame = new JFrame("BEngine Browser");

	private JFXPanel fxPanel;
	private BorderPane borderPane;
	private Scene scene;
	private static TabPane tabPane;
	private Tab plusTab;

	private BorderLayout borderLayout;

	private ArrayList<String> borderLayoutList = new ArrayList<String>();

	public WebBrowser() throws Exception {
		if (!wbTest.test()) {
			System.err.println("Wb Browser instance already created \ncall the method 'showWindow' to make it visible again");
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				frame.setSize(250, 250);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				borderLayout = new BorderLayout();
				frame.setLayout(borderLayout);

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
						frame.setVisible(false);
					}

					public void windowClosed(WindowEvent e) {
					}

					public void windowActivated(WindowEvent e) {
					}
				});

				frame.setVisible(false);
				fxPanel = new JFXPanel();
				Platform.runLater(new Runnable() {
					public void run() {
						initJavaFX();
					}
				});
			}
		}).start();
	}

	public JPanel addPanelToFrame(LayoutManager layout, String borderLayout) {
		for (String s : borderLayoutList) {
			System.out.println("Browser output!: " + s);
			if (borderLayout.equals(s)) {
				System.err.println("BorderLayout." + borderLayout + " is already taken, choose another location");
				return null;
			}
		}

		JPanel panel = new JPanel(layout);
		frame.add(panel, borderLayout);

		return panel;

	}

	public void addToFrame(Component component, String borderLayout) {
		for (String s : borderLayoutList) {
			System.out.println("Browser output!: " + s);
			if (borderLayout.equals(s)) {
				System.err.println("BorderLayout." + borderLayout + " is already taken, choose another location");
				return;
			}
		}
		frame.add(component, borderLayout);
		borderLayoutList.add(borderLayout);
	}

	private void initJavaFX() {
		tabPane = new TabPane();
		plusTab = new Tab("+");

		borderPane = new BorderPane();
		scene = new Scene(borderPane);
		fxPanel.setScene(scene);
		borderPane.setCenter(tabPane);

		tabPane.getTabs().add(plusTab);

		plusTab.setOnSelectionChanged(new EventHandler<Event>() {
			public void handle(Event event) {
				if (plusTab.isSelected()) {
					if (tabPane.getTabs().size() == 1) {
						frame.setVisible(false);
					}
					newTab(Strings.HOME_PAGE);
				}
			}
		});

		frame.add(fxPanel, BorderLayout.CENTER);
		borderLayoutList.add(BorderLayout.CENTER);
	}

	protected static void newTab(String url) {
		Platform.runLater(new Runnable() {
			public void run() {
				BorderPane mainPane = new BorderPane();
				Button back = new Button("<-");
				StackPane browserPane = new StackPane();
				FlowPane topBar = new FlowPane();
				
				WebView wv = new WebView();
				WebEngine we = wv.getEngine();
				WebHistory wh = we.getHistory();
				
				ProgressBar pb = new ProgressBar();
				Label pbLabel = new Label();
				Tab tab = new Tab("title");
				TextField searchBar = new TextField();

				StackPane.setAlignment(pb, Pos.BOTTOM_LEFT);
				StackPane.setAlignment(pbLabel, Pos.BOTTOM_LEFT);
				
				double searchBarOffset = 80;

				tab.setContent(mainPane);
				mainPane.setCenter(browserPane);
				mainPane.setTop(topBar);
				browserPane.getChildren().addAll(wv, pb, pbLabel);
				topBar.getChildren().addAll(back, searchBar);
				
				we.setUserAgent(Strings.USER_AGENT);

				searchBar.setMinWidth(back.getWidth() + frame.getWidth() - searchBarOffset);
				pb.progressProperty().bind(we.getLoadWorker().progressProperty());

				tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
				tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);

				tab.setOnClosed(new EventHandler<Event>() {
					public void handle(Event event) {
						we.load(null);
					}

				});
				
				we.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if (newValue == State.SUCCEEDED) {
							
							try {
								getImageIcon(we.getLocation());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							pb.setVisible(false);
							pbLabel.setVisible(false);
							searchBar.setText(we.getLocation());
							tab.setText(we.getTitle());
							try {
								String ico = new URL(we.getLocation().toString()).getHost() + "/favicon.ico";
								System.out.println(ico);
								Image image = new Image("http://www.facebook.com/favicon.ico");
								ImageView iv = new ImageView(image);
								tab.setGraphic(iv);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (newValue == State.RUNNING) {
							pb.setVisible(true);
							pbLabel.setVisible(true);
							searchBar.setText(we.getLocation());
						}

						if (newValue == State.FAILED) {
							pb.setVisible(false);
							pbLabel.setVisible(false);
							if (we.getLoadWorker().getException().toString().equals("java.lang.Throwable: Unknown host")) {
								LoadManager.loadSearchEngine(we, searchBar.getText().trim());
							}
						}

						if (newValue == State.CANCELLED) {
							pb.setVisible(false);
							pbLabel.setVisible(false);
						}
					}
				});

				we.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						pbLabel.setText("          " + newValue.intValue() + "%");
					}

				});

				Platform.runLater(new Runnable() {
					public void run() {
						searchBar.setOnAction(new EventHandler<ActionEvent>() {
							public void handle(ActionEvent event) {
								LoadManager.load(we, searchBar.getText().trim());
							}

						});

						searchBar.focusedProperty().addListener(new ChangeListener<Boolean>() {
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
								Platform.runLater(new Runnable() {
									public void run() {
										if (newValue) {
											searchBar.selectAll();
										}
									}
								});
							}
						});
					}
				});

				Platform.runLater(new Runnable() {
					public void run() {
						we.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
							public WebEngine call(PopupFeatures param) {
								return new PopupHandler().wv.getEngine();
							}

						});
					}
				});
				
				Platform.runLater(new Runnable() {
					public void run() {
						back.setOnAction(new EventHandler<ActionEvent>(){
							public void handle(ActionEvent event) {
								wh.go(-1);
							}
							
						});
					}
				});

				we.load(url);

				frame.addComponentListener(new ComponentListener() {
					public void componentShown(ComponentEvent e) {
					}

					public void componentResized(ComponentEvent e) {
						searchBar.setMinWidth(back.getWidth() + frame.getWidth() - searchBarOffset);
					}

					public void componentMoved(ComponentEvent e) {
					}

					public void componentHidden(ComponentEvent e) {
					}
				});

				refreshWindow();
			}
		});
	}
	
	private static Image getImageIcon(String location) throws Exception
	{
		URL url = new URL(location);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String input;
		while((input = br.readLine()) != null)
		{
		if(input.contains("link rel="))
		{
			System.out.println(input);
		}
		}
		
		br.close();
		Image image = new Image(input);
		return image;
	}

	private static void refreshWindow() {
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	public void showWindow() {
		if (tabPane.getTabs().size() == 1) {
			newTab(Strings.HOME_PAGE);
		}
		frame.setVisible(true);
		refreshWindow();
	}
}
