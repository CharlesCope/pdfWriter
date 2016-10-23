package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;
/** Can not make object out of this class it only a template for a concrete class.**/
public abstract class CmapFormat {

    protected int format;
    protected int length;
    protected int version;

    protected CmapFormat(RandomAccessFile raf) throws IOException {
    	//This is the length in bytes of the table.
    	length = raf.readUnsignedShort();
        /**
    	 * It was originally suggested that a version number of 0 is used to indicate
    	 * that only encoding sub tables of types 0 through 6 are present in the 'cmap' table.
    	 * This suggestion is now dropped. All 'cmap' tables should set the version number to 0.  
    	 */
        version = raf.readUnsignedShort();
    }

    protected static CmapFormat create(int format, RandomAccessFile raf)
    throws IOException {
        switch(format) {
            case 0:
            return new CmapFormat0(raf);
            case 2:
            return new CmapFormat2(raf);
            case 4:
            	System.out.println("Called Here");
            return new CmapFormat4(raf);
            case 6:
            return new CmapFormat6(raf);
        }
        return null;
    }

    public int getFormat() {return format;}

    public int getLength() {return length;}

    public int getVersion() {return version;}

    public abstract int mapCharCode(int charCode);

    public abstract int getFirst();
    public abstract int getLast();

    public String toString() {
        return new StringBuffer()
        .append("format: ")
        .append(format)
        .append(", length: ")
        .append(length)
        .append(", version: ")
        .append(version).toString();
    }
}