package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class LigatureSubst extends LookupSubtable {

    public static LigatureSubst read(RandomAccessFile raf, int offset) throws IOException {
        LigatureSubst ls = null;
        raf.seek(offset);
        int format = raf.readUnsignedShort();
        if (format == 1) {ls = new LigatureSubstFormat1(raf, offset);}
        return ls;
    }

}
