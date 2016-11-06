package pdfObjects;

public class Type1FontDictionary {
	private final String  PDFCRLF = "\r\n";
	private StringBuilder sbFontDict = new StringBuilder();
	private String strType = "/Type /Font" + PDFCRLF;
	private String strSubType = "/Subtype /Type1" + PDFCRLF;
	private String strName;
	private String strBaseFont;
	private String strFirstChar;
	private String strLastChar;
	private String strWidths;
	private String strFontDescriptor;
	private String strEncoding;
	private String strToUnicode;
	
	/** The constructors  for the class	 */
	public Type1FontDictionary(){}
	
	/** Property Getters */
	public String getType() {return strType;}// read only property
	public String getSubType() {return strSubType;	}// read only property
	public String getName() {	return strName;}
	public String getBaseFont() {return strBaseFont;}
	public String getFirstChar() {	return strFirstChar;	}
	public String getLastChar() {	return strLastChar;}
	public String getWidths() {return strWidths;}
	public String getFontDescriptor() {return strFontDescriptor;}
	public String getEncoding() {return strEncoding;}
	public String getToUnicode() {return strToUnicode;}
	
	/** Property Setters */	
	public void setName(String strName) {this.strName = strName;}
	public void setBaseFont(String strBaseFont) {this.strBaseFont = strBaseFont;	}
	public void setFirstChar(String strFirstChar) {this.strFirstChar = strFirstChar;}
	public void setLastChar(String strLastChar) {this.strLastChar = strLastChar;	}
	public void setWidths(String strWidths) {	this.strWidths = strWidths;}
	public void setFontDescriptor(String strFontDescriptor) {this.strFontDescriptor = strFontDescriptor;}
	public void setEncoding(String strEncoding) {this.strEncoding = strEncoding;	}
	public void setToUnicode(String strToUnicode) {this.strToUnicode = strToUnicode;}
	
	public String toString(){
		sbFontDict.append("<< " );// Start of dictionary
		sbFontDict.append(strType + PDFCRLF );
		sbFontDict.append(strSubType + PDFCRLF );
		if(strName.length()>0){sbFontDict.append("/Name " + strName + PDFCRLF );}//Optional obsolescent
		sbFontDict.append("/BaseFont /" + strBaseFont + PDFCRLF );
		sbFontDict.append("/FirstChar " + strFirstChar + PDFCRLF );
		sbFontDict.append("/LastChar "+ strLastChar + PDFCRLF );
		sbFontDict.append("/Widths [ " + strWidths +" ]" + PDFCRLF );
		sbFontDict.append("/FontDescriptor " + strFontDescriptor + PDFCRLF );
		if(strEncoding.length()>0){sbFontDict.append("/Encoding " + strEncoding + PDFCRLF );}//Optional
		if(strToUnicode.length()>0){sbFontDict.append("/ToUnicode "+ strToUnicode + PDFCRLF );}//Optional
		sbFontDict.append(">> " + PDFCRLF );// End of dictionary
		return sbFontDict.toString();
	}
}
