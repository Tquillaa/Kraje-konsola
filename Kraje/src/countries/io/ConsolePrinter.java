package countries.io;

public class ConsolePrinter {

    public void printLine() {
        System.out.println();
    }

    public void printLine(String line) {
        System.out.println(line);
    }

    public void printErrorLine(String line) {
        System.err.println(line);
    }

    public void printText(String text) {
        System.out.print(text);
    }
}