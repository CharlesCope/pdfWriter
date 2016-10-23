package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public interface LookupSubtableFactory {
    public LookupSubtable read(int type, RandomAccessFile raf, int offset)
    throws IOException;
}
