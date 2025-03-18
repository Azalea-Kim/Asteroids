package Core;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Spark{
    private double x,y,angle;
    private long startTime;
    private long lifeTime;
    private double vx, vy;
    private Rectangle shape;
    private boolean destroyed;

    public Spark(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = (Math.PI * 2) * Math.random();
        shape = new Rectangle(-1, -1, 4,1);
        double v = 0.5 + 1 * Math.random();
        vx = 2 * Math.cos(angle) * v;
        vy = 2 * Math.sin(angle) * v;
        startTime = System.currentTimeMillis();
        lifeTime = (long) (250 + 250 * Math.random());
    }


    public void update() {
        x += vx;
        y += vy;

        x = x < -1 ? AsteroidsGame.SCREEN_WIDTH : x;
        x = x > AsteroidsGame.SCREEN_WIDTH + 1 ? -1 : x;
        y = y < -1 ? AsteroidsGame.SCREEN_HEIGHT : y;
        y = y > AsteroidsGame.SCREEN_HEIGHT + 1 ? -1 : y;

        destroyed = System.currentTimeMillis() - startTime > lifeTime;
    }

    public void draw(Graphics2D g) {
        if (shape == null ) {
            return;
        }
        AffineTransform previousTransform = g.getTransform();
        g.setColor(Color.WHITE);
        g.translate(x, y);
        g.rotate(angle);
        g.draw(shape);
        g.setTransform(previousTransform);
    }
    public boolean isAlive(){
        return !destroyed;
    }
}

