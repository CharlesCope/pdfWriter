package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class KernSubtable {

    /** Creates new KernSubtable */
    protected KernSubtable() {}
    
    public abstract int getKerningPairCount();

    public abstract KerningPair getKerningPair(int i);

    public static KernSubtable read(RandomAccessFile raf) throws IOException {
        KernSubtable table = null;
        /* int version =*/ raf.readUnsignedShort();
        /* int length  =*/ raf.readUnsignedShort();
        int coverage   = raf.readUnsignedShort();
        int format     = coverage >> 8;
        
        switch (format) {
        case 0:
            table = new KernSubtableFormat0(raf);
            break;
        case 2:
            table = new KernSubtableFormat2(raf);
            break;
        default:
            break;
        }
        return table;
    }

}
