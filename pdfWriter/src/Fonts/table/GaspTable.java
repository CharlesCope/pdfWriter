package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GaspTable implements Table {
	 private byte[] byteTable;
	 private int dataLength;
	 private int fileOffset;
	 
	 protected GaspTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
		 fileOffset = de.getOffset();
		 raf.seek(de.getOffset());
		 dataLength = de.getLength();
		 byteTable = new byte[dataLength];
		 raf.read(byteTable, 0, dataLength);
	 }

	public int getType() {return gasp;	}
	
	public int getOffset(){return fileOffset;}
	
	public byte[] getAllBytes(){return byteTable;}

}
