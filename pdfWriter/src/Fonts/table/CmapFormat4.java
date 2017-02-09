package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import Fonts.PdfFont;

public class CmapFormat4 extends CmapFormat {

    public  int language;
    private int segCountX2;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;
    private int segCount;
    private int first, last;
    private int[] endCode;
    private int[] startCode;
    private int[] idDelta;
    private int[] idRangeOffset;
    private  HashMap<Integer, Integer> tmpGlyphToChar;
    private  HashMap<Integer, Integer> characterCodeToGlyphId;
    private int[] glyphIdToCharacterCode;

    protected CmapFormat4(RandomAccessFile raf) throws IOException {
    	super(raf);
    	format = 4;
        segCountX2 = raf.readUnsignedShort();
        segCount = segCountX2 / 2;
        endCode = new int[segCount];
        startCode = new int[segCount];
        idDelta = new int[segCount];
        idRangeOffset = new int[segCount];
        searchRange = raf.readUnsignedShort();
        entrySelector = raf.readUnsignedShort();
        rangeShift = raf.readUnsignedShort();
        last = -1;
       
        // Load up the array's
        // Ending character code for each segment, last = 0xFFFF.
        for (int i = 0; i < segCount; i++) {endCode[i] = raf.readUnsignedShort();
            if (endCode[i] > last) last = endCode[i];}
        
        // reserve Pad This value should be zero
        raf.readUnsignedShort(); 
       
        // Starting character code for each segment
        for (int i = 0; i < segCount; i++) {startCode[i] = raf.readUnsignedShort();
            if ((i==0 ) || (startCode[i] < first)) first = startCode[i];}
        // Delta for all character codes in segment
        for (int i = 0; i < segCount; i++) {idDelta[i] = raf.readUnsignedShort();}
        // Offset in bytes to glyph indexArray, or 0
        for (int i = 0; i < segCount; i++) {idRangeOffset[i] = raf.readUnsignedShort();}

        tmpGlyphToChar = new HashMap<Integer, Integer>(PdfFont.intGlyphCount);
        characterCodeToGlyphId = new HashMap<Integer, Integer>(PdfFont.intGlyphCount);
       
        int maxGlyphId = 0;

        long currentPosition = raf.getFilePointer();
        
        for (int intSegCounter = 0; intSegCounter < segCount; intSegCounter++){
            
        	int start = startCode[intSegCounter];
            int end = endCode[intSegCounter];
            int delta = idDelta[intSegCounter];
            int rangeOffset = idRangeOffset[intSegCounter];
            
            if (start != 65535 && end != 65535){
                for (int intCounter = start; intCounter <= end; intCounter++){
                    if (rangeOffset == 0){
                        int glyphid = (intCounter + delta) & 0xFFFF;
                        maxGlyphId = Math.max(glyphid, maxGlyphId);
                        tmpGlyphToChar.put(glyphid, intCounter);
                        characterCodeToGlyphId.put(intCounter, glyphid);
                    }
                    else{
                        long glyphOffset = currentPosition + ((rangeOffset / 2) +
                                (intCounter - start) + (intSegCounter - segCount)) * 2;
                        
                        raf.seek(glyphOffset);
                        int glyphIndex = raf.readUnsignedShort();
                        if (glyphIndex != 0){
                            glyphIndex = (glyphIndex + delta) & 0xFFFF;
                            if (!tmpGlyphToChar.containsKey(glyphIndex)){
                                maxGlyphId = Math.max(glyphIndex, maxGlyphId);
                                tmpGlyphToChar.put(glyphIndex, intCounter);
                                characterCodeToGlyphId.put(intCounter, glyphIndex);
                            }
                        }
                    }
                }
            }// End start end Loop
        }// End segCount Loop
        
        buildGlyphIdToCharacterCodeLookup(tmpGlyphToChar, maxGlyphId);
    }

    public int getFirst() { return first; }
    public int getLast()  { return last; }
    public int getGlyphCount(){return glyphIdToCharacterCode.length;}
    public int[] getGlyphIdArray() {return glyphIdToCharacterCode;}
    
    public int mapCharCode(int charCode) {
    	 if (characterCodeToGlyphId.containsKey(charCode)) {
    		 return characterCodeToGlyphId.get(charCode); } 
    	 else {return 0 ;
    	 }
    }

    private void buildGlyphIdToCharacterCodeLookup(HashMap<Integer, Integer> tmpGlyphToChar, int maxGlyphId){
        glyphIdToCharacterCode = newGlyphIdToCharacterCode(maxGlyphId + 1);
        for (Entry<Integer, Integer> entry : tmpGlyphToChar.entrySet())
        {
            // link the glyphId with the right character code
            glyphIdToCharacterCode[entry.getKey()] = entry.getValue();
        }
    }
    /**
     * Workaround for the fact that glyphIdToCharacterCode doesn't distinguish between
     * missing character codes and code 0.
     */
    private int[] newGlyphIdToCharacterCode(int size){
        int[] gidToCode = new int[size];
        Arrays.fill(gidToCode, -1);
        return gidToCode;
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
    public String toString() {
        return new StringBuffer()
        .append(super.toString())
        .append(", segCountX2: ")
        .append(segCountX2)
        .append(", searchRange: ")
        .append(searchRange)
        .append(", entrySelector: ")
        .append(entrySelector)
        .append(", rangeShift: ")
        .append(rangeShift)
        .append(", endCode: ")
        .append(endCode)
        .append(", startCode: ")
        .append(endCode)
        .append(", idDelta: ")
        .append(idDelta)
        .append(", idRangeOffset: ")
        .append(idRangeOffset).toString();
    }


}
