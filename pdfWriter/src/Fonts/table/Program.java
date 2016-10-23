package Fonts.table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class Program {

    private short[] instructions;

    public short[] getInstructions() {return instructions;}

    protected void readInstructions(RandomAccessFile raf, int count) throws IOException {
        instructions = new short[count];
        for (int i = 0; i < count; i++) {
            instructions[i] = (short) raf.readUnsignedByte();
        }
    }

    protected void readInstructions(ByteArrayInputStream bais, int count) {
        instructions = new short[count];
        for (int i = 0; i < count; i++) {
            instructions[i] = (short) bais.read();
        }
    }
}