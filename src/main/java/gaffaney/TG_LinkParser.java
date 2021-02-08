package gaffaney;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/****************************************************************************
 * <b>Title</b>: TG_LinkParser.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Parses HTML links from a String
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_LinkParser {
	private String data;
	private String base;
	
	/**
	 * Constructs a TG_LinkParser object
	 * @param data Data to be find links from
	 * @param base What should be pre-pended on URIs to make them URLs
	 */
	public TG_LinkParser(String data, String base) {
		this.data = data;
		this.base = base;
	}

	/**
	 * Used by other objects to pull URLs after object is constructed
	 * @return URL Array with URLs
	 */
	public URL[] getUrls(){
		
		Document doc = getDocumentfromBody(data);
		URI[] uris = getPartialUrlsFromDocument(doc);
		return convertPartialUrlsIntoFullUrls(uris, this.base);
	}
	
	
	/**
	 * Gets all URIs from the JSoup document
	 * @param doc JSoup document
	 * @return URI Array
	 */
	private URI[] getPartialUrlsFromDocument(Document doc) {
		Elements links = doc.select("a");
		List<URI> list = new ArrayList<URI>();
		for(int x = 0; x < links.size(); x++) {
			URI uri = getUriForHrefElement(links.get(x));
			if(uri.getPath() != null && uri.getPath().startsWith("/") && uri.getPath().length() > 1) {
				list.add(uri);
			}
		}
		
		return list.toArray(new URI[list.size()]);
	}
	
	/**
	 * Parses a URI from a given HTML Element
	 * @param link Link element
	 * @return URI of element
	 */
	private URI getUriForHrefElement(Element link) {
		String hrefValue = link.attr("href");
		URI possibleLink = null;
		try {
			possibleLink = new URI(hrefValue);
		} catch (URISyntaxException e) { e.printStackTrace(); }
		return possibleLink;
	}
	
	/**
	 * Converts URIs to URLs
	 * @param partial URI array with incomplete URLs
	 * @param base What will be pre-pended onto the URIs
	 * @return URL Array
	 */
	private URL[] convertPartialUrlsIntoFullUrls(URI[] partial, String base) {
		List<URL> urls = new ArrayList<URL>();
		try {
			for(int x = 0; x < partial.length; x++) {
				if(partial[x].isAbsolute()) {
					urls.add(partial[x].toURL());
				}else {
					urls.add(new URL("https://" + base + partial[x].toString()));
				}
			}
		} catch (MalformedURLException e) { e.printStackTrace(); }
		
		return urls.toArray(new URL[urls.size()]);
	}
	
	/**
	 * Creates a JSoup Document from the html data
	 * @param data HTML String from the body element
	 * @return JSoup Document
	 */
	private Document getDocumentfromBody(String data) {
		String charsetName = "utf8";
		return Jsoup.parse(data, charsetName);
	}
}
