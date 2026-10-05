package exception;

public class InvalidFileFormatException extends Exception {

    public InvalidFileFormatException(int lineNumber, String line) {
        super("Строка " + lineNumber
                + ": формат словаря должен быть «слово или выражение | перевод», получено: «"
                + line + "»");
    }
}
