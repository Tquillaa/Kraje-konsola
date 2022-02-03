package countries.exeption;

public class GiveUpException extends RuntimeException {
    public GiveUpException(String message) {
        super(message);
    }
}