package browser;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;

public class PopupHandler {
	
	protected WebView wv = new WebView();
	private boolean called = false;
	
	public PopupHandler()
	{
		Platform.runLater(new Runnable() {
			public void run() {
				wv.getEngine().getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>(){
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						if(!called)
						{
							System.out.println("Popup!");
							WebBrowser.newTab(wv.getEngine().getLocation());
							called = true;
						}
					}
					
				});
			}
		});
	}
}
