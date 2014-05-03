/**
 * 
 */
package main;

import java.io.File;

/**
 * @author ya
 * 
 */
public class Run {

	static String folder = "G:/world/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 2)
			folder = args[1];
		File file = new File(folder);
		String[] names = file.list();
		for (String name : names) {
			new Creature(folder + name, folder);
		}
	}
}
