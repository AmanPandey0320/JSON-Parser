package json.parsor.exception;

public class AnnotationException extends Error{
    private String className;

    public AnnotationException(String className){
        this.className = className;
    }

    @Override
    public String getMessage() {
        return "The class " + this.className + " is not annotated with JsonSerializable";
    }
}
