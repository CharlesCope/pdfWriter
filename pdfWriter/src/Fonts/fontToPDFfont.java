package Fonts;

import java.util.ArrayList;
import Fonts.table.CmapFormat0;
import Fonts.table.CmapFormat2;
import Fonts.table.CmapFormat4;
import Fonts.table.CmapFormat6;
import Fonts.table.NameTable;


/** Wanted a place to run code not tied to the GUI  */
public class fontToPDFfont {
	private static String OS = null;
	private static int intUnitsPerEM; 
	private static ChcFont myChcFont;
	private final static String  PDFCRLF = "\r\n";
	enum  State
    {
        FIRST, BRACKET, SERIAL
    }
	public static ChcFont getMyChcFont() {
		return myChcFont;
	}

	public static void setMyChcFont(ChcFont myChcFont) {
		fontToPDFfont.myChcFont = myChcFont;
	}

	public static PDFFont ConvertFontFileToPDFFont(String strFile){
		if(strFile.isEmpty()){return null;}
		// First create the True Type Font Object
		myChcFont = new ChcFont().create(strFile);
		// Then create the PDFFont Object to get data from True Type Font Object
		PDFFont myPDFFont = new PDFFont();
		myPDFFont.setUnitsPerEm(myChcFont.getHeadTable().getUnitsPerEm());
		intUnitsPerEM = myPDFFont.getUnitsPerEm();
		myPDFFont.setBoundingBoxLowerLeftx(myChcFont.getHeadTable().getXMin());
		myPDFFont.setBoundingBoxLowerLefty(myChcFont.getHeadTable().getYMin());
		myPDFFont.setBoundingBoxUpperRightx(myChcFont.getHeadTable().getXMax());
		myPDFFont.setBoundingBoxUpperRighty(myChcFont.getHeadTable().getYMax());
		
		String strBaseFontName = myChcFont.getNameTable().getRecord(NameTable.namePostscriptName);
		String strFontFamilyName = myChcFont.getNameTable().getRecord(NameTable.nameFontFamilyName);
		
		if(strBaseFontName.isEmpty() == false){myPDFFont.setFontBaseName(strBaseFontName);}
		if(strFontFamilyName.isEmpty() == false){myPDFFont.setFontFamilyName(strFontFamilyName);}

		myPDFFont.setFixedPitchFlag(myChcFont.getPostTable().getIsFixedPitch());
		
		/** Only Roman Encoding or Windows uni-code are allowed for a non symbolic font 
		 *  For symbolic font, no encoding entry is allowed and only one encoding entry is expected into the FontFile CMap
		 *  Any font whose character set is not a subset of the Adobe standard character set is considered to be symbolic.
		 *  If the Symbolic flag should be set then the Nonsymbolic flag must be cleared .
		 * */
		if(isWindows() == true){
			if(myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL)!= null){
				if (myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL).getFormat() == 4) {
					myPDFFont.setCmapFormat((CmapFormat4) myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL));
				} else if(myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL).getFormat() == 2){
					myPDFFont.setCmapFormat((CmapFormat2) myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL));
				}else if(myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL).getFormat() == 0){
					myPDFFont.setCmapFormat((CmapFormat0) myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL));
				}else if(myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL).getFormat() == 6){
					myPDFFont.setCmapFormat((CmapFormat6) myChcFont.getCmapTable().getCmapFormat(NameTable.platformMicrosoft, NameTable.encodingUGL));
				}
				
				myPDFFont.setNonsymbolicFlag(true);
				myPDFFont.setSymbolicFlag(false);
				
			}
			else {
				myPDFFont.setNonsymbolicFlag(false);
				myPDFFont.setSymbolicFlag(true);
			}
		}
		if(isMac() == true){
			if(myChcFont.getCmapTable().getCmapFormat(NameTable.platformMacintosh, NameTable.encodingRoman)!= null){
				myPDFFont.setNonsymbolicFlag(true);
				myPDFFont.setSymbolicFlag(false);
			}
			else {
				myPDFFont.setNonsymbolicFlag(false);
				myPDFFont.setSymbolicFlag(true);}
		}
		
		// Apple/Mac platform calls this the Style table and Microsoft Calls it SubFamily.
		String strStyle = myChcFont.getNameTable().getRecord(NameTable.nameFontSubfamilyName);
		if (strStyle.toUpperCase().contains("ITALI")== true){myPDFFont.setItalicFlag(true);}
		else{myPDFFont.setItalicFlag(false);}
		
		myPDFFont.setScriptFlag(myChcFont.getOS2Table().getIsScript());
		myPDFFont.setSerifFlag(myChcFont.getOS2Table().getIsSerif());		
		
		// TODO Need to find the data in file and set flags.
		//myPDFFont.setAllCapFlag(setFlag);
		//myPDFFont.setSmallCapFlag(setFlag);
		//myPDFFont.setForceBoldFlag(setFlag);

		
		Glyph MissingWidth = myChcFont.getGlyph(0);
		if (MissingWidth != null){myPDFFont.setMissingWidth(pdfScalingFormula(MissingWidth.advanceWidth,intUnitsPerEM));}
		else{myPDFFont.setMissingWidth(0);}
		
		int intVersion = myChcFont.getOS2Table().getVersion();
		
		if(intVersion >= 2){
			myPDFFont.setCapHeight(pdfScalingFormula(myChcFont.getOS2Table().getCapHeight(),intUnitsPerEM));
			myPDFFont.setXHeight(pdfScalingFormula(myChcFont.getOS2Table().getXHeight(),intUnitsPerEM));
		}
		/** NOTE: These are just rule-of-thumb values,in case the xHeight and CapHeight fields aren't available.*/
		else{
			System.out.println("Got Here");
			myPDFFont.setCapHeight((int) (.7 * intUnitsPerEM));
			myPDFFont.setXHeight((int) (.5 * intUnitsPerEM));
		}
		
		myPDFFont.setItalicAngle(myChcFont.getPostTable().getItalicAngle());
		myPDFFont.setAscent(pdfScalingFormula(myChcFont.getHheaTable().getAscender(),intUnitsPerEM));
		myPDFFont.setDescent(pdfScalingFormula(myChcFont.getHheaTable().getDescender(),intUnitsPerEM));
		myPDFFont.setLeading(pdfScalingFormula(myChcFont.getHheaTable().getLineGap(),intUnitsPerEM));
		myPDFFont.setStemV(myChcFont.getOS2Table().getWeightClass());
		myPDFFont.setMaxWidth(pdfScalingFormula(myChcFont.getHheaTable().getAdvanceWidthMax(),intUnitsPerEM));
		myPDFFont.setAvgWidth(pdfScalingFormula(myChcFont.getOS2Table().getAvgCharWidth(),intUnitsPerEM));
		myPDFFont.setFirstChar(0);
		myPDFFont.setLastChar(myChcFont.getNumGlyphs());
		myPDFFont.setPdfWidth(pdfScalingFormula(myChcFont.getGlyph(11).getAdvanceWidth(),intUnitsPerEM));
		myPDFFont.setWEntry(getWEntry());
		// TODO: Need an if statement here later but just for testing right now. myPDFFont.setToUnicodeCMAP("identityH");
		myPDFFont.setToUnicodeCMAP("identityH");
		
		// If we make it here return the converted file object
		return myPDFFont;
		
	}
	
    public static int pdfScalingFormula(int intAdvanceWidth, int intUnitsPerEm){
    	// Avoid divide by zero error.
    	if(intAdvanceWidth == 0 ){return 0;}
    	
    	return (intAdvanceWidth * 1000) / intUnitsPerEm;
    }

    private static String getWEntry(){
    	/** The W Entry array allows the definition of widths for individual CIDs */
    	
    	  int cidMax = ChcFont.intGlyphCount;
          int[] gidwidths = new int[cidMax * 2];
          
          for (int cid = 0; cid < cidMax; cid++){
              gidwidths[cid * 2] = cid;
              gidwidths[cid * 2 + 1] =  myChcFont.getHmtxTable().getAdvanceWidth(cid);
          }
          float scaling = 1000f / intUnitsPerEM;
          
          long lastCid = gidwidths[0];
          long lastValue = Math.round(gidwidths[1] * scaling);
        
          ArrayList<String> inner = null;
          ArrayList<String> outer = new ArrayList<String>();
         
          outer.add(String.valueOf(lastCid));
         
          State state = State.FIRST;

          for (int i = 2; i < gidwidths.length; i += 2){
        	  
              long cid   = gidwidths[i];
              long value = Math.round(gidwidths[i + 1] * scaling);

              switch (state){
              case FIRST:
            	  
            	  if (cid == lastCid + 1 && value == lastValue){state = State.SERIAL;}
            	  else if (cid == lastCid + 1){
            		  state = State.BRACKET;
            		  inner = new ArrayList<String>();
            		  inner.add(String.valueOf(lastValue));
            	  }

            	  else{
            		  inner = new ArrayList<String>();
            		  inner.add(String.valueOf(lastValue));
            		  outer.add(inner.toString());
            		  outer.add(String.valueOf(cid));
            	  }
            	  break;
              case BRACKET:
            	 
            	  if (cid == lastCid + 1 && value == lastValue){
            		  state = State.SERIAL;
            		  outer.add(inner.toString());
            		  outer.add(String.valueOf(lastCid)); }
            	  else if (cid == lastCid + 1){inner.add(String.valueOf(lastValue));}
            	  else{
            		  state = State.FIRST;
            		  inner.add(String.valueOf(lastValue));
            		  outer.add(inner.toString());
            		  outer.add(String.valueOf(cid));}
            	  break;
              case SERIAL:
            	  if (cid != lastCid + 1 || value != lastValue){
            		  outer.add(String.valueOf(lastCid));
            		  outer.add(String.valueOf(lastValue));
            		  outer.add(String.valueOf(cid));
            		  state = State.FIRST;
            	  }
            	  break;
              }
              lastValue = value;
              lastCid = cid;
          }

          switch (state)
          {
          case FIRST:
        	  inner =  new ArrayList<String>();
        	  inner.add(String.valueOf(lastValue));
        	  outer.add(inner.toString());
        	  break;
          case BRACKET:
        	  inner.add(String.valueOf(lastValue));
        	  outer.add(inner.toString());
        	  break;
          case SERIAL:
        	  outer.add(String.valueOf(lastCid));
        	  outer.add(String.valueOf(lastValue));
        	  break;
          }
           // Now return it without the comma and make it readable for humans.
          return outer.toString().replace(",", "").replace("]", "]" + PDFCRLF);
    }
 	public static String getOsName(){
		// The operating system of the host that my Java program is running 
		if(OS == null) { OS = System.getProperty("os.name"); }
	      return OS;
	}
	
	public static boolean isWindows(){return getOsName().startsWith("Windows");}
	
	public static boolean isMac(){return getOsName().startsWith("Mac");}
	
	public static String addZeros(String a)
	{
		int i=0;
		i=a.length();
		if ( i == 4 )
		return a;
		else
		{
		int j= 4 - i;
		for (int k=0; k<j; k++)
		{
		a="0"+a;
		}
		return a;
		}
		}
	
	public static char convertToChar(char[] charArray){
		char f = 0;
		for (char c : charArray) {
			f+=c;
		}
		return f;
	}
}
