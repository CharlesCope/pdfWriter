package Fonts.table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class LocaTable implements Table {

    private byte[] buf = null;
    private long[] offsets = null;
    private short factor = 0;
    private byte[] byteTable ;
    
    protected LocaTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());
        
        buf = new byte[de.getLength()];
        raf.read(buf);
    }

    public void init(int numGlyphs, boolean shortEntries) {
        if (buf == null) {
            return;
        }
        offsets = new long[numGlyphs + 1];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        if (shortEntries) {
            factor = 2;
            for (int i = 0; i <= numGlyphs; i++) {
                offsets[i] = (bais.read()<<8 | bais.read()) * factor;
            }
        } else {
            factor = 1;
            for (int i = 0; i <= numGlyphs; i++) {
                offsets[i] = (bais.read()<<24 | bais.read()<<16 | 
                              bais.read()<< 8 | bais.read());
            }
        }
        buf = null;
    }
    
    public long getOffset(int i) {
        if (offsets == null) {return 0;}
        return offsets[i];
    }
    public long[] getOffsets() {return offsets;}
    
    public int getType() {return loca;}
    
    public byte[] getAllBytes(){return byteTable;}
}
