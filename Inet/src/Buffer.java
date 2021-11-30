// If a class should be sent over network, it should implement this interface.
public interface Buffer {
    public byte[] WriteBuffer();
    public void ReadBuffer(byte[] data);
}
