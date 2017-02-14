package Fonts;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import Fonts.table.CmapFormat;
import Fonts.table.CmapTable;
import Fonts.table.CvtTable;
import Fonts.table.FpgmTable;
import Fonts.table.GaspTable;
import Fonts.table.GlyfTable;
import Fonts.table.HeadTable;
import Fonts.table.HheaTable;
import Fonts.table.HmtxTable;
import Fonts.table.LocaTable;
import Fonts.table.MaxpTable;
import Fonts.table.NameTable;
import Fonts.table.Os2Table;
import Fonts.table.PostTable;
import Fonts.table.PrepTable;
import Fonts.table.Table;
import Fonts.table.TableDirectory;
import Fonts.table.TableFactory;
import cidObjects.CIDSystemInfo;
import pdfCmaps.ToUnicodeWriter;
import pdfCmaps.identityH;

/**
 * The TrueType font by Charles Cope
 */
public class PdfFont {
	private static final int ITALIC = 1;
	private static final int OBLIQUE = 512;
	public static final int PLATFORM_UNICODE = 0;
	public static final int PLATFORM_MACINTOSH = 1;
	public static final int PLATFORM_WINDOWS = 3;
	 // Windows encodings
    public static final int ENCODING_WIN_SYMBOL = 0; // Unicode, non-standard character set
    public static final int ENCODING_WIN_UNICODE_BMP = 1; // Unicode BMP (UCS-2)
    public static final int ENCODING_WIN_SHIFT_JIS = 2;
    public static final int ENCODING_WIN_BIG5 = 3;
    public static final int ENCODING_WIN_PRC = 4;
    public static final int ENCODING_WIN_WANSUNG = 5;
    public static final int ENCODING_WIN_JOHAB = 6;
    public static final int ENCODING_WIN_UNICODE_FULL = 10; // Unicode Full (UCS-4)
	 // Unicode encodings
    public static final int ENCODING_UNICODE_1_0 = 0;
    public static final int ENCODING_UNICODE_1_1 = 1;
    public static final int ENCODING_UNICODE_2_0_BMP = 3;
    public static final int ENCODING_UNICODE_2_0_FULL = 4;
    enum  State{FIRST, BRACKET, SERIAL }
    private final String  PDFCRLF = "\r\n";
	private String strToUnicodeCMAP ="";
	public static int intGlyphCount ;
	private String path;
	private TableDirectory tableDirectory = null;
	private Table[] tables;
	private Os2Table os2;
	private CmapTable cmap;
	private GlyfTable glyf;
	private HeadTable head;
	private HheaTable hhea;
	private HmtxTable hmtx;
	private LocaTable loca;
	private MaxpTable maxp;
	private NameTable name;
	private PostTable post;
	private CvtTable cvt;
	private PrepTable prep;
	private FpgmTable fpgm;
	private GaspTable gasp;
	private byte[] byteOriginalData;
	private int intUnitsPerEm;
	private Integer BBoxLowerLeftx = 0;
	private Integer BBoxLowerLefty = 0;
	private Integer BBoxUpperRightx = 0;
	private Integer BBoxUpperRighty = 0;
	private String strFontBBox = "";
	private Integer intMissingWidth = 0;
	private String strBaseFontName = "";
	private String strFontFamilyName = "";
	private String strPrefixNameTag = "";
	// Flags
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
	private Integer intWeight = 0;
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
	private CIDSystemInfo cidSystemInfo = new CIDSystemInfo();
	
	// Subset stuff here
	private final SortedMap<Integer, Integer> uniToGID;
	private  Map<Integer, Integer> cidToGid;
	private Map<Integer, Integer> newSubGIDToOldGID;
	private final SortedSet<Integer> glyphIds; // new glyph ids
	private CmapFormat unicodeCmap;
	String JavaNewLine = System.getProperty("line.separator");
	private boolean hasAddedCompoundReferences;
	private boolean blnIsEmbedded = false;
	private float minSubSetVersion = (float) 1.0; 
	

	

	private static final byte[] PAD_BUF = new byte[] { 0, 0, 0 };

	/** Constructor   */
    public PdfFont() {
    	uniToGID = new TreeMap<Integer, Integer>();
    	glyphIds = new TreeSet<Integer>();
        glyphIds.add(0);
    	 
    }

