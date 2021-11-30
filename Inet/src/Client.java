import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //Socket socket = new Socket("localhost",4999);
        //DataInputStream dIn = new DataInputStream(socket.getInputStream());

        Map map = new Map("map1.png");


        Player player = new Player();

        Container c = new JLayeredPane();
        c.setPreferredSize(new Dimension(800, 600));
        c.setLayout(null);
        c.add(map, 999);
        c.add(player, 0);

        JFrame frame = new JFrame("Inet");
        frame.setSize(800,600);
        frame.add(c);
        frame.setVisible(true);
        frame.addKeyListener(player);
        //while(socket.isConnected()){

        //}
        Long time = System.nanoTime();
        while(true) {
            long now = System.nanoTime();
            double delta = (now - time) / 1000000000.0;
            player.UpdatePlayer(delta, map);
            time = now;
        }
    }

    public static void ReadMessage(DataInputStream dataInStream) throws IOException {
        int size = dataInStream.readInt(); // read length of incoming message
        if(size > 0) {

            byte[] messageData = new byte[size];
            dataInStream.read(messageData, 0, messageData.length); // read the message
            Message msg = new Message(messageData);
            HandleMessage(msg);

        }

    }

    public static void HandleMessage(Message message) {
        switch (message.id) {
            case 1: {                           //update my character
                Player temp = new Player();
                temp.ReadBuffer(message.data);
                System.out.println(temp.x + " : " + temp.y);
                break;
            }
        }





    }
}
