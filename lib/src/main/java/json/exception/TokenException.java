package json.exception;

public class TokenException extends Error {

    private int lineNumber;
    private int columnNumber;

    public TokenException(String cause, int lineNumber, int columnNumber) {
        super(cause);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String getMessage() {
        return "Error parsing JSON in line " + this.lineNumber + " at character " + this.columnNumber + ": " + super.getMessage();
    }

}
