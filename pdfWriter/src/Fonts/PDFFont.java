package Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Fonts.table.CmapFormat;
import cidObjects.CIDSystemInfo;
import pdfCmaps.identityH;

/** Notes on Java Data Types.
 * byte: Byte data type is an 8-bit signed,Minimum value is -128, Maximum value is 127 (inclusive) 
 * short:Short data type is a 16-bit signed,Minimum value is -32,768, Maximum value is 32,767 (inclusive)
 * int:Int data type is a 32-bit signed,Minimum value is - 2,147,483,648, Maximum value is 2,147,483,647(inclusive) 
 * */
public class PDFFont {
	private final String  PDFCRLF = "\r\n";
	private String strBaseFontName = "";
	private String strFontFamilyName = "";
	private String strFontBBox = "";
	private String strWEntry ="";
	private String strToUnicodeCMAP ="";
	private boolean blnFixedPitchFlag = false;
	private boolean blnSerifFlag = false;
	private boolean blnSymbolicFlag = false;
	private boolean blnScriptFlag = false;
	private boolean blnNonsymbolicFlag = false;
	private boolean blnItalicFlag = false;
	private boolean blnAllCapFlag = false;
	private boolean blnSmallCapFlag = false;
	private boolean blnForceBoldFlag = false;
	private Integer intFirstChar = 0;
	private Integer intLastChar = 0;
	private Integer intUnitsPerEm = 0;
	private Integer BBoxLowerLeftx = 0;
	private Integer BBoxLowerLefty = 0;
	private Integer BBoxUpperRightx = 0;
	private Integer BBoxUpperRighty = 0;
	private Integer intMissingWidth = 0;
	private Integer intCapHeight = 0;
	private Integer intXHeight = 0;
	private Integer intItalicAngle = 0;
	private Integer intAscent = 0;
	private Integer intDescent = 0;
	private Integer intLeading = 0;
	private Integer intStemV = 0;
	private Integer intStemH = 0;
	private Integer intMaxWidth = 0;
	private Integer intAvgWidth = 0;
	private Integer intWeight = 0;

	
	private CmapFormat cmapFormat = null;
	private CIDSystemInfo cidSystemInfo = new CIDSystemInfo();
	
	String JavaNewLine = System.getProperty("line.separator");
	
	/**The Constructor*/
	public PDFFont(){}
	public String getToUnicodeCMAP(){return strToUnicodeCMAP;}
	public void setToUnicodeCMAP(String strPdfCmapName){ 
		String strTemp = ""; 
		
		if (strPdfCmapName == "identityH"){
			// Create the Cmap object
		    identityH CmapH = new identityH();
		    strTemp = "<< /Length " + CmapH.Length() + " >>" + PDFCRLF;
		    strTemp += "stream" + PDFCRLF;
		    strTemp += CmapH.toString() + PDFCRLF;
		    strTemp += "endstream" + PDFCRLF;
		    
		    // set the values of the CIDdSystemInfo based on CMAP DATA
		    cidSystemInfo.setRegistry(CmapH.getRegistry());
		    cidSystemInfo.setOrdering(CmapH.getOrdering());
		    cidSystemInfo.setSupplement(CmapH.getSupplement());
		 }
		
		     
		            
		            
		strToUnicodeCMAP = strTemp;}

	public String getCIDSystemInfoDictionary(){ return cidSystemInfo.toString();}
	
	public String getWEntry(){ return strWEntry;}
	public void setWEntry(String WEntry){strWEntry = WEntry;}	 
	
