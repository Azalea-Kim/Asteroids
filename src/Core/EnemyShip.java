package Core;


import Display.PlayerListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.Random;

public class EnemyShip implements Hittable {
    private boolean visible, alive;
    private int sound;
    private double x, y, vx, vy;
    private double size;
    public Shape shape;
    private double targetX, targetY;
    public long hideStartTime;
    public long nextShowTime;
    public long nextStartShotTime;
    public long nextShotTime;
    public AffineTransform transform = new AffineTransform();
    public int level;
    public int resetTimes;
    public int UDcount;
    public AsteroidsGame game;



    public EnemyShip(AsteroidsGame g, int Level,double Size,boolean newSize) {
        this.game = g;
        this.alive = true;
        this.level = Level;
        this.resetTimes = level;
        if (newSize){
        this.size = 1 + (int) (3 * Math.random())*0.8;}
        else{
        this.size = Size;}
        setupNew();


    }
    private void setupNew() {
        this.visible = false;
        this.sound = 0;
        this.UDcount = 150 + level * 100;
        int p = (int) (4 * Math.random());
        if (p == 0) {
            x = 0;
            y = (int) (AsteroidsGame.SCREEN_HEIGHT * Math.random());
        } else if (p == 1) {
            x = AsteroidsGame.SCREEN_WIDTH;
            y = (int) (AsteroidsGame.SCREEN_HEIGHT * Math.random());
        } else if (p == 2) {
            x = (int) (AsteroidsGame.SCREEN_WIDTH * Math.random());
            y = 0;
        } else if (p == 3) {
            x = (int) (AsteroidsGame.SCREEN_WIDTH * Math.random());
            y = AsteroidsGame.SCREEN_HEIGHT;
        }
        vx = 2;
        vy = 0;
        resetTarget();
        setShape();
        hideStartTime = System.currentTimeMillis();
        nextShowTime = 5000 + (long) (10000 * Math.random()) - 1000 * level;//appear earlier harder.

        nextStartShotTime = hideStartTime;
        nextShotTime = 5000 + (long) (1000 * Math.random()) - 1000 * level;
    }

    public void update() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        long currentTime = System.currentTimeMillis();
        if (visible) {
            move();
            if (sound > 0) {
                sound--;
            }
            if (sound == 0) {
                PlayerListener.playSound("Sound_Effects/alien.wav");
                sound = 200;
            }
            if (currentTime - nextStartShotTime > nextShotTime && visible == true) {
                game.EnemyFire(x, y);
                nextStartShotTime = System.currentTimeMillis();
                nextShotTime = 5000 + (long) (1000 * Math.random()) - 1000 * level;
            }
        }
        if (currentTime - hideStartTime > nextShowTime) {
            visible = true;
        }
    }
    private void setShape() {
        Polygon EnemyPolygon = new Polygon();
        EnemyPolygon.addPoint(2, -1);
        EnemyPolygon.addPoint(1, -2);
        EnemyPolygon.addPoint(-1, -2);
        EnemyPolygon.addPoint(-2, -1);
        EnemyPolygon.addPoint(-4, 0);
        EnemyPolygon.addPoint(-2, 1);
        EnemyPolygon.addPoint(2, 1);
        EnemyPolygon.addPoint(4, 0);
        EnemyPolygon.addPoint(2, -1);
        EnemyPolygon.addPoint(-2, -1);
        EnemyPolygon.addPoint(-4, 0);
        EnemyPolygon.addPoint(4, 0);
        for (int p = 0; p < EnemyPolygon.npoints; p++) {
            EnemyPolygon.xpoints[p] *= (5 * size);
            EnemyPolygon.ypoints[p] *= (5 * size);
        }
        shape = EnemyPolygon;
    }

    public void move(){
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (game.checkEnemyPosition(targetX,targetY,level,size)) {
            vx += (dx / dist) * 0.1;
            vy += (dy / dist) * 0.1;
            vx = vx > 2 ? 2 : vx < -2 ? -2 : vx;
            vy = vy > 2 ? 2 : vy < -2 ? -2 : vy;
            x += vx;
            y += vy;
            screenWrap();
        } else{
            resetTarget();
            move();
        }
    }

    public void resetTarget(){
        boolean bx = new Random().nextBoolean();
        double sx;
        if (bx) {
            sx = -1;
        } else {
            sx = 1;
        }
        boolean by = new Random().nextBoolean();
        double sy;
        if (by) {
            sy = -1;
        } else {
            sy = 1;
        }
        targetX = x + 30 * Math.random() * sx;
        targetY = y + 30 * Math.random() * sy;
    }

    public AffineTransform getTransform() {
        transform.setToIdentity();
        transform.translate(x, y);
        return transform;
    }

    @Override
    public void hit() {
        if (resetTimes == 0) {
            alive = false;
        } else {
            resetTimes--;
            setupNew();
        }
    }

    public boolean EnemyBulletCollisionCheck(Bullet b) {
        Area a1 = new Area(shape);
        Area a2 = new Area(b.getHitBox());
        a1.transform(this.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }
    public boolean isHitbyBullet(Bullet b) {
        return EnemyBulletCollisionCheck(b);
    }

    public boolean EnemyAsteroidCollisionCheck(Asteroid a) {
        Area a1 = new Area(shape);
        Area a2 = new Area(a.shape);//a.shape a.getHitBox
        a1.transform(this.getTransform());
        a2.transform(a.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    @Override
    public boolean hitbyAsetroids(Asteroid a) {
        return EnemyAsteroidCollisionCheck(a);
    }

    public boolean isAlive() {
        return alive;
    }
    public boolean isVisible() {
        return visible;
    }
    public int getPoints() {
        return 200;
    }
    public int getSize() {
        return (int) size;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int getR() {
        return (int) (10 * size);
    }

    @Override
    /*public Shape getHitBox() {
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        return transform.createTransformedShape(shape);
    }*/
    public Shape getHitBox(){
        return shape;
    }

    public void screenWrap() {
        if (x > AsteroidsGame.SCREEN_WIDTH) {
            x -= AsteroidsGame.SCREEN_WIDTH;
        }
        if (x < 0) {
            x += AsteroidsGame.SCREEN_WIDTH;
        }
        if (y > AsteroidsGame.SCREEN_HEIGHT) {
            y -= AsteroidsGame.SCREEN_HEIGHT;
        }
        if (y < 0) {
            y += AsteroidsGame.SCREEN_HEIGHT;
        }
    }
    public void draw(Graphics2D g) {
        if (shape == null) {
            return;
        }
        AffineTransform previousTransform = g.getTransform();
        g.setColor(Color.WHITE);
        g.translate(x, y);
        g.draw(shape);
        g.setTransform(previousTransform);
        g.drawString(""+resetTimes, (int)x-10, (int)y);
    }



}
