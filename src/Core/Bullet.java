package Core;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Bullet implements Hittable{
    private double x, y; //upper left corner
    private boolean direction; // True for up, false for down
    private boolean alive = true;
    private String name;
    private static int bulletCounter = 0;
    private static final int BULLET_SPEED = 8;
    private double angle;
    private Shape circle;
    private double r;
    private double d;
    private double distance;

    public Bullet(int x, int y, boolean direction, double Angle,String name) {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.r = 2.5;
        this.name = name + " " + bulletCounter++;
        this.angle = Angle;
        this.d = 2*r;
        circle = this.reposition(x,y);

    }
    public Shape reposition(double x, double y){
        circle = new Ellipse2D.Double(x-r, y-r,d,d);
        return circle;
    }
    public void move() {
        screenWrap();
        y -= (BULLET_SPEED*Math.cos(Math.toRadians(angle)));
        x -= (BULLET_SPEED*Math.sin(Math.toRadians(angle)));
        distance += BULLET_SPEED;
        circle = this.reposition(x,y);
    }

    public double getDistance(){
        return distance;
    }
    public Shape getCircle(){
        return circle;
    }
    public int getR(){
        return (int)r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean cIntersect(double x1, double y1, double x2,
                              double y2, double r1, double r2)
    {   boolean intersect = false;
        double distSq = (x1 - x2) * (x1 - x2) +
                (y1 - y2) * (y1 - y2);
        double radSumSq = (r1 + r2) * (r1 + r2);
        if (distSq <= radSumSq){
            intersect = true;
        }return intersect;
    }

    public boolean hitbyShot(EnemyShots b) {
        if (this.cIntersect(x,y,b.getX(),b.getY(),r,r)){///Can only use rec2D??
            alive = false;
            b.destroyed = true;
        }
        return this.cIntersect(x,y,b.getX(),b.getY(),r,r);
    }


    public Shape getHitBox() {
        return reposition(x, y);
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

    public int getPoints() {
        return 0;
    }


    @Override
    public boolean isHitbyBullet(Bullet b) {
        return false;
    }

    public void destroy() {
        alive = false;
    }

}
