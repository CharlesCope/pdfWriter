package Fonts;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
public final  class FontManager {
	// Open type is hardly supported by java...
	private static final int FONT_OPEN_TYPE = 5;
	private static final int FONT_TRUE_TYPE = java.awt.Font.TRUETYPE_FONT;
	private static final int FONT_TYPE_1 = java.awt.Font.TYPE1_FONT;

	private Map<String, FontInfo> fontData;
	private  String[] SYSTEM_FONT_PATHS; 
	
	// Singleton instance of class
	private static FontManager instance;
	private static  String OS = null;
	
	public static String getOsName(){
		// The operating system of the host that my Java program is running 
		if(OS == null) { OS = System.getProperty("os.name"); }
	      return OS;
	}
	public static boolean isWindows(){return getOsName().startsWith("Windows");}
	public static boolean isMac(){return getOsName().startsWith("Mac");}
	public static boolean isLinux(){return  getOsName().toLowerCase().indexOf("nix") >= 0 
			|| getOsName().toLowerCase().indexOf("nux") >= 0 || getOsName().toLowerCase().indexOf("aix") > 0 ;}
	
	private FontManager() {
		if (isWindows() == true){
			SYSTEM_FONT_PATHS = new String[] {
					"c:\\windows\\fonts\\",
					"d:\\windows\\fonts\\",
					"e:\\windows\\fonts\\",
					"f:\\windows\\fonts\\",
					"c:\\winnt\\Fonts\\",
					"d:\\winnt\\Fonts\\",
					"c:\\cygwin\\usr\\share\\ghostscript\\fonts\\",
					"d:\\cygwin\\usr\\share\\ghostscript\\fonts\\",
					System.getProperty("java.home") + "/lib/fonts/"};
		}
		if(isMac() == true){
			SYSTEM_FONT_PATHS = new String[] {
					"/Network/Library/Fonts/",
					"/System/Library/Fonts/",
					"/System Folder/Fonts",
					"/usr/local/share/ghostscript/",
					"/Applications/GarageBand.app/Contents/Resources/",
					"/Applications/NeoOffice.app/Contents/share/fonts/truetype/",
					"/Library/Dictionaries/Shogakukan Daijisen.dictionary/Contents/",
					"/Library/Dictionaries/Shogakukan Progressive English-Japanese Japanese-English Dictionary.dictionary/Contents/",
					"/Library/Dictionaries/Shogakukan Ruigo Reikai Jiten.dictionary/Contents/",
					"/Library/Fonts/",
					"/Volumes/Untitled/WINDOWS/Fonts/",
					"/usr/share/enscript/",
					"/usr/share/groff/1.19.2/font/devps/generate/",
					"/usr/X11/lib/X11/fonts/",
					System.getProperty("user.home") + "/Library/Fonts/",
					System.getProperty("java.home") + "/lib/fonts/"};
			
			if(isLinux() == true){
				SYSTEM_FONT_PATHS = new String[] {
						"/etc/fonts/",
						"/system/etc/fonts/",
						"/usr/lib/X11/fonts",
						"/usr/share/a2ps/afm/",
						"/usr/share/enscript/afm/",
						"/usr/share/fonts/",
						"/usr/share/ghostscript/fonts/",
						"/usr/share/groff/1.18.1/font/",
						"/usr/share/libwmf/fonts/",
						"/usr/share/ogonkify/afm/",
						"/usr/X11R6/lib/X11/fonts/",
						"/var/lib/defoma/gs.d/dirs/fonts/",
						System.getProperty("user.home") + "/.fonts/",
						System.getProperty("java.home") + "/lib/fonts/" };
			}
		}
		
		
		getFontProperties(true); }
	

	
	/**
	 * Returns a static instance of the FontManager class.
	 *
	 * @return
	 *                      The instance of the {@link FontManager}.
	 */
	public static FontManager getInstance() {
		if (instance == null) {instance = new FontManager();}

		return instance;
	}
	private Properties propsCache = null;

	/**
	 * Gets a {@link Properties} object containing font information for the operating
	 * system which the {@link FontManager} is running on.
	 *
	 * @param reload
	 *                      Reloads the properties object returned if specified to true.
	 *
	 * @return
	 *                      Properties object containing font data information.
	 */
	public Properties getFontProperties(boolean reload) {
		if (reload) {
			this.propsCache = null;
		}

		if (this.propsCache == null) {

			Properties fontProperites;
			// make sure we are initialized
			if (this.fontData == null) {readSystemFonts(null);	}

			// copy all data from fontList into the properties file
			fontProperites = new Properties();

			String name;
			String path;

			for (FontInfo currentFont : this.fontData.values()) {
				name = currentFont.getName(); // Name will be key
				path = currentFont.getPath(); // Path will be value
				fontProperites.put(name, path);
			}
			
			this.propsCache = fontProperites;
		}

		return this.propsCache;
	}

