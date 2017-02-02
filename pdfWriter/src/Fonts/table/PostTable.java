package Fonts.table;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;


public class PostTable implements Table {
	public static final int NUMBER_OF_MAC_GLYPHS = 258;
	public static Map<String,Integer> MAC_GLYPH_NAMES_INDICES;
	public static final String[] MAC_GLYPH_NAMES = new String[]{
					".notdef",".null", "nonmarkingreturn", "space", "exclam", "quotedbl",
					"numbersign", "dollar", "percent", "ampersand", "quotesingle",
					"parenleft", "parenright", "asterisk", "plus", "comma", "hyphen",
					"period", "slash", "zero", "one", "two", "three", "four", "five",
					"six", "seven", "eight", "nine", "colon", "semicolon", "less",
					"equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F",
					"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
					"T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash",
					"bracketright", "asciicircum", "underscore", "grave", "a", "b",
					"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
					"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft",
					"bar", "braceright", "asciitilde", "Adieresis", "Aring",
					"Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute",
					"agrave", "acircumflex", "adieresis", "atilde", "aring",
					"ccedilla", "eacute", "egrave", "ecircumflex", "edieresis",
					"iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute",
					"ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave",
					"ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling",
					"section", "bullet", "paragraph", "germandbls", "registered",
					"copyright", "trademark", "acute", "dieresis", "notequal", "AE",
					"Oslash", "infinity", "plusminus", "lessequal", "greaterequal",
					"yen", "mu", "partialdiff", "summation", "product", "pi",
					"integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash",
					"questiondown", "exclamdown", "logicalnot", "radical", "florin",
					"approxequal", "Delta", "guillemotleft", "guillemotright",
					"ellipsis", "nonbreakingspace", "Agrave", "Atilde", "Otilde", "OE",
					"oe", "endash", "emdash", "quotedblleft", "quotedblright",
					"quoteleft", "quoteright", "divide", "lozenge", "ydieresis",
					"Ydieresis", "fraction", "currency", "guilsinglleft",
					"guilsinglright", "fi", "fl", "daggerdbl", "periodcentered",
					"quotesinglbase", "quotedblbase", "perthousand", "Acircumflex",
					"Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute",
					"Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex",
					"apple", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi",
					"circumflex", "tilde", "macron", "breve", "dotaccent", "ring",
					"cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash",
					"Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth",
					"Yacute", "yacute", "Thorn", "thorn", "minus", "multiply",
					"onesuperior", "twosuperior", "threesuperior", "onehalf",
					"onequarter", "threequarters", "franc", "Gbreve", "gbreve",
					"Idotaccent", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron",
					"ccaron", "dcroat"
			};

	private int version;
	private int italicAngle;
	private short underlinePosition;
	private short underlineThickness;
   	private int isFixedPitch;
	private int minMemType42;
	private int maxMemType42;
	private int minMemType1;
	private int maxMemType1;
    
    // v2
    private int numGlyphs;
    private int[] glyphNameIndex;
    private String[] psGlyphName;

    /** Creates new PostTable */
    protected PostTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
    	  MAC_GLYPH_NAMES_INDICES = new HashMap<String,Integer>(NUMBER_OF_MAC_GLYPHS);
          for (int i = 0; i < NUMBER_OF_MAC_GLYPHS; ++i){
              MAC_GLYPH_NAMES_INDICES.put(MAC_GLYPH_NAMES[i],i);
          }
    	
        raf.seek(de.getOffset());
        version = raf.readInt();
        italicAngle = raf.readInt();
        underlinePosition = raf.readShort();
        underlineThickness = raf.readShort();
        isFixedPitch = raf.readInt();
        minMemType42 = raf.readInt();
        maxMemType42 = raf.readInt();
        minMemType1 = raf.readInt();
        maxMemType1 = raf.readInt();
        
        if (version == 1.0f) {
        	psGlyphName = new String[NUMBER_OF_MAC_GLYPHS];
             System.arraycopy(MAC_GLYPH_NAMES, 0, psGlyphName, 0, NUMBER_OF_MAC_GLYPHS);
        }
        
