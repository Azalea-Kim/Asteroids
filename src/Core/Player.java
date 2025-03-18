package Core;

import Display.PlayerListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.geom.Area;
import java.io.IOException;

public class Player implements Hittable{
    public int thrustCount = 1;
    private double x;
    private double y;
    private int r;
    private double v;
    private double vx;
    private double vy;
    private double angle;
    private double vAngle;
    public boolean thrust = false;
    public AsteroidsGame game;
    private int weaponCountdown;
    private boolean alive = true;
    private Shape hitBox;

    public Player(AsteroidsGame g) {
        x = AsteroidsGame.SCREEN_WIDTH / 2;
        y = AsteroidsGame.SCREEN_HEIGHT / 2;
        r =20;
        angle = 0;
        vAngle = 0;
        v = 0;
        vx = 0;
        vy = 0;
        getTriangle();
        this.game = g;
    }

    public int[] getA(){
        double aa = Math.toRadians(angle);
        int ax = (int) (x-r*Math.sin(aa));
        int ay = (int)(y-r*Math.cos(aa));
        int[] A = {ax,ay};
        return A;
    }
    public int[] getB(){
        double bb = Math.toRadians(30-angle);
        int bx = (int) (x-r*Math.sin(bb));
        int by = (int)(y+r*Math.cos(bb));
        int[] B = {bx,by};
        return B;
    }

    public int[] getC(){
        double cc = Math.toRadians(30+angle);
        int cx = (int) (x+r*Math.sin(cc));
        int cy = (int)(y+r*Math.cos(cc));
        int[] C = {cx,cy};
        return C;
    }


    private void getTriangle(){
        Polygon p=new Polygon();
        p.addPoint(this.getB()[0],this.getB()[1]);
        p.addPoint(this.getA()[0],this.getA()[1]);
        p.addPoint(this.getC()[0],this.getC()[1]);
        hitBox = p;
    }
    public void updatePlaying(){
        screenWrap();
        this.x -= vx;
        this.y -= vy;
        getTriangle();
    }
    public void applyThrust(){
        checkAngle();
        screenWrap();
        vAngle = angle;
        vx += 0.15 * Math.sin(Math.toRadians(vAngle));
        vy += 0.15 * Math.cos(Math.toRadians(vAngle));
        thrust = true;

    }
    public void rotate(int ang){
        angle += ang;
    }
    public void applyBrake(){
        vx = 0;
        vy = 0;
    }

    public double h[] = new double[2];
    public boolean checkHyper(){
        h[0] = AsteroidsGame.SCREEN_WIDTH*Math.random();
        h[1] = AsteroidsGame.SCREEN_HEIGHT*Math.random();
        if(game.checkHyperPosition((int)h[0],(int)h[1])){
            return true;
        }
        else{
            checkHyper();
        }return false;
    }
    public void applyHyper(){
        if(checkHyper()){
        this.vx = 0;
        this.vy = 0;
        this.x = h[0];
        this.y = h[1];}
    }


    public int getAngle(){
        return (int)angle;
    }

    public void checkAngle () {
        if (angle > 360) {
            angle -= 360;
        }
        if (angle < 0) {
            angle += 360;
        }
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

    public boolean isHitbyBullet(Bullet b) {
        Shape s = b.getHitBox();
        if (s.intersects(getHitBox().getBounds())) {
            alive = false;
        }
        return s.intersects(hitBox.getBounds());
    }
    public boolean AsteroidsShipCollisionCheck(Asteroid a){
        Area a1 = new Area(a.getHitBox());
        Area a2 = new Area(getHitBox());
        a1.transform(a.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }
    public boolean hitbyAsetroids(Asteroid a){
        if (AsteroidsShipCollisionCheck(a)){
            alive = false;
        }return AsteroidsShipCollisionCheck(a);

    }

    public boolean EnemyShipCollisionCheck(EnemyShip e){
        Area a1 = new Area(e.getHitBox());
        Area a2 = new Area(getHitBox());
        a1.transform(e.getTransform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }
    public boolean hitbyEnemy(EnemyShip e){
        if (EnemyShipCollisionCheck(e)){
            alive = false;
        }return EnemyShipCollisionCheck(e);}


    public boolean ShotsShipCollisionCheck(EnemyShots e){
        Area a1 = new Area(e.getHitBox());
        Area a2 = new Area(getHitBox());
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    public boolean hitbyShots(EnemyShots e){
        if ( ShotsShipCollisionCheck(e)){
            alive = false;
        }return ShotsShipCollisionCheck(e);
    }

    public boolean BonusShipCollisionCheck(Bonus b){
        Area a1 = new Area(b.getHitbox());
        Area a2 = new Area(getHitBox());
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    public boolean hitbyBonus(Bonus b){
        return BonusShipCollisionCheck(b);
    }

    public Shape getHitBox() {
        return hitBox;
    }

    @Override
    public void hit() {
        alive = false;
    }

    public void resetDestroyed() {
        alive = true;
        x = AsteroidsGame.SCREEN_WIDTH / 2;
        y = AsteroidsGame.SCREEN_HEIGHT / 2;
        angle = 0;
        vAngle = 0;
        vx = 0;
        vy = 0;

    }
    public void tick() {
        if (weaponCountdown > 0) {
            weaponCountdown--;
        } else {
            weaponCountdown = 8;
        }
    }
    public Bullet fire() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Bullet b = null;
        if (weaponCountdown == 0) {
            b = new Bullet(this.getA()[0], this.getA()[1], true, angle,"Player");
            PlayerListener.playSound("Sound_Effects/laser.wav");
        }
        return b;
    }

    public boolean isAlive() {
        return alive;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getVx(){
        return vx;
    }
    public double getVy() {
        return vy;
    }
    public int getPoints() {
        return -100;
    }
    public int getR(){
        return r;
    }


}