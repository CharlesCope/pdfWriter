package pdfObjects;

public class FontDescriptor {
	private final String  PDFCRLF = "\r\n";
	private StringBuilder sbFontDescriptorDic = new StringBuilder();
	private String strType = "/Type /FontDescriptor" + PDFCRLF;
	private String strFontName="";
	private String strFontFamily="";
	private String strFontStretch="";
	private String strFontWeight="";
	private String strFlags="";
	private String strFontBBox="";
	private String strItalicAngle="";
	private String strAscent="";
	private String strDescent="";
	private String strLeading="";
	private String strCapHeight="";
	private String strXHeight="";
	private String strStemV="";
	private String strStemH="";
	private String strAvgWidth="";
	private String strMaxWidth="";
	private String strMissingWidth="";
	private String strFontFile="";
	private String strFontFile2="";
	private String strFontFile3="";
	private String strCharSet="";
	// for the CID
	private String strStyle="";
	private String strLang="";
	private String strFD="";
	private String strCIDSet="";
	
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
	/** The constructors  for the class	 */
	public FontDescriptor(){}
	
	/** Property Getters */
	public String getType() {return strType;} // read only property
	public String getFontName() {return strFontName;}
	public String getFontFamily() {return strFontFamily;	}
	public String getFontStretch() {return strFontStretch;}
	public String getFontWeight() {return strFontWeight;}
	public String getFlags() {return strFlags;	}
	public String getFontBBox() {	return strFontBBox;	}
	public String getItalicAngle() {return strItalicAngle;}
	public String getAscent() {return strAscent;	}
	public String getDescent() {return strDescent;}
	public String getLeading() {return strLeading;}
	public String getCapHeight() {return strCapHeight;}
	public String getXHeight() {return strXHeight;	}
	public String getStemV() {	return strStemV;	}
	public String getStemH() {return strStemH;	}
	public String getAvgWidth() {	return strAvgWidth;	}
	public String getMaxWidth() {return strMaxWidth;}
	public String getMissingWidth() {return strMissingWidth;}
	public String getFontFile() {return strFontFile;	}
	public String getFontFile2() {	return strFontFile2;}
	public String getFontFile3() {	return strFontFile3;	}
	public String getCharSet() {return strCharSet;	}
	public String getStyle() {return strStyle;}
	public String getLang() {return strLang;}
	public String getFD() {return strFD;}
	public String getCIDSet() {return strCIDSet;	}

	/** Property Setters */	
	public void setFontName(String strFontName) {this.strFontName = strFontName;}
	public void setFontFamily(String strFontFamily) {this.strFontFamily = strFontFamily;}
	public void setFontStretch(StretchName Name) {
		// Example how to set this property with enum value
		// FontDescriptor.StretchName.Expanded
		this.strFontStretch = Name.toString();
	}
	public void setFontWeight(String strFontWeight) {this.strFontWeight = strFontWeight;}
	public void setFlags(String strFlags) {this.strFlags = strFlags;}
	public void setFontBBox( String strBox) {	this.strFontBBox = strBox;}
	public void setItalicAngle(String strItalicAngle) {this.strItalicAngle = strItalicAngle;}
	public void setAscent(String strAscent) {this.strAscent = strAscent;	}
	public void setDescent(String strDescent) {this.strDescent = strDescent;}
	public void setLeading(String strLeading) {this.strLeading = strLeading;}
	public void setCapHeight(String strCapHeight) {this.strCapHeight = strCapHeight;	}
	public void setXHeight(String strXHeight) {this.strXHeight = strXHeight;}
	public void setStemV(String strStemV) {this.strStemV = strStemV;}
	public void setStemH(String strStemH) {this.strStemH = strStemH;	}
	public void setAvgWidth(String strAvgWidth) {this.strAvgWidth = strAvgWidth;}
	public void setMaxWidth(String strMaxWidth) {	this.strMaxWidth = strMaxWidth;}
	public void setMissingWidth(String strMissingWidth) {	this.strMissingWidth = strMissingWidth;}
	public void setFontFile(String strFontFile) {this.strFontFile = strFontFile;}
	public void setFontFile2(String strFontFile2) {this.strFontFile2 = strFontFile2;}
	public void setFontFile3(String strFontFile3) {this.strFontFile3 = strFontFile3;}
	public void setCharSet(String strCharSet) {this.strCharSet = strCharSet;}
	public void setStyle(String strStyle) {this.strStyle = strStyle;}
	public void setLang(String strLang) {this.strLang = strLang;}
	public void setFD(String strFD) {this.strFD = strFD;}
	public void setCIDSet(String strCIDSet) {this.strCIDSet = strCIDSet;	}
	
	
	
