package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Script {

    private int defaultLangSysOffset;
    private int langSysCount;
    private LangSysRecord[] langSysRecords;
    private LangSys defaultLangSys;
    private LangSys[] langSys;
    
    /** Creates new ScriptTable */
    protected Script(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        defaultLangSysOffset = raf.readUnsignedShort();
        langSysCount = raf.readUnsignedShort();
        if (langSysCount > 0) {
            langSysRecords = new LangSysRecord[langSysCount];
            for (int i = 0; i < langSysCount; i++) {
                langSysRecords[i] = new LangSysRecord(raf);
            }
        }

        // Read the LangSys tables
        if (langSysCount > 0) {
            langSys = new LangSys[langSysCount];
            for (int i = 0; i < langSysCount; i++) {
                raf.seek(offset + langSysRecords[i].getOffset());
                langSys[i] = new LangSys(raf);
            }
        }
        if (defaultLangSysOffset > 0) {
            raf.seek(offset + defaultLangSysOffset);
            defaultLangSys = new LangSys(raf);
        }
    }

    public LangSys getDefaultLangSys() {
        return defaultLangSys;
    }

}
