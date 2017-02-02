package Fonts;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
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

/**
 * The TrueType font.
 */
public class ChcFont {
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
	// Subset stuff here
	private final SortedMap<Integer, Integer> uniToGID;
	private final SortedSet<Integer> glyphIds; // new glyph ids
	private CmapFormat unicodeCmap;

	/** Constructor   */
    public ChcFont() {
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
    public HeadTable getHeadTable() {return head;}
    
    public HheaTable getHheaTable() {return hhea;}
    
    public HmtxTable getHmtxTable() {return hmtx;}
    
    public LocaTable getLocaTable() {return loca;}
    
    public MaxpTable getMaxpTable() {return maxp;}

    public NameTable getNameTable() {return name;}

    public PostTable getPostTable() {return post;}

    public int getAscent() {return hhea.getAscender();}

    public int getDescent() {return hhea.getDescender();}

    public int getNumGlyphs() {return maxp.getNumGlyphs();}

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
     
        } catch (IOException e) {e.printStackTrace();}
    }
    
    public static ChcFont create() { return new ChcFont(); }
    
    /**
     * @param pathName Path to the TTF font file
     */
    public ChcFont create(String pathName) {
        ChcFont f = new ChcFont();
        f.read(pathName);
        return f;
    }
    
    public void subSetAdd(int unicode) {
    	if (unicodeCmap == null){unicodeCmap = this.getUnicodeCmap();}
        int gid = unicodeCmap.mapCharCode(unicode);
        if (gid != 0){
            uniToGID.put(unicode, gid);
            glyphIds.add(gid);
        }
    }
    public byte[] getSubSetFontBytes(String strPDFilepath, int intNumberGlyph, boolean cmapRequired, boolean postRequired){
    
    	try {
    		// When finished testing use line below for append for now create new each time.
    		//DataOutputStream dosFile = new DataOutputStream(new FileOutputStream(strPDFilepath, true));
			DataOutputStream dosFile = new DataOutputStream(new FileOutputStream(strPDFilepath, false));
		
    	
    	// Some Test Data.
    	
    	subSetAdd(54532); //The GlyphId is 2830
    	subSetAdd(50948); //The GlyphId is 2146
    	subSetAdd(69);     //The GlyphId is 40
    	subSetAdd(54924); //The GlyphId is 2915
    	subSetAdd(54028); //The GlyphId is 2739
    	subSetAdd(51088); //The GlyphId is 2197
    	subSetAdd(87);     //The GlyphId is 58
    	subSetAdd(47196);//The GlyphId is 1339
    	subSetAdd(46300);
    	subSetAdd(32);
    	subSetAdd(97);
    	subSetAdd(99);
    	subSetAdd(100);
    	subSetAdd(101);
    	subSetAdd(105);
    	subSetAdd(108);
    	subSetAdd(49324);
    	subSetAdd(109);
    	subSetAdd(110);
    	subSetAdd(111);
    	subSetAdd(112);
    	subSetAdd(114);
    	subSetAdd(116);
    	subSetAdd(120);
    	subSetAdd(121);
    	subSetAdd(122);
    	subSetAdd(51068);
    	subSetAdd(61);
    	
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

    	if(cvt != null){	tables.put("cvt", cvt.getAllBytes());	}
    	
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

        // update checksumAdjustment in 'head' table
//        head[8] = (byte)(checksum >>> 24);
//        head[9] = (byte)(checksum >>> 16);
//        head[10] = (byte)(checksum >>> 8);
//        head[11] = (byte)checksum;
        
        dosFile.close();
        
    	} catch (IOException e) {e.printStackTrace();	}
    	
    	return null;
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
        for (int nup = 0, n = bytes.length; nup < n; nup++)
        {
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
   
    private int log2(int num){return (int)Math.round(Math.log(num) / Math.log(2));}
    private long toUInt32(int high, int low) {return (high & 0xffffL) << 16 | low & 0xffffL;}
    private long toUInt32(byte[] bytes){return (bytes[0] & 0xffL) << 24 | (bytes[1] & 0xffL) << 16
    		| (bytes[2] & 0xffL) << 8 | bytes[3] & 0xffL; }
    
}