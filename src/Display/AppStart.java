package Display;
import Core.AsteroidsGame;
import ScoreData.PersistentScoreKeeper;
import ucd.comp2011j.engine.GameManager;
import javax.swing.*;
import java.io.IOException;


public class AppStart {
    public static void main(String[] args){
        JFrame mainWindow = new JFrame();
        mainWindow.setSize(AsteroidsGame.SCREEN_WIDTH, AsteroidsGame.SCREEN_HEIGHT);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("Asteroids Game");
        mainWindow.setLocationRelativeTo(null);

        PlayerListener playerListener = new PlayerListener();
        mainWindow.addKeyListener(playerListener);
        AsteroidsGame game = new AsteroidsGame(playerListener);

        MenuListener menuListener = new MenuListener();
        mainWindow.addKeyListener(menuListener);
        GameScreen gameScreen = null;
        try {
            gameScreen = new GameScreen(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuScreen menuScreen = null;
        try {
            menuScreen = new MenuScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PersistentScoreKeeper scoreKeeper = new PersistentScoreKeeper();
        GameManager mmm = null;
        try {
            mmm = new GameManager(game, mainWindow, menuListener, menuScreen, new AboutScreen(), new ScoreScreen(scoreKeeper), gameScreen, scoreKeeper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainWindow.setVisible(true);
        mmm.run();



    }}
