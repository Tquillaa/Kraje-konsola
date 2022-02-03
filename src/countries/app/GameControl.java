package countries.app;

import countries.exeption.DataImportException;
import countries.exeption.GiveUpException;
import countries.exeption.NoSuchOptionException;
import countries.io.ConsolePrinter;
import countries.io.DataReader;
import countries.model.Score;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class GameControl {
    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);

    private final String GIVE_UP = "Q";
    private final String PRINT_GUESSED = "P";

    private Language language = null;

    private ScoreboardControl scoreboardControl = new ScoreboardControl(printer, dataReader);
    private Map<String, Boolean> gameContainer = new HashMap<>();
    private int guessed = 0;

    public GameControl() {
        chooseLanguage();
        try {
            scoreboardControl.initializeScoreboardFromDisc();
        } catch (DataImportException e) {
            printer.printErrorLine(e.getMessage() + " Initializing new scoreboard");
            scoreboardControl.initializeNewScoreboard();
        }
    }

    public void controlLoop() {
        OptionMainMenu option;

        do {
            printMainMenu();
            option = getOptionMainMenu();
            switch (option) {
                case EXIT:
                    exit();
                    break;
                case PLAY:
                    startGuessing();
                    break;
                case ADD_MORE_COUNTRIES:
                    addCountries();
                    break;
                case DELETE_COUNTRIES:
                    deleteCountries();
                    break;
                case SEE_COUNTRIES:
                    printLoadedCountries();
                    break;
                case PRINT_SCOREBOARD:
                    printScoreboard();
                    break;
            }
        } while (option != OptionMainMenu.EXIT);
    }

    public void chooseLanguage() {
        do {
            printLanguages();
            this.language = getLanguage();
        } while (this.language == null);
    }

    private void printLanguages() {
        printer.printLine("Choose guessing language : ");
        for (Language language: Language.values()) {
            printer.printLine(language.toString());
        }
    }

    private Language getLanguage() {
        boolean languageFlag = false;
        Language language = null;
        while (!languageFlag) {
            try {
                language = Language.createFromInt(dataReader.getInt());
                languageFlag = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", choose again: ");
            } catch (InputMismatchException ignored) {
                printer.printLine("Inserted value is incorrect. Try again and insert integer: ");
            }
        }
        return language;
    }

    private OptionMainMenu getOptionMainMenu() {
        boolean optionFlag = false;
        OptionMainMenu option = null;
        while (!optionFlag) {
            try {
                option = OptionMainMenu.createFromInt(dataReader.getInt());
                optionFlag = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", choose again: ");
            } catch (InputMismatchException ignored) {
                printer.printLine("Inserted value is incorrect. Try again and insert integer: ");
            }
        }
        return option;
    }

    private OptionCountriesMenu getOptionCountriesMenu() {
        boolean optionFlag = false;
        OptionCountriesMenu option = null;
        while (!optionFlag) {
            try {
                option = OptionCountriesMenu.createFromInt(dataReader.getInt());
                optionFlag = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", choose again: ");
            } catch (InputMismatchException ignored) {
                printer.printLine("Inserted value is incorrect. Try again and insert integer: ");
            }
        }
        return option;
    }

    private void printMainMenu() {
        printer.printLine("Choose option: ");
        for (OptionMainMenu option: OptionMainMenu.values()) {
            printer.printLine(option.toString());
        }
        printer.printLine("Already " + gameContainer.size() + " countries selected.");
    }

    private void printCountriesMenu() {
        printer.printLine("Choose continent: ");
        for (OptionCountriesMenu option: OptionCountriesMenu.values()) {
            printer.printLine(option.toString());
        }
    }

    private void addCountries() {
        OptionCountriesMenu option;

        do {
            printCountriesMenu();
            option = getOptionCountriesMenu();
            switch (option) {
                case AFRICA:
                    loadContinent(Paths.AFRICA.path[this.language.value]);
                    break;
                case ASIA:
                    loadContinent(Paths.ASIA.path[this.language.value]);
                    break;
                case AUSTRALIA:
                    loadContinent(Paths.AUSTRALIA.path[this.language.value]);
                    break;
                case EUROPE:
                    loadContinent(Paths.EUROPE.path[this.language.value]);
                    break;
                case NORTH_AMERICA:
                    loadContinent(Paths.NORTH_AMERICA.path[this.language.value]);
                    break;
                case SOUTH_AMERICA:
                    loadContinent(Paths.SOUTH_AMERICA.path[this.language.value]);
                    break;
                case ALL_COUNTRIES:
                    loadContinent(Paths.ALL_COUNTRIES.path[this.language.value]);
                    break;
                case UNRECOGNIZED_COUNTRIES:
                    loadContinent(Paths.UNRECOGNIZED_COUNTRIES.path[this.language.value]);
                    break;
            }
        } while (option != OptionCountriesMenu.BACK);
    }

    private void loadContinent(String path) {
        try (
                var fileReader = new FileReader(path);
                var reader = new BufferedReader(fileReader);
        ) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                gameContainer.put(nextLine.toUpperCase(), false);
            }
            printer.printLine("Added countries from selected continent.");
        } catch (IOException e) {
            printer.printErrorLine("Couldn't open file " + path);
        }
    }

    private void deleteCountries() {
        OptionCountriesMenu option;

        do {
            printCountriesMenu();
            option = getOptionCountriesMenu();
            switch (option) {
                case AFRICA:
                    deleteContinent(Paths.AFRICA.path[this.language.value]);
                    break;
                case ASIA:
                    deleteContinent(Paths.ASIA.path[this.language.value]);
                    break;
                case AUSTRALIA:
                    deleteContinent(Paths.AUSTRALIA.path[this.language.value]);
                    break;
                case EUROPE:
                    deleteContinent(Paths.EUROPE.path[this.language.value]);
                    break;
                case NORTH_AMERICA:
                    deleteContinent(Paths.NORTH_AMERICA.path[this.language.value]);
                    break;
                case SOUTH_AMERICA:
                    deleteContinent(Paths.SOUTH_AMERICA.path[this.language.value]);
                    break;
                case ALL_COUNTRIES:
                    deleteContinent(Paths.ALL_COUNTRIES.path[this.language.value]);
                    break;
                case UNRECOGNIZED_COUNTRIES:
                    deleteContinent(Paths.UNRECOGNIZED_COUNTRIES.path[this.language.value]);
                    break;
            }
        } while (option != OptionCountriesMenu.BACK);
    }

    private void deleteContinent(String path) {
        try (
                var fileReader = new FileReader(path);
                var reader = new BufferedReader(fileReader);
        ) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                gameContainer.remove(nextLine.toUpperCase());
            }
        } catch (IOException e) {
            printer.printErrorLine("Couldn't open file " + path);
        }
    }

    private void printLoadedCountries() {
        for(String country: gameContainer.keySet())
            printer.printLine(country);
        printer.printLine(gameContainer.size() + " countries loaded");
    }

    private void startGuessing() {
        if(gameContainer.size() > 0) {
            restart();
            boolean giveUpFlag = true;
            printer.printLine("Good luck!");
            printer.printLine("(Q - give up, P - print guessed countries)");
            Instant timeStart = Instant.now();
            while (guessed != gameContainer.size() && giveUpFlag) {
                try {
                    printer.printText("Your guess: ");
                    if (guess()) guessed++;
                    printer.printLine("You guessed " + guessed + " countries. " + (gameContainer.size() - guessed) + " left.");
                } catch (GiveUpException e) {
                    giveUpFlag = false;
                }
            }
            Instant timeEnd = Instant.now();

            Score score = createScore(timeStart, timeEnd, guessed);

            printer.printLine("Your time: " + score.getTime() + " ms, gives you " + score.getScore() + " score.");
        }
        else printer.printLine("Add countries from at least one continent.");
    }

    private Score createScore(Instant timeStart, Instant timeEnd, int guessed) {
        Score score = new Score(calculateTimeToMillis(timeStart, timeEnd));
        score.calculateScore(guessed);
        qualifiedScore(score);
        return score;
    }

    private long calculateTimeToMillis(Instant timeStart, Instant timeEnd) {
        return Duration.between(timeStart, timeEnd).toMillis();
    }

    private void qualifiedScore(Score score) {
        signScore(score);
        if(scoreboardControl.getScoreboardList().size() < 10)
            scoreboardControl.addScore(score);
        else if(qualified(score))
            scoreboardControl.replaceLast(score);
    }

    private boolean qualified(Score score) {
        return scoreboardControl.getScoreboardList().get(9).getScore() > score.getScore();
    }

    private void signScore(Score score) {
        printer.printText("Congratulations you're in TOP 10! Insert your name: ");
        score.setName(dataReader.getString());
    }

    private boolean guess() {
        String guess = dataReader.getStringCaseInsensitive();
        if(guess.equals(GIVE_UP)) {
//            printRemaining();
            throw new GiveUpException("User gave up");
        }
        if(guess.equals(PRINT_GUESSED)) {
            printGuessed();
        }
        if (gameContainer.containsKey(guess)) {
            gameContainer.replace(guess, true);
            return true;
        }
        return false;
    }

    private void restart() {
        for (String country: gameContainer.keySet()) {
            gameContainer.replace(country, false);
        }
        guessed = 0;
    }

    private void printRemaining() {
        for(String country: gameContainer.keySet()){
            if(!gameContainer.get(country))
                printer.printErrorLine(country);
        }
    }

    private void printGuessed() {
        for(String country: gameContainer.keySet()){
            if(gameContainer.get(country))
                printer.printText(country + " ");
        }
        printer.printLine();
    }

    private void printScoreboard() {
        scoreboardControl.printScoreboard();
    }

    private void exit() {
        scoreboardControl.saveScoreboard();
        dataReader.close();
    }

    private enum OptionMainMenu {
        EXIT(0, "Exit"),
        PLAY(1, "Play"),
        ADD_MORE_COUNTRIES(2, "Add more countries"),
        DELETE_COUNTRIES(3, "Delete countries"),
        SEE_COUNTRIES(4, "See added countries"),
        PRINT_SCOREBOARD(5, "Print scoreboard");

        private int value;
    private String description;

        OptionMainMenu(int value, String desc) {
            this.value = value;
            this.description = desc;
        }

        public static OptionMainMenu createFromInt(int option) {
            try {
                return OptionMainMenu.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("No option with id " + option);
            }
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

}

    private enum OptionCountriesMenu {
        BACK(0, "Back"),
        AFRICA(1, "Africa"),
        ASIA(2, "Asia"),
        AUSTRALIA(3, "Australia"),
        EUROPE(4, "Europe"),
        NORTH_AMERICA(5, "North America"),
        SOUTH_AMERICA(6, "South America"),
        ALL_COUNTRIES(7, "All countries"),
        UNRECOGNIZED_COUNTRIES(8, "Unrecognized countries");

        private int value;
        private String description;

        OptionCountriesMenu(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static OptionCountriesMenu createFromInt(int option) {
            try {
                return OptionCountriesMenu.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("No option with id " + option);
            }
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

    }

    private enum Paths {
        AFRICA(new String[]{"src/countries/countries/AfricaPL.txt", "src/countries/countries/AfricaEN.txt"}),
        ASIA(new String[] {"src/countries/countries/AsiaPL.txt", "src/countries/countries/AsiaEN.txt"}),
        AUSTRALIA(new String[] {"src/countries/countries/AustraliaPL.txt", "src/countries/countries/AustraliaEN.txt"}),
        EUROPE(new String[] {"src/countries/countries/EuropePL.txt", "src/countries/countries/EuropeEN.txt"}),
        NORTH_AMERICA(new String[] {"src/countries/countries/NorthAmericaPL.txt", "src/countries/countries/NorthAmericaEN.txt"}),
        SOUTH_AMERICA(new String[] {"src/countries/countries/SouthAmericaPL.txt", "src/countries/countries/SouthAmericaEN.txt"}),
        ALL_COUNTRIES(new String[] {"src/countries/countries/AllCountriesPL.txt", "src/countries/countries/AllCountriesEN.txt"}),
        UNRECOGNIZED_COUNTRIES(new String[] {"src/countries/countries/UnrecognizedCountriesPL.txt", "src/countries/countries/UnrecognizedCountriesEN.txt"});

        private String[] path;

        Paths(String[] path) {
            this.path = path;
        }
    }

    private enum Language {
        POLISH(0, "polski"),
        ENGLISH(1, "english");

        private int value;
        private String language;

        Language(int value, String language) {
            this.value = value;
            this.language = language;
        }

        public static Language createFromInt(int option) {
            try {
                return Language.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("No option with id " + option);
            }
        }

        @Override
        public String toString() {
            return value + " - " + language;
        }
    }
}