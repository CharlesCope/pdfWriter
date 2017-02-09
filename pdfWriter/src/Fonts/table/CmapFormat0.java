package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/** Simple Macintosh cmap table, mapping only the ASCII character set to glyphs. */
public class CmapFormat0 extends CmapFormat {
	private int[] glyphIdToCharacterCode;
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
       glyphIdToCharacterCode = newGlyphIdToCharacterCode(256);
        format = 0;
        first = -1;
        for (int i = 0; i < 256; i++) {
        	glyphIdArray[i] = raf.readUnsignedByte();
        	int glyphIndex = (glyphIdArray[i] + 256) % 256;
            glyphIdToCharacterCode[glyphIndex] = i;
            
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
    
    @Override
    public Integer getCharacterCode(int gid) {
    	if (gid < 0 || gid >= glyphIdToCharacterCode.length) {return null;}
    	// workaround for the fact that glyphIdToCharacterCode doesn't distinguish between
    	// missing character codes and code 0.
    	int code = glyphIdToCharacterCode[gid];
    	if (code == -1){return null;}
    	return code;
    }
    private int[] newGlyphIdToCharacterCode(int size) {
        int[] gidToCode = new int[size];
        Arrays.fill(gidToCode, -1);
        return gidToCode;
    }
	
}
