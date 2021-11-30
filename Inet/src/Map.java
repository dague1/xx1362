import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Map extends JComponent {
    BufferedImage ImageBuffer = null;

    public Map(String path) {
        try {
            ImageBuffer = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Could not Find Image");
        }

        setPreferredSize(new Dimension(800, 600));
        setBounds(0,0, 800, 600);
    }

    public void paint(Graphics g) {
        g.drawImage(ImageBuffer, 0, 0, null);
    }

    public boolean CheckCollision(int minX, int minY, int maxX, int maxY){
        Color collisionColor = new Color(0,0,0,255);
        for(int j = minY; j <= maxY; ++j) {
            for (int i = minX; i <= maxX; ++i) {
                Color pixelColor = new Color(ImageBuffer.getRGB(i, j));
                if(pixelColor.getRGB() == collisionColor.getRGB()){
                    return true;
                }
            }
        }
        return false;
    }
}
