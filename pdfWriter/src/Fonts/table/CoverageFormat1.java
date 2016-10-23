package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CoverageFormat1 extends Coverage {

    private int glyphCount;
    private int[] glyphIds;

    /** Creates new CoverageFormat1 */
    protected CoverageFormat1(RandomAccessFile raf) throws IOException {
        glyphCount = raf.readUnsignedShort();
        glyphIds = new int[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            glyphIds[i] = raf.readUnsignedShort();
        }
    }

    public int getFormat() {return 1;}

    public int findGlyph(int glyphId) {
        for (int i = 0; i < glyphCount; i++) {
            if (glyphIds[i] == glyphId) {return i;}
        }
        return -1;
    }

}
