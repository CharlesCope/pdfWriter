package Fonts;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import Fonts.table.CmapTable;
import Fonts.table.GlyfTable;
import Fonts.table.HeadTable;
import Fonts.table.HheaTable;
import Fonts.table.HmtxTable;
import Fonts.table.LocaTable;
import Fonts.table.MaxpTable;
import Fonts.table.NameTable;
import Fonts.table.Os2Table;
import Fonts.table.PostTable;
import Fonts.table.Table;
import Fonts.table.TableDirectory;
import Fonts.table.TableFactory;


/**
 * The TrueType font.
 */
public class ChcFont {
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

    /** Constructor   */
    public ChcFont() {}

    public Table getTable(int tableType) {
        for (int i = 0; i < tables.length; i++) {
            if ((tables[i] != null) && (tables[i].getType() == tableType)) {
                return tables[i];
            }
        }
        return null;
    }

    public Os2Table getOS2Table() {return os2;}
    
    public CmapTable getCmapTable() {return cmap;}
    
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

    public Glyph getGlyph(int i) {
        return (glyf.getDescription(i) != null)
            ? new Glyph(
                glyf.getDescription(i),
                hmtx.getLeftSideBearing(i),
                hmtx.getAdvanceWidth(i))
            : null;
    }

    public String getPath() {
        return path;
    }

    public TableDirectory getTableDirectory() {
        return tableDirectory;
    }

    /**
     * @param pathName Path to the TTF font file
     */
    protected void read(String pathName) {
        path = pathName;
        File f = new File(pathName);

        if (!f.exists()) {
            // TODO: Throw TTException
            return;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
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
            raf.close();

            // Initialize the tables that require it
            hmtx.init(hhea.getNumberOfHMetrics(), 
            		intGlyphCount - hhea.getNumberOfHMetrics());
            loca.init(intGlyphCount, head.getIndexToLocFormat() == 0);
            glyf.init(intGlyphCount, loca);
     
        } catch (IOException e) {e.printStackTrace();}
    }
    
    public static ChcFont create() {
        return new ChcFont();
    }
    
    /**
     * @param pathName Path to the TTF font file
     */
    public ChcFont create(String pathName) {
        ChcFont f = new ChcFont();
        f.read(pathName);
        return f;
    }
}