package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class SingleSubstFormat1 extends SingleSubst {

    private int coverageOffset;
    private short deltaGlyphID;
    private Coverage coverage;

    /** Creates new SingleSubstFormat1 */
    protected SingleSubstFormat1(RandomAccessFile raf, int offset) throws IOException {
        coverageOffset = raf.readUnsignedShort();
        deltaGlyphID = raf.readShort();
        raf.seek(offset + coverageOffset);
        coverage = Coverage.read(raf);
    }

    public int getFormat() {return 1;}

    public int substitute(int glyphId) {
        int i = coverage.findGlyph(glyphId);
        if (i > -1) {return glyphId + deltaGlyphID;}
        return glyphId;
    }

}

