package countries.exeption;

public class DataImportException extends RuntimeException {
    public DataImportException(String message) {
        super(message);
    }
}