package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LigatureSet {

    private int ligatureCount;
    private int[] ligatureOffsets;
    private Ligature[] ligatures;

    /** Creates new LigatureSet */
    public LigatureSet(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        ligatureCount = raf.readUnsignedShort();
        ligatureOffsets = new int[ligatureCount];
        ligatures = new Ligature[ligatureCount];
        for (int i = 0; i < ligatureCount; i++) {
            ligatureOffsets[i] = raf.readUnsignedShort();
        }
        for (int i = 0; i < ligatureCount; i++) {
            raf.seek(offset + ligatureOffsets[i]);
            ligatures[i] = new Ligature(raf);
        }
    }

}
