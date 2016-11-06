package cidObjects;

public class CIDFontDictionary {
	private final String  PDFCRLF = "\r\n";
	private StringBuilder sbCIDFontDict = new StringBuilder();
	private String strType = "/Type /Font" + PDFCRLF;
	private String strSubType;
	private String strBaseFont;
	private String strCIDSystemInfo;
	private String strFontDescriptor;
	private String strDW;
	private String strW;
	private String strDW2;
	private String strW2;
	private String strCIDToGIDMap;
	
	public static enum CIDFontTypes{
		CIDFontType0,
		CIDFontType2
	}
	
	/** Property Getters */
	public String getType() {return strType;}
	public String getSubType() {return strSubType;	}
	public String getBaseFont() {return strBaseFont;}
	public String getCIDSystemInfo() {return strCIDSystemInfo;}
	public String getFontDescriptor() {return strFontDescriptor;}
	public String getDW() {	return strDW;	}
	public String getW() {return strW;	}
	public String getDW2() {return strDW2;}
	public String getW2() {	return strW2;	}
	public String getCIDToGIDMap() {return strCIDToGIDMap;}

	/** Property Setters */	
	public void setSubType(CIDFontTypes Type) {
		// Example how to set this property with enum value
		// CIDFontTypes.CIDFontTypes.CIDFontType2
		this.strSubType = Type.toString();}
	public void setBaseFont(String strBaseFont) {this.strBaseFont = strBaseFont;	}
	public void setCIDSystemInfo(String strCIDSystemInfo) {	this.strCIDSystemInfo = strCIDSystemInfo;}
	public void setFontDescriptor(String strFontDescriptor) {this.strFontDescriptor = strFontDescriptor;}
	public void setDW(String strDW) {this.strDW = strDW;}
	public void setW(String strW) {this.strW = strW;}
	public void setDW2(String strDW2) {this.strDW2 = strDW2;	}
	public void setW2(String strW2) {this.strW2 = strW2;}
	public void setCIDToGIDMap(String strCIDToGIDMap) {this.strCIDToGIDMap = strCIDToGIDMap;}
	
	/** The constructors  for the class	 */
	public CIDFontDictionary(){}
	
	public String toString(){
		sbCIDFontDict.append("<< " );// Start of dictionary
		sbCIDFontDict.append(strType + PDFCRLF );
		sbCIDFontDict.append("/Subtype / " + strSubType + PDFCRLF );
		sbCIDFontDict.append("/BaseFont / " + strBaseFont+ PDFCRLF );
		sbCIDFontDict.append("/CIDSystemInfo " + strCIDSystemInfo + PDFCRLF );
		sbCIDFontDict.append("/FontDescriptor " + strFontDescriptor + PDFCRLF );
		if(strDW.length()>0){sbCIDFontDict.append("/ DW " + strDW + PDFCRLF );}//Optional
		if(strW.length()>0){sbCIDFontDict.append("/W " + strW + PDFCRLF );}//Optional
		if(strDW2.length()>0){sbCIDFontDict.append("/DW2 " + strDW2 + PDFCRLF );}//Optional
		if(strW2.length()>0){sbCIDFontDict.append("/W2" + strW2 + PDFCRLF );}//Optional
		if(strCIDToGIDMap.length()>0){sbCIDFontDict.append("/CIDToGIDMap "+ strCIDToGIDMap + PDFCRLF );}//Optional
		sbCIDFontDict.append(">> " + PDFCRLF );// End of dictionary
		return sbCIDFontDict.toString();
	}
}
