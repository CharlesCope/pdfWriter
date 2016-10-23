package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class LookupList {

    private int lookupCount;
    private int[] lookupOffsets;
    private Lookup[] lookups;

    /** Creates new LookupList */
    public LookupList(RandomAccessFile raf, int offset, LookupSubtableFactory factory)
    throws IOException {
        raf.seek(offset);
        lookupCount = raf.readUnsignedShort();
        lookupOffsets = new int[lookupCount];
        lookups = new Lookup[lookupCount];
        for (int i = 0; i < lookupCount; i++) {
            lookupOffsets[i] = raf.readUnsignedShort();
        }
        for (int i = 0; i < lookupCount; i++) {
            lookups[i] = new Lookup(factory, raf, offset + lookupOffsets[i]);
        }
    }

    public Lookup getLookup(Feature feature, int index) {
        if (feature.getLookupCount() > index) {
            int i = feature.getLookupListIndex(index);
            return lookups[i];
        }
        return null;
    }

}

