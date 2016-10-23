package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

import Fonts.ByteSwapper;


public class HeadTable implements Table {
	private String JavaNewLine = System.getProperty("line.separator");
    private int versionNumber;
    private int fontRevision;
    private int checkSumAdjustment;
    private int magicNumber;
    private short flags;
    private static short unitsPerEm;
    private long created;
    private long modified;
    private short xMin;
    private short yMin;
    private short xMax;
    private short yMax;
    private short macStyle;
    private short lowestRecPPEM;
    private short fontDirectionHint;
    private short indexToLocFormat;
    private short glyphDataFormat;
 
    protected HeadTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        versionNumber = raf.readInt();
        fontRevision = raf.readInt();
        checkSumAdjustment = raf.readInt();
        magicNumber = raf.readInt();
        flags = raf.readShort();
        unitsPerEm = raf.readShort();
        created = raf.readLong();
        modified = raf.readLong();
        xMin = raf.readShort();
        yMin = raf.readShort();
        xMax = raf.readShort();
        yMax = raf.readShort();
        macStyle = raf.readShort();
        lowestRecPPEM = raf.readShort();
        fontDirectionHint = raf.readShort();
        indexToLocFormat = raf.readShort();
        glyphDataFormat = raf.readShort();
    }

    public int getCheckSumAdjustment() {return checkSumAdjustment;}

    public long getCreated() {return created;}

    public short getFlags() {return flags;}
 
    public String getFlags(int FlagBit) {
    	/** Charles Code
    	 * Note: This is not the same as number of Flags
    	 *  Data stored in  big-endian representations
    	 *  Window reads in little- endian so swap it.
    	 *  https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6head.html
    	 * */
  
    	Short myFlag = ByteSwapper.swap(flags);
    	String strFlags = Integer.toBinaryString(myFlag);
    	strFlags = RPad(strFlags,16,'0');
    	
    	char aChar = strFlags.charAt(FlagBit);
    	String strFlagSet = "";
    	String strReturn = "";
    	if (aChar == '0'){strFlagSet = " Flag set to False ";}
    	if (aChar == '1'){strFlagSet = " Flag set to True ";}
    	switch (FlagBit) {
    	  case 0:
    		  strReturn = "Bit " + FlagBit + strFlagSet + "y value of 0 specifies baseline";
    	        break;
    	  case 1: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "x position of left most black bit is LSB";
  	        break;
    	 
    	  case 2: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "scaled point size and actual point size will differ";
  	        break;
    	  case 3: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "use integer scaling instead of fractional";
  	        break;
    	  case 4: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "(used by the Microsoft implementation of the TrueType scaler)";
  	        break;
    	  case 5: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set in fonts that are intended to be laid out vertically," + JavaNewLine;
    		  strReturn += "and in which the glyphs have been drawn such that an x-coordinate of 0 corresponds to the desired vertical baseline.";
  	        break;
    	  case 6: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit must be set to zero.";
  	        break;
    	  case 7: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set if the font requires layout for correct linguistic rendering (e.g. Arabic fonts).";
  	        break;
    	  case 8: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set for an AAT font which has one or more metamorphosis effects designated as happening by default.";
  	        break;
    	  case 9: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set if the font contains any strong right-to-left glyphs.";
  	        break;
    	  case 10: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set if the font contains Indic-style rearrangement effects.";
  	        break;
    	  case 11: 
    	  case 12:
    	  case 13: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "Defined by Adobe";
  	        break;
    	  case 14: 
    		  strReturn = "Bit " + FlagBit + strFlagSet + "This bit should be set if the glyphs in the font are simply generic symbols for code point ranges, such as for a last resort font.";
  	        break;
    	  default:
    		  strReturn = "Bit " + FlagBit + strFlagSet + "Not Defined";
    	        break;
    	}

    	return strReturn;}
    
    
      
    public short getFontDirectionHint() {return fontDirectionHint;}

    public int getFontRevision(){return fontRevision;}

    public short getGlyphDataFormat() {return glyphDataFormat;}

    public short getIndexToLocFormat() {return indexToLocFormat;}

    public short getLowestRecPPEM() {return lowestRecPPEM;}

    public short getMacStyle() {return macStyle;}

    public long getModified() {return modified;}

    public int getType() {return head;}

    public short getUnitsPerEm() {return unitsPerEm;}

    public int getVersionNumber() {return versionNumber;}

    public short getXMax() {return xMax;}

    public short getXMin() {return xMin;}

    public short getYMax() {return yMax;}

    public short getYMin() {return yMin;}

    public String toString() {
        return new StringBuffer()
            .append("head\n\tversionNumber: ").append(versionNumber)
            .append("\n\tfontRevision: ").append(fontRevision)
            .append("\n\tcheckSumAdjustment: ").append(checkSumAdjustment)
            .append("\n\tmagicNumber: ").append(magicNumber)
            .append("\n\tflags: ").append(flags)
            .append("\n\tunitsPerEm: ").append(unitsPerEm)
            .append("\n\tcreated: ").append(created)
            .append("\n\tmodified: ").append(modified)
            .append("\n\txMin: ").append(xMin)
            .append(", yMin: ").append(yMin)
            .append("\n\txMax: ").append(xMax)
            .append(", yMax: ").append(yMax)
            .append("\n\tmacStyle: ").append(macStyle)
            .append("\n\tlowestRecPPEM: ").append(lowestRecPPEM)
            .append("\n\tfontDirectionHint: ").append(fontDirectionHint)
            .append("\n\tindexToLocFormat: ").append(indexToLocFormat)
            .append("\n\tglyphDataFormat: ").append(glyphDataFormat)
            .toString();
    }
  
    private String RPad(String str, Integer length, char car) {
		//RPad("Hi", 10, 'R') //gives "HiRRRRRRRR" 
		return String.format("%" + (length - str.length()) + "s", "")
		               .replace(" ", String.valueOf(car)) 
		         +
		         str;
		}
}