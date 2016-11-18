package pdfWriter;


import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import Fonts.FontManager;
import Fonts.PDFFont;
import Fonts.fontToPDFfont;
import cidObjects.CIDFontDictionary;
import cidObjects.CIDFontDictionary.CIDFontTypes;
import pdfObjects.FontDescriptor;
import pdfObjects.Type0FontDictionary;

public class clsPdfWriter {

  //... Enum	
	public enum pdfPaperSize{
	    //... American paper sizes
	    pdfLetter ,
	    pdfLegal ,
	    pdfLedger ,
	    pdfTabloid ,
	    pdfExecutive ,
	    //... ISO A paper sizes
	    pdfA0 ,
	    pdfA1 ,
	    pdfA2 ,
	    pdfA3 ,
	    pdfA4 ,
	    pdfA5 ,
	    pdfA6 ,
	    pdfA7 
	    //... Add more paper size if you need them here.
	}
	
    public enum pdfTextAlign{
	    pdfAlignLeft ,
	    pdfAlignRight ,
	    pdfCenter ,
	}
	
	public enum pdfStandardFonts{
	    //-- These fonts, or their font metrics and suitable substitution fonts,
	    //-- must be available to the consumer application.
	    Times_Roman ,
	    Times_Bold ,
	    Times_Italic ,
	    Times_BoldItalic,
	    Helvetica ,
	    Helvetica_Bold ,
	    Helvetica_Oblique ,
	    Helvetica_BoldOblique ,
	    Courier ,
	    Courier_Bold ,
	    Courier_Oblique ,
	    Courier_BoldOblique ,
	    Symbol ,
	    ZapfDingbats ,
	}
	
	public enum pdfColorSpace{
	    //-- Some color spaces are related to device color representation (grayscale, RGB, CMYK)
	    pdfRGB ,
	    pdfGrayScale ,
	}	
		
  //... Structure
		
	public class ImageDictionary{
	    Long ImgDicWidth ;
	    Long ImgDicHeight ;
	    String ImgDicColorSpace ;
	    Long ImgDicBitsPerComponent ;
	    String ImgDicFilter ;
	    Object ImgDicDataStream ;
	    String ImgDicDots ;
	    Long ImgDicFileSize ;
	}	
	
  //... private Variables
	private final String  PDFCRLF = "\r\n";
    //-- I want to be able to keep up with each object and it offset using collection.
    //-- This will be a name value pair Example "1 obj" 08 
    private NameValueCollection	colCrossReferenceTable;
    private NameValueCollection	colFonts; 
    private NameValueCollection colPageText;
    private NameValueCollection colLineCode;
    private NameValueCollection colCircles;
    private NameValueCollection colDrawImages;
    private NameValueCollection colXobjectImages;
    public NameValueCollection colImages;
    //-- Keep up with the fonts used in the program.
    private Dictionary<String, Integer>	dicFontsUsed;

    private StringBuilder	FileText;
    private Integer intpdfObjectCount= 0;
    private Integer intFontCount  = 0;
    private Integer intStreamLength  = 0;
    //-- Information Dictionary members
    private String _pdfTitle = "";
    private String  _pdfAuthor = "";
    private String  _pdfSubject = "";
    private String  _pdfKeyWords = "";
    private String  _pdfCreator = "";
    private String  _pdfProducer = "";
    private Integer _pdfPageCount  = 1;
    private Boolean _pdfCommentFile = false;
    //-- Use to create the size of paper to draw upon
    private pdfPaperSize mvarPaperSize;
    private Integer intPageWidth = 612; //-- Default Size
    private Integer intPageHeight = 864; //-- Default Size
    private Integer intPageTree ; //-- Keeps up with our page tree
    private Integer intPageCount; //-- Keeps up with our page number
    private Integer intStringCount; //-- Keep up with string on pages
    private Integer intLineCount; //-- Keep up with the lines in the file/page
    private Integer intCircleCount; //-- Keep up with the Circles in the file/page
    private Integer intDrawImagesCount; //-- Keep up with the Draw Images in the file/page
    private Integer intXObjectCount; //-- Keep up with the XObject in the file
    private Integer intFontDescriptorCount; //-- Keep up with Font Descriptors
    private Integer intDynamicObjectCount;//-- Keep up with Dynamic Objects
    private Integer intToUnicodeObject = 0; //-- Keep up with the Unicode Cmap file;
    private Boolean blnToUnicodeNeeded = true; // Keep only one copy of unicode Cmap in file.
    private  List<PDFFont> PDFFontList = new LinkedList<>();

    //-- Used for our Jpeg files only.
    private ImageDictionary strImageJPEG;
  
    private Integer intCrossRefOffSet;
    
	public clsPdfWriter() {
		
		super();
	
		dicFontsUsed = new Hashtable<String, Integer>(); 
		colCrossReferenceTable = new NameValueCollection();
	    colFonts= new NameValueCollection(); 
	    colPageText= new NameValueCollection();
	    colLineCode= new NameValueCollection();
	    colCircles= new NameValueCollection();
	    colDrawImages= new NameValueCollection();
	    colXobjectImages= new NameValueCollection();
	    colImages= new NameValueCollection();

	    FileText = new StringBuilder();
	    
	    intpdfObjectCount= 0;
	    intFontCount  = 0;
	    intStreamLength  = 0;
	    //-- Information Dictionary members
	    _pdfTitle = "";
	    _pdfAuthor = "";
	    _pdfSubject = "";
	    _pdfKeyWords = "";
	    _pdfCreator = "";
	    _pdfProducer = "";
	    _pdfPageCount  = 1;
	    _pdfCommentFile = false;
	    //-- Use to create the size of paper to draw upon
	    //...pdfPaperSize mvarPaperSize 
	    
	    intPageWidth = 612; //-- Default Size
	    intPageHeight = 864; //-- Default Size
	    intPageTree =0; //-- Keeps up with our page tree
	    intPageCount=0; //-- Keeps up with our page number
	    intStringCount=0; //-- Keep up with string on pages
	    intLineCount=0; //-- Keep up with the lines in the file/page
	    intCircleCount=0; //-- Keep up with the Circles in the file/page
	    intDrawImagesCount=0; //-- Keep up with the Draw Images in the file/page
	    intXObjectCount=0; //-- Keep up with the XObject in the file
	    intFontDescriptorCount=0; //-- Keep up with Font Descriptors
	    
	    strImageJPEG = new ImageDictionary();
	    
	    intCrossRefOffSet = 0;
		
	}
	//... Get Set Properties    
	public String pdfTitle() {
		return _pdfTitle;
	}
	public void pdfTitle(String _pdfTitle) {
		this._pdfTitle = _pdfTitle;
	}
	public String pdfAuthor() {
		return _pdfAuthor;
	}
	public void pdfAuthor(String _pdfAuthor) {
		this._pdfAuthor = _pdfAuthor;
	}
	public String pdfSubject() {
		return _pdfSubject;
	}
	public void pdfSubject(String _pdfSubject) {
		this._pdfSubject = _pdfSubject;
	}
	public String pdfKeyWords() {
		return _pdfKeyWords;
	}
	public void pdfKeyWords(String _pdfKeyWords) {
		this._pdfKeyWords = _pdfKeyWords;
	}
	public String pdfCreator() {
		return _pdfCreator;
	}
	public void pdfCreator(String _pdfCreator) {
		this._pdfCreator = _pdfCreator;
	}
	public String pdfProducer() {
		return _pdfProducer;
	}
	public void pdfProducer(String _pdfProducer) {
		this._pdfProducer = _pdfProducer;
	}
	public Integer PageCount() {
		return _pdfPageCount;
	}
	public void PageCount(Integer _pdfPageCount) {
		this._pdfPageCount = _pdfPageCount;
	}
	public Boolean CommentFile() {
		return _pdfCommentFile;
	}
	public void CommentFile(Boolean _pdfCommentFile) {
		this._pdfCommentFile = _pdfCommentFile;
	}
	
	public pdfPaperSize PaperSize() {
		return mvarPaperSize;
	}
	public void PaperSize(pdfPaperSize mvarPaperSize) {
		this.mvarPaperSize = mvarPaperSize;
		
		switch(mvarPaperSize){
           //-- Letter, 8.5 x 11 in.
			case pdfLetter:
               intPageWidth = 612;
               intPageHeight = 792;
               break;
               //-- Legal, 8.5 x 14 in.
			case pdfLegal:
               intPageWidth = 612;
               intPageHeight = 1008;
               break;
               //-- Ledger, 17 x 11 in. 
           case pdfLedger:
               intPageWidth = 792;
               intPageHeight = 1224;
               break;
               //-- Tabloid, 11 x 17 in.
           case pdfTabloid:
               intPageWidth = 1224;
               intPageHeight = 792;
               break;
               //-- Executive, 10.55 x 7.25 in.
           case pdfExecutive:
               intPageWidth = 522;
               intPageHeight = 756;
               break;
               //-- The Largest A0, 46.81 x 33.11 in.
           case pdfA0:
               intPageWidth = 2384;
               intPageHeight = 3370;
               break;
               //-- A1, 33.11 x 23.39 in.
           case pdfA1:
               intPageWidth = 1684;
               intPageHeight = 2384;
               break;
               //-- A2, 23.39 x 16.54 in. 
           case pdfA2:
               intPageWidth = 1190;
               intPageHeight = 1684;
               break;
               //-- A3, 16.54  x 11.69 in.
           case pdfA3:
               intPageWidth = 842;
               intPageHeight = 1190;
               break;
               //-- A4, 11.69  x 8.27 in. 
           case pdfA4:
               intPageWidth = 595;
               intPageHeight = 842;
               break;
               //-- A5, 8.27  x 5.83 in. 
           case pdfA5:
               intPageWidth = 420;
               intPageHeight = 595;
               break;
               //-- A6, 5.83  x 4.13 in. 
           /*case pdfA0:
               intPageWidth = 298;
               intPageHeight = 420;
               break;
               //-- A7, 4.13 x 2.91 in. 
           case pdfA0:
               intPageWidth = 209;
               intPageHeight = 298;
               break;*/
		   default:
			   intPageWidth = 209;
               intPageHeight = 298;  
               break;

		}
		
	}	
    
