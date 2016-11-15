package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TableFactory {

    public static Table create(DirectoryEntry de, RandomAccessFile raf) throws IOException {
        Table t = null;
        switch (de.getTag()) {
        case Table.BASE:
            break;
        case Table.CFF:
            break;
        case Table.DSIG:
            break;
        case Table.EBDT:
            break;
        case Table.EBLC:
            break;
        case Table.EBSC:
            break;
        case Table.GDEF:
            break;
        case Table.GPOS:
            t = new GposTable(de, raf);
            break;
        case Table.GSUB:
            t = new GsubTable(de, raf);
            break;
        case Table.JSTF:
            break;
        case Table.LTSH:
            break;
        case Table.MMFX:
            break;
        case Table.MMSD:
            break;
        case Table.OS_2:
            t = new Os2Table(de, raf);
            break;
        case Table.PCLT:
            break;
        case Table.VDMX:
            break;
        case Table.cmap:
        	t = new CmapTable(de, raf);
            break;
        case Table.cvt:
            t = new CvtTable(de, raf);
            break;
        case Table.fpgm:
            t = new FpgmTable(de, raf);
            break;
        case Table.fvar:
            break;
        case Table.gasp:
            break;
        case Table.glyf:
            t = new GlyfTable(de, raf);
            break;
        case Table.hdmx:
            break;
        case Table.head:
            t = new HeadTable(de, raf);
            break;
        case Table.hhea:
            t = new HheaTable(de, raf);
            break;
        case Table.hmtx:
            t = new HmtxTable(de, raf);
            break;
        case Table.kern:
            t = new KernTable(de, raf);
            break;
        case Table.loca:
            t = new LocaTable(de, raf);
            break;
        case Table.maxp:
            t = new MaxpTable(de, raf);
            break;
        case Table.name:
            t = new NameTable(de, raf);
            break;
        case Table.prep:
            t = new PrepTable(de, raf);
            break;
        case Table.post:
            t = new PostTable(de, raf);
            break;
        case Table.vhea:
            break;
        case Table.vmtx:
            break;
        }
        return t;
    }
}
