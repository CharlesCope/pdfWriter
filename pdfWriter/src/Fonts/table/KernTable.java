package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class KernTable implements Table {
    
    @SuppressWarnings("unused")
	private int version;
    private int nTables;
    private KernSubtable[] tables;

    /** Creates new KernTable */
    protected KernTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        version = raf.readUnsignedShort();
        nTables = raf.readUnsignedShort();
        tables = new KernSubtable[nTables];
        for (int i = 0; i < nTables; i++) {
            tables[i] = KernSubtable.read(raf);
        }
    }

    public int getSubtableCount() {return nTables;}
    
    public KernSubtable getSubtable(int i) {return tables[i];}

    /** Get the table type, as a table directory value.
     * @return The table type
     */
    public int getType() {return kern;}

}
