package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

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

    protected HheaTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        version = raf.readInt();
        ascender = raf.readShort();
        descender = raf.readShort();
        lineGap = raf.readShort();
        advanceWidthMax = raf.readShort();
        minLeftSideBearing = raf.readShort();
        minRightSideBearing = raf.readShort();
        xMaxExtent = raf.readShort();
        caretSlopeRise = raf.readShort();
        caretSlopeRun = raf.readShort();
        for (int i = 0; i < 5; i++) {
            raf.readShort();
        }
        metricDataFormat = raf.readShort();
        numberOfHMetrics = raf.readUnsignedShort();
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

    public int getType() {return hhea;}

    public short getXMaxExtent() {return xMaxExtent;}
}