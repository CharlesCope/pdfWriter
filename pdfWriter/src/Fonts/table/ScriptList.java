package Fonts.table;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ScriptList {

    private int scriptCount = 0;
    private ScriptRecord[] scriptRecords;
    private Script[] scripts;
    
    /** Creates new ScriptList */
    protected ScriptList(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        scriptCount = raf.readUnsignedShort();
        scriptRecords = new ScriptRecord[scriptCount];
        scripts = new Script[scriptCount];
        for (int i = 0; i < scriptCount; i++) {
            scriptRecords[i] = new ScriptRecord(raf);
        }
        for (int i = 0; i < scriptCount; i++) {
            scripts[i] = new Script(raf, offset + scriptRecords[i].getOffset());
        }
    }

    public int getScriptCount() {
        return scriptCount;
    }
    
    public ScriptRecord getScriptRecord(int i) {
        return scriptRecords[i];
    }
    
    public Script findScript(String tag) {
        if (tag.length() != 4) {
            return null;
        }
        int tagVal = ((tag.charAt(0)<<24)
            | (tag.charAt(1)<<16)
            | (tag.charAt(2)<<8)
            | tag.charAt(3));
        for (int i = 0; i < scriptCount; i++) {
            if (scriptRecords[i].getTag() == tagVal) {
                return scripts[i];
            }
        }
        return null;
    }

}
