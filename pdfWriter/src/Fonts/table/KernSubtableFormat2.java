package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class KernSubtableFormat2 extends KernSubtable {

    @SuppressWarnings("unused")
	private int rowWidth;
    @SuppressWarnings("unused")
	private int leftClassTable;
    @SuppressWarnings("unused")
	private int rightClassTable;
    @SuppressWarnings("unused")
	private int array;

    /** Creates new KernSubtableFormat2 */
    protected KernSubtableFormat2(RandomAccessFile raf) throws IOException {
        rowWidth = raf.readUnsignedShort();
        leftClassTable = raf.readUnsignedShort();
        rightClassTable = raf.readUnsignedShort();
        array = raf.readUnsignedShort();
    }

    public int getKerningPairCount() {return 0;}

    public KerningPair getKerningPair(int i) {return null;}

}
