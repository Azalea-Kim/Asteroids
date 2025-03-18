package Display;
import javax.sound.sampled.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class PlayerListener implements KeyListener {
    public static void playSound(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        File url = new File(fileName);
        Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream( url );
        clip.open(ais);
        clip.start();
    }


    public void reset(){
        left  = false;
        right= false;
        fire= false;
        pause= false;
        thrust= false;
        brake= false;
        exit= false;
        hyper= false;
    }

    private boolean left;
    private boolean right;
    private boolean fire;
    private boolean pause;
    private boolean thrust;
    private boolean brake;
    private boolean exit;
    private boolean hyper;
    public boolean isPressingLeft() {
        return left;
    }
    public boolean isPressingRight() {
        return right;
    }

    public boolean isPressingFire() {
        return fire;
    }

    public boolean hasPressedPause() {
        return pause;
    }
    public boolean isPressingBrake() {
        return brake;
    }

    public boolean hasPressedExit() {
        return exit;
    }
    public boolean isPressingThrust() {
        return thrust;
    }


    public boolean hasPressedHyper() {
        return hyper;
    }


    @Override
    //keyTyped
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'P' || e.getKeyChar() == 'p') {
            pause = true;
        }else if (e.getKeyChar() == 'h' || e.getKeyChar() == 'H') {
            hyper = true;}
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            fire = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            thrust = true;
        } else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
            brake = true;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;}
        else if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X') {
            exit = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            thrust = false;
        }else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
            brake = false;}
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            fire = false;}
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;}
    }

    public void resetPause() {
        pause = false;
    }

    public void resetFire() {//!!!!!
        //fire = false;
    }
    public void resetExit() {
        exit = false;
    }
    public void resetHyper() {
        hyper = false;
    }

}