    public Table getTable(int tableType) {
        for (int i = 0; i < tables.length; i++) {
            if ((tables[i] != null) && (tables[i].getType() == tableType)) {
                return tables[i];
            }
        }
        return null;
    }
  
    public FpgmTable getFpgmTable(){return fpgm;}
    
    public GlyfTable getGlyfTable(){return glyf;}
    
    public PrepTable getPrepTable(){return prep;}
    
    public CvtTable getCvtTable(){return cvt;}
    
    public Os2Table getOS2Table() {return os2;}
       
    public CmapTable getCmapTable() {return cmap;}
    
    public CmapFormat getUnicodeCmap(){
    	//Returns the best Unicode sub table  from the font (the most general).
    	if (unicodeCmap == null){
    		unicodeCmap = cmap.getCmapFormat(PLATFORM_UNICODE, ENCODING_UNICODE_2_0_FULL);	
    	}
    
    	if (unicodeCmap == null){
    		unicodeCmap = cmap.getCmapFormat(PLATFORM_UNICODE,ENCODING_UNICODE_2_0_BMP);
    	}		
    	if (unicodeCmap == null){
    		unicodeCmap = cmap.getCmapFormat(PLATFORM_WINDOWS,ENCODING_WIN_UNICODE_BMP);
    	}
    	if (unicodeCmap == null){
    		unicodeCmap = cmap.getCmapFormat(PLATFORM_WINDOWS,ENCODING_WIN_SYMBOL);
    	}

    	return unicodeCmap;
    	
    }
 
    public int getCharacterCodeToGlyphId(int intCharCode){return getUnicodeCmap().mapCharCode(intCharCode);	}
   
    public HeadTable getHeadTable() {return head;}
    
    public int getUnitsPerEM(){return intUnitsPerEm; }
    
    public HheaTable getHheaTable() {return hhea;}
    
    public HmtxTable getHmtxTable() {return hmtx;}
    
    public LocaTable getLocaTable() {return loca;}
    
    public MaxpTable getMaxpTable() {return maxp;}

    public NameTable getNameTable() {return name;}

    public PostTable getPostTable() {return post;}
    
	public void setFirstChar(int FirstChar){intFirstChar = FirstChar;}
    public String getFirstChar(){return  intFirstChar.toString();}
    
    public void setLastChar(int LastChar){intLastChar = LastChar;}
	public String getLastChar(){return  intLastChar.toString();}

    public int getNumGlyphs() {return maxp.getNumGlyphs();}
    
    public int getSubSetNumGlyphs() {return glyphIds.size();}
    
	public String getFontWeight() {return  intWeight.toString();}
	
	public String getCapHeight(){return intCapHeight.toString();}
	
	public String getXHeight(){return intXHeight.toString();}
	
    public int getBoundingBoxLowerLeftx() {return BBoxLowerLeftx;}
	
    public int getBoundingBoxLowerLefty() {return BBoxLowerLefty;}
	
    public int getBoundingBoxUpperRightx() {return BBoxUpperRightx;}
	
    public int getBoundingBoxUpperRighty() {return BBoxUpperRighty;}
    
    public int getMissingWidth(){return intMissingWidth; }
    
    public float getMinSubSetVersion() {return minSubSetVersion;	}
    
    public String getItalicAngle(){return intItalicAngle.toString();}
    
    public String getFontBaseName(){return strBaseFontName;}
    
    public String getFontFamilyName(){return strFontFamilyName;}
	
    public boolean getIsEmbedded() {	return blnIsEmbedded;}


    /**
	 * The tag consists of exactly six upper case letters; the
	 * choice of letters is arbitrary, but different subsets in the same PDF file must have
	 * different tags. 
	 */
	public void setIsEmbedded(boolean blnIsEmbedded) {this.blnIsEmbedded = blnIsEmbedded;	}
	
    public String getPrefixNameTag() {
    	int intRandom = 65;
		int asciiForUpperA = 65;
		int asciiForUpperZ = 90;
		char[] charArray = new char[6];
		// Loop six times picking random letters
		  for (int intIndex = 0; intIndex < 6; intIndex++) {
			  intRandom =  randInt(asciiForUpperA,asciiForUpperZ);
			  charArray[intIndex] = (char) intRandom;
	        }
		
		  strPrefixNameTag =  String.valueOf(charArray);
    	
    	return strPrefixNameTag;
	}
   
