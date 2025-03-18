package Core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Asteroid implements Hittable {
    private double x,y;
    private boolean alive = true;
    public AsteroidsGame game;
    public double angle;
    public double vAngle;
    public Shape shape;
    public boolean destroyed;
    public double vx, vy, va;
    public int size;
    public int halfSize;
    public Color color = Color.WHITE;
    public boolean visible = true;
    public AffineTransform transform = new AffineTransform();

    // size = 1 small, 2 medium, 3 large
    public Asteroid(AsteroidsGame game, double x, double y, int size, double ang){
        this.game = game;
        this.x = x;
        this.y = y;
        this.size = size;
        this.halfSize = size * 10;
        angle = 360* Math.random();
        if (size == 3){
            vAngle = angle;}
        else{
            vAngle = ang;
        }
        double v = 0.5 + 1 * Math.random();
        vx = (4 - size) * Math.cos(Math.toRadians(vAngle)) * v;
        vy = (4 - size) * Math.sin(Math.toRadians(vAngle)) * v;
        va = 0.1 + 0.5 * Math.random();
        generateRandomShape();
    }


    private void generateRandomShape() {
        Polygon randomAsteroidShape = new Polygon();
        int f = 5 + (int) (5 * Math.random());
        double da = (2 * Math.PI) / f;
        double a = (2 * Math.PI) * Math.random();
        for (int i = 0; i < f; i++) {
            double ad = halfSize + halfSize * Math.random();
            double ax = ad * Math.cos(a);
            double ay = ad * Math.sin(a);
            randomAsteroidShape.addPoint((int) ax, (int) ay);
            a += da;
        }
        shape = randomAsteroidShape;
    }

    public void update() {
        angle += va;

        x += vx;
        y += vy;

        x = x < -halfSize ? game.getScreenWidth() : x;
        x = x > game.getScreenWidth() + halfSize ? -halfSize : x;
        y = y < -halfSize ? game.getScreenHeight() : y;
        y = y > game.getScreenHeight() + halfSize ? -halfSize : y;

    }

    public AffineTransform getTransform() {
        transform.setToIdentity();
        transform.translate(x, y);
        transform.rotate(angle);
        return transform;
    }
    public void hit() {
        if (size == 3) {
            for (int i=0;i<2;i++){
                double a = 0;
                int p = (int) (2 * Math.random());
                if (p == 0) {
                    a = vAngle + 10 + 20 * Math.random();
                } else if (p == 1) {
                    a = vAngle - (10 + 20 * Math.random());

                }
                game.addAsteroid(new Asteroid(game, x, y, size - 1, a));}
        }
        else if (size == 2){
            for (int i=0;i<2;i++){
                double b = 0;
                int pp = (int) (2 * Math.random());
                if (pp == 0) {
                    b = vAngle + 20 + 20 * Math.random();
                } else if (pp == 1) {
                    b = vAngle - (20 + 20 * Math.random());

                }game.addAsteroid(new Asteroid(game, x, y, size - 1, b));}
        }
        else if (size == 1){
            game.showExplosion(x,y);
        }
        destroyed = true;
        alive = false;

    }


    public boolean AsteroidBulletCollisionCheck(Bullet b){
        Area a1 = new Area(shape);
        Area a2 = new Area(b.getHitBox());
        a1.transform(this.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    @Override
    public boolean isHitbyBullet(Bullet b) {
        if(AsteroidBulletCollisionCheck(b)){
            alive = false;
        }return AsteroidBulletCollisionCheck(b);
    }

    public boolean AsteroidShotCollisionCheck(EnemyShots e){
        Area a1 = new Area(shape);
        Area a2 = new Area(e.getHitBox());
        a1.transform(this.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }
    
    public boolean hitByEnemyShots(EnemyShots e){
        if(AsteroidShotCollisionCheck(e)){
            alive = false;
        }return AsteroidShotCollisionCheck(e);
    }

    @Override
    public int getPoints() {
        if (size == 1){
            return 100;
        }else if (size == 2){
            return 50;
        }else if (size ==3){
            return 25;
        }
        return 0;
    }

    @Override
    /*public Shape getHitBox() {
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(Math.toRadians(angle));
        return transform.createTransformedShape(shape);
    }*/
    public Shape getHitBox(){
        return shape;
    }

    public void draw(Graphics2D g){
        if (shape == null || !visible) {
            return;
        }
        AffineTransform previousTransform = g.getTransform();
        g.setColor(color);
        g.translate(x, y);
        g.rotate(Math.toRadians(angle));
        g.draw(shape);
        g.setTransform(previousTransform);
    }

    @Override
    public boolean hitbyAsetroids(Asteroid a) {
        return false;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public int getR() {
        return 0;
    }


    @Override
    public boolean isAlive() {
        return alive;
    }
}
