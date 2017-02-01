package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class MaxpTable implements Table {

    @SuppressWarnings("unused")
	private int versionNumber;
    private int numGlyphs;
    private int maxPoints;
    private int maxContours;
    private int maxCompositePoints;
    private int maxCompositeContours;
    private int maxZones;
    private int maxTwilightPoints;
    private int maxStorage;
    private int maxFunctionDefs;
    private int maxInstructionDefs;
    private int maxStackElements;
    private int maxSizeOfInstructions;
    private int maxComponentElements;
    private int maxComponentDepth;
    private byte[] byteTable;
    
    protected MaxpTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
        raf.seek(de.getOffset());
        versionNumber = raf.readInt();
        numGlyphs = raf.readUnsignedShort();
        maxPoints = raf.readUnsignedShort();
        maxContours = raf.readUnsignedShort();
        maxCompositePoints = raf.readUnsignedShort();
        maxCompositeContours = raf.readUnsignedShort();
        maxZones = raf.readUnsignedShort();
        maxTwilightPoints = raf.readUnsignedShort();
        maxStorage = raf.readUnsignedShort();
        maxFunctionDefs = raf.readUnsignedShort();
        maxInstructionDefs = raf.readUnsignedShort();
        maxStackElements = raf.readUnsignedShort();
        maxSizeOfInstructions = raf.readUnsignedShort();
        maxComponentElements = raf.readUnsignedShort();
        maxComponentDepth = raf.readUnsignedShort();
    }

    public int getMaxComponentDepth() {return maxComponentDepth;}

    public int getMaxComponentElements() {return maxComponentElements;}

    public int getMaxCompositeContours() {return maxCompositeContours;}

    public int getMaxCompositePoints() {return maxCompositePoints;}

    public int getMaxContours() {return maxContours;}

    public int getMaxFunctionDefs() {return maxFunctionDefs;}

    public int getMaxInstructionDefs() {return maxInstructionDefs;}

    public int getMaxPoints() {return maxPoints;}

    public int getMaxSizeOfInstructions() {return maxSizeOfInstructions;}

    public int getMaxStackElements() {return maxStackElements;}

    public int getMaxStorage() {return maxStorage;}

    public int getMaxTwilightPoints() {return maxTwilightPoints;}

    public int getMaxZones() {return maxZones;}

    public int getNumGlyphs() {return numGlyphs;}
    public void setNumGlyphs(int subSetNumGlyphs) {	numGlyphs = subSetNumGlyphs;}

    public int getType() {return maxp;}
    
    public byte[] getAllBytes(){
    	// For subset we need to be able to adjust the number of glyphs.
    	byteTable[4] = (byte) ((numGlyphs >>> 8) & 0xff);
    	byteTable[5] = (byte) (numGlyphs & 0xff);
    	
    	return byteTable;}
}