	public String toString(){
		sbFontDescriptorDic.append("<< " + PDFCRLF);// Start of dictionary
		sbFontDescriptorDic.append(strType);
		sbFontDescriptorDic.append("/FontName /"+strFontName + PDFCRLF );
		if(strFontFamily.length()>0){sbFontDescriptorDic.append("/FontFamily "+strFontFamily+ PDFCRLF );}//Optional
		if(strFontStretch.length()>0){sbFontDescriptorDic.append("/FontStretch "+strFontStretch+ PDFCRLF );}//Optional
		if(strFontWeight.length()>0){sbFontDescriptorDic.append("/FontWeight " +strFontWeight+ PDFCRLF );}//Optional
		sbFontDescriptorDic.append("/Flags " + strFlags + PDFCRLF );
		sbFontDescriptorDic.append("/FontBBox ["+ strFontBBox + "]"+ PDFCRLF );
		sbFontDescriptorDic.append("/ItalicAngle "+ strItalicAngle + PDFCRLF );
		if(strAscent.length()>0){sbFontDescriptorDic.append("/Ascent " +strAscent+ PDFCRLF );}//Optional Type 3
		if(strDescent.length()>0){sbFontDescriptorDic.append("/Descent "+ strDescent+ PDFCRLF );}//Optional Type 3
		if(strLeading.length()>0){sbFontDescriptorDic.append("/Leading "+ strLeading + PDFCRLF );}//Optional
		if(strCapHeight.length()>0){sbFontDescriptorDic.append("/CapHeight "+ strCapHeight + PDFCRLF );}//Optional Type 3
		if(strXHeight.length()>0){sbFontDescriptorDic.append("/XHeight "+ strXHeight + PDFCRLF );}//Optional
		if(strStemV.length()>0){sbFontDescriptorDic.append("/StemV "+ strStemV + PDFCRLF );}//Optional Type 3
		if(strStemH.length()>0){sbFontDescriptorDic.append("/StemH "+ strStemH + PDFCRLF );}//Optional
		if(strAvgWidth.length()>0){sbFontDescriptorDic.append("/AvgWidth "+ strAvgWidth + PDFCRLF );}//Optional
		if(strMaxWidth.length()>0){sbFontDescriptorDic.append("/MaxWidth "+ strMaxWidth + PDFCRLF );}//Optional
		if(strMissingWidth.length()>0){sbFontDescriptorDic.append("/MissingWidth "+ strMissingWidth + PDFCRLF );}//Optional
		if(strFontFile.length()>0){sbFontDescriptorDic.append("/FontFile " + strFontFile + PDFCRLF );}//Optional
		if(strFontFile2.length()>0){sbFontDescriptorDic.append("/FontFile2 "+ strFontFile2 + PDFCRLF );}//Optional
		if(strFontFile3.length()>0){sbFontDescriptorDic.append("/FontFile3 " + strFontFile3 + PDFCRLF );}//Optional
		if(strCharSet.length()>0){sbFontDescriptorDic.append("/CharSet "+ strCharSet + PDFCRLF );}//Optional
		if(strStyle.length()>0){sbFontDescriptorDic.append("/Style "+ strStyle + PDFCRLF );}//Optional
		if(strLang.length()>0){sbFontDescriptorDic.append("/Lang "+ strLang + PDFCRLF );}//Optional
		if(strFD.length()>0){sbFontDescriptorDic.append("/FD "+ strFD + PDFCRLF );}//Optional
		if(strCIDSet.length()>0){sbFontDescriptorDic.append("/CIDSet "+ strCIDSet + PDFCRLF );}//Optional
		sbFontDescriptorDic.append(">> " + PDFCRLF );// End of dictionary
		return sbFontDescriptorDic.toString();
	}
}
