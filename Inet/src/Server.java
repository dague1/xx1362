import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(4999);
        ArrayList<Socket> ConnectedClients = new ArrayList<>();
        ArrayList<Player> PlayerCharacters = new ArrayList<>();

        boolean running = true;


        while(running){
            try{
                Socket connection = socket.accept();
                ConnectedClients.add(connection);

                Player newPlayer = new Player();
                newPlayer.x = 10;
                newPlayer.y = 999;

                PlayerCharacters.add(newPlayer); 


                SendMessage(newPlayer, connection);



            } catch (IOException error){

            }




        }

    }

    public static <T extends Buffer> void SendMessage(T data, Socket socket) throws IOException {
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream()); // Interface on our datastream.

        Message msg = new Message(1, data); //Create message
        var buffdata = msg.WriteBuffer(); //Converts message to byte array

        dOut.writeInt(buffdata.length); // write the length of the message
        dOut.write(buffdata); // write the actual message

    }
}
