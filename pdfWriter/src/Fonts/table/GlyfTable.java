package Fonts.table;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.SortedSet;


public class GlyfTable implements Table {
	private static final byte[] PAD_BUF = new byte[] { 0, 0, 0 };
    private byte[] buf = null;
    private GlyfDescript[] descript;
    private byte[] byteTable;
    private long fileOffset;
    
    protected GlyfTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
    	fileOffset = de.getOffset();
        raf.seek(de.getOffset());
        byteTable = new byte[de.getLength()];
        raf.read(byteTable, 0, de.getLength());
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
            long len = loca.getOffset((i + 1)) - loca.getOffset(i);
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
    
    public byte[] getAllBytes(){return byteTable;}
    
    public byte[] getSubSetBytes(long[] newOffsets, long[] oldOffsets, byte[] data, SortedSet <Integer> ssGlyphIds){
    	// Ok I will try to work it out here.	
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	long[] offsets = oldOffsets;
    	InputStream is = new ByteArrayInputStream(data); 


    	try
    	{
    		is.skip(fileOffset);

    		long prevEnd = 0;    // previously read glyph offset
    		long newOffset = 0;  // new offset for the glyph in the subset font
    		int newGid = 0;      // new GID in subset font

    		// for each glyph in the subset
    		for (Integer gid : ssGlyphIds)
    		{
    			long offset = offsets[gid];
    			long length = offsets[gid + 1] - offset;

    			newOffsets[newGid++] = newOffset;
    			is.skip(offset - prevEnd);

    			byte[] buf = new byte[(int)length];
    			is.read(buf);

    			// detect glyph type
    			if (buf.length >= 2 && buf[0] == -1 && buf[1] == -1)
    			{
    				// compound glyph
    				int off = 2*5;
    				int flags;
    				do
    				{
    					// flags
    					flags = (buf[off] & 0xff) << 8 | buf[off + 1] & 0xff;
    					off += 2;

    					// glyphIndex
    					int componentGid = (buf[off] & 0xff) << 8 | buf[off + 1] & 0xff;
    					if (!ssGlyphIds.contains(componentGid))
    					{
    						ssGlyphIds.add(componentGid);
    					}

    					int newComponentGid = ssGlyphIds.headSet(componentGid).size();
    					buf[off]   = (byte)(newComponentGid >>> 8);
    					buf[off + 1] = (byte)newComponentGid;
    					off += 2;

    					// ARG_1_AND_2_ARE_WORDS
    					if ((flags & 1 << 0) != 0)
    					{
    						off += 2 * 2;
    					}
    					else
    					{
    						off += 2;
    					}
    					// WE_HAVE_A_TWO_BY_TWO
    					if ((flags & 1 << 7) != 0)
    					{
    						off += 2 * 4;
    					}
    					// WE_HAVE_AN_X_AND_Y_SCALE
    					else if ((flags & 1 << 6) != 0)
    					{
    						off += 2 * 2;
    					}
    					// WE_HAVE_A_SCALE
    					else if ((flags & 1 << 3) != 0)
    					{
    						off += 2;
    					}
    				}
    				while ((flags & 1 << 5) != 0); // MORE_COMPONENTS

    				// WE_HAVE_INSTRUCTIONS
    				if ((flags & 0x0100) == 0x0100)
    				{
    					// USHORT numInstr
    					int numInstr = (buf[off] & 0xff) << 8 | buf[off + 1] & 0xff;
    					off += 2;

    					// BYTE instr[numInstr]
    					off += numInstr;
    				}

    				// write the compound glyph
    				bos.write(buf, 0, off);

    				// offset to start next glyph
    				newOffset += off;
    			}
    			else if (buf.length > 0)
    			{
    				// copy the entire glyph
    				bos.write(buf, 0, buf.length);

    				// offset to start next glyph
    				newOffset += buf.length;
    			}

    			// 4-byte alignment
    			if (newOffset % 4 != 0)
    			{
    				int len = 4 - (int)(newOffset % 4);
    				bos.write(PAD_BUF, 0, len);
    				newOffset += len;
    			}

    			prevEnd = offset + length;
    		}
    		newOffsets[newGid++] = newOffset;
    	} catch (IOException e) {e.printStackTrace();}
    	finally
    	{
    		try {
				is.close();
			} catch (IOException e) {e.printStackTrace();}
    	}
    	return bos.toByteArray();
    	}
}