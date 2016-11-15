package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class CmapTable implements Table {

    
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

    public CmapFormat getCmapFormat(short platformId, short encodingId) {
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