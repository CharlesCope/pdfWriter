package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;


public class CmapIndexEntry {

    private int platformId;
    private int encodingId;
    private int offset;

    protected CmapIndexEntry(RandomAccessFile raf) throws IOException {
        platformId = raf.readUnsignedShort();
        encodingId = raf.readUnsignedShort();
        offset = raf.readInt();
    }

    public int getEncodingId() {return encodingId;}

    public int getOffset() {return offset;}

    public int getPlatformId() {return platformId;}

    public String toString() {
        String platform;
        String encoding = "";

        switch (platformId) {
        	case 0: platform = " Unicode - Indicates Unicode version"; break;    
        	case 1: platform = " Macintosh - Script Manager code"; break;
        	case 2: platform = " Reserved - Do Not Use"; break;
            case 3: platform = " Windows - Microsoft encoding"; break;
            default: platform = "";
        }
        
        
        if (platformId == 0) {
            // Unicode Platform-specific Encoding Identifiers
            switch (encodingId) {
                case 0: encoding = "- Default semantics"; break;
                case 1: encoding = "- Version 1.1 semantics"; break;
                case 2: encoding = "- ISO 10646 1993 semantics (deprecated)"; break;
                case 3: encoding = "- Unicode 2.0 or later semantics (BMP only)"; break;
                case 4: encoding = "- Unicode 2.0 or later semantics (non-BMP characters allowed)"; break;
                case 5: encoding = "- Unicode Variation Sequences"; break;
                case 6: encoding = "- Full Unicode coverage (used with type 13.0 cmaps by OpenType)"; break;
                default: encoding = "";
            }
        }
        else if (platformId == 1){
        	encoding = "- backwards compatibility with QuickDraw"; 
        }
        else if (platformId == 3){
        	// Windows specific encodings
            switch (encodingId) {
                case 0: encoding = "- Symbol"; break;
                case 1: encoding = "- Unicode"; break;
                case 2: encoding = "- ShiftJIS"; break;
                case 3: encoding = "- Big5"; break;
                case 4: encoding = "- PRC"; break;
                case 5: encoding = "- Wansung"; break;
                case 6: encoding = "- Johab"; break;
                default: encoding = "";
            }
        }
        return new StringBuffer()
        .append("Sub Table - ")
        .append( "platform id: " )
        .append( platformId )
        .append( platform )
        .append( ", encoding id: " )
        .append( encodingId )
        .append( encoding )
        .append( ", offset: " )
        .append( offset ).toString();
    }
}
