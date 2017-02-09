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
    private int[] glyphIdToCharacterCode;
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
    @Override
  	public Integer getCharacterCode(int gid) {
      	  if (gid < 0 || gid >= glyphIdToCharacterCode.length) {return null;}
            // workaround for the fact that glyphIdToCharacterCode doesn't distinguish between
            // missing character codes and code 0.
            int code = glyphIdToCharacterCode[gid];
            if (code == -1){return null;}
            return code;
  	}
}
