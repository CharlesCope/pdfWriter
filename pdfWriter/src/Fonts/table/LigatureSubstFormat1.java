package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LigatureSubstFormat1 extends LigatureSubst {

    private int coverageOffset;
    private int ligSetCount;
    private int[] ligatureSetOffsets;
    @SuppressWarnings("unused")
	private Coverage coverage;
    private LigatureSet[] ligatureSets;

    /** Creates new LigatureSubstFormat1 */
    protected LigatureSubstFormat1(RandomAccessFile raf,int offset) throws IOException {
        coverageOffset = raf.readUnsignedShort();
        ligSetCount = raf.readUnsignedShort();
        ligatureSetOffsets = new int[ligSetCount];
        ligatureSets = new LigatureSet[ligSetCount];
        for (int i = 0; i < ligSetCount; i++) {
            ligatureSetOffsets[i] = raf.readUnsignedShort();
        }
        raf.seek(offset + coverageOffset);
        coverage = Coverage.read(raf);
        for (int i = 0; i < ligSetCount; i++) {
            ligatureSets[i] = new LigatureSet(raf, offset + ligatureSetOffsets[i]);
        }
    }

    public int getFormat() {return 1;}

}
