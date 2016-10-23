package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/** Simple Macintosh cmap table, mapping only the ASCII character set to glyphs. */
public class CmapFormat0 extends CmapFormat {

    private int[] glyphIdArray = new int[256];
    public int[] getGlyphIdArray() {
		return glyphIdArray;
	}

	public void setGlyphIdArray(int[] glyphIdArray) {
		this.glyphIdArray = glyphIdArray;
	}

	private int first, last;

    protected CmapFormat0(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 0;
        first = -1;
        for (int i = 0; i < 256; i++) {
            glyphIdArray[i] = raf.readUnsignedByte();
            if (glyphIdArray[i] > 0) {
                if (first == -1) first = i;
                last = i;
            }
        }
    }

    public int getFirst() { return first; }
    public int getLast()  { return last; }

    public int mapCharCode(int charCode) {
        if (0 <= charCode && charCode < 256) {
            return glyphIdArray[charCode];
        } else {return 0;}
    }
}
