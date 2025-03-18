package Display;


import Core.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class GameScreen extends JPanel{
    final JFXPanel fxPanel = new JFXPanel(); //initiallize toolkit
    private AsteroidsGame game;
    BufferedImage myPicture = ImageIO.read(new File("Images/gamescreen.jpg"));
    int w = myPicture.getWidth();
    int h = myPicture.getHeight();

    public GameScreen(AsteroidsGame game) throws IOException {
        this.game = game;
    }

    public static Color makeTransparent(Color source, int alpha)//0to255
    {
        return new Color(source.getRed(), source.getGreen(), source.getBlue(), alpha);
    }

    private void drawPlayerShip(Graphics2D gc, Player p) throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        int a[] = p.getA();
        int b[] = p.getB();
        int c[] = p.getC();
        int r = p.getR();
        int x = (int) p.getX();
        int y = (int) p.getY();
        int[] xs = new int[]{a[0], b[0], c[0]};
        int[] ys = new int[]{a[1], b[1], c[1]};
        Polygon pg = new Polygon(xs, ys, 3);
        gc.setColor(Color.RED);
        gc.fillPolygon(pg);

        if (game.getPlayerShield()) {
            int R = r + 10;
            Shape circle = new Ellipse2D.Double(x - R, y - R, 2 * R, 2 * R);
            gc.setColor(makeTransparent(Color.YELLOW, 60));
            gc.fill(circle);
        }

        if (p.thrust == true && p.thrustCount % 3 == 1) {
            String bip = "Sound_Effects/ShipThrust.mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            //PlayerListener.playSound("Sound_Effects/thrust.mp3");
            int xt = (b[0] + c[0]) / 2;
            int yt = (b[1] + c[1]) / 2;
            int z = r / 2;
            int k = r / 4;
            double ang = p.getAngle();
            int bx = (int) (xt - k * Math.cos(Math.toRadians(ang)));
            int cx = (int) (xt + k * Math.cos(Math.toRadians(ang)));
            int by = (int) (yt + k * Math.sin(Math.toRadians(ang)));
            int cy = (int) (yt - k * Math.sin(Math.toRadians(ang)));
            int ax = (int) (xt + z * Math.sin(Math.toRadians(ang)));
            int ay = (int) (yt + z * Math.cos(Math.toRadians(ang)));

            int[] tx = new int[]{ax, bx, cx};
            int[] ty = new int[]{ay, by, cy};
            Polygon tg = new Polygon(tx, ty, 3);
            gc.setColor(Color.YELLOW);
            gc.fillPolygon(tg);
        }
        p.thrustCount++;
        p.thrust = false;
    }

    private void drawEnemy(Graphics2D gc) {
        for (EnemyShip e:game.getEShip()){
            if (e.isVisible()){
                e.draw(gc);
                if (game.getEnemyShield()){
                    int s = e.getSize();
                    int x  = (int)e.getX();
                    int y = (int)e.getY();
                    Shape oval = new Ellipse2D.Double(x-4*s*7, y-2*s*7,8*s*7,4*s*7);
                    gc.setColor(makeTransparent(Color.GREEN, 60));
                    gc.fill(oval);
                }
            }}
    }

    private void drawAsteriods(Graphics2D g){
        for (Asteroid obj : game.getAsteroids()) {
            if(obj.isAlive()){
                obj.draw(g);}

        }
    }
    private void drawSparks(Graphics2D g){
        for (Spark s: game.getSparks()){
            if (s.isAlive()){
                s.draw(g);
            }}
    }
    private void drawExplosions(Graphics2D g){
        for(ShipExplosion s:game.getShipExplosions()){
            s.draw(g);
        }
    }

    private void drawBullet(Graphics2D gc, Bullet b) {
        gc.setColor(Color.WHITE);
        gc.fill(b.getCircle());
    }

    private void drawShots(Graphics2D g){
        for (EnemyShots e: game.getShots()){
            g.setColor(Color.WHITE);
            g.draw(e.getHitBox());
        }
    }
    private void drawBonus(Graphics2D g){
        for(Bonus b: game.getBonuses()){
            g.setColor(Color.WHITE);
            int type = b.getType();
            g.setColor(Color.WHITE);
            g.draw(b.getHitbox());
            if (type == 0) {
                g.setColor(Color.RED);
                int R = b.getR()/2-2;
                int x = (int)b.getX()+16;
                int y = (int)b.getY()+17;
                int ax = x;
                int ay = y- R;
                int bx = x - R/2; //30 degrees
                int by = (int)(y+ R*Math.cos(Math.toRadians(30)));
                int cx = x + R/2; //30 degrees
                int cy = (int)(y+ R*Math.cos(Math.toRadians(30)));

                int[] tx = new int[]{ax, bx, cx};
                int[] ty = new int[]{ay, by, cy};
                Polygon tg = new Polygon(tx, ty, 3);
                g.fillPolygon(tg);
            }
            else if (type == 1) {
                double x = b.getX();
                double y = b.getY();
                int R = b.getR()/2-4;
                Shape circle = new Ellipse2D.Double(x+4, y+4, 2 * R, 2 * R);
                g.setColor(Color.YELLOW);
                g.fill(circle);

            }else if (type == 2) {
                g.setColor(Color.cyan);
                int r = b.getR();
                g.drawString("S", (int)(b.getX()+r/4), (int)(b.getY()+r-6));
            }
        }


    }
    private void drawLevel(Graphics g){
        g.setColor(Color.WHITE);
        Font ffont = new Font("Arial", Font.TRUETYPE_FONT, 33);
        g.setFont(ffont);
        int levelprint = game.getCurrentLevel() - 1;
        if (levelprint != 0){
            g.drawString("You've passed LEVEL "+levelprint,AsteroidsGame.SCREEN_WIDTH / 2-160,130);}

    }

    protected void paintComponent(Graphics g){
        if (game != null) {
            Graphics2D g2 = (Graphics2D) g;
            g.drawImage(myPicture,0,0,w,h,null);

            g.setColor(Color.cyan);
            Font font = new Font("Arial", Font.ITALIC, 22);
            g.setFont(font);
            g.drawString("Lives: " + game.getLives(), 5, 20);
            g.drawString("Score: " + game.getPlayerScore(), AsteroidsGame.SCREEN_WIDTH / 2-40, 20);
            g.drawString("Level: " + game.getLevel(), AsteroidsGame.SCREEN_WIDTH-85, 20);


            try {
                drawPlayerShip(g2,game.getShip());
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            drawEnemy(g2);
            for (Bullet bullet : game.getBullets()) {
                drawBullet(g2, bullet);
            }
            drawAsteriods(g2);
            drawSparks(g2);
            drawExplosions(g2);
            drawShots(g2);
            drawBonus(g2);

            if (game.isPaused() && !game.isGameOver()) {
                g.setColor(Color.cyan);
                g.drawString("Press 'p' to continue ", AsteroidsGame.SCREEN_WIDTH / 2-92, 330);

            } if (game.getmoveLevel()&& !game.getOnce() ){
                drawLevel(g);
            }
            if (game.isGameOver()) {
                g.setColor(Color.LIGHT_GRAY);
                Font ffont = new Font("Arial", Font.BOLD, 100);
                g.setFont(ffont);
                g.drawString("Game over ", AsteroidsGame.SCREEN_WIDTH / 2 -240, AsteroidsGame.SCREEN_HEIGHT/2);

            }
        }}

}

