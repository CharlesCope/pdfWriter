package pdfObjects;

public class FontDescriptor {
	private final String  PDFCRLF = "\r\n";
	private StringBuilder sbFontDescriptorDic = new StringBuilder();
	private String strType = "/Type /FontDescriptor" + PDFCRLF;
	private String strFontName;
	private String strFontFamily;
	private String strFontStretch;
	private String strFontWeight;
	private String strFlags;
	private String strFontBBox;
	private String strItalicAngle;
	private String strAscent;
	private String strDescent;
	private String strLeading;
	private String strCapHeight;
	private String strXHeight;
	private String strStemV;
	private String strStemH;
	private String strAvgWidth;
	private String strMaxWidth;
	private String strMissingWidth;
	private String strFontFile;
	private String strFontFile2;
	private String strFontFile3;
	private String strCharSet;
	
	public static enum StretchName{
		UltraCondensed,
		ExtraCondensed,
		Condensed,
		SemiCondensed,
		Normal, 
		SemiExpanded,
		Expanded, 
		ExtraExpanded,
		UltraExpanded
	}
	
	public String getStrType() {return strType;} // read only property
	public String getStrFontName() {return strFontName;}
	public String getStrFontFamily() {return strFontFamily;	}
	public String getStrFontStretch() {return strFontStretch;}
	public String getStrFontWeight() {return strFontWeight;}
	public String getStrFlags() {return strFlags;	}
	public String getStrFontBBox() {	return strFontBBox;	}
	public String getStrItalicAngle() {return strItalicAngle;}
	public String getStrAscent() {return strAscent;	}
	public String getStrDescent() {return strDescent;}
	public String getStrLeading() {return strLeading;}
	public String getStrCapHeight() {return strCapHeight;}
	public String getStrXHeight() {return strXHeight;	}
	public String getStrStemV() {	return strStemV;	}
	public String getStrStemH() {return strStemH;	}
	public String getStrAvgWidth() {	return strAvgWidth;	}
	public String getStrMaxWidth() {return strMaxWidth;}
	public String getStrMissingWidth() {return strMissingWidth;}
	public String getStrFontFile() {return strFontFile;	}
	public String getStrFontFile2() {	return strFontFile2;}
	public String getStrFontFile3() {	return strFontFile3;	}
	public String getStrCharSet() {return strCharSet;	}
	
	
	public void setStrFontName(String strFontName) {this.strFontName = strFontName;}
	public void setStrFontFamily(String strFontFamily) {this.strFontFamily = strFontFamily;}
	public void setStrFontStretch(StretchName Name) {
		// Example how to set this property with enum value
		// FontDescriptor.StretchName.Expanded
		this.strFontStretch = Name.toString();
	}
	public void setStrFontWeight(String strFontWeight) {	this.strFontWeight = strFontWeight;}
	public void setStrFlags(String strFlags) {this.strFlags = strFlags;}
	public void setStrFontBBox(String strFontBBox) {this.strFontBBox = strFontBBox;}
	public void setStrItalicAngle(String strItalicAngle) {this.strItalicAngle = strItalicAngle;}
	public void setStrAscent(String strAscent) {this.strAscent = strAscent;	}
	public void setStrDescent(String strDescent) {this.strDescent = strDescent;}
	public void setStrLeading(String strLeading) {this.strLeading = strLeading;}
	public void setStrCapHeight(String strCapHeight) {this.strCapHeight = strCapHeight;	}
	public void setStrXHeight(String strXHeight) {this.strXHeight = strXHeight;}
	public void setStrStemV(String strStemV) {this.strStemV = strStemV;}
	public void setStrStemH(String strStemH) {this.strStemH = strStemH;	}
	public void setStrAvgWidth(String strAvgWidth) {this.strAvgWidth = strAvgWidth;}
	public void setStrMaxWidth(String strMaxWidth) {	this.strMaxWidth = strMaxWidth;}
	public void setStrMissingWidth(String strMissingWidth) {	this.strMissingWidth = strMissingWidth;}
	public void setStrFontFile(String strFontFile) {this.strFontFile = strFontFile;}
	public void setStrFontFile2(String strFontFile2) {this.strFontFile2 = strFontFile2;}
	public void setStrFontFile3(String strFontFile3) {this.strFontFile3 = strFontFile3;}
	public void setStrCharSet(String strCharSet) {this.strCharSet = strCharSet;}
	
	/** The constructors  for the class	 */
	public FontDescriptor(){}
	
	
	public String toString(){
		sbFontDescriptorDic.append("<< " );// Start of dictionary
		sbFontDescriptorDic.append(strType + PDFCRLF );
		
		sbFontDescriptorDic.append(">> " + PDFCRLF );// End of dictionary
		return sbFontDescriptorDic.toString();
	}
}
