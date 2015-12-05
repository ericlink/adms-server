package core.medicaldevice.lifescan;

public class ParseException extends Exception {

    public ParseException(Throwable t) {
        super(t);
    }

    public ParseException(String message) {
        super(message);
    }
}
