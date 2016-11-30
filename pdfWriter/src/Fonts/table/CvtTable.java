package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CvtTable implements Table {

    private short[] values;
    private byte[] byteTable;

    protected CvtTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());
        
        int len = de.getLength() / 2;
        values = new short[len];
        for (int i = 0; i < len; i++) {
            values[i] = raf.readShort();
        }
    }

    public int getType() {return cvt;}

    public short[] getValues() {return values;}
    
    public byte[] getAllBytes(){return byteTable;}
}
