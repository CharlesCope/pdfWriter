package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class ScriptRecord {

    private int tag;
    private int offset;
    
    /** Creates new ScriptRecord */
    protected ScriptRecord(RandomAccessFile raf) throws IOException {
        tag = raf.readInt();
        offset = raf.readUnsignedShort();
    }

    public int getTag() {return tag;}
    
    public int getOffset() {return offset;}

}