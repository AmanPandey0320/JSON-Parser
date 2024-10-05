package json.parsor;

public class TokenConstants {
    public final static String UNEXPECTED_EOF_EXCEPTION = "Unexpected end of file";
    public final static String NULL_POINTER_EXCEPTION = "Unexpected end of file, null pointer observed";
    public final static String INVALID_NEWLINE_EXCEPTION = "Unexpected end of file, invalid token NEWLINE found";

    public final static String INVALID_TOKEN_EXCEPTION = "Invalid token found";
    public final static String INVALID_STRING_EXCEPTION = "Invalid string found";

    public static String UNEXPCTED_TOKEN_EXCEPTION = "Unexpected token found";
    public final static String UNEXPCTED_CHAR_EXCEPTION(char ch){
        return "Unexpected token found\""+ch+"\"";
    }
}
