package Fonts.table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class LocaTable implements Table {

    private byte[] buf = null;
    private int[] offsets = null;
    private short factor = 0;

    protected LocaTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        buf = new byte[de.getLength()];
        raf.read(buf);
    }

    public void init(int numGlyphs, boolean shortEntries) {
        if (buf == null) {
            return;
        }
        offsets = new int[numGlyphs + 1];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        if (shortEntries) {
            factor = 2;
            for (int i = 0; i <= numGlyphs; i++) {
                offsets[i] = (bais.read()<<8 | bais.read());
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
    
    public int getOffset(int i) {
        if (offsets == null) {return 0;}
        return offsets[i] * factor;
    }

    public int getType() {return loca;}
}