	public void ShowingText(Integer intPage, Integer sngHorzOffSet, Integer sngVertOffSet, String strTextToShow, pdfStandardFonts FontName, 
			 	Integer Fontsize , Color color, pdfTextAlign Align , Integer Rotate){
		 //--Ok I want to add any text for a page to the collection for that page.
		 switch(Align){
		 case	pdfAlignLeft:
			//--The default showing.
			 break;
		 case	pdfAlignRight:
			 sngHorzOffSet = intPageWidth - (sngHorzOffSet + (strTextToShow.length() * Fontsize));
			 break;
		 case	pdfCenter:
			 sngHorzOffSet = intPageWidth / 2 + sngHorzOffSet;
			 break;
			 
		 }
		 
	     //--Keep Up with our fonts that are used in the program
	     //--Test if the Font key exists, and then add it if it doesn't.
		 
	     if(dicFontsUsed.get("pdfStandardFonts."+FontName.toString()) == null){
	         intFontCount += 1;
	         dicFontsUsed.put("pdfStandardFonts." + FontName.toString(), intFontCount);
	     }
	
	
	     String strCodeText  = "";
	
	     //--rg- Set RGB color for nonstroking operations
	     strCodeText += color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " rg"+PDFCRLF;
	
	     //--BT- Begin text object
	     strCodeText += "BT" + PDFCRLF;
	     //--Tf - Set text font and size
	     strCodeText += "/F" + dicFontsUsed.get("pdfStandardFonts." + FontName.toString()).toString() + " " + Fontsize.toString()+ " Tf" + PDFCRLF;
	     //--Now do we need to rotate it
	     if(Rotate != 0){
	         //--System.Math.Sin and System.Math.Cos is in radians. we need to convert to degrees
	         Double dblDegrees  = Math.PI / 180.0;
	         //--Little Math reminder long time ago since I study math 30plus years
	         //--There is 360 degrees in a circle and we will pivot around the start point of the text.
	         //--The ratio of the height to the hypotenuse is called the sine
	         Double sngSinAngle  = Math.sin(dblDegrees * Rotate);
	         //--The ratio of the base to the hypotenuse is called the cosine 
	         Double sngCosAngle  = Math.cos(dblDegrees * Rotate);
	         String fmt = "%03.3f";
	         //--Tm- Set text matrix and text line matrix
	         //--Using the + operator caused a error in compiler so had to go back to VB6 style string concatenation to get over exception.
	         strCodeText += String.format(fmt, sngCosAngle) + " " + String.format(fmt, sngSinAngle) + " " + String.format(fmt, -sngSinAngle) + " " +
	        		 String.format(fmt, sngCosAngle) + " " + sngHorzOffSet + " " + sngVertOffSet + " Tm" + PDFCRLF;
	     }else{
	         //--
	         //--Where to place the string on the page                               Td- Move text position
	         strCodeText += sngHorzOffSet + " " + sngVertOffSet + " Td" + PDFCRLF;
	     }
	
	
	     //--Text to display on page
	     strCodeText += "(" + CheckReserveChar(strTextToShow) + ") Tj" + PDFCRLF;
	
	
	     //--End the Text block
	     strCodeText += "ET" + PDFCRLF;
	     //--Need to keep keys unique
	     intStringCount += 1;
	     colPageText.add(intPage + "." + intStringCount, strCodeText);		 
	    
	}
	 
	public void ShowingText(Integer intPage, Integer sngHorzOffSet, Integer sngVertOffSet, String strTextToShow, Font font,Integer intFontSize , Color color, pdfTextAlign Align , Integer Rotate){
		//-- Keep Up with our fonts that are used in the program
		//-- Test if the Font key exists, and then add it if it doesn't.

		if (dicFontsUsed.get( font.getFontName())==null){
			intFontCount += 1;
			
			dicFontsUsed.put(font.getName(), intFontCount);
			String strFilePath = getFontPath(font);
		
			PDFFont	pdfFont = fontToPDFfont.ConvertFontFileToPDFFont(strFilePath); 
			PDFFontList.add(pdfFont);
		}
		
		PDFFont curPDFFont = PDFFontList.get(dicFontsUsed.get(font.getName())-1);
		
		Double sngLength ;
		
		  switch(Align){
		      case pdfAlignLeft:
		          //-- The default showing.
		    	  break;
		      case pdfAlignRight:
		          sngLength = curPDFFont.getStringWidth(strTextToShow, intFontSize);
		          sngHorzOffSet =(int)(intPageWidth - (sngLength + sngHorzOffSet));
		          break;
		      case pdfCenter:
		    	  sngLength = curPDFFont.getStringWidth(strTextToShow, intFontSize);
		          //-- When Center forget about the offset to make the text center
		          //-- This could be overloaded to allow center plus offset
		          sngHorzOffSet = (int)((intPageWidth / 2) - (sngLength / 2)); //'+ sngHorzOffSet
		          break;
		  }
		  
		  String strCodeText  = "";
		  //-- rg- Set RGB color for nonstroking operations
		  strCodeText += color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " rg"+PDFCRLF;
		  //-- BT- Begin text object
		  strCodeText += "BT" +PDFCRLF;
		  //-- Tf - Set text font and size
		  strCodeText += "/F" + dicFontsUsed.get(font.getName()).toString() + " " + intFontSize.toString() + " Tf" +PDFCRLF;
		  
		//-- Now do we need to rotate it
		  if (Rotate != 0) {
		      //-- System.Math.Sin and System.Math.Cos is in radians. we need to convert to degrees
			  Double dblDegrees  = Math.PI / 180.0;
		      //-- Little Math reminder long time ago since I study math 30plus years
		      //-- There is 360 degrees in a circle and we will pivot around the start point of the text.
		      //-- The ratio of the height to the hypotenuse is called the sine
		      
		      Double sngSinAngle  = Math.sin(dblDegrees * Rotate);
		      //-- The ratio of the base to the hypotenuse is called the cosine 
		      Double sngCosAngle  = Math.cos(dblDegrees * Rotate);
		      String fmt = "%03.3f";
		      //-- Tm- Set text matrix and text line matrix
		          	  
		      strCodeText += String.format(fmt, sngCosAngle) + " " + String.format(fmt, sngSinAngle) + " " + String.format(fmt, -sngSinAngle) + " " +
		 	        		 String.format(fmt, sngCosAngle) + " " + sngHorzOffSet + " " + sngVertOffSet + " Tm" + PDFCRLF;
		      
		  }else{
		      //-- 
		      //-- Where to place the string on the page                               Td- Move text position
		      strCodeText += sngHorzOffSet.toString() + " " + sngVertOffSet.toString() + " Td" +PDFCRLF;
		  }
		  // TODO Don't know if I need this or not Check it when I get it working ( CheckReserveChar(strTextToShow))
		  String strTemp = curPDFFont.getEncodedString(strTextToShow );
	
		  //-- Text to display on page
		  strCodeText += "<" + strTemp + "> Tj" +PDFCRLF;


		  //-- End the Text block
		  strCodeText += "ET" +PDFCRLF;
		  //-- Need to keep keys unique
		  intStringCount += 1;
		  colPageText.add(intPage.toString() + "." + intStringCount.toString(), strCodeText);
	}
	
	private String CheckReserveChar(String strValue){
     //-- Ok PDF Files have reserve meaning for the above character so proceed them with the back slash.
     //-- Look for the \ in the Temp String and Replace it with \\
     //-- Then Look for ( in the Temp String and Replace it with \(
     //-- Then Look for ) in the Temp String and Replace it with \)
		strValue = strValue.replace("\\", "\\\\");
		strValue = strValue.replace("(", "\\(");
		strValue = strValue.replace(")", "\\)");
				
		return strValue;
				
     //-- Replace- returns a string in which a specified substring has been replaced with another substring a specified number of times.
     //-- Replace(expression, find, replace[, start[, count[, compare]]])
	}
	 
	
	
	//Region "Graphic Paths"
    public void LineHorizontal(Integer intPage, Integer sngHorzOffSet, Integer sngVertOffSet, Integer LineLength , Color LineColor, Integer intThickness){
    	String strComment  = "";
        if (_pdfCommentFile == true) {
            strComment = "% Comment- Call to LineHorizontal " + PDFCRLF;
        }
        String strCodeLine  = strComment;
        String fmt  = "%03d";
        //--Set the color of the line
        strCodeLine += LineColor.getRed() + " " + LineColor.getGreen() + " " + LineColor.getBlue() + " RG" + PDFCRLF;
        //--Set the thickness of the line
        strCodeLine += intThickness.toString() + " w" + PDFCRLF;
        //--m - Begin a new subpath by moving the current point to coordinates (x, y),
        //--omitting any connecting line segment. If the previous path construction operator
        //--in the current path was also m, the new m overrides it; no vestige of the previous m
        //--operation remains in the path.
        //--This is the Starting point of the line
        strCodeLine += String.format(fmt, sngHorzOffSet) + " " + String.format(fmt,sngVertOffSet) + " m" + PDFCRLF;
        //--This draws the line to the length from the start point
        //--Line draws from right of page to left of page.
        Integer sngLenght  = sngHorzOffSet - LineLength;
        //--l - Append a straight line segment from the current point to the point (x, y).
        strCodeLine += String.format(fmt, sngLenght) + " " + String.format(fmt, sngVertOffSet) + " l" + PDFCRLF;
        //--The S is for paint the line (path) Stroked 
        strCodeLine += "S" + PDFCRLF;
        //--Need to keep keys unique
        intLineCount += 1;
        colLineCode.add(intPage.toString() + "." + intLineCount.toString(), strCodeLine);

    }

