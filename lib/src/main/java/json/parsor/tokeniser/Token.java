package json.parsor.tokeniser;

public class Token {
    private TokenType tokenType;
    private Object tokenValue;

    private int column;
    private int line;

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Object getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(Object tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Token(TokenType tokenType, int column, int line) {
        this.tokenType = tokenType;
        this.tokenValue = null;
        this.column = column;
        this.line = line;
    }

    public Token(TokenType tokenType, Object val, int line, int column) {
        this.tokenType = tokenType;
        this.tokenValue = val;
        this.column = column;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token value: "+this.tokenValue.toString()+", token type: "+this.tokenType.toString();
    }
}
