package Core;

import java.awt.*;
public interface Hittable {
    public boolean isAlive();
    public int getPoints();
    public boolean isHitbyBullet(Bullet b);
    public Shape getHitBox();
    public void hit();
    public boolean hitbyAsetroids(Asteroid a);
    public double getX();
    public double getY();
    public int getR();
}


