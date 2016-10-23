package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Coverage Index (GlyphID) = StartCoverageIndex + GlyphID - Start GlyphID
 *
 */
public class RangeRecord {

    private int start;
    private int end;
    private int startCoverageIndex;

    /** Creates new RangeRecord */
    public RangeRecord(RandomAccessFile raf) throws IOException {
        start = raf.readUnsignedShort();
        end = raf.readUnsignedShort();
        startCoverageIndex = raf.readUnsignedShort();
    }

    public boolean isInRange(int glyphId) {
        return (start <= glyphId && glyphId <= end);
    }
    
    public int getCoverageIndex(int glyphId) {
        if (isInRange(glyphId)) {
            return startCoverageIndex + glyphId - start;
        }
        return -1;
    }

}