	/** PDF Notes On Subject. 
	 * The PostScript name for the value of BaseFont is determined in one of two ways:
	 *  Use the PostScript name that is an optional entry in the “name” table of the TrueType font. 
	 *  
	 *  In the absence of such an entry in the “name” table, derive a PostScript name
	 *  from the name by which the font is known in the host operating system. On a
	 *  Windows system, the name is based on the lfFaceName field in a LOGFONT
	 *  structure; in the Mac OS, it is based on the name of the FOND resource. If the
	 *  name contains any spaces, the spaces are removed.        
	 */
	public String getFontBaseName(){return strBaseFontName;}
	/** The value of the Flags entry in a font descriptor is an unsigned 32-bit(big-endian integer containing
	 * flags specifying various characteristics of the font.
	 * Example "0000 0000 0000 0100 0000 0000 0010 0010"
	 * Decimal Value = 262178
	 * Flags Set 2,6,19
	*/ 
	public String getFontFamilyName(){return strFontFamilyName;}
	public String getFontDescriptorFlags(){
		
		// big-endian string
		StringBuilder str32Flag = new StringBuilder("00000000000000000000000000000000");
		final int RADIX = 10;
	
		// set our flag bits
		str32Flag.setCharAt(31, Character.forDigit(Boolean.compare(blnFixedPitchFlag,false), RADIX));
		str32Flag.setCharAt(30, Character.forDigit(Boolean.compare(blnSerifFlag,false), RADIX));
		str32Flag.setCharAt(29, Character.forDigit(Boolean.compare(blnSymbolicFlag,false), RADIX));
		str32Flag.setCharAt(28, Character.forDigit(Boolean.compare(blnScriptFlag,false), RADIX));
		// 5 Flag not Used
		str32Flag.setCharAt(26, Character.forDigit(Boolean.compare(blnNonsymbolicFlag,false), RADIX));
		str32Flag.setCharAt(25, Character.forDigit(Boolean.compare(blnItalicFlag,false), RADIX));
		// 8 to 16 Flags not Used
		str32Flag.setCharAt(15, Character.forDigit(Boolean.compare(blnAllCapFlag,false), RADIX));
		str32Flag.setCharAt(14, Character.forDigit(Boolean.compare(blnSmallCapFlag,false), RADIX));
		str32Flag.setCharAt(13, Character.forDigit(Boolean.compare(blnForceBoldFlag,false), RADIX));
		// 12 to 0 Flags not Used
		Integer intFlagsVaule = Integer.parseUnsignedInt(str32Flag.toString(), 2);
		return  intFlagsVaule.toString();
		
	}
	/** Sources for the code I used...
	 *https://github.com/zendframework/zf1/blob/master/library/Zend/Pdf/Resource/Font/FontDescriptor.php Line:116
	 * http://www.websupergoo.com/helppdfnet/default.htm?page=source%2Fdefault.htm
	 * This XRect corresponds to the xMin, xMax, yMin and yMax entries in the 'head' TrueType table.
	 * Distances are measured up and right in points. Points are a traditional measure for print work and there are 72 points in an inch 
	 * Notes on Font Bounding Box
	 * PDF Rectangles:
	 * rectangle is written as an array of four numbers giving the coordinates of a pair of 
	 * diagonally opposite corners. Typically, the array takes the form [ lowerLeftx lowerLefty upperRightx upperRighty ]
	 * */
	public String getFontBBox(){
		// Results not matching data in file
		Integer lowerLeftx = 0;
		Integer lowerLefty = 0;
		Integer upperRightx = 0;
		Integer upperRighty =0;
		
		lowerLeftx = (int) toEmSpace(BBoxLowerLeftx);
		lowerLefty = (int) toEmSpace(BBoxLowerLefty);
		upperRightx = (int) toEmSpace(BBoxUpperRightx);
		upperRighty= (int) toEmSpace(BBoxUpperRighty);
		
		strFontBBox = lowerLeftx.toString() + " ";
		strFontBBox += lowerLefty.toString() + " ";
		strFontBBox += upperRightx.toString() + " ";
		strFontBBox += upperRighty.toString() + " ";
		
		return strFontBBox;
		
	}
	
	public int getSpaceWidthToPDFWidth(){
		/**Advance width rule : The space's advance width is set by visually selecting a value that is appropriate for the current font.
		 * The general guidelines for the advance widths are:
		 *  The minimum value should be no less than 1/5 the em, 
		 *  which is equivalent to the value of a thin space in traditional typesetting.
		 *  For an average width font a good value is ~1/4 the em.
		 *  Example: In Monotype's font Times New Roman-regular the space is 512 units, the em is 2048.  
		 */
		// Just me taking a stab at it.
		double dblMin = intUnitsPerEm * .20;
		double dblAvg = intUnitsPerEm * .25;
		double dblWide = intUnitsPerEm * .33;
		double dblTotal = (dblMin + dblAvg + dblWide)/ 3;
		int results = fontToPDFfont.pdfScalingFormula((int)dblTotal,intUnitsPerEm);
		
		if (results < (intUnitsPerEm * .5)){return results;}
		else{ return fontToPDFfont.pdfScalingFormula((int) (intUnitsPerEm * .5),intUnitsPerEm);}
		
	}
	