        else if (version == 0x00020000) {
            numGlyphs = raf.readUnsignedShort();
            glyphNameIndex = new int[numGlyphs];
            for (int i = 0; i < numGlyphs; i++) {
                glyphNameIndex[i] = raf.readUnsignedShort();
            }
            int h = highestGlyphNameIndex();
            if (h > 257) {
                h -= 257;
                psGlyphName = new String[h];
                for (int i = 0; i < h; i++) {
                    int len = raf.readUnsignedByte();
                    byte[] buf = new byte[len];
                    raf.readFully(buf);
                    psGlyphName[i] = new String(buf);
                }
            }
        } 
        else if (version == 2.5f) {
        	numGlyphs = raf.readUnsignedShort();
        	int[] glyphNameIndex = new int[numGlyphs];
        	
        	for (int i = 0; i < glyphNameIndex.length; i++){
        		int offset = raf.readByte();
        		glyphNameIndex[i] = i + 1 + offset;
        	}
        	psGlyphName = new String[glyphNameIndex.length];
        	for (int i = 0; i < psGlyphName.length; i++){
        		String name = MAC_GLYPH_NAMES[glyphNameIndex[i]];
        		if (name != null){psGlyphName[i] = name;}
        	}
        }
        else if (version == 3.0f){
        	// no postscript information is provided.
        	}
    }

    private int highestGlyphNameIndex() {
        int high = 0;
        for (int i = 0; i < numGlyphs; i++) {
            if (high < glyphNameIndex[i]) {
                high = glyphNameIndex[i];
            }
        }
        return high;
    }

    public String getGlyphName(int i) {
        if (version == 0x00020000) {
            return (glyphNameIndex[i] > 257)
                ? psGlyphName[glyphNameIndex[i] - 258]
                : MAC_GLYPH_NAMES[glyphNameIndex[i]];
        } else {
            return null;
        }
    }

    public boolean getIsFixedPitch(){
    	//Set to 0 if the font is proportionally spaced 
    	if(isFixedPitch == 0){return false;}
    	//mono-spaced
    	else{return true;}
    	
    }
    
    /** Get the table type, as a table directory value.
     * @return The table type
     */
    public int getType() {return post; }

    
    public int getItalicAngle(){
    	/** Data stored as mantissa IEEE single precision standard.*/	
    	if(italicAngle != 0){
    		String binary = Integer.toBinaryString(italicAngle);
    		short sResults = (short) Integer.parseInt(binary.substring(0, 16), 2);
    		return sResults;
    	}
    	return italicAngle;}
    
    public byte[] getSubSetBytes(SortedSet <Integer> ssGlyphIds){
    	// version 3 does not have any data will throw error.
    	if (version == 3.0f){ return null;}
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream out = new DataOutputStream(bos);

    	try {
    		writeFixed(out, 2.0);// version
    		writeFixed(out, italicAngle);
    		out.writeShort(underlinePosition);
    		out.writeShort(underlineThickness);
    		out.writeInt((int)isFixedPitch);
    		out.writeInt((int)minMemType42);
    		out.writeInt((int)maxMemType42);
    		out.writeInt((int)minMemType1);
    		out.writeInt((int)maxMemType1);
    		// version 2.0

    		// numberOfGlyphs
    		out.writeShort(ssGlyphIds.size());

    		// glyphNameIndex[numGlyphs]
    		Map<String, Integer> names = new TreeMap<String, Integer>();
    		for (int gid : ssGlyphIds)
    		{
    			String name = psGlyphName[gid];
    			Integer macId = MAC_GLYPH_NAMES_INDICES.get(name);
    			if (macId != null) {
    				// the name is implicit, as it's from MacRoman
    				out.writeShort(macId);
    			}
    			else
    			{
    				// the name will be written explicitly
    				Integer ordinal = names.get(name);
    				if (ordinal == null){
    					ordinal = names.size();
    					names.put(name, ordinal);
    				}
    				out.writeShort(258 + ordinal);
    			}
    		}

    		// names[numberNewGlyphs]
    		for (String name : names.keySet())
    		{
    			byte[] buf = name.getBytes(Charset.forName("US-ASCII"));
    			out.writeByte(buf.length);
    			out.write(buf);
    		}

    		out.flush();
    	} catch (IOException e) {e.printStackTrace();} 
    	return bos.toByteArray();
    }
    
    private void writeFixed(DataOutputStream out, double f) throws IOException{
    	double ip = Math.floor(f);
    	double fp = (f-ip) * 65536.0;
    	out.writeShort((int)ip);
    	out.writeShort((int)fp);
    }
}