    public void LineVertical(Integer intPage, Integer sngHorzOffSet, Integer sngVertOffSet, Integer LineLength , Color LineColor, Integer intThickness) {
    
    	String strComment  = "";
        if (_pdfCommentFile == true) {
            strComment = "% Comment- Call to LineVertical " + PDFCRLF;
        }
        String strCodeLine  = strComment;
        String fmt  = "%03d";
        
        //--Set the color of the line
        strCodeLine += LineColor.getRed() + " " + LineColor.getGreen() + " " + LineColor.getBlue() + " RG" + PDFCRLF;
        //--Set the thickness of the line
        strCodeLine += intThickness.toString() + " w" + PDFCRLF;
        //--m - Begin a new subpath by moving the current point to coordinates (x, y),
        //--omitting any connecting line segment. If the previous path construction operator
        //--in the current path was also m, the new m overrides it; no vestige of the previous m
        //--operation remains in the path.
        //--This is the Starting point of the line
        strCodeLine += String.format(fmt, sngHorzOffSet) + " " + String.format(fmt,sngVertOffSet) + " m" + PDFCRLF;

        //--Line draws from bottom of page to top of page.
        Integer sngHeight  = sngVertOffSet - LineLength;
        //--l - Append a straight line segment from the current point to the point (x, y).
        strCodeLine += String.format(fmt, sngHorzOffSet) + " " + String.format(fmt, sngHeight) + " l" + PDFCRLF;
        //--The S is for paint the line(path) Stroked 
        strCodeLine += "S" + PDFCRLF;
        //--Need to keep keys unique
        intLineCount += 1;
        colLineCode.add(intPage.toString() + "." + intLineCount.toString(), strCodeLine);

    }

    public void DrawImg(Integer intPage, String Name, Integer sngHorzLeftOffSet, Integer sngVertBottomOffSet, 
    		Integer imgWidth, Integer ImgHeight, Boolean BitMapFile ){
    
        String strComment  = "";
        if (_pdfCommentFile == true) {
            strComment = "% Comment- Call to LineHorizontal " + PDFCRLF;
        }
        
        
        String strBitMap ;
        //--Found JPEG Files was draw upside down from Bitmap files in the transform matric
        if (BitMapFile == true) {
            strBitMap = " 0 0 -";
        }else{
            strBitMap = " 0 0 ";
        }
        //--The page size divide by 72 equal 1 inch so 612/72 = 8.5
        //--The Horizontal offset must be between
        if (sngHorzLeftOffSet > intPageWidth) {
            //--Just keep the image on the page
            sngHorzLeftOffSet = intPageWidth - 20;
        }else if (sngHorzLeftOffSet < 0) {
            //--Just keep the image on the page
            sngHorzLeftOffSet = 20;
        }
        if( sngVertBottomOffSet > intPageHeight ) {
            //--Just keep the image on the page
            sngVertBottomOffSet = intPageHeight - 20;
        }else if ( sngVertBottomOffSet < 0 ) {
            //--Just keep the image on the page
            sngVertBottomOffSet = 20;
        }
        if(imgWidth > intPageWidth ) {
            //--Dont let the image be larger than the page.
            imgWidth = intPageWidth - 20;
        }
        if(ImgHeight > intPageHeight ) {
            //--Dont let the image be larger than the page.
            ImgHeight = intPageHeight - 20;
        }
        String fmt = "%4d";
        String strDrawImage  = strComment;
        //--q - Save graphics state
        strDrawImage = "q" + PDFCRLF;
        strDrawImage += String.format(fmt,  imgWidth) + strBitMap;
        strDrawImage += String.format(fmt,  ImgHeight) + " ";
        strDrawImage += String.format(fmt,  sngHorzLeftOffSet) + " ";
        //--cm - Concatenate matrix to current transformation matrix
        strDrawImage += String.format(fmt,  sngVertBottomOffSet) + " cm ";
        //--Do - Invoke named XObject
        strDrawImage += "/" + Name + " Do ";
        //--Q - Restore graphics state
        strDrawImage += "Q" + PDFCRLF;
        //--Need to keep keys unique
        intDrawImagesCount += 1;
        colDrawImages.add(intPage.toString() + "." + intDrawImagesCount.toString(), strDrawImage);

    }

    public void DrawCircle(Integer intPage , Point Center , Integer Radius , Color LineColor){
    	String strComment  = "";
        if (_pdfCommentFile == true) {
            strComment = "% Comment- Call to DrawCircle " + PDFCRLF;
        }
    
        //--Given the coordinates of the four points of the BezierCurve
        //--the curve is generated by varying the Parameter T from 0.0 to 1.0
        Double sngParameterT = 0.55;
        String strCircle  = strComment;
        String fmt = "%03d";
        //--Set the color of the line
        strCircle += LineColor.getRed() + " " + LineColor.getGreen() + " " + LineColor.getBlue() + " RG" + PDFCRLF;
        //--Set our center point
        Point Point0 = new Point(Center.x, Center.y - Radius);
        
        strCircle += String.format(fmt, Point0.x) + " " + String.format(fmt, Point0.y) + " m" + PDFCRLF;
        

        Point Point1 = new Point((int)(Center.x + sngParameterT * Radius), Center.y - Radius);
        Point Point2 = new Point(Center.x + Radius,(int)(Center.y - sngParameterT * Radius));
        Point Point3 = new Point(Center.x + Radius, Center.y);
        //--Our First Curve
        strCircle += BezierCurve(Point1, Point2, Point3);
        Point Point4 = new Point(Center.x + Radius, (int)(Center.y + sngParameterT * Radius));
        Point Point5 = new Point((int)(Center.x + sngParameterT * Radius), Center.y + Radius);
        Point Point6 = new Point(Center.x, Center.y + Radius);
        //--Our Second Curve
        strCircle += BezierCurve(Point4, Point5, Point6);
        Point Point7 = new Point((int)(Center.x - sngParameterT * Radius), Center.y + Radius);
        Point Point8 = new Point(Center.x - Radius, (int)(Center.y + sngParameterT * Radius));
        Point Point9 = new Point(Center.x - Radius, Center.y);
        //--Our Third Curve
        strCircle += BezierCurve(Point7, Point8, Point9);

        Point Point10 = new Point(Center.x - Radius, (int)(Center.y - sngParameterT * Radius));
        Point Point11 = new Point((int)(Center.x - sngParameterT * Radius), Center.y - Radius);
        Point Point12 = new Point(Center.x, Center.y - Radius);
        //--Our Closing Curve
        strCircle += BezierCurve(Point10, Point11, Point12);
        //--The S is for paint the line (path) Stroked 
        strCircle += "S" + PDFCRLF;

        //--Need to keep keys unique
        intCircleCount += 1;
        colCircles.add(intPage.toString() + "." + intCircleCount.toString(), strCircle);
    }

    private String BezierCurve(Point ControlPoint1 , Point ControlPoint2 , Point FinalPoint ) {
        //--To Do Need to Overload this function to make it public use or private use later
        //--Curved path segments are specified as cubic Bézier curves. Such curves are defined by four points: 
        //--the two endpoints (the current point P0 and the final point P3 ) and two control points P1 and P2
        //--This function should only be called after the Start point has been created using the Move m control char in pdf.
        return ControlPoint1.x + " " + ControlPoint1.y + " " + ControlPoint2.x + " " + ControlPoint2.y + " " + FinalPoint.x + " " + FinalPoint.y + " c" + PDFCRLF;
    }

    //End Region	
	
		
	 
	//...Region "Helper Subs"
    private void upDateRefernceTable(){
        //-- This just keep up with each pdf obj that is created an added to the cross reference table at the
        //-- end of the file.
        intpdfObjectCount += 1;
        colCrossReferenceTable.add(intpdfObjectCount.toString() + " 0 obj", FileText.length()+"");
	}
    //...End Region

	//...Region "File Flow Function"
    private String FileHeader(){
        //-- The first line of a PDF file is a header identifying the version of the PDF
        //-- specification to which the file conforms
        //-- %PDF−1.0
        //-- %PDF−1.1
        //-- %PDF−1.2
        //-- %PDF−1.3
        //-- %PDF−1.4
        //-- %PDF−1.5
        //-- %PDF−1.6
        //-- %PDF-1.7 This is the Refernce I used to create the file but only using version 1.3
        //-- of the Features so I am hard coding it for now maybe later pass in the values
    	String strFileHeader  = "%PDF-1.4";
        //-- Note: if (a PDF file contains binary data, as most do (see Section 3.1, “Lexical Conventions”),
        //-- it is recommended that the header line be immediately followed by a comment line containing at 
        //-- least four binary characters—that is, characters whose codes are 128 or greater 
        //-- %âãÏÓ these seem to be the standard on files I have tested.
        strFileHeader += "%aaIO" + PDFCRLF;
        //-- Now give it back

        return strFileHeader;
        //-- After this function is called you must call the pdfFileInfo function
    }

