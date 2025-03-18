package Core;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EnemyShots implements Hittable{
    private long startTime;
    private double x, y,vx,vy;
    private double angle;
    private Shape shape;
    public boolean destroyed;
    private boolean alive;

    public EnemyShots(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        vx = 0;
        vy = 0;
        startTime = System.currentTimeMillis();
        alive = true;
        destroyed = false;
        reposition(x,y);
    }


    public Shape reposition(double x, double y){
        shape = new Ellipse2D.Double(x-3, y-3,6,6);
        return shape;
    }
    public void update(double xx, double yy) {
        screenWrap();
        double targetX = xx;
        double targetY = yy;
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        vx += (dx / dist) * 0.03;
        vy += (dy / dist) * 0.03;
        x += vx;
        y += vy;

        destroyed |= System.currentTimeMillis() - startTime > 3000;
    }

    public void destroy() {
        alive = false;
        destroyed = true;
    }

    public void screenWrap () {
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

    public Shape getHitBox() {
        return reposition(x, y);
    }
    @Override
    public void hit() {
    }

    @Override
    public boolean hitbyAsetroids(Asteroid a) {
        return false;
    }
    public boolean isAlive() {
        return alive;
    }

    @Override
    public int getPoints() {
        return 0;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getR() {
        return 0;
    }
    public boolean isHitbyBullet(Bullet b) {
        return false;
    }
    public boolean isDestroyed(){
        return destroyed;
    }


}
