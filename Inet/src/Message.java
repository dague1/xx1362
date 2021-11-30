//Message wraps a byte[] array, id decides how the data should be handled.
import java.nio.ByteBuffer;
public class Message {

    int id;
    byte[] data;

    public byte[] WriteBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(id);
        buffer.put(data);

        return buffer.array();
    }

    public void ReadBuffer(byte[] inData){
        ByteBuffer buffer = ByteBuffer.wrap(inData);
        id = buffer.getInt();
        data = new byte[inData.length - 4];
        buffer.get(data);
    }

    public <T extends Buffer> Message(int id, T object) { // Constructor
        this.id = id;
        data = object.WriteBuffer();
    }
    public Message(byte[] array) { // Constructor
        ReadBuffer(array);
    }

}
