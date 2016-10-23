package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class NameTable implements Table {

    @SuppressWarnings("unused")
	private short formatSelector;
    private short numberOfNameRecords;
    private short stringStorageOffset;
    private NameRecord[] records;

    protected NameTable(DirectoryEntry de,RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        formatSelector = raf.readShort();
        numberOfNameRecords = raf.readShort();
        stringStorageOffset = raf.readShort();
        records = new NameRecord[numberOfNameRecords];
        
        // Load the records, which contain the encoding information and string offsets
        for (int i = 0; i < numberOfNameRecords; i++) {
            records[i] = new NameRecord(raf);
        }
        
        // Now load the strings
        for (int i = 0; i < numberOfNameRecords; i++) {
            records[i].loadString(raf, de.getOffset() + stringStorageOffset);
        }
    }

    public String getRecord(short nameId) {

        // Search for the first instance of this name ID
        for (int i = 0; i < numberOfNameRecords; i++) {
            if (records[i].getNameId() == nameId) {
                return records[i].getRecordString();
            }
        }
        return "";
    }

    public int getType() {
        return name;
    }
}
