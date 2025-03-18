package Core;

import java.awt.*;

public class Bonus implements Hittable{
    double x,y;
    private long startTime;
    double targetX,targetY;
    double vx,vy;
    int r;
    int type;
    boolean destroyed;
    // type 0:    type 1:   type 2:    type 3:
    public Bonus(int type){
        startTime = System.currentTimeMillis();
        this.x = AsteroidsGame.SCREEN_WIDTH * Math.random();
        this.y = 0;
        this.r = 30;
        this.destroyed= false;
        targetX = AsteroidsGame.SCREEN_WIDTH * Math.random();
        targetY = AsteroidsGame.SCREEN_HEIGHT * Math.random();
        vx = 2;
        vy = 0;
        this.type = type;
    }
    public void update(){
        screenWrap();
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 100) {
            targetX = AsteroidsGame.SCREEN_WIDTH * Math.random();
            targetY = AsteroidsGame.SCREEN_WIDTH * Math.random();
        }
        vx += (dx / dist) * 0.02;
        vy += (dy / dist) * 0.02;

        vx = vx > 2 ? 2 : vx < -2 ? -2 : vx;
        vy = vy > 2 ? 2 : vy < -2 ? -2 : vy;

        x += vx;
        y += vy;

        destroyed |= System.currentTimeMillis() - startTime > 8000;
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
    public Rectangle getHitbox(){
        Rectangle h = new Rectangle((int)(x-2/r),(int)(y-2/r),r,r);
        return h;
    }

    public int getType(){
        return type;
    }
    public boolean isDestroyed(){
        return destroyed;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public boolean isHitbyBullet(Bullet b) {
        return false;
    }

    @Override
    public Shape getHitBox() {
        Rectangle h = new Rectangle((int)(x-2/r),(int)(y-2/r),r,r);
        return h;
    }


    public void hit(){
        destroyed = true;
    }

    @Override
    public boolean hitbyAsetroids(Asteroid a) {
        return false;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getR(){
        return r;
    }
}
