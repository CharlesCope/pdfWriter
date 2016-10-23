package Fonts.table;

public interface GlyphDescription {
    public int getEndPtOfContours(int i);
    public byte getFlags(int i);
    public short getXCoordinate(int i);
    public short getYCoordinate(int i);
    public short getXMaximum();
    public short getXMinimum();
    public short getYMaximum();
    public short getYMinimum();
    public boolean isComposite();
    public int getPointCount();
    public int getContourCount();
    //  public int getComponentIndex(int c);
    //  public int getComponentCount();
}
