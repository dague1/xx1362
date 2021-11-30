import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.instrument.Instrumentation;
import java.nio.ByteBuffer;

public class Player extends JComponent implements Buffer, KeyListener, ActionListener {
    double x = 10, y = 10;
    private double dx = 0;
    private double dy = 0;

    private double moveSpeed = 30;

    public Player(){
        setPreferredSize(new Dimension(10, 10));
        setBounds((int)x,(int)y, 10,10);
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(0,0,10,10);
    }

    public void UpdatePlayer(double delta, Map map){
        if(!map.CheckCollision((int)(x + dx * delta), (int)(y + dy * delta), (int)(x + dx * delta + 10), (int)(y + dy * delta + 10))){
            x += dx * delta;
            y += dy * delta;
        }

        setBounds((int)x,(int)y, 10,10);
    }

    public byte[] WriteBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(8); // Create a ByteBuffer object of size 8 bytes.
        buffer.putInt((int)x);
        buffer.putInt((int)y);

        return buffer.array(); // Return the ByteBuffer object as an array.
    }

    public void ReadBuffer(byte[] data){
        ByteBuffer buffer = ByteBuffer.wrap(data); //Wraps data around ByteBuffer. (extends functionality)
        x = buffer.getInt();
        y = buffer.getInt();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){
            dy = -moveSpeed;
        }
        else if(key == KeyEvent.VK_DOWN){
            dy = moveSpeed;
        }
        else if(key == KeyEvent.VK_RIGHT){
            dx = moveSpeed;
        }
        else if(key == KeyEvent.VK_LEFT){
            dx = -moveSpeed;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}