	/**
	 * Clears internal font list of items. Used to clean list while constructing
	 * a new list.
	 */
	public void clearFontList() {
		if (this.fontData != null) {	this.fontData.clear();}
	}

	/**
	 * Searches all default system font paths and any font paths
	 * specified by the extraFontPaths parameter, and records data about all
	 * found fonts.  This font data is used to substitute fonts which are not
	 * embedded inside a PDF document.
	 *
	 * @param extraFontPaths
	 *                      An array String object where each entry represents
	 *          a system directory path containing font programs.
	 */
	public void readSystemFonts(String[] extraFontPaths) {
		if (this.fontData == null) {this.fontData = new HashMap<String, FontInfo>(150);}

		File directory;

		List<String> fontDirectories;
		if (extraFontPaths == null) {
			fontDirectories = Arrays.asList(SYSTEM_FONT_PATHS);
		} else {
			int length = SYSTEM_FONT_PATHS.length + extraFontPaths.length;
			fontDirectories = new ArrayList<String>(length);

			for (String sysPath : SYSTEM_FONT_PATHS) {
				fontDirectories.add(sysPath);
			}
			for (String sysPath : extraFontPaths) {
				fontDirectories.add(sysPath);
			}

		}

		for (String path : fontDirectories) {
			if (path != null) {
				directory = new File(path);
				readDirectory(directory);
			}
		}
	}
	
	private void readDirectory(File directory) {
		Font font;
		String fontName;

		if (directory.canRead()) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					readDirectory(file);
				} else {
					fontName = file.getName();
					// try loading the font
					font = buildFont(file.getAbsolutePath());
					// if a readable font was found
					if (font != null) {
						// normalize name
						fontName = font.getName().toLowerCase();
						this.fontData.put(fontName, new FontInfo(
								file,
								font.getName().toLowerCase()));
					}
				}
			}
		}
	}
	
	
	/**
	 * <p>Gets all available font names on the operating system.</p>
	 *
	 * @return font names of all found fonts.
	 */
	public Set<String> getAvailableNames() {
		if (this.fontData == null) {return Collections.<String>emptySet();}

		return this.fontData.keySet();
	}

	
	
	/**
	 * Loads a font specified by the fontpath parameter.  If font path is invalid
	 * or the file can not be loaded, null is returned.
	 *
	 * @param fontPath font path of font program to laod
	 * @return a valid font if loadable, null otherwise
	 */
	private Font buildFont(String fontPath) {
		Font font = null;
		try {
			File file = new File(fontPath);
			if (!file.canRead()) {
				return null;
			}

			// found true type font
			if ((fontPath.endsWith(".ttf") || fontPath.endsWith(".TTF")) ||
					(fontPath.endsWith(".dfont") || fontPath.endsWith(".DFONT")) ||
					(fontPath.endsWith(".ttc") || fontPath.endsWith(".TTC"))) {
				font = createFont(file, FONT_TRUE_TYPE);
			}
			// found Type 1 font
			else if ((fontPath.endsWith(".pfa") || fontPath.endsWith(".PFA")) ||
					(fontPath.endsWith(".pfb") || fontPath.endsWith(".PFB"))) {
				font = createFont(file, FONT_TYPE_1);
			}
			// found OpenType font
			else if ((fontPath.endsWith(".otf") || fontPath.endsWith(".OTF")) ||
					(fontPath.endsWith(".otc") || fontPath.endsWith(".OTC"))) {
				font = createFont(file, FONT_OPEN_TYPE);
			}
		} catch (Throwable e) {System.out.println("Error reading font program." + e);}
		return font;
	}

	private Font createFont(File path, int fontType) {
		// see if the font file can be loaded with Java Fonts
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			// disabling create font as it brings the JVM down a little too often.
			Font javaFont = java.awt.Font.createFont(fontType, in);

			return javaFont;
		} catch (Throwable e) {System.out.println("Error reading font file with "+  e);}
		finally {
			try {
				if (in != null) {in.close();}
			} catch (Throwable e) {System.out.println("Error closing font stream."+ e);}
		}

		return null;
	}


}
