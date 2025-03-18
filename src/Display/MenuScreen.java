package Display;

import Core.AsteroidsGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuScreen extends JPanel{
    private static final long serialVersionUID = 1616386874546775416L;
    BufferedImage myPicture = ImageIO.read(new File("Images/Menu.jpg"));

    int w = myPicture.getWidth();
    int h = myPicture.getHeight();


    public MenuScreen() throws IOException {
    }

    private void drawString(Graphics g, String text, Rectangle rect, int size) {
        Graphics2D g2d = (Graphics2D) g.create();

        Font font = new Font("Phosphate", Font.BOLD, size);//
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2d.setColor(Color.cyan);
        g2d.drawString(text, x, y);

    }

    public void paintComponent(Graphics g) {
        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, AsteroidsGame.SCREEN_WIDTH, AsteroidsGame.SCREEN_HEIGHT);

        g.drawImage(myPicture,0,0,w/2,h/2,null);
        drawString(g, "Welcome to Asteroids Game", new Rectangle(AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 32 -5,
                AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 3), 46);
        drawString(g, "To play a game press N", new Rectangle(AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 6,
                AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 3), 23);
        drawString(g, "To see the controls press A", new Rectangle(AsteroidsGame.SCREEN_WIDTH / 3, 2 * AsteroidsGame.SCREEN_HEIGHT / 6,
                AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 3), 23);
        drawString(g, "To see the High scores press H", new Rectangle(AsteroidsGame.SCREEN_WIDTH / 3, 3 * AsteroidsGame.SCREEN_HEIGHT / 6,
                AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 3), 23);
        drawString(g, "To exit press X", new Rectangle(AsteroidsGame.SCREEN_WIDTH / 3, 4 * AsteroidsGame.SCREEN_HEIGHT / 6,
                AsteroidsGame.SCREEN_WIDTH / 3, AsteroidsGame.SCREEN_HEIGHT / 3), 23);

    }

}
