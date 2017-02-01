package Fonts.table;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;


public class CmapTable implements Table {
	
    public static final int PLATFORM_WINDOWS = 3;
    public static final int ENCODING_WIN_UNICODE_BMP = 1; // Unicode BMP (UCS-2)
    
	private int version;
    private int numTables;
    private CmapIndexEntry[] entries;
    private CmapFormat[] formats;

    protected CmapTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        long fp = raf.getFilePointer();
        version = raf.readUnsignedShort();
        numTables = raf.readUnsignedShort();
        entries = new CmapIndexEntry[numTables];
        formats = new CmapFormat[numTables];

        /** The 'cmap' encoding sub tables must be sorted first in ascending order by platform identifier
         * and then by platform-specific encoding identifier. 
         */
        
        // Get each of the index entries
        for (int i = 0; i < numTables; i++) {
            entries[i] = new CmapIndexEntry(raf);
        }

   
        for (int i = 0; i < numTables; i++) {
            raf.seek(fp + entries[i].getOffset());
            int format = raf.readUnsignedShort();
            formats[i] = CmapFormat.create(format, raf);
        }
        
    }

    public CmapFormat getCmapFormat(int platformId, int encodingId) {
    	// Find the requested format
        for (int i = 0; i < numTables; i++) {
            if (entries[i].getPlatformId() == platformId && entries[i].getEncodingId() == encodingId) {
                return formats[i];
            }
        }
        return null;
    }
    
   
    public int getTableVersionNumber(){return version;}
    public int getNumberOfEncodingTables(){return numTables;}
    public int getType() {return cmap;}
    
    public byte[] getSubSetBytes(SortedMap<Integer, Integer> uniToGID, SortedSet<Integer> glyphIds){
    
    	
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream out = new DataOutputStream(bos);

    	// cmap header
    	try {
    		out.writeShort( 0);

    		// version
    		out.writeShort( 1); // numberSubtables

    		// encoding record
    		out.writeShort( PLATFORM_WINDOWS); // platformID
    		out.writeShort( ENCODING_WIN_UNICODE_BMP); // platformSpecificID
    		out.writeInt((int) 4 * 2 + 4); // offset

    		// build Format 4 subtable (Unicode BMP)
    		Iterator<Entry<Integer, Integer>> it = uniToGID.entrySet().iterator();
    		Entry<Integer, Integer> lastChar = it.next();
    		Entry<Integer, Integer> prevChar = lastChar;
    		int lastGid = glyphIds.headSet(lastChar.getValue()).size();

    		// +1 because .notdef is missing in uniToGID
    		int[] startCode = new int[uniToGID.size()+1];
    		int[] endCode = new int[uniToGID.size()+1];
    		int[] idDelta = new int[uniToGID.size()+1];
    		int segCount = 0;
    		while(it.hasNext())
    		{
    			Entry<Integer, Integer> curChar2Gid = it.next();
    			int curGid = glyphIds.headSet(curChar2Gid.getValue()).size();

    			// todo: need format Format 12 for non-BMP
    			if (curChar2Gid.getKey() > 0xFFFF)
    			{
    				throw new UnsupportedOperationException("non-BMP Unicode character");
    			}

    			if (curChar2Gid.getKey() != prevChar.getKey()+1 ||
    					curGid - lastGid != curChar2Gid.getKey() - lastChar.getKey())
    			{
    				if (lastGid != 0)
    				{
    					// don't emit ranges, which map to GID 0, the
    					// undef glyph is emitted a the very last segment
    					startCode[segCount] = lastChar.getKey();
    					endCode[segCount] = prevChar.getKey();
    					idDelta[segCount] = lastGid - lastChar.getKey();
    					segCount++;
    				}
    				else if (!lastChar.getKey().equals(prevChar.getKey()))
    				{
    					// shorten ranges which start with GID 0 by one
    					startCode[segCount] = lastChar.getKey() + 1;
    					endCode[segCount] = prevChar.getKey();
    					idDelta[segCount] = lastGid - lastChar.getKey();
    					segCount++;
    				}
    				lastGid = curGid;
    				lastChar = curChar2Gid;
    			}
    			prevChar = curChar2Gid;
    		}

    		// trailing segment
    		startCode[segCount] = lastChar.getKey();
    		endCode[segCount] = prevChar.getKey();
    		idDelta[segCount] = lastGid -lastChar.getKey();
    		segCount++;

    		// GID 0
    		startCode[segCount] = 0xffff;
    		endCode[segCount] = 0xffff;
    		idDelta[segCount] = 1;
    		segCount++;

    		// write format 4 subtable
    		int searchRange = 2 * (int)Math.pow(2, Math.floor(log2(segCount)));
    		out.writeShort(4); // format
    		out.writeShort(8 * 2 + segCount * 4*2); // length
    		out.writeShort(0); // language
    		out.writeShort(segCount * 2); // segCountX2
    		out.writeShort(searchRange); // searchRange
    		out.writeShort(log2(searchRange / 2)); // entrySelector
    		out.writeShort(2 * segCount - searchRange); // rangeShift

    		// endCode[segCount]
    		for (int i = 0; i < segCount; i++)
    		{
    			out.writeShort(endCode[i]);
    		}

    		// reservedPad
    		out.writeShort( 0);

    		// startCode[segCount]
    		for (int i = 0; i < segCount; i++)
    		{
    			out.writeShort(startCode[i]);
    		}

    		// idDelta[segCount]
    		for (int i = 0; i < segCount; i++)
    		{
    			out.writeShort(idDelta[i]);
    		}

    		for (int i = 0; i < segCount; i++)
    		{
    			out.writeShort(0);
    		}
    	} catch (IOException e) {e.printStackTrace();}
    	return bos.toByteArray();
    }

    private int log2(int num) {return (int)Math.round(Math.log(num) / Math.log(2));}

    public String toString() {
    	StringBuffer sb = new StringBuffer().append("cmap\n");
    	// Get each of the index entries
    	for (int i = 0; i < numTables; i++) {
    		sb.append("\t").append(entries[i].toString()).append("\n");
    	}

    	// Get each of the tables
    	for (int i = 0; i < numTables; i++) {
    		sb.append("\t").append(formats[i].toString()).append("\n");
    	}
    	return sb.toString();
    }
}