    public String getAscent(){return intAscent.toString();}
    
    public String getDescent(){return intDescent.toString();}
    
    public String getLeading(){return intLeading.toString();}
    
    public String getMaxWidth(){return  intMaxWidth.toString();}
    
    public String getAvgWidth(){return intAvgWidth.toString();}
	
    public String getStemH(){ return  intStemH.toString();} 
  
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
    
    public boolean getIsFixedPitch(){return blnFixedPitchFlag;}
    
    public boolean getIsItalicFlag(){return blnItalicFlag;}
    
    public boolean getIsScriptFlag(){return blnScriptFlag;}
    
    public boolean getIsSerifFlag(){return blnSerifFlag;}
    
    public boolean getIsAllCapFlag(){return blnAllCapFlag;}
    
    public boolean getIsSmallCapFlag(){return blnSmallCapFlag;}
    
    public boolean getIsForceBoldFlag(){return blnForceBoldFlag;}
    
    public boolean getIsSymbolicFlag(){return blnSymbolicFlag;}
    
    public boolean getIsNonsymbolicFlag(){return blnNonsymbolicFlag;}
    
    public byte[] getOriginalData(){	
    	if(byteOriginalData == null){
    		if(path != null){
    			File f = new File(path);
    			try {
    				RandomAccessFile raf;
    				raf = new RandomAccessFile(f, "r");
    				byteOriginalData = new byte[(int) raf.length()];
    				raf.seek(0);
    				raf.readFully(byteOriginalData);
    				raf.close();
    			} catch (IOException e) {e.printStackTrace();}
    		}
    	}
    	return byteOriginalData;
    }

    public Glyph getGlyph(int i) {
        return (glyf.getDescription(i) != null)
            ? new Glyph(
                glyf.getDescription(i),
                hmtx.getLeftSideBearing(i),
                hmtx.getAdvanceWidth(i))
            : null;
    }
 
    public int getGlyphWidthToPDFWidth(int CharCode){
    	try {return pdfScalingFormula(getGlyph(CharCode).getAdvanceWidth());
    	} catch (Exception e) {	return 0;}
    }
    public String getPath() {return path; }

    public TableDirectory getTableDirectory() {return tableDirectory; }

