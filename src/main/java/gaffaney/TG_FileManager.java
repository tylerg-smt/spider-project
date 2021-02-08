package gaffaney;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/****************************************************************************
 * <b>Title</b>: TG_FileManager.java
 * <b>Project</b>: Spyder
 * <b>Description: </b> Controls where files are saved for the crawler
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 ****************************************************************************/
public class TG_FileManager {
	private String baseDirectory;
	
	/**
	 * Constructs a TG_FileManager object. 
	 * @param baseDirectory Where files should be saved to locally
	 */
	public TG_FileManager(String baseDirectory) {
		this.baseDirectory = baseDirectory;
		makeDirectory(baseDirectory);
	}
	
	/**
	 * If needed, will make directories to avoid IOExceptions
	 * @param path Where directories should be made too.  If path is a file, it will make its parent dirs
	 */
	public void makeDirectory(String path) {
		File f = new File(path);
		if(f.isDirectory())
			f.mkdirs();
		else
			f.getParentFile().mkdirs();
	}
	
	/**
	 * Saves the data as a file.
	 * @param data String to be saved to a file
	 * @param path A file name to be given at the baseDirectory
	 */
	public void saveFile(String data, String path) {
		String fullPath = this.baseDirectory + path;
		makeDirectory(fullPath);
		
		File file = new File(fullPath);
		writeDataToFile(data, file);
	}
	
	/**
	 * Writes the data to a File object and saves it.
	 * @param data String to be saved to a file
	 * @param file Actual file to get the data.
	 */
	private void writeDataToFile(String data, File file) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(data);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
