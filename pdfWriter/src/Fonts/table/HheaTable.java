package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.SortedSet;

public class HheaTable implements Table {

    @SuppressWarnings("unused")
	private int version;
    private short ascender;
    private short descender;
    private short lineGap;
    private short advanceWidthMax;
    private short minLeftSideBearing;
    private short minRightSideBearing;
    private short xMaxExtent;
    private short caretSlopeRise;
    private short caretSlopeRun;
    private short metricDataFormat;
    private int   numberOfHMetrics;
    private int dataLength;
    private byte[] byteTable;
    
    protected HheaTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        dataLength = de.getLength();
        byteTable = new byte[dataLength];
        raf.read(byteTable, 0, dataLength);
        raf.seek(de.getOffset());
        
        version = raf.readInt();//4
        ascender = raf.readShort();//2
        descender = raf.readShort();//2
        lineGap = raf.readShort();//2
        advanceWidthMax = raf.readShort();//2
        minLeftSideBearing = raf.readShort();//2
        minRightSideBearing = raf.readShort();//2
        xMaxExtent = raf.readShort();//2
        caretSlopeRise = raf.readShort();//2
        caretSlopeRun = raf.readShort();//2
        for (int i = 0; i < 5; i++) {
            raf.readShort();//2 x 5
        }
        metricDataFormat = raf.readShort();//2
        numberOfHMetrics = raf.readUnsignedShort();//2
    }

    public short getAdvanceWidthMax() {return advanceWidthMax;}

    public short getAscender() {return ascender;}

    public short getCaretSlopeRise() {return caretSlopeRise;}

    public short getCaretSlopeRun() {return caretSlopeRun;}

    public short getDescender() {return descender;}

    public short getLineGap() {return lineGap;}

    public short getMetricDataFormat() {return metricDataFormat;}

    public short getMinLeftSideBearing() {return minLeftSideBearing;}

    public short getMinRightSideBearing() {return minRightSideBearing;}

    public int getNumberOfHMetrics() {return numberOfHMetrics;}
    public void setNumberOfHMetrics(int intSubsetNumberOfHMetrics) {
    	numberOfHMetrics = intSubsetNumberOfHMetrics;}

    public int getType() {return hhea;}

    public short getXMaxExtent() {return xMaxExtent;}
    
    public byte[] getAllBytes(){return byteTable;}
    
    public byte[] getSubSetBytes(SortedSet <Integer> ssGlyphIds){
 		byte[] byteSubTable = Arrays.copyOf(byteTable, dataLength);
 		
 		int hmetrics = ssGlyphIds.subSet(0, numberOfHMetrics).size();
 		if (ssGlyphIds.last() >= numberOfHMetrics && !ssGlyphIds.contains(numberOfHMetrics -1)) {
 			++hmetrics;
 		}

 		// For subset we need to be able to adjust the number of glyphs.
 		byteSubTable[34] = (byte) ((hmetrics >>> 8) & 0xff);
 		byteSubTable[35] = (byte) (hmetrics & 0xff);
 		return byteSubTable;
    }
}