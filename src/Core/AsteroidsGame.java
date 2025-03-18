package Core;

import Display.PlayerListener;
import ucd.comp2011j.engine.Game;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AsteroidsGame implements Game {

    public AsteroidsGame(PlayerListener l) {
        this.listener = l;
    }

    public static final int SCREEN_WIDTH = 768;
    public static final int SCREEN_HEIGHT = 512;

    private PlayerListener listener;
    private Player player;
    private boolean playerShield;
    private boolean EnemyShield;
    private int playerLives;
    private int playerScore;

    private boolean pause = true;
    private int levelNumber = 1; //can't be 1 because over is <= level
    private int currentLevel = 0;
    private boolean finishLevel = false;
    private boolean moveLevel = false;
    private boolean GameOver = false;
    private boolean levelOnce = false;

    private int updateCountvalue = 60;
    private int updateCount = updateCountvalue;

    private List<Bullet> playerBullets;
    private ArrayList<Hittable> targets;
    private List<Asteroid> Asteroids = new ArrayList<Asteroid>();
    private List<Asteroid> AsteroidsAdd = new ArrayList<Asteroid>();
    private List<Asteroid> AsteroidsRemove = new ArrayList<Asteroid>();
    private List<EnemyShots> enemyShots = new ArrayList<EnemyShots>();
    private List<Spark> sparks = new ArrayList<Spark>();
    private List<ShipExplosion> shipExplosions = new ArrayList<ShipExplosion>();
    private List<EnemyShip> Enemys = new ArrayList<EnemyShip>();
    private List<Bonus> bonuses = new ArrayList<Bonus>();

    @Override
    public void startNewGame() {
        listener.reset();

        Asteroids = new ArrayList<Asteroid>();
        sparks = new ArrayList<Spark>();
        shipExplosions = new ArrayList<ShipExplosion>();
        player = new Player(this);
        targets = new ArrayList<Hittable>();
        playerBullets = new ArrayList<Bullet>();
        enemyShots = new ArrayList<EnemyShots>();
        Enemys = new ArrayList<EnemyShip>();
        sparks = new ArrayList<Spark>();
        bonuses = new ArrayList<Bonus>();
        createAsteroids(4);
        createEnemy(1);

        updateCount = updateCountvalue;
        playerLives = 20;
        playerScore = 0;
        currentLevel = 1;
        GameOver = false;
        pause = true;

        try {
            listener.playSound("Sound_Effects/space.wav");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateGame() {
        if (!isPaused()) {
            player.tick();
            player.updatePlaying();
            updateLevelnumber();
            updateAsteroids();
            updateSpark();
            updateLife();
            updateShipExplodes();
            createBonus();
            updateBonus();

            playerBullets();
            EnemyShotsAsteroids();
            try {
                movePlayer();
                updateEnemy();
                playerBonus();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (updateCount > 0) {
                updateCount--;
                playerShield = true;
            } else {
                playerShield = false;
                try {
                    PlayerCrash();
                    EnemyShotsPlayer();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            targets.clear();
            targets.addAll(Asteroids);
            targets.addAll(Enemys);
            targets.add(player);

            levelOnce = true;

        }
    }

    private void movePlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (listener.isPressingFire()) {
            Bullet b = player.fire();
            if (b != null) {
                playerBullets.add(b);
                listener.resetFire();}
        }
        if (listener.hasPressedHyper() ){
            player.applyHyper();
            listener.playSound("Sound_Effects/hyperSpace.wav");
            listener.resetHyper();
        }if (listener.hasPressedExit()) {
            GameOver = true;
            listener.resetExit();
        }

        if (listener.isPressingBrake()) {
            player.applyBrake();
        }
        if (listener.isPressingLeft()) {
            player.rotate(5);
        }
        if (listener.isPressingRight()) {
            player.rotate(-5);
        }
        if (listener.isPressingThrust()) {
            player.applyThrust();
        }

    }
    private void PlayerCrash() throws UnsupportedAudioFileException, LineUnavailableException, IOException { //Create invader list for the PLAYER!!!
        for (Asteroid asteroid : Asteroids) {
            if (asteroid.isAlive()) {
                if (player.hitbyAsetroids(asteroid)) {
                    asteroid.hit();
                    listener.playSound("Sound_Effects/explosion.wav");
                    ShipExplosion s = new ShipExplosion(player.getX(), player.getY(), player.getR(), Color.RED);
                    shipsExplodes(s);
                    playerLives -= 1;
                }
            }
        }
        for (EnemyShip enemyShip: Enemys) {
            if (player.hitbyEnemy(enemyShip)) {
                listener.playSound("Sound_Effects/explosion.wav");
                double x = enemyShip.getX();
                double y = enemyShip.getY();
                int r = enemyShip.getR();
                enemyShip.hit();
                ShipExplosion s = new ShipExplosion(player.getX(), player.getY(), player.getR(), Color.RED);
                shipsExplodes(s);
                playerLives -= 1;
                ShipExplosion ss = new ShipExplosion(x, y, r, Color.WHITE);
                shipExplosions.add(ss);
            }
        }
    }

    private void playerBonus() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (Bonus bonus : bonuses) {
            if (player.hitbyBonus(bonus)) {
                bonus.hit();
                listener.playSound("Sound_Effects/coin.wav");
                int Type = bonus.getType();
                if (Type == 0) {
                    playerLives++;
                    listener.playSound("Sound_Effects/health.wav");
                } else if (Type == 1) {
                    updateCount = updateCountvalue * 10; //value
                    listener.playSound("Sound_Effects/getShield.wav"); //shield
                } else if (Type == 2) {
                    playerScore = playerScore + 200; //random
                    listener.playSound("Sound_Effects/score.wav");
                }
            }
        }}

    public void addAsteroid(Asteroid asteroid) {
        AsteroidsAdd.add(asteroid);
    }
    public void createOneAsteroid() {
        int p = (int) (4 * Math.random());
        int x = 0;
        int y = 0;
        if (p == 0) {
            x = 0;
            y = (int) (SCREEN_HEIGHT * Math.random());
        } else if (p == 1) {
            x = SCREEN_WIDTH;
            y = (int) (SCREEN_HEIGHT * Math.random());
        } else if (p == 2) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = 0;
        } else if (p == 3) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = SCREEN_HEIGHT;
        }
        Asteroid asteroid = new Asteroid(this, x, y, 3, 0);
        addAsteroid(asteroid);
    }

    public void createAsteroids(int x) {
        for (int i = 0; i < x; i++) {
            createOneAsteroid();
        }
    }

    public void updateAsteroids() {
        for (Asteroid asteroid : Asteroids) {
            asteroid.update();
            if (asteroid.destroyed) {
                AsteroidsRemove.add(asteroid);
            }
        }
        Asteroids.addAll(AsteroidsAdd);
        AsteroidsAdd.clear();
        Asteroids.removeAll(AsteroidsRemove);
        if (Asteroids.size() == 0) {
            finishLevel = true;
        }
        AsteroidsRemove.clear();
    }


    public void createEnemy(int level) {
        EnemyShip e = new EnemyShip(this, level,0,true);
        Enemys.add(e);
    }
    public void updateEnemy() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        List<EnemyShip> remove = new ArrayList<EnemyShip>();
        for (EnemyShip enemyShip : Enemys) {
            if (enemyShip.isAlive()) {
                enemyShip.update();
            } else {
                remove.add(enemyShip);
            }
            if (enemyShip.isVisible()) {
                if (enemyShip.UDcount > 0) {
                    enemyShip.UDcount--;
                    EnemyShield = true;
                } else {
                    EnemyShield = false;
                    AsteroidCrashEnemy();
                }
            }
        }
        Enemys.removeAll(remove);
    }
    public void EnemyFire(double x, double y) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        listener.playSound("Sound_Effects/Shot.wav");
        double vx = player.getVx();
        double vy = player.getVy();
        double dx = (player.getX() - vx) - x;
        double dy = (player.getY() - vy) - y;
        double angle = Math.atan2(dy, dx);
        enemyShots.add(new EnemyShots(x, y, angle));
    }

    private void AsteroidCrashEnemy() {
        for (Asteroid asteroid : Asteroids) {
            for (EnemyShip enemyShip : Enemys) {
                if (enemyShip.hitbyAsetroids(asteroid)) {
                    double x = enemyShip.getX();
                    double y = enemyShip.getY();
                    int r = enemyShip.getR();
                    ShipExplosion S = new ShipExplosion(x, y, r, Color.WHITE);
                    shipExplosions.add(S);
                    asteroid.hit();
                    enemyShip.hit();
                }
            }
        }
    }


    private int bonusAdded = 1;
    private int bonusScore = 100;
    private int scoreAdded = 0;

    private void createBonus() {
        if (playerScore >= (bonusAdded + scoreAdded) * bonusScore) {
            scoreAdded++;
            if ((int) (6* Math.random()) == 0) {
                int type = (int) (3 * Math.random());
                bonuses.add(new Bonus(type));
                bonusAdded++;
            }
        }
    }
    public void updateBonus() {
        List<Bonus> remove = new ArrayList<Bonus>();
        for (Bonus bonus : bonuses) {
            if (!bonus.isDestroyed()) {
                bonus.update();
            } else {
                remove.add(bonus);
            }
        }
        bonuses.removeAll(remove);
    }

    public void shipsExplodes(ShipExplosion s) {
        shipExplosions.add(s);
    }

    public void updateShipExplodes() {
        for (ShipExplosion shipExplosion : shipExplosions) {
            shipExplosion.update();
        }
    }
    public void showExplosion(double x, double y) {
        for (int i = 0; i < 30; i++) {
            sparks.add(new Spark(x, y));
        }
    }
    public void updateSpark() {
        for (Spark spark : sparks) {
            spark.update();
        }
    }

    private void playerBullets() {
        List<Bullet> remove = new ArrayList<Bullet>();
        for (Bullet playerBullet : playerBullets) {
            if (playerBullet.isAlive() && playerBullet.getDistance() < 450) {
                playerBullet.move();
                for (Hittable t : targets) {
                    if (t instanceof Asteroid) {
                        if (t.isHitbyBullet(playerBullet)) {
                            playerScore += t.getPoints();
                            t.hit();
                            playerBullet.destroy();
                        }
                    } else if (t instanceof EnemyShip) {
                        if (t.isHitbyBullet(playerBullet)) {
                            double x = t.getX();
                            double y = t.getY();
                            int r = t.getR();
                            playerScore += t.getPoints();
                            t.hit(); //set up new
                            playerBullet.destroy();
                            ShipExplosion s = new ShipExplosion(x, y, r, Color.GREEN);
                            shipExplosions.add(s);
                        }
                    }
                }
                for (EnemyShots e : enemyShots) {
                    if (playerBullet.hitbyShot(e)) {
                        playerBullet.destroy();
                        e.destroy();
                    }
                }
            } else {
                remove.add(playerBullet);
            }
        }
        playerBullets.removeAll(remove);
    }


    private void EnemyShotsPlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        List<EnemyShots> remove = new ArrayList<EnemyShots>();
        for (EnemyShots e : enemyShots) {
            if (e.isAlive() && !e.isDestroyed()) { //check!!!
                e.update(player.getX(), player.getY());
                for (Hittable t : targets) {
                    if (t instanceof Player) {
                        if (((Player) t).hitbyShots(e)) {
                            listener.playSound("Sound_Effects/explosion.wav");
                            double x = t.getX();
                            double y = t.getY();
                            int r = t.getR();
                            playerLives -= 1;
                            e.destroy();
                            ShipExplosion a = new ShipExplosion(x, y, r, Color.RED);
                            shipsExplodes(a);
                        }
                    }
                }
            } else {
                remove.add(e);
            }
        }
        enemyShots.removeAll(remove);
    }
    private void EnemyShotsAsteroids(){
        List<EnemyShots> remove = new ArrayList<EnemyShots>();
        for (EnemyShots e : enemyShots) {
            if (e.isAlive() && !e.isDestroyed()) {
                e.update(player.getX(), player.getY());
                for (Hittable t : targets) {
                    if (t instanceof Asteroid) {
                        if (((Asteroid) t).hitByEnemyShots(e)) {
                            double x = t.getX();
                            double y = t.getY();
                            int r = t.getR();
                            ShipExplosion a = new ShipExplosion(x, y, r, Color.WHITE);
                            shipsExplodes(a);
                            t.hit();
                            e.destroy();
                        }
                    }
                }
            } else {
                remove.add(e);
            }
        }
        enemyShots.removeAll(remove);
    }


    public void updateLevelnumber() {
        levelNumber = currentLevel + 1;
    }

    private boolean canChangeLife = false;
    private int n = 1;
    private void updateLife() {
        if (playerScore >= n * 10000) {//10000
            canChangeLife = true;
            n++;
        }
        if (canChangeLife) {
            playerLives++;
            canChangeLife = false;
        }
    }

    @Override
    public boolean isPaused() {
        return pause;
    }
    @Override
    public void checkForPause() {
        if (listener.hasPressedPause()) {
            pause = !pause;
            listener.resetPause();
        }
    }

    @Override
    public boolean isLevelFinished() {
        return finishLevel;
    }

    @Override
    public int getTargetFPS() {
        return 0;
    }

    @Override
    public boolean isPlayerAlive() {
        return player.isAlive();
    }


    public boolean checkHyperPosition(int x, int y){
        for (Asteroid asteroid : Asteroids) {
            if (asteroid.isAlive()) {
                Area a1 = new Area(asteroid.getHitBox());
                Rectangle a = new Rectangle(x -15,y -15,30,30);
                a1.transform(asteroid.getTransform());
                Area a2 = new Area(a);
                a1.intersect(a2);
                if (!a1.isEmpty()) {
                    return false;
                }
            }
        }for (EnemyShip enemyShip: Enemys){
            Area a1 = new Area(enemyShip.getHitBox());
            Rectangle a = new Rectangle(x -15,y -15,30,30);
            a1.transform(enemyShip.getTransform());
            Area a2 = new Area(a);
            a1.intersect(a2);
            if (!a1.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public boolean checkEnemyPosition(double x, double y, int level, double size){
        for(Asteroid asteroid: Asteroids){
            if(asteroid.isAlive()){
                EnemyShip enemyShip = new EnemyShip(this,level,size, false);
                    Area a1 = new Area(enemyShip.getHitBox());
                    AffineTransform transform = new AffineTransform();
                    transform.setToIdentity();
                    transform.translate(x, y);

                    Area a2 = new Area(asteroid.getHitBox());
                    a1.transform(transform);
                    //a1.transform(enemyShip.getTransform());
                    a2.transform(asteroid.getTransform());
                    a1.intersect(a2);
                    if (!a1.isEmpty()) {
                        return false;
                    }
                }
            }return true;
        }

    public boolean checkPlayerPosition(){
        for (Asteroid asteroid : Asteroids) {
            if (asteroid.isAlive()) {
                Area a1 = new Area(asteroid.getHitBox());
                Rectangle a = new Rectangle(SCREEN_WIDTH/2 -15,SCREEN_HEIGHT/2 -15,30,30);
                a1.transform(asteroid.getTransform());
                Area a2 = new Area(a);
                a1.intersect(a2);
                if (!a1.isEmpty()) {
                    return false;
                }
            }
        }for (EnemyShip enemyShip: Enemys){
            Area a1 = new Area(enemyShip.getHitBox());
            Rectangle a = new Rectangle(SCREEN_WIDTH/2 -15,SCREEN_HEIGHT/2 -15,30,30);
            a1.transform(enemyShip.getTransform());
            Area a2 = new Area(a);
            a1.intersect(a2);
            if (!a1.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void resetDestroyedPlayer() {
        if(checkPlayerPosition()){
        playerBullets = new ArrayList<Bullet>();
        enemyShots = new ArrayList<EnemyShots>();
        updateCount = updateCountvalue;
        player.resetDestroyed();
        moveLevel = false;
    }else{
            pause = true;
            updateAsteroids();
            resetDestroyedPlayer();
        }
    }

    @Override
    public void moveToNextLevel() {
        try {
            listener.playSound("Sound_Effects/complete.wav");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        moveLevel = true;
        levelOnce = false;
        finishLevel = false;
        pause = true;

        updateCount = updateCountvalue;
        if (currentLevel<= levelNumber){
            currentLevel++;}

        player.resetDestroyed();
        createAsteroids(3+currentLevel);
        createEnemy(currentLevel);
        Asteroids = new ArrayList<Asteroid>();
        playerBullets = new ArrayList<Bullet>();
        enemyShots = new ArrayList<EnemyShots>();

        sparks.clear();
        shipExplosions.clear();
        bonuses.clear();

    }
    @Override
    public boolean isGameOver() {
        return !(playerLives > 0 && currentLevel <= levelNumber)|| GameOver;

    }
    public boolean getOnce(){
        return levelOnce;
    }
    public int getLevel(){
        if (currentLevel<= levelNumber){
            return currentLevel;}
        else{
            return levelNumber;
        }
    }
    public int getCurrentLevel(){
        return currentLevel;
    }
    public boolean getmoveLevel(){
        return moveLevel;
    }
    @Override
    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }
    @Override
    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public Player getShip() {
        return player;
    }
    public List<Asteroid> getAsteroids(){
        return Asteroids;
    }
    public List<Bullet> getBullets() {
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        bullets.addAll(playerBullets);
        return bullets;
    }
    public List<Spark> getSparks(){
        return sparks;
    }
    public List<ShipExplosion> getShipExplosions(){
        return shipExplosions;
    }
    public List<EnemyShots> getShots(){
        return  enemyShots;
    }
    public List<Bonus> getBonuses(){
        return bonuses;
    }
    public List<EnemyShip> getEShip(){
        return Enemys;
    }
    public boolean  getEnemyShield(){
        return EnemyShield;
    }
    public boolean getPlayerShield(){
        return playerShield;
    }
    public int getPlayerScore() {
        return playerScore;
    }
    public int getLives() {
        return playerLives;
    }
}
