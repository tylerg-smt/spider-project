package gaffaney;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

/****************************************************************************
 * <b>Title</b>: TG_WebCrawler.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Crawls a website for links
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_WebCrawler {
	private TG_HTTPSession session;
	private HashSet<String> crawlCache;
	private TG_FileManager fileManager;
	
	/**
	 * Kicks off the website crawling
	 * @param startingPage
	 * @param fileManager
	 */
	public TG_WebCrawler(String startingPage, TG_FileManager fileManager) {
		this.fileManager = fileManager;
		this.session = new TG_HTTPSession(getHost(startingPage), 443);
		this.crawlCache = new HashSet<String>();
		crawl(getUrl(startingPage));
	}
	
	/**
	 * Crawls a URL if it hasnt already been crawled.  Also saves it to a 'web' directory
	 * @param url URL to crawl
	 */
	private void crawl(URL url) {
		System.out.println("> " + url.toString());
		String data = getWebpageData(url);
		String fileName = url.getFile().contains(".") ? url.getFile() : url.getFile() + ".html";
		fileManager.saveFile(data, fileName);
		
		TG_LinkParser parser = new TG_LinkParser(data, url.getHost());
		URL[] urls = parser.getUrls();
		
		for(int x = 0; x < urls.length; x++) {
			URL currentUrl = urls[x];
			if(!isRegistered(currentUrl)) {
				register(currentUrl);
				crawl(currentUrl);
			}
		}
	}
	
	/**
	 * Registers a URL, preventing it from being crawled again
	 * @param url URL that has been crawled
	 */
	private void register(URL url) {
		this.crawlCache.add(url.toString());
	}
	
	/**
	 * Returns a boolean if the URL has been crawled
	 * @param url URL to check
	 * @return boolean
	 */
	private boolean isRegistered(URL url) {
		return this.crawlCache.contains(url.toString());
	}
	
	/**
	 * Returns webpage body content from URL
	 * @param url URL to fetch data from
	 * @return String
	 */
	private String getWebpageData(URL url) {
		TG_Request request = new TG_Request(url.getFile(), "GET", "1.1");
		
		try {
			TG_Response response = session.performRequest(request);
			return response.getBody();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts a String to a URL
	 * @param url path that will be converted
	 * @return URL
	 */
	private static URL getUrl(String url) {
		try {
			URL actualUrl = new URL(url);
			return actualUrl;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Pulls the host part from a String url
	 * @param url
	 * @return
	 */
	private static String getHost(String url) {
		return getUrl(url).getHost();
	}
}
