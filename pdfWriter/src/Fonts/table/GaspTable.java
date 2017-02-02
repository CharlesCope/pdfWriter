package Fonts.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GaspTable implements Table {
	 private byte[] byteTable;
	 private int dataLength;
	 
	 protected GaspTable(DirectoryEntry de, RandomAccessFile raf) throws IOException {
		 raf.seek(de.getOffset());
		 dataLength = de.getLength();
		 byteTable = new byte[dataLength];
		 raf.read(byteTable, 0, dataLength);
	 }

	public int getType() {return gasp;	}
	
	public byte[] getAllBytes(){return byteTable;}

}
