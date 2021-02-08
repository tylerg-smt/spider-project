package gaffaney;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
/****************************************************************************
 * <b>Title</b>: TG_AdminCrawler.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Crawls the admin tool pages and logs in
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_AdminCrawler {
	private TG_HTTPSession session;
	private TG_FileManager fileManager;

	/**
	 * Constructor that kicks off the crawling. This controls the main flow
	 * @param fm File manager to save files to
	 */
	public TG_AdminCrawler(TG_FileManager fm) {
		try {
			this.fileManager = fm;
			
			URL[] links = new URL[] {
				new URL("https://www.siliconmtn.com/sb/admintool?cPage=index&amp;actionId=FLUSH_CACHE"),
				new URL("https://www.siliconmtn.com/sb/admintool?cPage=stats&actionId=FLUSH_CACHE"),
				new URL("https://www.siliconmtn.com/sb/admintool?cPage=index&actionId=SCHEDULE_JOB_INSTANCE&organizationId=SMT"),
				new URL("https://www.siliconmtn.com/sb/admintool?cPage=index&actionId=WEB_SOCKET&organizationId=SMT"),
				new URL("https://www.siliconmtn.com/sb/admintool?cPage=index&actionId=ERROR_LOG&organizationId=SMT"),
			};
				
			this.session = new TG_HTTPSession("www.siliconmtn.com", 443);
			
			login(new URL("https://www.siliconmtn.com/admintool"));
			
			for(int x = 0; x < links.length; x++) {
				crawlSecurePage(links[x]);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This performs the login request on the URL
	 * @param url Location to perform the request
	 */
	private void login(URL url) {
		System.out.println("> " + url);
		TG_Request req = getLoginRequest(url);
		
		try {
			TG_Response res = this.session.performRequest(req);
			this.fileManager.saveFile(res.getBody(), url.getFile() + ".html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * After logging in, this crawls a page with the proper security tokens attached to the request.
	 * @param url Location to perform the request
	 */
	private void crawlSecurePage(URL url) {
		System.out.println("> " + url);
		TG_Request req = getSecureRequest(url);
		
		try {
			TG_Response res = this.session.performRequest(req);
			this.fileManager.saveFile(res.getBody(), url.getFile() + ".html");
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * This builds a secure GET request with JSESSIONID attached
	 * @param url Location to perform the request
	 * @return a TG_Request object
	 */
	private TG_Request getSecureRequest(URL url) {
		TG_Request req = new TG_Request(url.getFile(),"GET","1.1");
		
		req.addHeader("Accepts", "*/*");
		req.addHeader("Host", "www.siliconmtn.com");
		req.addHeader("User-Agent", "tgaffaney/1.2");
		
		
		return req;
	}
	
	/**
	 * This builds a login request, I was having trouble with my credentials so I'm borrowing one of the public credentials already posted
	 * @param url Location to perform the request
	 * @return a TG_Request object
	 */
	private TG_Request getLoginRequest(URL url) {
		
		TG_Request req = new TG_Request(url.getFile(),"POST","1.1");
		
		try {
			
			String data = "requestType=" + URLEncoder.encode("reqBuild", "UTF-8");
			data += "&" + URLEncoder.encode("pmid", "UTF-8") + "=" + URLEncoder.encode("ADMIN_LOGIN", "UTF-8");
			data += "&" + URLEncoder.encode("emailAddress", "UTF-8") + "=" + URLEncoder.encode("branden.goldenberg@siliconmtn.com", "UTF-8");
	        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode("SMTRul3s!", "UTF-8");
	        req.setData(data);
	        
			req.addHeader("Host", "www.siliconmtn.com");
			req.addHeader("Content-Type", "application/x-www-form-urlencoded");
			int contentLength = data.length();
			req.addHeader("Content-Length", contentLength + "");
			req.addHeader("User-Agent", "tgaffaney/1.2");
			
		} catch (UnsupportedEncodingException e) { e.printStackTrace();}
        
		return req;
	}
}
