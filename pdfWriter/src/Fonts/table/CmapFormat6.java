package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class CmapFormat6 extends CmapFormat {

    @SuppressWarnings("unused")
	private int format;
    @SuppressWarnings("unused")
	private int length;
    @SuppressWarnings("unused")
	private int version;
    @SuppressWarnings("unused")
	private int firstCode;
    @SuppressWarnings("unused")
	private int entryCount;
    private int[] glyphIdArray;

    public int[] getGlyphIdArray() {return glyphIdArray;}
	public void setGlyphIdArray(int[] glyphIdArray) {this.glyphIdArray = glyphIdArray;}

	protected CmapFormat6(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 6;
    }

    public int getFirst() { return 0; }
    public int getLast()  { return 0; }
    
    public int mapCharCode(int charCode) {
        return 0;
    }
}
