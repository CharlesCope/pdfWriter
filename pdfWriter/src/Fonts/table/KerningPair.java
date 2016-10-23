package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class KerningPair {

    private int left;
    private int right;
    private short value;

    /** Creates new KerningPair */
    protected KerningPair(RandomAccessFile raf) throws IOException {
        left = raf.readUnsignedShort();
        right = raf.readUnsignedShort();
        value = raf.readShort();
    }

    public int getLeft() {return left;}

    public int getRight() {return right;}

    public short getValue() {return value;}

}