	public int getGlyphWidthToPDFWidth(int CharCode){
		
		try {
			
			return fontToPDFfont.pdfScalingFormula(fontToPDFfont.getMyChcFont().getGlyph(CharCode).getAdvanceWidth(), intUnitsPerEm);
			
		} catch (Exception e) {
			return 0;
		}
	}

	public void setFontBaseName(String strName){strBaseFontName = strName;}
	
	public void setFontFamilyName(String strName){strFontFamilyName = strName;}
	
	/** Our Flags for the font Descriptor */
	public void setFixedPitchFlag(boolean setFlag){blnFixedPitchFlag = setFlag;}
	
	public void setSerifFlag(boolean setFlag){blnSerifFlag = setFlag;}	
	
	public void setSymbolicFlag(boolean setFlag){blnSymbolicFlag = setFlag;}
	
	public void setScriptFlag(boolean setFlag){blnScriptFlag = setFlag;}
	
	public void setNonsymbolicFlag(boolean setFlag){blnNonsymbolicFlag = setFlag;}
	
	public void setItalicFlag(boolean setFlag){blnItalicFlag = setFlag;}
	
	public void setAllCapFlag(boolean setFlag){blnAllCapFlag = setFlag;}
	
	public void setSmallCapFlag(boolean setFlag){blnSmallCapFlag = setFlag;}
	
	public void setForceBoldFlag(boolean setFlag){blnForceBoldFlag = setFlag;}
	
	public int getUnitsPerEm() {return intUnitsPerEm;}
	
	public void setUnitsPerEm(int UnitsPerEm) {intUnitsPerEm = UnitsPerEm;}
	
	public void setFirstChar(int FirstChar){intFirstChar = FirstChar;}
	public String getFirstChar(){return  intFirstChar.toString();}
	
	public void setLastChar(int LastChar){intLastChar = LastChar;}
	public String getLastChar(){return  intLastChar.toString();}
	
	public void setMissingWidth(int missingWidth){intMissingWidth = missingWidth;}
	public String getMissingWidth(){return intMissingWidth.toString();}
	
	public void setCapHeight(int capHeight){intCapHeight = capHeight;}
	public String getCapHeight(){return intCapHeight.toString();}
	
	public void setXHeight(int xHeight){intXHeight  = xHeight;}
	public String getXHeight(){return intXHeight.toString();}
	
	public void setItalicAngle(int italicAngle){intItalicAngle = italicAngle;}
	public String getItalicAngle(){return intItalicAngle.toString();}

	public void setAscent(int Ascent){ intAscent = Ascent;}
	public String getAscent(){return intAscent.toString();}
	
	public void setDescent(int Descent){intDescent = Descent;}
	public String getDescent(){return intDescent.toString();}
	
	public void setLeading(int Leading){intLeading = Leading;}
	public String getLeading(){return intLeading.toString();}
	
	public void setStemV(int StemV){intStemV = StemV;}
	public String getStemV(){
		final int ULTRA_LIGHT = 1;
		final int EXTRA_LIGHT = 2;
		final int LIGHT = 3;
		final int SEMI_LIGHT = 4;
		final int MEDIUM_NORMAL = 5;
		final int SEMI_BOLD = 6;
		final int BOLD = 7;
		final int EXTRA_BOLD = 8;
		final int ULTRA_BOLD = 9;
		Integer intReturnValue = 50;
		
		switch (intStemV) {
		case ULTRA_LIGHT:  intReturnValue = 50;		break;
		case EXTRA_LIGHT:  intReturnValue = 68;		break;
		case LIGHT:  intReturnValue = 88;			break;
		case SEMI_LIGHT:  intReturnValue = 100;		break;
		case MEDIUM_NORMAL:  intReturnValue = 125;	break;
		case SEMI_BOLD:  intReturnValue = 135;		break;
		case BOLD:  intReturnValue = 165;			break;
		case EXTRA_BOLD:  intReturnValue = 201;		break;
		case ULTRA_BOLD:  intReturnValue = 241;		break;
		default: intReturnValue = 50;				break;}

		return  intReturnValue.toString();}

