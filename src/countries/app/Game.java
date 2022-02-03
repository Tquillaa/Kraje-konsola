package countries.app;

public class Game {
    private static final String APP_NAME = "CountriesGuesser v2.0";

    public static void main(String[] args) {
        System.out.println(APP_NAME);
        GameControl gameControl = new GameControl();
        gameControl.controlLoop();
    }
}