package json.parsor.exception;

public class BuilderException extends Error {
    private String message;

    public BuilderException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