	public void setStemH(int StemH){intStemH = StemH;}
	public String getStemH(){ return  intStemH.toString();} 
	
	public void setMaxWidth(int MaxWidth){intMaxWidth = MaxWidth;}
	public String getMaxWidth(){return  intMaxWidth.toString();}
	
	public void setAvgWidth(int AvgWidth){intAvgWidth = AvgWidth;}
	public String getAvgWidth(){return intAvgWidth.toString();}
	
	public String getFontWeight() {return  intWeight.toString();}
	public void setFontWeight(int intWeight) {	this.intWeight = intWeight * 100;}
	
	public int getBoundingBoxLowerLeftx() {return BBoxLowerLeftx;}
	public int getBoundingBoxLowerLefty() {return BBoxLowerLefty;}
	public int getBoundingBoxUpperRightx() {return BBoxUpperRightx;}
	public int getBoundingBoxUpperRighty() {return BBoxUpperRighty;}
	
	public void setBoundingBoxLowerLeftx(int bBoxLowerLeftx) {BBoxLowerLeftx = bBoxLowerLeftx;}
	public void setBoundingBoxLowerLefty(int bBoxLowerLefty) {BBoxLowerLefty = bBoxLowerLefty;}
	public void setBoundingBoxUpperRightx(int bBoxUpperRightx) {BBoxUpperRightx = bBoxUpperRightx;}
	public void setBoundingBoxUpperRighty(int bBoxUpperRighty) {BBoxUpperRighty = bBoxUpperRighty;}
	
	/** https://github.com/zendframework/zf1/blob/master/library/Zend/Pdf/Resource/Font.php Line:522
	 *  If the font's glyph space is not 1000 units per em, converts the value. */
    public double toEmSpace(double dblValue){
	        if (intUnitsPerEm == 1000) {return dblValue;}
	        return Math.ceil((dblValue / intUnitsPerEm) * 1000);    // always round up
	    }
	
    public String getFontDictionary(){
    	
    	String strResults = getFirstChar()+ JavaNewLine; 
    	strResults += getLastChar()+ JavaNewLine;
    	strResults += getFontDescriptorFlags() + " ";
    	strResults += getFontBBox() + " ";
    	strResults += getMissingWidth() + " ";
    	strResults += getStemV() + " ";
    	strResults += getItalicAngle() + " ";
    	strResults += getCapHeight() + " ";
    	strResults += getXHeight() + " ";
    	strResults += getAscent() + " ";
    	strResults += getDescent() + " ";
    	strResults += getLeading()+ " ";
    	strResults += getMaxWidth() + " "; 
    	strResults += getAvgWidth() + " ";
    	
    	return strResults;
    }
    
	/** Need a toString Method for debugging and development */
	
