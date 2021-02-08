package gaffaney;

/****************************************************************************
 * <b>Title</b>: main.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Main method to run both crawlers
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class main {

	public static void main(String[] args) {
		
		TG_FileManager fm = new TG_FileManager(System.getProperty("user.dir") + "/web/");
		
		new TG_WebCrawler("https://www.siliconmtn.com/home", fm);
		
		new TG_AdminCrawler(fm);
	}
}
