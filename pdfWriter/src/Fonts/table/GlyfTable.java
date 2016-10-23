package Fonts.table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class GlyfTable implements Table {

    private byte[] buf = null;
    private GlyfDescript[] descript;

    protected GlyfTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        raf.seek(de.getOffset());
        buf = new byte[de.getLength()];
        raf.read(buf);
/*
        TableMaxp t_maxp = (TableMaxp) td.getEntryByTag(maxp).getTable();
        TableLoca t_loca = (TableLoca) td.getEntryByTag(loca).getTable();
        descript = new TableGlyfDescript[t_maxp.getNumGlyphs()];
        for (int i = 0; i < t_maxp.getNumGlyphs(); i++) {
            raf.seek(tde.getOffset() + t_loca.getOffset(i));
            int len = t_loca.getOffset((short)(i + 1)) - t_loca.getOffset(i);
            if (len > 0) {
                short numberOfContours = raf.readShort();
                if (numberOfContours < 0) {
                    //          descript[i] = new TableGlyfCompositeDescript(this, raf);
                } else {
                    descript[i] = new TableGlyfSimpleDescript(this, numberOfContours, raf);
                }
            } else {
                descript[i] = null;
            }
        }

        for (int i = 0; i < t_maxp.getNumGlyphs(); i++) {
            raf.seek(tde.getOffset() + t_loca.getOffset(i));
            int len = t_loca.getOffset((short)(i + 1)) - t_loca.getOffset(i);
            if (len > 0) {
                short numberOfContours = raf.readShort();
                if (numberOfContours < 0) {
                    descript[i] = new TableGlyfCompositeDescript(this, raf);
                }
            }
        }
*/
    }

    public void init(int numGlyphs, LocaTable loca) {
        if (buf == null) {return;}
        descript = new GlyfDescript[numGlyphs];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        for (int i = 0; i < numGlyphs; i++) {
            int len = loca.getOffset((i + 1)) - loca.getOffset(i);
            if (len > 0) {
                bais.reset();
                bais.skip(loca.getOffset(i));
                short numberOfContours = (short)(bais.read()<<8 | bais.read());
                if (numberOfContours >= 0) {
                    descript[i] = new GlyfSimpleDescript(this, numberOfContours, bais);
                } else {
                    descript[i] = new GlyfCompositeDescript(this, bais);
                }
            }
        }

        buf = null;

        for (int i = 0; i < numGlyphs; i++) {
            if (descript[i] == null) continue;

            descript[i].resolve();
        }
    }

    public GlyfDescript getDescription(int i) {return descript[i];}

    public int getType() {return glyf;}
}