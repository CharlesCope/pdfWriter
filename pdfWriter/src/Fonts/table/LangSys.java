package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class LangSys {

    @SuppressWarnings("unused")
	private int lookupOrder;
    @SuppressWarnings("unused")
	private int reqFeatureIndex;
    private int featureCount;
    private int[] featureIndex;
    
    /** Creates new LangSys */
    protected LangSys(RandomAccessFile raf) throws IOException {
        lookupOrder = raf.readUnsignedShort();
        reqFeatureIndex = raf.readUnsignedShort();
        featureCount = raf.readUnsignedShort();
        featureIndex = new int[featureCount];
        for (int i = 0; i < featureCount; i++) {
            featureIndex[i] = raf.readUnsignedShort();
        }
    }
    
    protected boolean isFeatureIndexed(int n) {
        for (int i = 0; i < featureCount; i++) {
            if (featureIndex[i] == n) {
                return true;
            }
        }
        return false;
    }

}
