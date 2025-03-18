package Display;

import Core.AsteroidsGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AboutScreen extends JPanel{
    private static final long serialVersionUID = -1264717778772722118L;
    private boolean menu = false;
    private PlayerListener listener;
    BufferedImage myPicture = ImageIO.read(new File("Images/M.jpg"));
    int w = myPicture.getWidth();
    int h = myPicture.getHeight();

    public AboutScreen() throws IOException {
    }


    public void paintComponent(Graphics g) {
        g.drawImage(myPicture,0,0,w/2,h/2,null);

        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, AsteroidsGame.SCREEN_WIDTH, AsteroidsGame.SCREEN_HEIGHT);
        g.setColor(Color.cyan);
        Font font = new Font("Bradley Hand", Font.BOLD,23);
        g.setFont(font);
        int xx = 1 * AsteroidsGame.SCREEN_WIDTH / 6 + 58;
        drawString(g, "Asteriods Game Controls", new Rectangle(0, 0, AsteroidsGame.SCREEN_WIDTH, 64), 36);
        g.drawString("Rotate Left", xx, 96 + 0 * 32);
        g.drawString("left arrow", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 0 * 32);
        g.drawString("Rotate Right", xx, 96 + 1 * 32);
        g.drawString("right arrow", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 1 * 32);
        g.drawString("Fire", xx, 96 + 2 * 32);
        g.drawString("space bar", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 2 * 32);
        g.drawString("Play/Pause", xx, 96 + 6 * 32);
        g.drawString("p", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 6 * 32);
        g.drawString("Apply Thrust", xx, 96 + 4 * 32);
        g.drawString("upper arrow", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 4 * 32);
        g.drawString("Apply Brake", xx, 96 + 3 * 32);
        g.drawString("b", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 3 * 32);
        g.drawString("Hyperspace Jump", xx, 96 + 5 * 32);
        g.drawString("h", 4 * AsteroidsGame.SCREEN_WIDTH / 6, 96 + 5 * 32);
        drawString(g, "Press 'M' to return to the Main Menu", new Rectangle(0, 416, AsteroidsGame.SCREEN_WIDTH, 80), 24);
    }

    private void drawString(Graphics g, String text, Rectangle rect, int size) {
        Graphics2D g2d = (Graphics2D) g.create();

        Font font = new Font("Bradley hand", Font.BOLD, size);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2d.setColor(Color.cyan);
        g2d.drawString(text, x, y);
    }


}
