package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CoverageFormat2 extends Coverage {

    private int rangeCount;
    private RangeRecord[] rangeRecords;

    /** Creates new CoverageFormat2 */
    protected CoverageFormat2(RandomAccessFile raf) throws IOException {
        rangeCount = raf.readUnsignedShort();
        rangeRecords = new RangeRecord[rangeCount];
        for (int i = 0; i < rangeCount; i++) {
            rangeRecords[i] = new RangeRecord(raf);
        }
    }

    public int getFormat() {return 2;}

    public int findGlyph(int glyphId) {
        for (int i = 0; i < rangeCount; i++) {
            int n = rangeRecords[i].getCoverageIndex(glyphId);
            if (n > -1) {return n;}
        }
        return -1;
    }

}
