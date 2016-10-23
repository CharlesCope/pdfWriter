package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class ClassDef {

    public abstract int getFormat();

    protected static ClassDef read(RandomAccessFile raf) throws IOException {
        ClassDef c = null;
        int format = raf.readUnsignedShort();
        if (format == 1) {
            c = new ClassDefFormat1(raf);
        } else if (format == 2) {
            c = new ClassDefFormat2(raf);
        }
        return c;
    }
}