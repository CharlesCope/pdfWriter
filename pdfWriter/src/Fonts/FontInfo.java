package Fonts;

import java.io.File;

public class FontInfo {

	private final File fontFile;
	private final String name;

	/**
	 * Constructs the information.
	 *
	 * @param fontFile
	 * 			The file from which the font is loaded.
	 * @param name
	 * 			The name of the font.
	 */
	public FontInfo(File fontFile, String name) {
		this.fontFile = fontFile;
		this.name = name;
	}
	
	public String getName() {	return this.name; }
	public String getPath() {return this.fontFile.getAbsolutePath();}

}


