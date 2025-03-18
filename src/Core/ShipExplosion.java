package Core;

import java.awt.*;

public class ShipExplosion {
    public double x,y;
    private int r;
    private int maxRadius;
    private Color color;

    public ShipExplosion(double x,double y, int r, Color c){
        this.x = x;
        this.y = y;
        this.r = r;
        this.maxRadius = 2*r;
        this.color = c;

    }

    public boolean update(){
        r = r + 1;
        if (r>=maxRadius){
            return true;
        }
        return false;
    }
    public void draw(Graphics2D g){
        g.setColor(color);
        if (!update()){
            int rr = r/4;
            g.fillOval((int)(x-rr),(int)(y-rr),2*rr,2*rr);
            int dr = r/6;
            for (int i = 0; i<3;i++){
                int R = rr + dr;
                g.drawOval((int)(x-R),(int)(y-R),2*R,2*R);
                dr += r/6;
            }

        }


    }

}
