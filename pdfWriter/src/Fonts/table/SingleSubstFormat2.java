package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SingleSubstFormat2 extends SingleSubst {

    private int coverageOffset;
    private int glyphCount;
    private int[] substitutes;
    private Coverage coverage;

    /** Creates new SingleSubstFormat2 */
    protected SingleSubstFormat2(RandomAccessFile raf, int offset) throws IOException {
        coverageOffset = raf.readUnsignedShort();
        glyphCount = raf.readUnsignedShort();
        substitutes = new int[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            substitutes[i] = raf.readUnsignedShort();
        }
        raf.seek(offset + coverageOffset);
        coverage = Coverage.read(raf);
    }

    public int getFormat() {
        return 2;
    }

    public int substitute(int glyphId) {
        int i = coverage.findGlyph(glyphId);
        if (i > -1) {
            return substitutes[i];
        }
        return glyphId;
    }

}

