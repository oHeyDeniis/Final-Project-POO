package BlackJack.packet;

import java.nio.ByteBuffer;

public class PacketBuffer {

    private final ByteBuffer buffer;

    // Construtor para quando vamos ENVIAR dados (aloca um tamanho padrão seguro,
    // ex: 1024 bytes)
    public PacketBuffer() {
        this.buffer = ByteBuffer.allocate(1024);
    }

    // Construtor para quando vamos RECEBER dados (envolve os bytes que chegaram da
    // rede)
    public PacketBuffer(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes);
    }

    // Retorna o ByteBuffer interno caso precise
    public ByteBuffer getInternalBuffer() {
        return buffer;
    }

    // Retorna apenas os bytes que foram realmente escritos (para enviar na rede)
    public byte[] getAvailableBytes() {
        byte[] bytes = new byte[buffer.position()];
        System.arraycopy(buffer.array(), 0, bytes, 0, buffer.position());
        return bytes;
    }

    // ==========================================
    // MÉTODOS DE ESCRITA (WRITE)
    // ==========================================

    public void writeByte(byte value) {
        buffer.put(value);
    }

    public void writeFloat(float value) {
        buffer.putFloat(value);
    }

    public void writeInt(int value) {
        buffer.putInt(value);
    }

    public void writeBoolean(boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }

    public void writeString(String value) {
        if (value == null) {
            buffer.putInt(0);
            return;
        }
        byte[] stringBytes = value.getBytes();
        buffer.putInt(stringBytes.length); // Grava o tamanho da string automaticamente
        buffer.put(stringBytes); // Grava os caracteres da string
    }

    // ==========================================
    // MÉTODOS DE LEITURA (READ)
    // ==========================================

    public byte readByte() {
        return buffer.get();
    }

    public int readInt() {
        return buffer.getInt();
    }

    public boolean readBoolean() {
        return buffer.get() == 1;
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public String readString() {
        int length = buffer.getInt(); // Lê o tamanho que foi gravado automaticamente
        if (length <= 0)
            return "";

        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);
        return new String(stringBytes);
    }
}