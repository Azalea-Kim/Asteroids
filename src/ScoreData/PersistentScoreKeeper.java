package ScoreData;

import ucd.comp2011j.engine.Score;
import ucd.comp2011j.engine.ScoreKeeper;

import java.io.*;
import java.util.Scanner;

public class PersistentScoreKeeper implements ScoreKeeper {
    private Score[] scores;

    public PersistentScoreKeeper() {
        scores = new Score[10];
        loadFile();
    }

    public void loadFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt"))) {
            String line = br.readLine();
            int i = 0;
            while(line != null && i < 10) {
                Scanner s = new Scanner(line);
                int score = s.nextInt();
                String name = s.nextLine().trim();
                scores[i++] = new Score(name, score);
                line = br.readLine();
                s.close();
            }
        } catch (IOException e) {
            System.err.println("Error reading score file");
        }
    }

    @Override
    public void addScore(String name, int score) {
        scores[9] = new Score(name, score);
        sortScores();
    }

    private void sortScores() {
        Score newSS = scores[9];
        int newS = scores[9].getScore();
        for (int i = 8; i>=0;i--){
            if(newS>=scores[i].getScore()){
                scores[i+1] = scores[i];
                scores[i] = newSS;
            }
        }
    }

    public void saveScores() {
        sortScores();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scores.txt"))) {
            for(int i = 0; i < scores.length; i++) {
                bw.write(scores[i].getScore() + " " + scores[i].getName()+"\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing score file");
        }
    }

    @Override
    public Score[] getScores() {
        Score[] scs = new Score[10];
        for (int i = 0; i < scs.length; i++) {
            scs[i] = scores[i];
        }
        return scs;
    }

    @Override
    public int getLowestScore() {
        sortScores();
        return scores[9].getScore();
    }
}
