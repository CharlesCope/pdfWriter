package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapFormat2 extends CmapFormat {
   
	private int[] subHeaderKeys = new int[256];
	private int maxSubHeaderIndex = 0;
    @SuppressWarnings("unused")
	private int[] subHeaders1;
    @SuppressWarnings("unused")
	private int[] subHeaders2;
    private int[] glyphIndexArray;

    public int[] getGlyphIndexArray() {return glyphIndexArray;}
	public void setGlyphIndexArray(int[] glyphIndexArray) {this.glyphIndexArray = glyphIndexArray;}
	// Started getting code from here.
	//https://svn.apache.org/repos/asf/pdfbox/trunk/fontbox/src/main/java/org/apache/fontbox/ttf/CmapSubtable.java
	
	protected CmapFormat2(RandomAccessFile raf) throws IOException {
        super(raf);
        format = 2;
        
        for (int i = 0; i < 256; i++){
            subHeaderKeys[i] = raf.readUnsignedShort();
            maxSubHeaderIndex = Math.max(maxSubHeaderIndex, subHeaderKeys[i] / 8);
        }
        // ---- Read all SubHeaders to avoid useless seek on DataSource
        SubHeader[] subHeaders = new SubHeader[maxSubHeaderIndex + 1];
        for (int i = 0; i <= maxSubHeaderIndex; ++i){
            int firstCode = raf.readUnsignedShort();
            int entryCount = raf.readUnsignedShort();
            short idDelta = raf.readShort();
            int idRangeOffset = raf.readUnsignedShort() - (maxSubHeaderIndex + 1 - i - 1) * 8 - 2;
            subHeaders[i] = new SubHeader(firstCode, entryCount, idDelta, idRangeOffset);
        }
        
        
    }

    public int getFirst() { return 0; }
    public int getLast()  { return 0; }
    
    public int mapCharCode(int charCode) {
		return charCode;
    }
    /**
     * 
     * Class used to manage CMap - Format 2.
     * 
     */
    private static class SubHeader{
        private final int firstCode;
        private final int entryCount;
        /**
         * used to compute the GlyphIndex : P = glyphIndexArray.SubArray[pos] GlyphIndex = P + idDelta % 65536.
         */
        private final short idDelta;
        /**
         * Number of bytes to skip to reach the firstCode in the glyphIndexArray.
         */
        private final int idRangeOffset;

        private SubHeader(int firstCodeValue, int entryCountValue, short idDeltaValue, int idRangeOffsetValue){
            firstCode = firstCodeValue;
            entryCount = entryCountValue;
            idDelta = idDeltaValue;
            idRangeOffset = idRangeOffsetValue;
        }

        @SuppressWarnings("unused")
        private int getFirstCode(){return firstCode;}
        @SuppressWarnings("unused")
        private int getEntryCount(){return entryCount;}
        @SuppressWarnings("unused")
        private short getIdDelta(){return idDelta;}
        @SuppressWarnings("unused")
        private int getIdRangeOffset(){return idRangeOffset;}
    }

}
