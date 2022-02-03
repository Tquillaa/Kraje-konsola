package countries.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scoreboard implements Serializable {
    private List<Score> scoreboardList = new ArrayList<>();
    public static final String SCOREBOARD_FILE_PATH = "score.board";

    public List<Score> getScoreboardList() {
        return scoreboardList;
    }

    private void sort() {
        scoreboardList.sort(Comparator.comparing(Score::getTime));
    }

    public void addScore(Score score) {
        scoreboardList.add(score);
        this.sort();
    }

    public void replaceLast(Score score) {
        scoreboardList.set(9, score);
        this.sort();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Score score : scoreboardList) {
            sb.append(score.toString()).append("  ");
        }
        return sb.toString();
    }
}