	public String toString(){
		
		String strToString = "<< Start of PDF font dictionary >> " + JavaNewLine + JavaNewLine;
		strToString += getFontDictionary()+ JavaNewLine + JavaNewLine;
		strToString += "<< End PDF Font Dictionary >>" + JavaNewLine;
		strToString += "BaseFont Name >> " + strBaseFontName + JavaNewLine;
		strToString += "Flags Values >> " + getFontDescriptorFlags() + JavaNewLine;
		strToString += "Flags Set Values Fixed Pitch >> " + blnFixedPitchFlag + JavaNewLine;
		strToString += "Flags Set Values Serif >> " + blnSerifFlag + JavaNewLine;
		strToString += "Flags Set Values Symbolic >> " + blnSymbolicFlag + JavaNewLine;
		strToString += "Flags Set Values Non Symbolic >> " + blnNonsymbolicFlag + JavaNewLine;
		strToString += "Flags Set Values Script >> " + blnScriptFlag + JavaNewLine;
		strToString += "Flags Set Values Italic >> " + blnItalicFlag + JavaNewLine;
		strToString += "Flags Set Values All Caps >> " + blnAllCapFlag + JavaNewLine;
		strToString += "Flags Set Values Small Caps >> " + blnSmallCapFlag + JavaNewLine;
		strToString += "Flags Set Values Force Bold >> " + blnForceBoldFlag + JavaNewLine;
		strToString += "Font Bounding Box >> " + getFontBBox() + JavaNewLine;
		strToString += "Character Missing Width >> " + getMissingWidth() + JavaNewLine;
		strToString += "Capital letters Height >> " + getCapHeight() + JavaNewLine;
		strToString += "Lower case x Height >> " + getXHeight() + JavaNewLine;
		strToString += "The  Font Weight >>" + getFontWeight() + JavaNewLine;
		strToString += "Italic Angle Slope Right neg number >> " + getItalicAngle() + JavaNewLine;
		strToString += "Ascent maximum height above baseline >> " + getAscent() + JavaNewLine;
		strToString += "Descent maximum depth below baseline >> " + getDescent() + JavaNewLine;
		strToString += "spacing between baselines of consecutive lines >> " + getLeading() + JavaNewLine;
		strToString += "The thickness, measured horizontally, of the dominant vertical stems of glyphs in the font. >> " + getStemV() + JavaNewLine;
		strToString += "Maximum advance width value in ‘hmtx’ table. >> " + getMaxWidth()+ JavaNewLine;
		strToString += "Average weighted advance width of lower case letters and space >> " + getAvgWidth() + JavaNewLine;
		return strToString;
		
	}
	
	public int getCharacterCodeToGlyphId(int intCharCode){return cmapFormat.mapCharCode(intCharCode);	}
	
	public CmapFormat getCmapFormat() {return cmapFormat;}
	
	public void setCmapFormat(CmapFormat cmapFormat) {this.cmapFormat = cmapFormat;}
	
	public String getUnicodeEscapeString(int intValue){return "\\u"+ addZeros(Integer.toHexString(intValue));}
	
	public String getUnicodeString(int intValue){return "U+"+addZeros(Integer.toHexString(intValue));}
	
	public Double getStringWidth(String strToMeasure, int intFontSize){
		//-- Make sure we have something before checking it length
		if (strToMeasure == null){return 0.0;}
		if(strToMeasure == ""){return 0.0;}
		if(intFontSize ==0){return 0.0;}

		Integer intWidth = 0;
		int intTemp = 0;

		for (int offset = 0; offset < strToMeasure.length(); ){
			int codePoint = strToMeasure.codePointAt(offset);
			int intGlyphID = this.getCharacterCodeToGlyphId(codePoint);

			// deal with the space
			if(codePoint == 32){intTemp = this.getSpaceWidthToPDFWidth();}
			// Get the width for the Glyph
			else{intTemp = this.getGlyphWidthToPDFWidth(intGlyphID);}
			// Deal with missing data
			if (intTemp == 0){ intTemp = intMissingWidth;}

			intWidth += intTemp;
			offset += Character.charCount(codePoint);
		}

		return intWidth * intFontSize / 1000.0;

	}
	
	public String getEncodedString(String strToConvert){
		// This encodes the  unicode string to Hex Values instead of ASCII values for the pdf CID to work.
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		for (int offset = 0; offset < strToConvert.length(); ){
			int codePoint = strToConvert.codePointAt(offset);
			// Next get the character identifier (CID) numbers  for the CodePoint
			int intCID = this.getCharacterCodeToGlyphId(codePoint);

			// multi-byte encoding with 1 to 4 bytes
			byte[] bytes = new byte[] { (byte)(intCID >> 8 & 0xff), (byte)(intCID & 0xff) } ;

			try {out.write(bytes);} catch (IOException e) {e.printStackTrace();}

			offset += Character.charCount(codePoint);
		}
	
		return Hex.getString(out.toByteArray());
		
	}
 	private String addZeros(String a){
		int i = 0;
		i = a.length();
		if ( i == 4 ){return a;}
		else{
			int j= 4 - i;
			for (int k=0; k<j; k++){a = "0" + a;}
			return a;
		}
	}
	
	
}
