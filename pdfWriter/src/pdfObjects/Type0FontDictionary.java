package pdfObjects;

public class Type0FontDictionary {
	private final String  PDFCRLF = "\r\n";
	private StringBuilder sbFontDict = new StringBuilder();
	private String strType = "/Type /Font" + PDFCRLF;
	private String strSubType = "/Subtype /Type0" + PDFCRLF;
	private String strBaseFont="";
	private String strEncoding="";
	private String strDescendantFonts="";
	private String strToUnicode="";
	
	/** The constructors  for the class	 */
	public Type0FontDictionary(){}
	
	/** Property Getters */
	public String getType() {return strType;}// read only property
	public String getSubType() {return strSubType;	}// read only property
	public String getBaseFont() {return strBaseFont;}
	public String getEncoding() {return strEncoding;}
	public String getDescendantFonts() {return strDescendantFonts;	}
	public String getToUnicode() {return strToUnicode;}
	
	/** Property Setters */
	public void setBaseFont(String strBaseFont) {this.strBaseFont = strBaseFont;	}
	public void setEncoding(String strEncoding) {this.strEncoding = strEncoding;	}
	public void setDescendantFonts(String strDescendantFonts) {this.strDescendantFonts = strDescendantFonts;}
	public void setToUnicode(String strToUnicode) {this.strToUnicode = strToUnicode;}
	
	public String toString(){
		sbFontDict.append("<< " );// Start of dictionary
		sbFontDict.append(strType);
		sbFontDict.append(strSubType);
		sbFontDict.append("/BaseFont /" + strBaseFont + PDFCRLF );
		sbFontDict.append("/Encoding /" + strEncoding + PDFCRLF );
		sbFontDict.append("/DescendantFonts [" + strDescendantFonts + "]" +PDFCRLF );
		if(strToUnicode.length()>0){sbFontDict.append("/ToUnicode "+ strToUnicode + PDFCRLF );}//Optional
		sbFontDict.append(">> " + PDFCRLF );// End of dictionary
		return sbFontDict.toString();
	}
}
