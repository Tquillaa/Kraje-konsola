package countries.model;

import java.io.Serializable;

public class Score implements Serializable {
    private long time;
    private String name;
    private double score;

    public Score(long time) {
        this.time = time;
    }

    public Score(long time, String name) {
        this.time = time;
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void calculateScore(int guessed) {
        if(guessed == 0)
            this.score = 0;
        else if(guessed <= 30) {
            this.score = this.time / (double) guessed;
        } else {
            double weight = 0;
            for (int i = 0; i < guessed - 30; i++)
                weight += calculateSingleWeight(i);
            this.score = this.time / weight;
        }
    }

    private double calculateSingleWeight(int guessed) {
        if(guessed == 0) return 1;
        return calculateSingleWeight(guessed - 1) + 0.01;
    }

    @Override
    public String toString() {
        return (int) score + " " + name;
    }

}