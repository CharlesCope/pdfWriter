package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FpgmTable extends Program implements Table {
	private byte[] byteTable;
    protected FpgmTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());
        readInstructions(raf, de.getLength());
    }

    public int getType() {return fpgm;}
    public byte[] getAllBytes(){return byteTable;}
}