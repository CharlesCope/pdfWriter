package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class Coverage {

    public abstract int getFormat();

    /**
     * @param glyphId The ID of the glyph to find.
     * @return The index of the glyph within the coverage, or -1 if the glyph
     * can't be found.
     */
    public abstract int findGlyph(int glyphId);
    
    protected static Coverage read(RandomAccessFile raf) throws IOException {
        Coverage c = null;
        int format = raf.readUnsignedShort();
        if (format == 1) {
            c = new CoverageFormat1(raf);
        } else if (format == 2) {
            c = new CoverageFormat2(raf);
        }
        return c;
    }

}
