package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FpgmTable extends Program implements Table {

    protected FpgmTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        readInstructions(raf, de.getLength());
    }

    public int getType() {return fpgm;}
}