    /**
     * @param pathName Path to the TTF font file
     */
    protected void read(String pathName) {
        path = pathName;
        File f = new File(pathName);

        if (!f.exists()) {return;  }

        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            raf.seek(0);

            tableDirectory = new TableDirectory(raf);
            tables = new Table[tableDirectory.getNumTables()];

            // Load Only the tables needed and in correct order to get needed information.
            os2  = (Os2Table) TableFactory.create(tableDirectory.getEntryByTag(Table.OS_2), raf);
            maxp = (MaxpTable) TableFactory.create(tableDirectory.getEntryByTag(Table.maxp), raf);
            intGlyphCount = maxp.getNumGlyphs();
            cmap = (CmapTable) TableFactory.create(tableDirectory.getEntryByTag(Table.cmap), raf);
            glyf = (GlyfTable) TableFactory.create(tableDirectory.getEntryByTag(Table.glyf), raf);
            head = (HeadTable) TableFactory.create(tableDirectory.getEntryByTag(Table.head), raf);
            hhea = (HheaTable) TableFactory.create(tableDirectory.getEntryByTag(Table.hhea), raf);
            hmtx = (HmtxTable) TableFactory.create(tableDirectory.getEntryByTag(Table.hmtx), raf);
            loca = (LocaTable) TableFactory.create(tableDirectory.getEntryByTag(Table.loca), raf);
            name = (NameTable) TableFactory.create(tableDirectory.getEntryByTag(Table.name), raf);
            post = (PostTable) TableFactory.create(tableDirectory.getEntryByTag(Table.post), raf);
            cvt = (CvtTable )TableFactory.create(tableDirectory.getEntryByTag(Table.cvt), raf);
            prep = (PrepTable)TableFactory.create(tableDirectory.getEntryByTag(Table.prep), raf);
            fpgm = (FpgmTable)TableFactory.create(tableDirectory.getEntryByTag(Table.fpgm), raf);
            gasp = (GaspTable)TableFactory.create(tableDirectory.getEntryByTag(Table.gasp), raf);
            raf.close();

            // Initialize the tables that require it
            hmtx.init(hhea.getNumberOfHMetrics(), 
            		intGlyphCount - hhea.getNumberOfHMetrics());
            loca.init(intGlyphCount, head.getIndexToLocFormat() == 0);
            glyf.init(intGlyphCount, loca);
            
            // Set the best unicode Cmap for this font
            getUnicodeCmap();
            
            // Now get the data from the tables
            intUnitsPerEm = head.getUnitsPerEm(); 
            BBoxLowerLeftx = (int) head.getXMin();
            BBoxLowerLefty = (int) head.getYMin();
            BBoxUpperRightx =(int) head.getXMax();
            BBoxUpperRighty = (int) head.getYMax();
            Glyph MissingWidth = getGlyph(0);
    		if (MissingWidth != null){intMissingWidth = pdfScalingFormula(MissingWidth.advanceWidth);}
    		else{intMissingWidth =(0);}
    		strBaseFontName = name.getRecord(NameTable.namePostscriptName);
    		strFontFamilyName = name.getRecord(NameTable.nameFontFamilyName);
    		
    		blnFixedPitchFlag = post.getIsFixedPitch();

    		int fsSelection = os2.getSelection();
    		blnItalicFlag =((fsSelection & (ITALIC | OBLIQUE)) != 0);

    		blnScriptFlag = os2.getIsScript();
    		blnSerifFlag = os2.getIsSerif();		
    		
    		/** Only Roman Encoding or Windows uni-code are allowed for a non symbolic font 
    		 *  For symbolic font, no encoding entry is allowed and only one encoding entry is expected into the FontFile CMap
    		 *  Any font whose character set is not a subset of the Adobe standard character set is considered to be symbolic.
    		 *  If the Symbolic flag should be set then the Nonsymbolic flag must be cleared .
    		 * */
    		blnSymbolicFlag = true;
    		blnNonsymbolicFlag = false;
    		
    		intWeight = os2.getWeightClass() * 100; 
    		
            if (os2.getVersion() >= 1.2){
            	intCapHeight = pdfScalingFormula(os2.getCapHeight());
            	intXHeight = pdfScalingFormula(os2.getXHeight());
             
            }
            else {
                // estimate by summing the typographical +ve ascender and -ve descender
            	intCapHeight = pdfScalingFormula(os2.getTypoAscender() + os2.getTypoDescender());
            	// estimate by halving the typographical ascender
            	intXHeight = pdfScalingFormula((int) (os2.getTypoAscender() / 2.0f));
            }
            
            intItalicAngle = post.getItalicAngle();
            intAscent = pdfScalingFormula((int) hhea.getAscender());
            intDescent = pdfScalingFormula(hhea.getDescender());
            intLeading = pdfScalingFormula(hhea.getLineGap());
            intStemV = os2.getWeightClass();  // This is calculated in the get property
            intStemH = 190; // Hard coded till I figure it out.
            
            intMaxWidth = pdfScalingFormula(hhea.getAdvanceWidthMax());
            intAvgWidth = pdfScalingFormula(os2.getAvgCharWidth());
            intFirstChar = 0; // Just default most Latin starts at 32 before that non printable codes
            intLastChar = maxp.getNumGlyphs(); // Just defaults can be set to subsets
    		
            // TODO Need to find the data in file and set flags.
    		//blnAllCapFlag =
    		//blnSmallCapFlag =
    		//blnForceBoldFlag =

    		
        } catch (IOException e) {e.printStackTrace();}
    }
    
    
    
    /**
     * @param pathName Path to the TTF font file
     */
    public PdfFont create(String pathName) {
        PdfFont font = new PdfFont();
        font.read(pathName);
        return font;
    }
  
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
    
    public String getSubWEntry(){
    	// Okay if we need subset of width we need to create some maps first.
    	try {
    		createSubGIDMap();
    		createSubCidToGid();
    		byte[] temp = hmtx.getSubSetBytes(getOriginalData(), glyphIds,hhea.getNumberOfHMetrics());
    		hmtx.setSubSetAdvance(temp, glyphIds.size());

    		float scaling = 1000f / intUnitsPerEm;

    		ArrayList<String> widths = new  ArrayList<String>();
    		ArrayList<String> ws = new  ArrayList<String>();

    		int prev = Integer.MIN_VALUE;
    		// Use a sorted list to get an optimal width array  
    		Set<Integer> keys = new TreeSet<Integer>(cidToGid.keySet());
    		// Get the Widths first for all the glyphs.
    		for (int cid : keys){
    			int gid = cidToGid.get(cid);
    			long width = Math.round(hmtx.getSubSetAdvanceWidth(gid) * scaling);
    			ws.add(String.valueOf(width)); 
    		}

    		// Now add all the width to the array.    		

    		int intIndex = 0;
    		for (int cid : keys){

    			if (prev == cid - 1){
    				widths.add(String.valueOf(ws.get(intIndex)));}
    			if (prev != cid - 1){
    				widths.add("]");
    				widths.add(String.valueOf(cid)); 
    				widths.add("[");
    				widths.add(String.valueOf(ws.get(intIndex)));
    			}
    			prev = cid;
    			intIndex++;

    		}


    		String strTemp = widths.toString().replace(",", "").replace("[]", "[") + "]";
    		strTemp = strTemp.replace("]", "]" + PDFCRLF);
    		// Now return it without the comma and make it readable for humans.
    		return strTemp;		        
    	} catch (IOException e) {e.printStackTrace();}
    	return "Error in getSubWEntry method";
    }
    public String getWEntry(){ 
    	/** The W Entry array allows the definition of widths for individual CIDs */
    	
    	int cidMax = intGlyphCount;
        int[] gidwidths = new int[cidMax * 2];
        
        for (int cid = 0; cid < cidMax; cid++){
            gidwidths[cid * 2] = cid;
            gidwidths[cid * 2 + 1] = hmtx.getAdvanceWidth(cid);
        }
        float scaling = 1000f / intUnitsPerEm;
        
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
	    
	public String getCIDSystemInfoDictionary(){ return cidSystemInfo.toString();}
	
	public String getToUnicodeCMAP(){return strToUnicodeCMAP;}
	
	public void setToUnicodeCMAP(String strPdfCmapName){ 
	// Note these are not the same as Font Cmaps these are PDF Cmaps only.
		String strTemp = ""; 
		
		if (blnIsEmbedded == false){ // We need to create the Cmap our self
			try {
				byte[] btyeCmap = getBuildToUnicodeCMap();
				String strCMap = new String(btyeCmap);
				
				strTemp = "<< /Length " + btyeCmap.length + " >>";
				strTemp += "stream" + PDFCRLF;
				strTemp += strCMap + PDFCRLF;
				strTemp += "endstream" + PDFCRLF;
				cidSystemInfo.setRegistry("(Adobe)");
				cidSystemInfo.setOrdering("(Identity)");
				cidSystemInfo.setSupplement(0);
				strToUnicodeCMAP = strTemp;
			} catch (IOException e) {e.printStackTrace();}
		}
		else{ // We just use a predefined Cmap
			

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
				strToUnicodeCMAP = strTemp;}
		}
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
		int results = pdfScalingFormula((int)dblTotal);
		
		if (results < (intUnitsPerEm * .5)){return results;}
		else{ return pdfScalingFormula((int) (intUnitsPerEm * .5));}
		
	}
    
    public double toEmSpace(double dblValue){
        if (intUnitsPerEm == 1000) {return dblValue;}
        return Math.ceil((dblValue / intUnitsPerEm) * 1000);    // always round up
    }
  
    public void subSetAdd(int unicode) {
    	if (unicodeCmap == null){unicodeCmap = this.getUnicodeCmap();}
        int gid = unicodeCmap.mapCharCode(unicode);
        if (gid != 0){
            uniToGID.put(unicode, gid);
            glyphIds.add(gid);
        }
    }
 
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
   
    public int pdfScalingFormula(int intAdvanceWidth){
    	// Avoid divide by zero error.
    	if(intAdvanceWidth == 0 ){return 0;}
    	return (intAdvanceWidth * 1000) / intUnitsPerEm;
    }
    
    private byte[] getBuildToUnicodeCMap() throws IOException {
    	// build GID -> Unicode map
    	Map<Integer, Integer>  gidToUni = new HashMap<Integer, Integer>(intGlyphCount);
    	 
    	 
    	for (int gid = 1, max = intGlyphCount; gid <= max; gid++){
            // skip composite glyph components that have no code point
            Integer codePoint = unicodeCmap.getCharacterCode(gid);
            if (codePoint != null){
            	gidToUni.put(gid, codePoint); // CID = GID
            }
        }
    	  ToUnicodeWriter toUniWriter = new ToUnicodeWriter();
          boolean hasSurrogates = false;
          
          for (int gid = 1, max = glyphIds.size(); gid <= max; gid++){
              // optional CID2GIDMap for subsetting
              int cid;
              if (newSubGIDToOldGID != null){
                  if (!newSubGIDToOldGID.containsKey(gid)){continue;}
                  else{cid = newSubGIDToOldGID.get(gid);        }
              }
              else{cid = gid;}
              // skip composite glyph components that have no code point
              Integer codePoint = gidToUni.get(cid); // old GID -> Unicode
              
              if (codePoint != null){
                  if (codePoint > 0xFFFF){hasSurrogates = true;}
               
                  toUniWriter.add(cid, new String(new int[]{ codePoint }, 0, 1));
                  //System.out.println("cid = " + cid + " New String = " + new String(new int[]{ codePoint }, 0, 1 ));
              }
          }
         
          
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          toUniWriter.writeTo(out);

          if (hasSurrogates == true ){minSubSetVersion = 1.5f;}
        	  
          return out.toByteArray();
    }
    
    private void createSubGIDMap() throws IOException{
        addCompoundReferences();

        newSubGIDToOldGID = new HashMap<Integer, Integer>();
        int newGID = 0;
        for (int oldGID : glyphIds)
        {
        	newSubGIDToOldGID.put(newGID, oldGID);
            newGID++;
        }
        
    }
    private  void createSubCidToGid(){
    	// Need to Call createSubGIDMap() first.
    	cidToGid = new HashMap<Integer, Integer>(newSubGIDToOldGID.size());
    	 for (Map.Entry<Integer, Integer> entry : newSubGIDToOldGID.entrySet())
         {
             int newGID = entry.getKey();
             int oldGID = entry.getValue();
             cidToGid.put(oldGID, newGID);
         }
    }
    private void addCompoundReferences() throws IOException {
    	if (hasAddedCompoundReferences){return;}

    	hasAddedCompoundReferences = true;

    	boolean hasNested;
    	do
    	{

    		long[] offsets = loca.getOffsets();
    		InputStream is = new ByteArrayInputStream(getOriginalData());
    		Set<Integer> glyphIdsToAdd = null;
    		try
    		{
    			is.skip(glyf.getOffset());
    			long lastOff = 0L;
    			for (Integer glyphId : glyphIds){
    				long offset = offsets[glyphId];
    				long len = offsets[glyphId + 1] - offset;
    				is.skip(offset - lastOff);
    				byte[] buf = new byte[(int)len];
    				is.read(buf);
    				// rewrite glyphIds for compound glyphs
    				if (buf.length >= 2 && buf[0] == -1 && buf[1] == -1){
    					int off = 2*5;
    					int flags;
    					do
    					{
    						flags = (buf[off] & 0xff) << 8 | buf[off + 1] & 0xff;
    						off +=2;
    						int ogid = (buf[off] & 0xff) << 8 | buf[off + 1] & 0xff;
    						if (!glyphIds.contains(ogid)){
    							if (glyphIdsToAdd == null){glyphIdsToAdd = new TreeSet<Integer>();}
    							glyphIdsToAdd.add(ogid);
    						}
    						off += 2;
    						// ARG_1_AND_2_ARE_WORDS
    						if ((flags & 1 << 0) != 0) {off += 2 * 2;}
    						else {off += 2; }
    						// WE_HAVE_A_TWO_BY_TWO
    						if ((flags & 1 << 7) != 0) {off += 2 * 4;}
    						// WE_HAVE_AN_X_AND_Y_SCALE
    						else if ((flags & 1 << 6) != 0){off += 2 * 2;}
    						// WE_HAVE_A_SCALE
    						else if ((flags & 1 << 3) != 0){off += 2;}
    					}
    					while ((flags & 1 << 5) != 0); // MORE_COMPONENTS

    				}
    				lastOff = offsets[glyphId + 1];
    			}
    		}
    		finally {is.close();}
    		if (glyphIdsToAdd != null) { glyphIds.addAll(glyphIdsToAdd); }
    		hasNested = glyphIdsToAdd != null;
    	} while (hasNested);

    }
    
    public byte[] getSubSetCIDSet(){
    	byte[] bytes = new byte[Collections.max(cidToGid.keySet()) / 8 + 1];
    	for (int cid : cidToGid.keySet())  {
    		int mask = 1 << 7 - cid % 8;
    		bytes[cid / 8] |= mask;
    	}
    	return bytes;
    }
    public byte[] getSubSetCIDToGIDMapping(){
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	int cidMax = Collections.max(cidToGid.keySet());

    	for (int intCount = 0; intCount <= cidMax; intCount++){
    		int gid;
    		if (cidToGid.containsKey(intCount)){gid = cidToGid.get(intCount);	}
    		else{gid = 0;}
    		try {
    			out.write(new byte[] { (byte)(gid >> 8 & 0xff), (byte)(gid & 0xff) });
    		} catch (IOException e) {e.printStackTrace();}
    	}
    	return out.toByteArray();
    }
    
    public byte[] getSubSetFontBytes( boolean cmapRequired, boolean postRequired){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	int intNumberGlyph = glyphIds.size();
    	try {

    		DataOutputStream dosFile = new DataOutputStream(baos);

    		Map<String, byte[]> tables = new TreeMap<String, byte[]>();
    		long[] newLoca = new long[intNumberGlyph + 1];

    		/* The following TrueType tables are always required: “head,” “hhea,” “loca,” “maxp,” “cvt ,” “prep,” “glyf,” “hmtx,” and “fpgm.
    		 * If used with a simple font dictionary, the font program must additionally contain a “cmap” table
    		 */

    		if (head != null){
    			head.setCheckSumAdjustment(0);
    			head.setIndexToLocFormat((short) 1);// Force long format
    			tables.put("head", head.getAllBytes());
    		}

    		if(hhea !=null){tables.put("hhea", hhea.getSubSetBytes(glyphIds));}

    		if(maxp !=null){
    			maxp.setNumGlyphs(intNumberGlyph);
    			tables.put("maxp", maxp.getAllBytes());
    		}

    		if (glyf != null){tables.put("glyf", glyf.getSubSetBytes(newLoca, loca.getOffsets(),getOriginalData(),glyphIds));}

    		if (loca != null){tables.put("loca", loca.getSubSetBytes(newLoca));}

    		if (cmapRequired == true){if (cmap != null){	tables.put("cmap", cmap.getSubSetBytes(uniToGID, glyphIds));}}

    		if (hmtx != null){	tables.put("hmtx", hmtx.getSubSetBytes(getOriginalData(), glyphIds,hhea.getNumberOfHMetrics()));	}

    		if(postRequired == true){if (post != null){tables.put("post", post.getSubSetBytes(glyphIds));}}

    		if(cvt != null){	tables.put("cvt ", cvt.getAllBytes());}

    		if(prep != null){tables.put("prep",prep.getAllBytes());	}

    		if(fpgm != null){tables.put("fpgm", fpgm.getAllBytes());	}

    		if(gasp != null){tables.put("gasp", gasp.getAllBytes());}

    		// calculate checksum  should get 917664 checksum 
    		long checksum = writeFileHeader(dosFile, tables.size());
    		long offset = 12L + 16L * tables.size();

    		for (Map.Entry<String, byte[]> entry : tables.entrySet()){
    			checksum += writeTableHeader(dosFile, entry.getKey(), offset, entry.getValue());
    			offset += (entry.getValue().length + 3) / 4 * 4;
    		}
    		checksum = 0xB1B0AFBAL - (checksum & 0xffffffffL);

    		head.setCheckSumAdjustment(checksum);

    		for (byte[] bytes : tables.values()){writeTableBody(dosFile, bytes);}

    		dosFile.close();

    	} catch (IOException e) {e.printStackTrace();	}

    	return baos.toByteArray();
    }
        
    private long writeFileHeader(DataOutputStream out, int nTables) throws IOException  {
        out.writeInt(0x00010000);
        out.writeShort(nTables);
        
        int mask = Integer.highestOneBit(nTables);
        int searchRange = mask * 16;
        out.writeShort(searchRange);
        
        int entrySelector = log2(mask);
    
        out.writeShort(entrySelector);
        
        // numTables * 16 - searchRange
        int last = 16 * nTables - searchRange;
        out.writeShort(last);
        
        return 0x00010000L + toUInt32(nTables, searchRange) + toUInt32(entrySelector, last);
    }
 
    private long writeTableHeader(DataOutputStream out, String tag, long offset, byte[] bytes)throws IOException {
        long checksum = 0;
        for (int nup = 0, n = bytes.length; nup < n; nup++){
            checksum += (bytes[nup] & 0xffL) << 24 - nup % 4 * 8;
        }
        checksum &= 0xffffffffL;

        byte[] tagbytes = tag.getBytes("US-ASCII");

        out.write(tagbytes, 0, 4);
        out.writeInt((int)checksum);
        out.writeInt((int)offset);
        out.writeInt(bytes.length);

        // account for the checksum twice, once for the header field, once for the content itself
        return toUInt32(tagbytes) + checksum + checksum + offset + bytes.length;
    }
    
    private void writeTableBody(OutputStream os, byte[] bytes) throws IOException {
        os.write(bytes);
        if (bytes.length % 4 != 0){os.write(PAD_BUF, 0, 4 - bytes.length % 4);}
    }
    private int log2(int num){return (int)Math.round(Math.log(num) / Math.log(2));}
    private long toUInt32(int high, int low) {return (high & 0xffffL) << 16 | low & 0xffffL;}
    private long toUInt32(byte[] bytes){return (bytes[0] & 0xffL) << 24 | (bytes[1] & 0xffL) << 16
    		| (bytes[2] & 0xffL) << 8 | bytes[3] & 0xffL; }
  
    public String getEncodedString(String strToConvert){
		// This encodes the  unicode string to Hex Values instead of ASCII values for the pdf CID to work.
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		for (int offset = 0; offset < strToConvert.length(); ){
			int codePoint = strToConvert.codePointAt(offset);
			// Next get the character identifier (CID) numbers  for the CodePoint
			int intCID = getCharacterCodeToGlyphId(codePoint);

			// multi-byte encoding with 1 to 4 bytes
			byte[] bytes = new byte[] { (byte)(intCID >> 8 & 0xff), (byte)(intCID & 0xff) } ;

			try {out.write(bytes);} catch (IOException e) {e.printStackTrace();}

			offset += Character.charCount(codePoint);
		}
	
		return Hex.getString(out.toByteArray());
		
	}    
	
    public String getUnicodeEscapeString(int intValue){return "\\u"+ addZeros(Integer.toHexString(intValue));}
	
	public String getUnicodeString(int intValue){return "U+"+addZeros(Integer.toHexString(intValue));}
	
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
    
    private int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
   
    public String getFontDictionary(){
    	
    	String strResults = "First Char >> "+ getFirstChar()+ JavaNewLine; 
    	strResults += "Last Char >> " + getLastChar()+ JavaNewLine;
    	strResults += "Font Descriptor Flags >> " + getFontDescriptorFlags() + " " + JavaNewLine;;
    	strResults +="Font BBox >> [ " + getFontBBox() + " ]" + JavaNewLine;
    	strResults += "Missing Width >> " +  getMissingWidth() + " " + JavaNewLine;
    	strResults += "StemV >> " +getStemV() + " " + JavaNewLine;
    	strResults += "Italic Angle >> " + getItalicAngle() + " " + JavaNewLine;
    	strResults += "Cap Height >> " +getCapHeight() + " " + JavaNewLine;
    	strResults += "X Height >> " + getXHeight() + " " + JavaNewLine;
    	strResults += "Ascent >> " + getAscent() + " " + JavaNewLine;
    	strResults += "Descent >> " + getDescent() + " " + JavaNewLine;
    	strResults += "Leading >> " + getLeading()+ " " + JavaNewLine;
    	strResults += "Max Width >> " + getMaxWidth() + " " + JavaNewLine; 
    	strResults += "Avg Width >> " + getAvgWidth() + " " + JavaNewLine;
    	
    	return strResults;
    }
    
/** Need a toString Method for debugging and development */
	
	public String toString(){
		
		String strToString = "<< Start of PDF font dictionary >> " + JavaNewLine;
		strToString += getFontDictionary();
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
}