    private String pdfFileInfo() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment  = "";
        if (_pdfCommentFile){
            strComment = "% Comment- Call to pdfFileInfo " + PDFCRLF;
        }
        String strFileInfo  = strComment + intpdfObjectCount + " 0 obj" +  PDFCRLF;
        //-- Any entry whose value is not known should be omitted from the dictionary 
        //-- rather than included with an empty string as its value.
        if (_pdfTitle.length() > 0) {
            strFileInfo += "<< /Title (" + _pdfTitle + ")" +  PDFCRLF;
        }else{
            strFileInfo += "<< ";
        }
        if (_pdfAuthor.length() > 0 ) {
            strFileInfo += "/Author (" + _pdfAuthor + ")" +  PDFCRLF;
        }
        if (_pdfCreator.length() > 0 ) {
            strFileInfo += "/Creator (" + _pdfCreator + ")" +  PDFCRLF;
        }
        if (_pdfKeyWords.length() > 0 ) {
            strFileInfo += "/Keywords (" + _pdfKeyWords + ")" +  PDFCRLF;
        }
        if (_pdfSubject.length() > 0 ) {
            strFileInfo += "/Subject (" + _pdfSubject + ")" +  PDFCRLF;
        }
        if (_pdfProducer.length() > 0 ) {
            strFileInfo += "/Producer (" + _pdfProducer + ")" +  PDFCRLF;
        }
        //-- We should alway show the Creation Date of the file.
        //-- A date is an ASCII string of the form( D : YYYYMMDDHHmmSSOHH ' mm ' )
        //-- HH followed by ' is the absolute value of the offset from UT in hours (00–23)
        //-- Here in the Easter Time zone of the US we are 5 hours behind UT time.
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        
        strFileInfo += "/CreationDate (D:" + sdf.format(new Date()) + "-05'00')" +  PDFCRLF;
        //strFileInfo += "/ModDate (D:" & Format(Now, "yyyymmddhhmmss") & "-05'00')" +  PDFCRLF;
        strFileInfo += ">>" +  PDFCRLF;
        strFileInfo += "endobj" +  PDFCRLF;
        intDynamicObjectCount = 1;
        return strFileInfo;
        //-- After this function is called you must call the rootCatalog function
    }

    private String rootCatalog(){
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment   = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to rootCatalog " +  PDFCRLF;
        }
        //-- The catalog contains references to other objects defining the document’s contents,
        //-- outline, article threads (PDF 1.1), named destinations, and other attributes.
        //-- In addition, it contains information about how the document should be displayed on the screen,
        //-- such as whether its outline and thumbnail page images should be displayed automatically and 
        //-- whether some location other than the first page should be shown when the document is opened.

        String strRoot = strComment + intpdfObjectCount + " 0 obj" +  PDFCRLF;
        //-- *Required* The type of PDF object that this dictionary describes; must be Catalog for the catalog dictionary.
        strRoot += "<< /Type /Catalog" +  PDFCRLF;
        //-- Hard Coding does not work break ever time I change code...........
        Integer intAvailableObject  = intpdfObjectCount + 1;
        strRoot += "/Outlines " + intAvailableObject + " 0 R" +  PDFCRLF;
        intAvailableObject += 1;
        //-- *Required* The page tree node that is the root of the document’s page tree.
        strRoot += "/Pages " + intAvailableObject + " 0 R" +  PDFCRLF;
        //-- *Optional* A name object specifying the page layout to be used when the document is opened
        //-- SinglePage           Display one page at a time
        //-- OneColumn            Display the pages in one column
        //-- TwoColumnLeft        Display the pages in two columns, with odd-numbered pages on the left
        //-- TwoColumnRight       Display the pages in two columns, with odd-numbered pages on the right
        //-- TwoPageLeft(PDF 1.5) Display the pages two at a time, with odd-numbered pages on the left
        //-- TwoPageRight(PDF 1.5) Display the pages two at a time, with odd-numbered pages on the right
        //--Default value: SinglePage.
        // strRoot += "/PageLayout /SinglePage" +  PDFCRLF;
        //-- *Optional* A name object specifying how the document should be displayed when opened
        //-- UseNone      Neither document outline nor thumbnail images visible
        //-- UseOutlines  Document outline visible
        //-- UseThumbs    Thumbnail images visible
        //-- FullScreen   Full-screen mode, with no menu bar, window controls, or any other window visible
        //-- UseOC(PDF 1.5) Optional content group panel visible
        //-- UseAttachments(PDF 1.6) Attachments panel visible
        //-- Default value: UseNone
        // strRoot += "/PageMode /UseNone" +  PDFCRLF;
        //-- *Optional* (PDF 1.4) A language identifier specifying the natural language for all text in the document
        //-- except where overridden by language specifications for structure element
        //-- ISO 639-1: two-letter codes, one per language or macrolanguage
        //-- Example:
        //-- English  - en
        //-- español  - es
        //-- Japanese - ja
        //-- Korean   - ko
        // strRoot += "/Lang (en)" +  PDFCRLF;
        //-- *Optional*  A viewer preferences dictionary  specifying the way the document is to be displayed
        //-- on the screen. if (this entry is absent, applications should use their own current user preference settings.
        //-- *Optional* HideToolbar     - A flag specifying whether to hide the viewer application’s tool bars when the document is active. 
        //-- *Optional* DisplayDocTitle - A flag specifying whether the window’s title bar should display the document title taken from the Title entry
        //-- of the document Information Dictionary
        //-- *Optional* HideWindowUI    - A flag specifying whether to hide user interface elements in the document’s window (such as scroll bars and navigation controls), leaving only the document’s contents displayed
        //-- Not used but available are listed below
        //-- FitWindow    boolean/true false
        //-- CenterWindow boolean/true false
        //-- HideMenubar  boolean/true false
        // strRoot += "/ViewerPreferences << /HideToolbar false /DisplayDocTitle true /HideWindowUI false >>" +  PDFCRLF;
        strRoot += ">>" +  PDFCRLF;
        strRoot += "endobj" +  PDFCRLF;

        intDynamicObjectCount +=1;
        return strRoot;
        //-- After this function is called you must call the OutLines function
    }

    private String OutLines() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to OutLines " +  PDFCRLF;
        }
        //-- I have not implemented this yet but it needed so I left it in the code.
        String strOutLine  = strComment + intpdfObjectCount + " 0 obj" +  PDFCRLF;
        strOutLine += "<< /Type /Outlines" +  PDFCRLF;
        strOutLine += "/Count 0" +  PDFCRLF;
        strOutLine += ">>" +  PDFCRLF;
        strOutLine += "endobj" +  PDFCRLF;
        return strOutLine;
        //-- After this function is called you must call the PageTree function
    }

    private String PageTree() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment  = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to PageTree " +  PDFCRLF;
        }
        //-- The pages of a document are accessed through a structure known as the page tree, 
        //-- which defines the ordering of pages in the document
        //-- The tree contains nodes of two types—intermediate nodes, called page tree nodes, 
        //-- and leaf nodes, called page objects
        String strPageTree = strComment + intpdfObjectCount + " 0 obj" +  PDFCRLF;
        intPageTree = intpdfObjectCount;
        //-- The simplest structure would consist of a single page tree node that references all 
        //-- of the document’s page objects directly.
        //-- *Required* The type of PDF object that this dictionary describes; must be Pages for a page tree node.
        strPageTree += "<< /Type /Pages" +  PDFCRLF;
        //-- *Required* Kids -  An array of indirect references to the immediate children of this node. The children may
        //-- be page objects or other page tree nodes.
        Integer intKids  = intpdfObjectCount;
        //-- We must have at least one page in the file
        //-- The 2 comes from 1 for the PageTree object and 1 for the Resources object
        intKids += 2;
        strPageTree += "/Kids [ " +  PDFCRLF;
        strPageTree += intKids + " 0 R " +  PDFCRLF;
        //-- if (there is more than one page add there objects here.
        for(int intCounter = 2 ; intCounter<= _pdfPageCount; intCounter++){
            intKids += 3;
            strPageTree += intKids + " 0 R " +  PDFCRLF;
        }

        strPageTree += "]" +  PDFCRLF;
        //-- *Required* Count - The number of leaf nodes (page objects) that are descendants of this node within the page tree.
        strPageTree += "/Count " + _pdfPageCount.toString() +  PDFCRLF;
        Integer intResources   = intpdfObjectCount;
        intResources += 1;
        strPageTree += "/Resources " + intResources.toString() + " 0 R " +  PDFCRLF;
        strPageTree += ">>" +  PDFCRLF;
        strPageTree += "endobj" +  PDFCRLF;
        return strPageTree;
        //-- After this function is called you must call the Resources function
    }

    private String Resources() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to Resources " +  PDFCRLF;
        }
        String strResource  = strComment + intpdfObjectCount.toString() + " 0 obj" +  PDFCRLF;
        //-- Entries in a resource dictionary Not Added yet.
        //-- ExtGState - A dictionary that maps resource names to graphics state parameter dictionaries
        //-- ColorSpace- A dictionary that maps each resource name to either the name of a device-dependent color space or an array describing a color space
        //-- Pattern - A dictionary that maps resource names to pattern objects
        //-- Shading - A dictionary that maps resource names to shading dictionaries
        //-- Properties- A dictionary that maps resource names to property list dictionaries for marked content

        //-- We have to have /ProcSet /PDF / Text at a min..
        //-- For resource type ProcSet, the value is an array of procedure set names

        strResource += "<< /ProcSet [ /PDF /Text" + (colXobjectImages.size() > 0 ? " /ImageC" : "") + "]" +  PDFCRLF;
        //-- A dictionary that maps resource names to external objects
        if (colXobjectImages.size() > 0 ) {
            strResource += "/XObject << " +  PDFCRLF;
            //-- Write the Images 
            Set<String> keys = colXobjectImages.keySet();
            for (String k: keys){
                strResource += "/" + k + " ";
                strResource += colXobjectImages.get(k) +  PDFCRLF;
                //-- Need to keep up with our XObject so our file trailer will point to correct object.
                intXObjectCount += 1;
            }
            strResource += ">>" +  PDFCRLF;
        }

        //-- A dictionary that maps resource names to font dictionaries
        if (colFonts.size() > 0 ) {
            strResource += "/Font << " +  PDFCRLF;
            //-- Here we need to loop through the collection of fonts.
            Set<String> keys = colFonts.keySet();
            for (String key: keys){
                strResource += "/" + key.toString() + " " + colFonts.get(key).toString() + " 0 R " +  PDFCRLF;
            }
            strResource += ">>" +  PDFCRLF;
        }


        strResource += ">>" +  PDFCRLF;
        strResource += "endobj" +  PDFCRLF;

        return strResource;
        //-- After this function is called you must call the Page function
    }

    private String Page() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment  = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to Page " +  PDFCRLF;
        }
        String strPage  = strComment + intpdfObjectCount.toString() + " 0 obj" +  PDFCRLF;
        strPage += "<< /Type /Page" +  PDFCRLF;
        strPage += "/Parent " + intPageTree.toString() + " 0 R" +  PDFCRLF;
        //-- *Required* MediaBox- Defining the boundaries of the physical medium on which the page is intended to be displayed or printed
        strPage += "/MediaBox [ 0 0 " + intPageWidth.toString() + " " + intPageHeight.toString() + "]" +  PDFCRLF;
        //-- *Optional* CropBox - Its contents are to be clipped (cropped) to this rectangle
        strPage += "/CropBox [ 0 0 " + intPageWidth.toString() + " " + intPageHeight.toString() + "]" +  PDFCRLF;

        Integer intNextObjects  = intpdfObjectCount;
        intNextObjects += 1;
        strPage += "/Contents " + intNextObjects.toString() + " 0 R" +  PDFCRLF;
        strPage += ">>" +  PDFCRLF;
        strPage += "endobj" +  PDFCRLF;
        //-- Keeps up with our human page count.
        intPageCount += 1;
        return strPage;
        //-- After this function is called you must call the ContentStream function
    }

    private String ContentStream() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment   = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to ContentStream " +  PDFCRLF;
        }
        String strContent  = strComment + intpdfObjectCount.toString() + " 0 obj" +  PDFCRLF;
        //-- *Required* Length - The number of bytes from the beginning of the line following
        //-- the keyword stream to the last byte just before the keyword endstream.
        //-- if (the length is not know before hand write the stream and set the length in another
        //-- object and point to that object in the Length section.Example << /Length 15 0 R >>
        Integer intNextObjNumber = intpdfObjectCount + 1;

        strContent += "<< /Length " + intNextObjNumber.toString() + " 0 R" +  PDFCRLF + ">>" +  PDFCRLF;
        strContent += "stream";
        //-- Figure the stream length as per note above
        Integer intStartOffSet  = strContent.length();
        strContent += PDFCRLF;
        //-- Draw any images on this page
        Set<String> keys = colDrawImages.keySet();
        for (String key: keys){
        	if(key.startsWith(intPageCount.toString())){
        		strContent += colDrawImages.get(key).toString();
        	}
        }
        
        //-- Write out the Text for this page
        keys = colPageText.keySet();
        for (String key: keys){
        	if(key.startsWith(intPageCount.toString())){
        		strContent += colPageText.get(key).toString();
        	}
        }

        //-- Write any lines for this page.
        keys = colLineCode.keySet();
        for (String key: keys){
        	if(key.startsWith(intPageCount.toString())){
        		strContent += colLineCode.get(key).toString();
        	}
        }
      
        //-- Write and Circles for this page.
        keys = colCircles.keySet();
        for (String key: keys){
        	if(key.startsWith(intPageCount.toString())){
        		strContent += colCircles.get(key).toString();
        	}
        }

        
          //-- Ok done writting the Content need to figure the length
        intStreamLength = strContent.length() - intStartOffSet;
        strContent += "endstream" +  PDFCRLF;
        strContent += "endobj" +  PDFCRLF;
        return strContent;
        //-- After this function is called you must call the StreamLengthObj function
    }

    private String StreamLengthObj() {
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment   = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to StreamLengthObj " +  PDFCRLF;
        }
        String strLength  = strComment + intpdfObjectCount.toString() + " 0 obj" +  PDFCRLF;
        strLength += intStreamLength.toString() +  PDFCRLF;
        strLength += "endobj" +  PDFCRLF;
        return strLength;
        //-- After this function it can loop again or call the buildCrossReffenceTable
    }

    private String	buildCrossReferenceTable() {
    	String strComment  = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to buildCrossReffenceTable " +  PDFCRLF;
        }
        //-- Each cross-reference section begins with a line containing the keyword
        //-- xref
        String  strCrossReffence = strComment + "xref" +  PDFCRLF;
        //-- For a file that has never been updated, the cross-reference section contains
        //-- only one subsection, whose object numbering begins at 0.
        strCrossReffence += "0 ";
        //-- Each cross-reference subsection contains entries for a contiguous range of object
        //-- numbers starting at 0
        Integer intObjNumberCount   = 1;
        //-- Our Collection count start at 1 so we need to add 1 to it for the zero base needed
        intObjNumberCount += colCrossReferenceTable.size();
        strCrossReffence += intObjNumberCount.toString() +  PDFCRLF;
        //-- The first entry in the table (object number 0) is always free
        //-- and has a generation number of 65,535;
        strCrossReffence += "0000000000 65535 f" +  PDFCRLF;
        //-- There are two kinds of cross-reference entries: one for objects that are in use and another
        //-- for objects that have been deleted and therefore are free.
        //-- Both types of entries have similar basic formats, distinguished by the keyword n (for an in-use entry)
        //-- or f(for a free entry).
        //-- The format of an in-use entry is
        //-- nnnnnnnnnn ggggg n eol
        //-- nnnnnnnnnn is a 10-digit byte offset
        //-- ggggg is a 5-digit generation number
        //-- n is a literal keyword identifying this as an in-use entry
        //-- eol is a 2-character end-of-line sequence

        Integer	intPad   = 0;
        //-- Need 10 elements for the offset use the built in format for interger method.
        String fmt  = "%010d";

        //-- Loop through our collection of PDF Objects
        Set<String> keys = colCrossReferenceTable.keySet();
        for (String key: keys){
        	intPad = Integer.parseInt(colCrossReferenceTable.get(key));
        	strCrossReffence += String.format(fmt,  intPad) + " 00000 n" +  PDFCRLF;
        }
        
        //-- Now return our Cross reffence table
        return strCrossReffence;
        //-- After this function is called you must call the FileTrailer Function to end the file.
    }

    private String FileTrailer(Integer byteOfSet  ) { 
    	String strComment = "";
        if (_pdfCommentFile == true ) {
            strComment = "% Comment- Call to FileTrailer " +  PDFCRLF;
        }
        //--The trailer of a PDF file enables an application reading the file to quickly find 
        //--the cross-reference table and certain special objects
        //--Applications should read a PDFfile from its end. 
        String strTrailer  = strComment + "trailer" +  PDFCRLF;
        strTrailer += "<< /Size " + Integer.toString(1 + colCrossReferenceTable.size()) +  PDFCRLF;
        //-- It not required to have a Info Dictionary object but I think it best to have it.
        //-- It ok to hard code this pdf object because it never changes.
        strTrailer += "/Info 1 0 R" +  PDFCRLF;
        //-- Need to keep up with all the objects to know where to point to root object
        Integer intRootObject  = intFontCount + intDynamicObjectCount + intXObjectCount + intFontDescriptorCount;
        strTrailer += "/Root " + intRootObject.toString() + " 0 R" +  PDFCRLF;
        strTrailer += ">>" +  PDFCRLF;
        strTrailer += "startxref" +  PDFCRLF;
        //-- Need the starting point of the cross reffence table to be pass in.
        strTrailer += byteOfSet.toString() +  PDFCRLF;
        //-- The two preceding lines contain the keyword startxref and the byte offset 
        //-- from the beginning of the file to the beginning of the xref keyword in the 
        //-- last cross-reference section
        //-- The last line of the file contains only the end-of-file marker, %%EOF
        strTrailer += "%%EOF";
        return strTrailer;

    }
   //... End Region	 
	 
	 
	 
    //Region "Helper Function"
    private String LoadStandardFont(String strFontName ) {
    	String strComment   = "";
        if( _pdfCommentFile == true){
            strComment = "% Comment- Call to LoadStandardFont " + PDFCRLF;
        }
        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strFont  = strComment + intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;

        //-- Keep our collection up to date
        colFonts.add("F" + dicFontsUsed.get(strFontName).toString(), intpdfObjectCount.toString());

        String strBaseFont  = "";
        //-- Here are the 14 Standard type of fonts supported by Adobe Reader.
        //-- Can not use the - in Enums so I had to do this work around maybe later find better solution.
        if(strFontName.equals("pdfStandardFonts.Times_Roman")){
                strBaseFont = "Times−Roman";
        }else if(strFontName.equals("pdfStandardFonts.Times_Bold")){
                strBaseFont = "Times−Bold";
        }else if(strFontName.equals("pdfStandardFonts.Times_Italic")){
                strBaseFont = "Times−Italic";
        }else if(strFontName.equals("pdfStandardFonts.Times_BoldItalic")){
                strBaseFont = "Times−BoldItalic";
        }else if(strFontName.equals("pdfStandardFonts.Helvetica")){
                strBaseFont = "Helvetica";
        }else if(strFontName.equals("pdfStandardFonts.Helvetica_Bold")){
                strBaseFont = "Helvetica−Bold";
        }else if(strFontName.equals("pdfStandardFonts.Helvetica_Oblique")){
                strBaseFont = "Helvetica−Oblique";
        }else if(strFontName.equals("pdfStandardFonts.Helvetica_BoldOblique")){
                strBaseFont = "Helvetica−BoldOblique";
        }else if(strFontName.equals("pdfStandardFonts.Courier")){
                strBaseFont = "Courier";
        }else if(strFontName.equals("pdfStandardFonts.Courier_Bold")){
                strBaseFont = "Courier−Bold";
        }else if(strFontName.equals("pdfStandardFonts.Courier_Oblique")){
                strBaseFont = "Courier−Oblique";
        }else if(strFontName.equals("pdfStandardFonts.Courier_BoldOblique")){
                strBaseFont = "Courier−BoldOblique";
        }else if(strFontName.equals("pdfStandardFonts.Symbol")){
                strBaseFont = "Symbol";
        }else if(strFontName.equals("pdfStandardFonts.ZapfDingbats")){
                strBaseFont = "ZapfDingbats";
        }
        
        strFont += "<< /Type /Font" + PDFCRLF;
        strFont += "/Subtype /Type1" + PDFCRLF;
        strFont += "/Name /F" + dicFontsUsed.get(strFontName).toString() + PDFCRLF;
        strFont += "/BaseFont /" + strBaseFont + PDFCRLF;
        strFont += "/Encoding /WinAnsiEncoding" + PDFCRLF;
        strFont += ">>" + PDFCRLF;
        strFont += "endobj" + PDFCRLF;
        return strFont;
    }
    
    private String LoadType0Font(String strFontName){
    	String strComment  = "";
    	PDFFont curPDFFont = PDFFontList.get(dicFontsUsed.get(strFontName)-1);

    	if( _pdfCommentFile == true){strComment = "% Comment- Call to Load Type 0 Font " + PDFCRLF; }

    	//-- Need to set our Collection for this object 
    	upDateRefernceTable();
    	String strFont  = strComment + intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;

    	//-- Keep our collection up to date
    	colFonts.add("F" + dicFontsUsed.get(strFontName).toString(), intpdfObjectCount.toString());
    	Type0FontDictionary type0FontDic = new Type0FontDictionary();
    	FontDescriptor fontDesc = new FontDescriptor();

    	type0FontDic.setBaseFont(curPDFFont.getFontBaseName());
    	type0FontDic.setEncoding("Identity-H");
    	fontDesc.setFontName(curPDFFont.getFontBaseName());
    	fontDesc.setFlags(curPDFFont.getFontDescriptorFlags());
    	fontDesc.setFontBBox(curPDFFont.getFontBBox());
    	fontDesc.setMissingWidth(curPDFFont.getMissingWidth());
    	fontDesc.setStemV(curPDFFont.getStemV());
    	fontDesc.setStemH(curPDFFont.getStemH()); // This is not working need to find out where it getting the data.
    	fontDesc.setItalicAngle(curPDFFont.getItalicAngle());
    	fontDesc.setCapHeight(curPDFFont.getCapHeight());
    	fontDesc.setXHeight(curPDFFont.getXHeight());
    	fontDesc.setAscent(curPDFFont.getAscent());
    	fontDesc.setDescent(curPDFFont.getDescent());
    	fontDesc.setLeading(curPDFFont.getLeading());
    	fontDesc.setMaxWidth(curPDFFont.getMaxWidth());
    	fontDesc.setAvgWidth(curPDFFont.getAvgWidth());
    	fontDesc.setFontWeight(curPDFFont.getFontWeight());

    	// Point to our supporting Font Dictionary objects
    	type0FontDic.setDescendantFonts(String.valueOf(intpdfObjectCount + 1) + " 0 R");
    	// Keep up with the Unicode Cmap location.
    	if (intToUnicodeObject == 0){ intToUnicodeObject =  intpdfObjectCount + 2;}
    	type0FontDic.setToUnicode(intToUnicodeObject.toString() + " 0 R");

    	strFont += type0FontDic.toString();
    	strFont += "endobj" + PDFCRLF;

    	upDateRefernceTable();
    	intDynamicObjectCount +=1;
    	strFont += intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;

    	CIDFontDictionary cidFontDic = new CIDFontDictionary();
    	cidFontDic.setSubType(CIDFontTypes.CIDFontType2);
    	cidFontDic.setBaseFont(type0FontDic.getBaseFont());

    	if(blnToUnicodeNeeded == true){
    		cidFontDic.setFontDescriptor(String.valueOf(intpdfObjectCount + 3) + " 0 R");}
    	else if (blnToUnicodeNeeded == false){
    		cidFontDic.setFontDescriptor(String.valueOf(intpdfObjectCount + 1) + " 0 R"); }

    	cidFontDic.setCIDSystemInfo(String.valueOf(intToUnicodeObject + 1) + " 0 R");
    	cidFontDic.setW(curPDFFont.getWEntry()); 
    	// cidFontDic.setCIDToGIDMap("/Identity");
    	strFont += cidFontDic.toString();
    	strFont += "endobj" + PDFCRLF;

    	// Check to see if we need to add cmap
    	if(blnToUnicodeNeeded == true){
    		upDateRefernceTable();
    		intDynamicObjectCount +=1;
    		strFont += intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;
    		strFont += curPDFFont.getToUnicodeCMAP();
    		strFont += "endobj" + PDFCRLF;

    		upDateRefernceTable();
    		intDynamicObjectCount +=1;
    		strFont += intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;
    		strFont += curPDFFont.getCIDSystemInfoDictionary();
    		strFont += "endobj" + PDFCRLF;	  
    		blnToUnicodeNeeded = false;
    	}

    	upDateRefernceTable();
    	intDynamicObjectCount +=1;
    	strFont += intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;
    	strFont += fontDesc.toString();
    	strFont += "endobj" + PDFCRLF;


    	return strFont;
    }
    
    //Region "Images Function"
    
    class BMPPara{
    	byte[] ImgBuf=new byte[1];
        byte[] ImgColor=new byte[1];
        Byte ImgBPP;
        //-- Long
        Integer imgWidth ;
        Integer ImgHeight ;
        pdfColorSpace ColorSpace;        
    }

    private String LoadImgFromBMPFile(String Name , String FileName , pdfColorSpace ColorSpace){  
        //-- Byte
    	BMPPara para = new BMPPara();
    	para.ColorSpace = ColorSpace;
    	
    	
        Boolean blnFlag;

        blnFlag = LawBMP(FileName, para);
        //-- ok if we have a bitmap the load image from array
        if (blnFlag == true) {
        	String strComment  = "";
            if (_pdfCommentFile == true) {
                strComment = "% Comment- Call to LoadImgFromBMPFile " + PDFCRLF;
        	}
            return strComment + LoadImgFromArray(Name, para);
        }

        //-- Set the Flag
        return "";

    }

    private String LoadImgFromJPEGFile(String Name , String FileName, File file) throws IOException {

        //-- Ok the first thing we need to do is parse the jpeg file.
        //-- Just check the file name to see if it has extensions
    	
        if( FileName.contains(".") == false) {
        	//...msg
        	JOptionPane.showMessageDialog(null, "Image File - clsPdfWriter", "File " + FileName + " does not have an extension Invalid filename specified.", 0);
            return "";
        }

        //-- Just check to see if it's a jpeg format file
        if( FileName.toLowerCase().endsWith("jpg") || FileName.toLowerCase().endsWith("jpeg")) {
        	Boolean blnPDFParse ; 
            //-- This will assing the data from the file to a structure define by me.
            blnPDFParse = LawJPG(FileName);
            //-- Check to see if the Parsing failed
            if (blnPDFParse == false) {
                return "";
        	}
        }else{
        	//...msg
            //MsgBox("Image format not supported." & vbNewLine & "Only jpg or jpeg images are supported." & vbNewLine & "Impossible to include image in PDF file.", MsgBoxStyle.Critical, "Image File - PDFSimple")
        	
        	JOptionPane.showMessageDialog(null, "Image File - PDFSimple", 
        			"Image format not supported. Only jpg or jpeg images are supported. Impossible to include image in PDF file.", 0);
            return "";
        }

        //-- Need to set our Collection for this object 
        upDateRefernceTable();
        String strComment  = "";
        if (_pdfCommentFile == true) {
            strComment = "% Comment- Call to LoadImgFromJPEGFile " + PDFCRLF;
        }
        //-- The image dictionary specifies the width, height, and number of bits per component
        //-- explicitly. The number of color components can be inferred from the color space specified in the dictionary

        //-- Jpeg 
        //<</Type /XObject
        ///Subtype /Image
        ///Filter [/DCTDecode ]
        ///Width 75
        ///Height 70
        ///ColorSpace /DeviceRGB
        ///BitsPerComponent 8
        ///Length 2307
        ///Name /ImgJPEG1>>

        //-- Set our Object number
        String strImage  = strComment + intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;

        //-- This writes out the Xobject Dictionary

        strImage += "<</Type /XObject" + PDFCRLF;
        strImage += "/Subtype /Image" + PDFCRLF;
        strImage += "/Filter [/" + strImageJPEG.ImgDicFilter.toString() + " ]" + PDFCRLF;
        strImage += "/Width " + strImageJPEG.ImgDicWidth.toString() + PDFCRLF;
        strImage += "/Height " + strImageJPEG.ImgDicHeight.toString() + PDFCRLF;
        strImage += "/ColorSpace /" + strImageJPEG.ImgDicColorSpace.toString() + PDFCRLF;
        strImage += "/BitsPerComponent " + strImageJPEG.ImgDicBitsPerComponent.toString() + PDFCRLF;
        strImage += "/Length " + strImageJPEG.ImgDicFileSize.toString() + PDFCRLF;
        strImage += "/Name /" + Name.toString() + ">>" + PDFCRLF;
        //-- Sample data is represented as a stream of bytes, interpreted as 8-bit unsigned integers in the range 0 to 255.
        strImage += "stream" + PDFCRLF;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writeString(writer, strImage);
        writer.close();
        
        
        
        DataOutputStream dis;
		dis = new DataOutputStream(new FileOutputStream(file, true));
		byte[] bytes = (byte[]) strImageJPEG.ImgDicDataStream;
		dis.write(bytes);
		dis.close();
		intCrossRefOffSet  += bytes.length;
	        
         
        
        strImage=PDFCRLF;
        strImage += "endstream" + PDFCRLF;
        strImage += "endobj" + PDFCRLF;

        BufferedWriter writer2 = new BufferedWriter(new FileWriter(file, true));
        writeString(writer2, strImage);
        writer2.close();
        
        //-- Add it to the Resource Dic        
        colXobjectImages.add(Name, intpdfObjectCount.toString() + " 0 R ");
        return strImage;
    }

    private  Boolean LawBMP(String FileName , BMPPara p
    		/*Byte[] ImgBuf , Byte[] ImgColor, ByRef imgWidth As Integer, Integer ImgHeight , ByRef ImgBPP As Byte,
    		pdfColorSpace ColorSpace*/){ 


        // BITMAPFILEHEADER_Type
    	String bfType  = "OO"; // The string “BM” (hex value &H424D).
    	Integer bfSize ; // The size of the file, measured in [Bytes].
    	Integer bfDummy ;// Not used, set to zero.
    	Integer bfOffBits ;// The start offset of the bitmap data in the file.

        // BITMAPINFOHEADER
        Integer biSize; // 40 (the size of this structure).
        Integer biWidth; // The width of the bitmap in pixels.
        Integer biHeight; // The height of the bitmap in pixels.
        Integer biPlanes; // 1 (DIBs always have one plane).
        Integer biBitCount;// 1 for monochrome, 4 for 16 colors, 8 for 256 color, 24 for 24-bit RGB color.
        Integer biCompression; // Specifies the type of compression for compressed
        Integer biSizeImage; // The size of the image in bytes.
        Integer biXPelsPerMeter; // Number of horizontal pixels per meter for
        Integer biYPelsPerMeter; // Number of vertical pixels per meter for
        Integer biClrUsed; // Number of entries in the DIB color table
        Integer biClrImportant; // Number of entries in the DIB color table that


        Integer fb ;
        Integer XBMP ;
        Byte BPP ;
        Integer intCounterOuter = 0;
        Boolean blnFlag;
        Byte[] TempImg = null; 
        Byte[] TempCol = null; // RGBQUAD_Type
        Integer lngGray; 

        //-- Gets the next available free file
        /*InputStream in = new FileInputStream(FileName);
        
        

        // BITMAPFILEHEADER
        in.read(bfType);
        in.read(bfSize);
        in.read(bfDummy);
        in.read(bfDummy);
        in.read(bfOffBits);
        
        //-- Test to see if the open file is type Bit Map
        if(bfType = "BM" ) {

            // BITMAPINFOHEADER
            FileGet(fb, biSize)
            FileGet(fb, biWidth)
            FileGet(fb, biHeight)
            FileGet(fb, biPlanes)
            FileGet(fb, biBitCount)
            FileGet(fb, biCompression)
            FileGet(fb, biSizeImage)
            FileGet(fb, biXPelsPerMeter)
            FileGet(fb, biYPelsPerMeter)
            FileGet(fb, biClrUsed)
            FileGet(fb, biClrImportant)


            BPP = biBitCount
            //-- Check to see if the bit count is 8 or less
            if(BPP <= 8 Then

                //-- law the palette of colors

                ReDim TempCol((2 ^ BPP) * 4)
                FileGet(fb, TempCol)

                If ColorSpace = pdfColorSpace.pdfRGB Then
                    ReDim ImgColor(3 * (2 ^ BPP))
                    For intCounter = 0 To (2 ^ BPP) - 1
                        //-- Found Bug color were mixed up between red and blue so reversed the colors
                        ImgColor(intCounter * 3 + 1) = TempCol(intCounter * 4 + 3) // Blue
                        ImgColor(intCounter * 3 + 2) = TempCol(intCounter * 4 + 2) // green
                        ImgColor(intCounter * 3 + 3) = TempCol(intCounter * 4 + 1) // Red


                    Next
                Else
                    ReDim ImgColor((2 ^ BPP))
                    For intCounter = 0 To (2 ^ BPP) - 1
                        lngGray = (0.33 * TempCol(intCounter * 4 + 1) + 0.59 * TempCol(intCounter * 4 + 2) + 0.11 * TempCol(intCounter * 4 + 3))
                        ImgColor(intCounter + 1) = IIf(lngGray > 255, 255, lngGray)
                    Next
                End If
            End If

            XBMP = ((biWidth * BPP / 8) + 3) And &HFFFFFFFC // [Bytes].

            imgWidth = biWidth
            ImgHeight = biHeight
            ImgBPP = CByte(biBitCount)
            //-- Figure our Temp Image Size the arrays are 0 base so minus one
            Dim lngTempImageSize As Long = (biHeight * XBMP) - 1

            ReDim TempImg(lngTempImageSize)
            FileGet(fb, TempImg, bfOffBits + 1)
            FileClose(fb)
            //-- Ok give us a buffer the same size as the image
            p.ImgBuf = new Byte[lngTempImageSize];


            If BPP > 8 Then
                blnFlag = ((biWidth Mod 4) <> 0)

                If ColorSpace = pdfColorSpace.pdfRGB Then
                    For intCounter = 0 To UBound(TempImg) - 1 Step 3
                        p.ImgBuf(3 * intCounterOuter + 0) = TempImg(intCounter + 2)
                        p.ImgBuf(3 * intCounterOuter + 1) = TempImg(intCounter + 1)
                        p.ImgBuf(3 * intCounterOuter + 2) = TempImg(intCounter)
                        If (((intCounterOuter + 1) Mod biWidth) = 0) And blnFlag Then
                            intCounter += (biWidth Mod 4)
                        End If

                        intCounterOuter += 1
                    Next

                Else
                    //-- Not check this area could be a bug.
                    For intCounter = 0 To UBound(TempImg) - 1 Step 3
                        lngGray = 0.33 * TempImg(intCounter + 2) + 0.59 * TempImg(intCounter + 1) + 0.11 * TempImg(intCounter)
                        p.ImgBuf(intCounterOuter + 1) = IIf(lngGray > 255, 255, lngGray)
                        If (((intCounterOuter + 1) Mod biWidth) = 0) And blnFlag Then intCounter += (biWidth Mod 4)
                        intCounterOuter += 1
                    Next

                    ReDim Preserve p.ImgBuf(intCounterOuter)

                End If

            ElseIf BPP <= 8 Then
                blnFlag = (biWidth Mod IIf(BPP = 8, 4, 8)) <> 0
                For intCounter = 0 To UBound(TempImg)
                    p.ImgBuf(intCounterOuter + 1) = TempImg(intCounter)
                    If ((intCounterOuter + 1) Mod Int((biWidth + (8 / BPP) - 1) / (8 / BPP))) = 0 And blnFlag Then
                        intCounter += (XBMP - (intCounter Mod XBMP))
                    End If
                    intCounterOuter += 1
                Next

                ReDim Preserve p.ImgBuf(intCounterOuter)

            End If
            //-- Yes we have a bitmap file.
            return True
        Else
            //-- Sorry not a bitmap file
            return False
        End If

        //-- If the file is open then close it.
        If fb > 0 Then FileClose(fb)
        */
        
        //...test
        return false;

    }

    private Boolean LawJPG(String pFileName){
    	

    	String str_TChar;
    	Long	sIMG;
    	Integer	inIMG;

    	Long	in_PEnd;
    	Long	in_idx;
    	String	str_SegmMk;
    	Integer	in_SegmSz;
    	Byte	bChar;
    	Integer	in_TmpColor;
    	Long	in_bpc;
    	
    	

        //-- Force it to true just to be safe
    	Boolean LawJPG = true;
        //-- Extract info from a JPEG file
    	
    	File file = new File(pFileName);
        byte[] ArrBFile = new byte[(int) file.length()];
        
        DataInputStream dis;
		try {
			dis = new DataInputStream(new FileInputStream(file));
	        dis.readFully(ArrBFile);
	        dis.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        sIMG = file.length();
        if (sIMG < 250 ){
            //-- Testing to see if the file is large enought to be a jpeg            
        	JOptionPane.showMessageDialog(null, "File Image - clsPdfWriter", "File Image is non JPEG Cannot add image to PDF file.", 0);
            //-- Setting a Flag to show parsing failure
            return false;
        }
        //-- Ok passed the first size test of the jpeg file
        strImageJPEG.ImgDicFileSize = sIMG;

        

        in_PEnd = sIMG - 2;	//	UBound(ArrBFile) - 1;

        //-- Look for the ASCII Character(ÿØ) Dec (255,216)in the first byte in the File
        //-- or the ASCII Character (ÿÙ) Dec (255,217)in the Last byte of the file
        if (!PDFIntAsHex(ArrBFile, 0).equals("FFD8") || !PDFIntAsHex(ArrBFile, in_PEnd).equals("FFD9")){
            //-- Setting a Flag to show parsing failure
        	JOptionPane.showMessageDialog(null, "File Image - clsPdfWriter", "Invalid JPEG marker Cannot add iamge to PDF file.", 0);
            return false;
        }
        //-- Ok Passed the first two test on the jpeg file so let parse it

        in_idx = (long)2;
        //-- Start at the begin of the file and loop until the end of the file
        while( in_idx < in_PEnd-1){

            //-- Get the Header Info
            str_SegmMk = PDFIntAsHex(ArrBFile, in_idx);

            in_SegmSz = PDFIntVal(ArrBFile, in_idx + 2);

            //-- Look for ÿÿ if found we have thumbnails
            if (str_SegmMk.equals("FFFF")){
                while (Integer.toHexString(ArrBFile[(int)(in_idx + 1)]).equals("FF")){
                    //-- Count as long as we find the ÿ how many thumbnails
                    in_idx = in_idx + 1;
                }
                in_SegmSz = PDFIntVal(ArrBFile, in_idx + 2);
            }
            //-- A JFIF-standard file will start with the four bytes (hex) FF D8 FF E0,
            //-- followed by two variable bytes (often hex 00 10), followed by 'JFIF'.

            if(str_SegmMk.equals("FFE0")){
                    //-- Get the resolution from the file
                    bChar = ArrBFile[(int)(in_idx + 11)];

                    if (bChar == 0) {
                        strImageJPEG.ImgDicDots = "Dots";
                    }else if( bChar == 1 ) {

                        strImageJPEG.ImgDicDots = "Dots/inch (DPI)";
            		}else if( bChar == 2 ) {

                        strImageJPEG.ImgDicDots = "Dots/cm";
                        //-- Had a problem parsing the file tell the customer and exit
        			}else {
        				JOptionPane.showMessageDialog(null, "File Image - clsPdfWriter", "Invalid image resolution"+ bChar +"Valid resolution is: 0, 1, 2. Cannot add image to PDF file.", 0);
                        return false;
        			}
            }else if(str_SegmMk.equals("FFC0") || str_SegmMk.equals("FFC1") || str_SegmMk.equals("FFC2") || str_SegmMk.equals("FFC3") || 
            			str_SegmMk.equals("FFC5") || str_SegmMk.equals("FFC6") || str_SegmMk.equals("FFC7") ){
                    //-- Get the Width

                    strImageJPEG.ImgDicWidth =(long)PDFIntVal(ArrBFile, in_idx + 7);
                    //-- Get the Height

                    strImageJPEG.ImgDicHeight = (long)PDFIntVal(ArrBFile, in_idx + 5);
                    //-- Get the ColorSpace
                    in_TmpColor = ArrBFile[(int)(in_idx + 9)] * 8;

                    //-- Base on the Color Space set the Device
                    if (in_TmpColor == 8) {

                        strImageJPEG.ImgDicColorSpace = "DeviceGray";
                    }else if( in_TmpColor == 24) {

                        strImageJPEG.ImgDicColorSpace = "DeviceRGB";
                    }else if( in_TmpColor == 32) {

                        strImageJPEG.ImgDicColorSpace = "DeviceCMYK";
            		}else {

                        strImageJPEG.ImgDicBitsPerComponent = (long)in_TmpColor;
            		}
            }
            

            in_idx = in_idx + in_SegmSz + 2;
        }

        //-- Get the BitsPerComponent from file or set them to DeviceGray if file does not have it.
        //-- Change the defaut type to long so had to check for zero instead of empty string here.
        //-- seems to work.
        if (strImageJPEG.ImgDicBitsPerComponent!=null){
            in_bpc = strImageJPEG.ImgDicBitsPerComponent;
        }else{
            //-- Just set it to DeviceGray
            in_bpc = (long)8;
            strImageJPEG.ImgDicBitsPerComponent = (long)8;
        }

        //-- Hard Coded

        strImageJPEG.ImgDicFilter = "DCTDecode";

        //-- Make sure the Data Stream is empty before writting to it

        strImageJPEG.ImgDicDataStream = "";
        
        strImageJPEG.ImgDicDataStream = ArrBFile;

        /*
        //-- Open the jpeg file and read the data into the variable str_TChar

        FileOpen(inIMG, pFileName, OpenMode.Binary)
        //-- returns a string of spaces that match the file size dont think I need this in VB.Net but for now leave it.
        //-- This was because VB6 did not handle strings the same way c++ did.
        str_TChar = New String(" ", sIMG)
        //-- The number of bytes read equals the number of characters already in the string.
        FileGet(inIMG, str_TChar)
        //-- This just assignes it to the 6 element of our array
        strImageJPEG.ImgDicDataStream = str_TChar
        //-- Close the File
        FileClose(inIMG)
        */
        
          
    	return LawJPG; //...test
	}
  

    private  String LoadImgFromArray(String Name , BMPPara p
    		/*ByRef ImgBuf() As Byte, ByRef ImgColor() As Byte, ByRef imgWidth As Integer, ByRef ImgHeight As Integer, ByRef ImgBPP As Byte, Optional ByRef ColorSpace As pdfColorSpace = pdfColorSpace.pdfRGB*/) {
    	
    	
    	/*
        Dim BPP As Byte = 8 / ImgBPP
        //-- Need to set our Collection for this object 
        upDateReffenceTable()
        //-- The image dictionary specifies the width, height, and number of bits per component
        //-- explicitly. The number of color components can be inferred from the color space specified in the dictionary
        //-- Sample output to file
        //-- <<
        //-- /Type /XObject         ( Hard Coded)
        //-- /Subtype /Image        ( Hard Coded)
        //-- /Name /Img1            ( Pass In)
        //-- /Width 205             ( Pass In)
        //-  /Height 46             ( Pass In)
        //-- /BitsPerComponent 8    (Computed here)
        //-- /ColorSpace /DeviceRGB (Convert from Enum to String)
        //-- /Length 10 0 R         ( Points to our next availabe object)

        //-- Set our Object number
        Dim strImage As String = intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;

        //-- This writes out the Xobject Dictionary
        strImage += "<<" & vbCrLf
        strImage += "/Type /XObject" + PDFCRLF;
        strImage += "/Subtype /Image" + PDFCRLF;
        strImage += "/Name /" + Name + PDFCRLF;
        strImage += "/Width " + imgWidth.toString() + PDFCRLF;
        strImage += "/Height " + ImgHeight.toString() + PDFCRLF;

        //-- strDevice is use in the select case below
        Dim strDevice As String = IIf(ColorSpace = pdfColorSpace.pdfRGB, "DeviceRGB", "DeviceGray") //--IIf(expr, truepart, falsepart)

        Select Case ImgBPP
            Case 24
                strImage += "/BitsPerComponent 8" + PDFCRLF;
                strImage += "/ColorSpace /" + strDevice + PDFCRLF;
                strImage += "/Length " + intpdfObjectCount.toString() + " 0 R" + PDFCRLF;

            Case 8, 4, 1
                strImage += "/BitsPerComponent " + CStr(ImgBPP) + PDFCRLF;
                strImage += "/ColorSpace [/Indexed /" + strDevice + " " + CStr((2 ^ ImgBPP) - 1) + " "
                strImage += intpdfObjectCount.toString() + " 0 R]" + PDFCRLF;
                Dim intNextObject As Integer = intpdfObjectCount
                intNextObject += 1
                strImage += "/Length " + intNextObject.toString() + " 0 R"

        End Select
        //-- Found a replacement that works for strConv
        Dim strPixel As String = System.Text.Encoding.GetEncoding(1252).GetString(ImgBuf)

        //-- Sample data is represented as a stream of bytes, interpreted as 8-bit unsigned integers in the range 0 to 255.

        strImage += ">>" + PDFCRLF;
        strImage += "stream" + PDFCRLF;
        strImage += strPixel + PDFCRLF;
        strImage += "endstream" + PDFCRLF;
        strImage += "endobj" + PDFCRLF;

        //-- Add it to the Resource Object must be here because it the end of the obj.
        colXobjectImages.Add(Name, intpdfObjectCount.toString() + " 0 R ")


        //-- Need to set our Collection for this object 
        upDateReffenceTable()
        strImage += intpdfObjectCount.toString() + " 0 obj" + PDFCRLF;
        strImage += strPixel.Length.toString() + PDFCRLF;
        //-- When using bmp file we creat a second object for the length so we need to count it for the file trailer.
        intXObjectCount += 1
        //-- This ends Xobject Dictionary
        strImage += "endobj" + PDFCRLF;

        If ImgBPP <= 8 Then
            Dim strColor As String

            //-- Need to set our Collection for this object 
            upDateReffenceTable()
            //-- Get the color based on the code
            //-- vbUnicode Constant cant be used so I replace it with the value 64
            strColor = StrConv(System.Text.UnicodeEncoding.Unicode.GetString(ImgColor), 64)

            //-- Figure the length base on the color bits

            strImage += "<<" + PDFCRLF;
            strImage += "/Length " + strColor.Length.toString() + " >>" + PDFCRLF;
            strImage += "stream" + PDFCRLF;
            strImage += strColor + PDFCRLF;
            strImage += "endstream" + PDFCRLF;
            strImage += "endobj" + PDFCRLF;

        End If
    	
    	return strImage;
    	*/
    	
    	return "";//...test
    	
    	
    }

    private String PDFIntAsHex(byte[] ArrBF, long in_Index){
        // PDFIntAsHex = Right("00" & Hex(ArrBF[in_Index]), 2) & Right("00" & Hex(ArrBF[in_Index + 1]), 2)
    	String res = String.format("%02X%02X",  ArrBF[(int) in_Index], ArrBF[(int) (in_Index + 1)]);
    	
    	return   res;
    }

    private Integer PDFIntVal(byte[] ArrBF, long in_idx) {
        Integer res = (0xFF & ArrBF[(int) in_idx]) * 256 + (0xFF & ArrBF[(int) (in_idx + 1)]);
    	return res;
        
        //	PDFIntVal = CInt(ArrBF(in_idx)) * 256 + CInt(ArrBF(in_idx + 1))
    }

    private String getFontPath(Font font){
    	String strFontName = font.getName().toLowerCase();
    	String strFilePath = "";
    	Properties fontProperties = FontManager.getInstance().getFontProperties(false);
    	strFilePath = (String) fontProperties.get(strFontName);
    	if(strFilePath==null || strFilePath.isEmpty()){
    		// No Physical Font found default to  "Times New Roman" 	
    		strFilePath = (String) fontProperties.get("times new roman" );
    	}
    	return strFilePath;
	}
//End Region    
	 
    
    public void writeString(BufferedWriter writer, String str) throws IOException{
    	writer.write(str);
    	intCrossRefOffSet += str.length();
    }
	 
    public void WritePDF(String strFilePath){
	    //-- Here we will write the PDF File to a string Builder string first then write the file
	    //-- once we know we have the file built.
      File file = new File(strFilePath);
      BufferedWriter writer = null;
      try {
		writer = new BufferedWriter(new FileWriter(file));
		writeString(writer, FileHeader());
		
		writeString(writer, pdfFileInfo());
		
	
	    Enumeration<String> keyFonts = dicFontsUsed.keys();
	  //-- Load only the font in use by the application to keep file size small and easy to read.
	    while(keyFonts.hasMoreElements()){
	    	String key = keyFonts.nextElement();
	    	if (key.startsWith("pdfStandardFonts") ) {//-- Check to see if we need to load any standard fonts
	            writeString(writer, LoadStandardFont(key));
	        }
	        // Else just load everything else as Type 0 Font.	        
	        writeString(writer, LoadType0Font(key));
	        
	    }
	    
	    writer.close();
	
	    //-- Load up any Images into our XObject 
	    Set<String> keys = colImages.keySet();
	    
	    for (String key: keys){
	    	if (colImages.get(key).toString().toUpperCase().endsWith("BMP") == true ) {
	            //-- Load up any Bit Map Images in the Collection
	            //...writeString(writer, LoadImgFromBMPFile(key, colImages.get(key).toString(), pdfColorSpace.pdfRGB));
	        }
	
	        if (colImages.get(key).toString().toUpperCase().endsWith("JPG") == true || colImages.get(key).toString().toUpperCase().endsWith("JPEG") == true ) {
	            //-- Load up any Jpeg Images in the Collection
	        	//...
	            //FileText.append(LoadImgFromJPEGFile(key, colImages.get(key).toString()));
	        	LoadImgFromJPEGFile(key, colImages.get(key).toString(), file);
	        }
        }
	    
	    BufferedWriter	writer2 = new BufferedWriter(new FileWriter(file, true));
	
	    writeString(writer2, rootCatalog());
	    writeString(writer2, OutLines());
	    writeString(writer2, PageTree());
	    writeString(writer2, Resources());
	
	    //-- Need to call page for every page of the count
	    for(int intCounter = 1; intCounter <= _pdfPageCount; intCounter++){
	    	writeString(writer2, Page());
	    	writeString(writer2, ContentStream());
	    	writeString(writer2, StreamLengthObj());
	    }
	
	
	    //-- Need to know where the reference table starts
	    //-- The next entry is the cross reference table so add one to the length of the string
	    //-- to point to the cross reference table start point.
	    intCrossRefOffSet +=   1;
	    //-- Now build or cross reference table
	    writeString(writer2, buildCrossReferenceTable());
	    writeString(writer2, FileTrailer(intCrossRefOffSet));
	    
	    writer2.close();
	    }catch (Exception e) {e.printStackTrace();}
	  
	
	}		 
	    
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	
    
	
}
