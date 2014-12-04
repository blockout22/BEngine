package browser;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.web.WebEngine;

public class LoadManager {
	
	private static String SEARCH_ENGINE = "http://www.google.com/";
	
	protected static void load(WebEngine we, String url)
	{
		String search = url.trim();
		
		if(search.contains(" "))
		{
			loadSearchEngine(we, search);
			return;
		}
		
		String toURL = convertURL(search);
		
		if(toURL == null)
		{
			toURL = convertURL("http://" + search);
		}
		
		we.load(toURL);
	}
	
	protected static void loadSearchEngine(WebEngine we, String url) {
		String page = SEARCH_ENGINE + "search?q=" + url;
		String toURL = convertURL(page.replace(" ", "+"));
		
		load(we, toURL);
	}
	
	private static String convertURL(String url) {
		try {
			return new URL(url).toExternalForm();
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
