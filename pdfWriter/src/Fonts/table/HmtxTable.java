package Fonts.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.SortedSet;



public class HmtxTable implements Table {
	private int[] advanceWidth;
	private int[] SubSetAdvanceWidth;
    private byte[] buf = null;
    private int[] hMetrics = null;
    private short[] leftSideBearing = null;
    private byte[] byteTable;
    private int fileOffset;
    private int numHMetrics;
    protected HmtxTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
    	fileOffset = de.getOffset();
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());
        
        
        
        buf = new byte[de.getLength()];
        raf.read(buf);
/*
        TableMaxp t_maxp = (TableMaxp) td.getEntryByTag(maxp).getTable();
        TableHhea t_hhea = (TableHhea) td.getEntryByTag(hhea).getTable();
        int lsbCount = t_maxp.getNumGlyphs() - t_hhea.getNumberOfHMetrics();
        hMetrics = new int[t_hhea.getNumberOfHMetrics()];
        for (int i = 0; i < t_hhea.getNumberOfHMetrics(); i++) {
            hMetrics[i] = raf.readInt();
        }
        if (lsbCount > 0) {
            leftSideBearing = new short[lsbCount];
            for (int i = 0; i < lsbCount; i++) {
                leftSideBearing[i] = raf.readShort();
            }
        }
*/
    }

    public void init(int numberOfHMetrics, int lsbCount) {
        if (buf == null) {return;}
        numHMetrics = numberOfHMetrics;
        hMetrics = new int[numberOfHMetrics];
        advanceWidth = new int[ numberOfHMetrics ];
        short[] leftSideBearingDummy = new short[numberOfHMetrics];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
      
        for (int i = 0; i < numberOfHMetrics; i++) {
        	advanceWidth[i] = (bais.read()<<8 | bais.read());
        	leftSideBearingDummy[i] = (short)(bais.read()<<8 | bais.read());	
        }
     
        bais.reset();
        
        
        for (int i = 0; i < numberOfHMetrics; i++) {
            hMetrics[i] = (bais.read()<<24 | bais.read()<<16 | 
                           bais.read()<< 8 | bais.read());
        }
        if (lsbCount > 0) {
            leftSideBearing = new short[lsbCount];
            for (int i = 0; i < lsbCount; i++) {
                leftSideBearing[i] = (short)(bais.read()<<8 | bais.read());
            }
        }
        buf = null;
    }
    public int getSubSetAdvanceWidth(int gid){return SubSetAdvanceWidth[gid];}
    
    public int getAdvanceWidth(int gid) {
    	  if (gid < numHMetrics) {
              return advanceWidth[gid];
          }
          else {
              // monospaced fonts may not have a width for every glyph
              // the last one is for subsequent glyphs
              return advanceWidth[advanceWidth.length -1];
          }
//        if (hMetrics == null) {return 0;}
//        if (i < hMetrics.length) {return hMetrics[i] >> 16;}
//        else {return hMetrics[hMetrics.length - 1] >> 16;}
    }

    public short getLeftSideBearing(int i) {
        if (hMetrics == null) {return 0;}
        if (i < hMetrics.length) {return (short)(hMetrics[i] & 0xffff);}
        else {return leftSideBearing[i - hMetrics.length];}
    }

    public int getType() {return hmtx;}
    
    public int getOffset(){return fileOffset;}
    
    public byte[] getAllBytes(){return byteTable;}
    
    public void setSubSetAdvance(byte[] data,int NumberOfHMetrics){
		SubSetAdvanceWidth = new int[NumberOfHMetrics ];
		short[] leftSideBearingDummy = new short[NumberOfHMetrics];
		 
		ByteArrayInputStream bInput = new ByteArrayInputStream(data);

		for (int i = 0; i < NumberOfHMetrics; i++) {
			SubSetAdvanceWidth[i] = (bInput.read()<<8 | bInput.read());
			leftSideBearingDummy[i] = (short)(bInput.read()<<8 | bInput.read());	
		}
		bInput.reset(); 
    }
    
    public byte[] getSubSetBytes(byte[] data,SortedSet <Integer> ssGlyphIds, int NumberOfHMetrics){

    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	InputStream is = new ByteArrayInputStream(data);

    	// more info: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6hmtx.html
    	int lastgid = NumberOfHMetrics - 1;
    	// true if lastgid is not in the set: we'll need its width (but not its left side bearing) later
    	boolean needLastGidWidth = false;

    	if (ssGlyphIds.last() > lastgid && !ssGlyphIds.contains(lastgid)){needLastGidWidth = true;}

    	try {
    		is.skip(fileOffset);
    		long lastOffset = 0;
    		for (Integer glyphId : ssGlyphIds){
    			
    			// offset in original file
    			long offset;
    			if (glyphId <= lastgid){
    				// copy width and lsb
    				offset = glyphId * 4;
    				lastOffset = copyBytes(is, bos, offset, lastOffset, 4);
    			}
    			else{
    				if (needLastGidWidth){
    					// one time only: copy width from lastgid, whose width applies
    					// to all later glyphs
    					needLastGidWidth = false;
    					offset = lastgid * 4;
    					lastOffset = copyBytes(is, bos, offset, lastOffset, 2);

    					// then go on with lsb from actual glyph (lsb are individual even in monotype fonts)
    				}

    				// copy lsb only, as we are beyond numOfHMetrics
    				offset = NumberOfHMetrics * 4 + (glyphId - NumberOfHMetrics) * 2;
    				lastOffset = copyBytes(is, bos, offset, lastOffset, 2);
    			}
    		}

    		

    	} 
    	catch (IOException e) {e.printStackTrace();}
    	finally{
    		try {
    			is.close();
    		} catch (IOException e) {e.printStackTrace();}
    	}
    	return bos.toByteArray();
    }
    private long copyBytes(InputStream is, OutputStream os, long newOffset, long lastOffset, int count){
    	// skip over from last original offset
    	long nskip = newOffset - lastOffset;
    	try {
    		if (nskip != is.skip(nskip)){
    			throw new EOFException("Unexpected EOF exception parsing glyphId of hmtx table.");
    		}

    		byte[] buf = new byte[count];
    		if (count != is.read(buf, 0, count)){
    			throw new EOFException("Unexpected EOF exception parsing glyphId of hmtx table.");
    		}
    		os.write(buf, 0, count);

    	} catch (IOException e) {e.printStackTrace();}

    	return newOffset + count; 
    }
}