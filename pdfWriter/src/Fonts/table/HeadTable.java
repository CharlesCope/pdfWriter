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
    private byte[] byteTable;
    
    protected HeadTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());

        versionNumber = raf.readInt();//4
        fontRevision = raf.readInt();//4
        checkSumAdjustment = raf.readInt();//4
        magicNumber = raf.readInt();//4
        flags = raf.readShort();//2
        unitsPerEm = raf.readShort();//2
        created = raf.readLong();//8
        modified = raf.readLong();//8
        xMin = raf.readShort();//2
        yMin = raf.readShort();//2
        xMax = raf.readShort();//2
        yMax = raf.readShort();//2
        macStyle = raf.readShort();//2
        lowestRecPPEM = raf.readShort();//2
        fontDirectionHint = raf.readShort();//2
        indexToLocFormat = raf.readShort();//2
        glyphDataFormat = raf.readShort();//2
    }

    public int getCheckSumAdjustment() {return checkSumAdjustment;}
    public void setCheckSumAdjustment(int intNewCheckSumAdjustment) {
    	 checkSumAdjustment = intNewCheckSumAdjustment;}
    
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
    public void  setIndexToLocFormat(short subSetLocFormat) {
    	indexToLocFormat= subSetLocFormat;}

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

    public byte[] getAllBytes(){
    	// For subset we need to be able to adjust the check sum adjustment
    	byteTable[8] = (byte) ((checkSumAdjustment >>> 24) & 0xff);
    	byteTable[9] = (byte) ((checkSumAdjustment >>> 16) & 0xff);
    	byteTable[10] = (byte) ((checkSumAdjustment >>> 8) & 0xff);
    	byteTable[11] = (byte) (checkSumAdjustment & 0xff);
    	// For subset we need to force long format
    	byteTable[50] = (byte) ((indexToLocFormat >>> 8) & 0xff);
    	byteTable[51] = (byte) (indexToLocFormat & 0xff);
    	return byteTable;}
    
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