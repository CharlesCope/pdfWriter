package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class PrepTable extends Program implements Table {

    public PrepTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        readInstructions(raf, de.getLength());
    }

    public int getType() {return prep;}
}