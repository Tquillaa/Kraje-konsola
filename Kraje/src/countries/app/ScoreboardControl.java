package countries.app;

import countries.exeption.DataImportException;
import countries.io.ConsolePrinter;
import countries.io.DataReader;
import countries.model.Score;
import countries.model.Scoreboard;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ScoreboardControl {
    private Scoreboard scoreboard;
    private ConsolePrinter printer;
    private DataReader dataReader;

    public ScoreboardControl(ConsolePrinter printer, DataReader dataReader) {
        this.printer = printer;
        this.dataReader = dataReader;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public List<Score> getScoreboardList() {
        return scoreboard.getScoreboardList();
    }

    public void initializeScoreboardFromDisc() throws DataImportException {
        try (
                var fis = new FileInputStream(Scoreboard.SCOREBOARD_FILE_PATH);
                var ois = new ObjectInputStream(fis);
            ) {
            this.scoreboard = (Scoreboard) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new DataImportException("File " + Scoreboard.SCOREBOARD_FILE_PATH + " not found.");
        } catch (IOException e) {
            throw new DataImportException("Error occurred while trying to read " + Scoreboard.SCOREBOARD_FILE_PATH + ".");
        } catch (ClassNotFoundException e) {
            throw new DataImportException("Mismatch data type in file " + Scoreboard.SCOREBOARD_FILE_PATH + ".");
        }
    }

    public void initializeNewScoreboard() {
        scoreboard = new Scoreboard();
    }

    public void saveScoreboard() {
        boolean closeFlag = true;
        do {
            try (
                    var fos = new FileOutputStream(Scoreboard.SCOREBOARD_FILE_PATH);
                    var oos = new ObjectOutputStream(fos);
            ) {
                oos.writeObject(scoreboard);
                closeFlag = false;
            } catch (FileNotFoundException e) {
                printer.printErrorLine("File not found, press Y/y to close without saving, or anything else to try again.");
                String choice = dataReader.getStringCaseInsensitive();
                if(choice.equals("Y") || choice.equals("y")) closeFlag = false;
            } catch (IOException e) {
                printer.printErrorLine("Saving scoreboard to file failed, press Y/y to close without saving, or anything else to try again.");
                String choice = dataReader.getStringCaseInsensitive();
                if(choice.equals("Y") || choice.equals("y")) closeFlag = false;
            }
        } while (closeFlag);
    }

    public void addScore(Score score) {
        scoreboard.addScore(score);
    }

    public void replaceLast(Score score) {
        scoreboard.replaceLast(score);
    }

    public void printScoreboard() {
        int i = 1;
        for (Score score : scoreboard.getScoreboardList()) {
            printer.printLine(i + ". " + score.toString());
            i++;
        }
    }
}
