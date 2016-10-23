package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Lookup {

    // LookupFlag bit enumeration
    public static final int IGNORE_BASE_GLYPHS = 0x0002;
    public static final int IGNORE_BASE_LIGATURES = 0x0004;
    public static final int IGNORE_BASE_MARKS = 0x0008;
    public static final int MARK_ATTACHMENT_TYPE = 0xFF00;

    private int type;
    @SuppressWarnings("unused")
	private int flag;
    private int subTableCount;
    private int[] subTableOffsets;
    private LookupSubtable[] subTables;

    /** Creates new Lookup */
    public Lookup(LookupSubtableFactory factory, RandomAccessFile raf, int offset)
    throws IOException {
        raf.seek(offset);
        type = raf.readUnsignedShort();
        flag = raf.readUnsignedShort();
        subTableCount = raf.readUnsignedShort();
        subTableOffsets = new int[subTableCount];
        subTables = new LookupSubtable[subTableCount];
        for (int i = 0; i < subTableCount; i++) {
            subTableOffsets[i] = raf.readUnsignedShort();
        }
        for (int i = 0; i < subTableCount; i++) {
            subTables[i] = factory.read(type, raf, offset + subTableOffsets[i]);
        }
    }

    public int getType() {return type;}

    public int getSubtableCount() {return subTableCount;}

    public LookupSubtable getSubtable(int i) {return subTables[i];}

}
