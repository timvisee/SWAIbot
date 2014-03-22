package com.timvisee.swaibot.util;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebsiteUtils {
	
	/**
	 * Open an URI in the default browser
	 * @param uri URI to open
	 */
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * Open an URL in the default browser
	 * @param url URL to open
	 */
	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Open an URL tin the default browser
	 * @param url URL to open
	 */
	public static void openWebpage(String url) {
		try {
			openWebpage(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
