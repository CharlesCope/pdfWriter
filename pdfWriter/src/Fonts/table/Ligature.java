package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Ligature {

    private int ligGlyph;
    private int compCount;
    private int[] components;

    /** Creates new Ligature */
    public Ligature(RandomAccessFile raf) throws IOException {
        ligGlyph = raf.readUnsignedShort();
        compCount = raf.readUnsignedShort();
        components = new int[compCount - 1];
        for (int i = 0; i < compCount - 1; i++) {
            components[i] = raf.readUnsignedShort();
        }
    }
    
    public int getGlyphCount() {return compCount;}
    
    public int getGlyphId(int i) {return (i == 0) ? ligGlyph : components[i-1];}

}
