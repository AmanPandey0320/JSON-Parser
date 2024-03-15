package json.parsor.tokeniser;

import java.util.ArrayList;

import json.parsor.exception.TokenException;
import json.parsor.util.TokenUtil;


/*
*
* @description
* @author Aman Kr Pandey
* */
public class Tokeniser {

    private String jsonString;
    private int index;
    private int lineNumber;
    private int columnNumber;
    private int jsonStringLength;
    private ArrayList<Token> tokens;

    public String getJsonString() {
        return jsonString;
    }

    public int getIndex() {
        return index;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public int getJsonStringLength() {
        return jsonStringLength;
    }

    public Tokeniser(String jsonString) {
        this.jsonString = jsonString;
        this.columnNumber = 1;
        this.lineNumber = 1;
        this.index = 0;
        assert jsonString != null;
        this.jsonStringLength = jsonString.length();
        this.tokens = new ArrayList<Token>();
        this.tokenise();
    }

    /**
     *
     *
     *
     * @return
     */

    private boolean isEnd(){
        if(this.index >= this.jsonStringLength){
            return true;
        }
        return false;
    }

    private char front(){
        if(this.isEnd()) return JsonTokens.NULL_CHAR;
        return this.jsonString.charAt(this.index);
    }

    /**
     *
     *
     *
     *
     * @return
     */

    private boolean next(){
        if(this.isEnd()){
            return false;
        }else if(this.front() == JsonTokens.NEWLINE){
            this.nextLine();
        }else{
            this.columnNumber++;
        }
        this.index++;
        return true;
    }

    /**
     *
     *
     *
     * @return
     */

    private boolean nextCol(){
        if(this.isEnd()) return false;
        this.columnNumber++;
        return true;
    }

    private boolean nextLine(){
        if(this.isEnd()) return false;
        this.columnNumber = 1;
        this.lineNumber++;
        return true;
    }

    /**
     *
     *
     *
     * @param c
     * @return
     */
    private boolean matches(char c){
        if(this.isEnd()){
            return false;
        }

        if(this.front() == c){
            // it's a match
            return true;
        }
        // no match
        return false;
    }

    /**
     *
     *
     */
    private boolean matchesAny(char... tokens){
        for(char token:tokens){
            if(this.matches(token)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     * @return Token
     */

    private Token tokeniseString(){
        StringBuilder stringBuilder = new StringBuilder(100);
        Token token;
        char c;
        if(this.front() == JsonTokens.DOUBLE_QUOTE){
            this.next();
        }

        while(true){
            c = this.front();

            if(TokenUtil.isEndofString(c)){
                token = new Token(TokenType.STRING,this.lineNumber,this.columnNumber);
                token.setTokenValue(stringBuilder.toString());

                return token;
            }else if(this.matches(JsonTokens.SLASH)){
                this.next();
                c = this.front();
                if(TokenUtil.getEscape(c) == JsonTokens.HEX_UNICODE){
                    //parse hex string
                }else if(this.front() == JsonTokens.NULL_CHAR){
                    throw new TokenException(TokenConstants.NULL_POINTER_EXCEPTION, this.lineNumber, this.columnNumber);
                }else{
                    stringBuilder.append(c);
                }
            }else if(this.matches(JsonTokens.NEWLINE)){
                throw new TokenException(TokenConstants.INVALID_NEWLINE_EXCEPTION, this.lineNumber, this.columnNumber);
            }else{
                if(0x20 <= c && c <= 0x10FFFF){
                    stringBuilder.append(c);
                }else{
                    throw new TokenException(TokenConstants.INVALID_TOKEN_EXCEPTION, this.lineNumber, this.columnNumber);
                }
            }

            this.next();
        }
    }

    private Token tokeniseNumber() throws TokenException{
        StringBuilder stringBuilder = new StringBuilder(32);
        Token token;
        char c;
        while(true){
            if(this.isEnd()){
                // unexpected end of line -> throw error
                throw new TokenException(TokenConstants.UNEXPECTED_EOF_EXCEPTION,this.lineNumber, this.columnNumber);
            }

            // ending statement
            if(this.matchesAny(JsonTokens.COMMA,JsonTokens.CURLY_BRACKETS_CLOSE,JsonTokens.SQUARE_BRACKET_CLOSE)){
                token = new Token(TokenType.NUMBER, this.lineNumber, this.columnNumber);
                token.setTokenValue(stringBuilder.toString());
                return token;
            }
            if(this.matches(JsonTokens.MINUS)){
                stringBuilder.append(this.front());
                this.next();
            }

            if(this.matches(JsonTokens.PLUS)){
                stringBuilder.append(this.front());
                this.next();
            }

            // number before fraction
            if(TokenUtil.isDigit(this.front())){
                while(!this.isEnd() && TokenUtil.isDigit(this.front())){
                    stringBuilder.append(this.front());
                    this.next();
                }
            }
            if(this.matches(JsonTokens.DOT)){
                // number after fraction
                stringBuilder.append(this.front());
                this.next();

                if(this.isEnd()){
                    throw new TokenException(
                            TokenConstants.UNEXPECTED_EOF_EXCEPTION,
                            this.lineNumber,
                            this.columnNumber
                    );
                }

                if(!TokenUtil.isDigit(this.front())){
                    throw new TokenException(
                            TokenConstants.UNEXPCTED_CHAR_EXCEPTION(this.front()),
                            this.lineNumber,
                            this.columnNumber
                    );
                }

            }

            if(!this.isEnd() && (this.matchesAny(JsonTokens.CAPITAL_EXPONENT_E,JsonTokens.SMALL_EXPONENT_E))){
                //exponent
                stringBuilder.append(this.front());
                this.next();

                if(this.isEnd()){
                    throw new TokenException(
                            TokenConstants.UNEXPECTED_EOF_EXCEPTION,
                            this.lineNumber,
                            this.columnNumber
                    );
                }

                if(!TokenUtil.isDigit(this.front())){
                    throw new TokenException(
                            TokenConstants.UNEXPCTED_CHAR_EXCEPTION(this.front()),
                            this.lineNumber,
                            this.columnNumber
                    );
                }
            }
        }
    }

    private Token tokeniseBool() throws TokenException{
        StringBuilder stringBuilder = new StringBuilder(8);

        while(!this.isEnd()){
            if(TokenUtil.isLetter(this.front())){
                stringBuilder.append(this.front());
                this.next();
            }else if(this.matches(JsonTokens.COMMA)){
                //word ends here
                break;
            }else{
                throw new TokenException(
                        TokenConstants.UNEXPCTED_CHAR_EXCEPTION(this.front()),
                        this.lineNumber,
                        this.columnNumber
                );
            }
        }

        String value = stringBuilder.toString().toLowerCase();

        if(value.equals("null")){
            return new Token(TokenType.NULL,null,this.lineNumber, this.columnNumber);
        }else if(value.equals("true")){
            return new Token(TokenType.BOOL,true,this.lineNumber,this.columnNumber);
        }else if(value.equals("false")){
            return new Token(TokenType.BOOL,false,this.lineNumber,this.columnNumber);
        }else{
            throw new TokenException(
                    TokenConstants.INVALID_TOKEN_EXCEPTION,
                    this.lineNumber,
                    this.columnNumber
            );
        }
    }




    /**
     *
     *
     *
     */
    private void tokenise(){

        Token token = null;

        while(!this.isEnd()){
            token = null;
            if(this.matches(JsonTokens.CURLY_BRACKETS_OPEN)){
                token = new Token(TokenType.CURLY_BRACKET_OPEN, JsonTokens.CURLY_BRACKETS_OPEN, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.CURLY_BRACKETS_CLOSE)){
                token = new Token(TokenType.CURLY_BRACKET_CLOSE,JsonTokens.CURLY_BRACKETS_CLOSE, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.SQUARE_BRACKET_OPEN)){
                token = new Token(TokenType.SQUARE_BRACKET_OPEN,JsonTokens.SQUARE_BRACKET_OPEN, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.SQUARE_BRACKET_CLOSE)){
                token = new Token(TokenType.SQUARE_BRACKET_CLOSE,JsonTokens.SQUARE_BRACKET_CLOSE, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.COMMA)){
                token = new Token(TokenType.COMMA,JsonTokens.COMMA, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.COLON)){
                token = new Token(TokenType.COLON,JsonTokens.COLON, this.lineNumber, this.columnNumber);
            }else{
                char ch = this.front();
                if(ch == JsonTokens.NULL_CHAR){
                    throw new TokenException(TokenConstants.UNEXPECTED_EOF_EXCEPTION,this.lineNumber, this.columnNumber);
                }

                if(TokenUtil.isWhiteSpace(ch)){
                    // move to next token seq
                    this.next();
                    continue;
                }else if(ch == JsonTokens.DOUBLE_QUOTE){
                    //parse string token
                    this.next();
                    token = this.tokeniseString();
                }else if(TokenUtil.isDigit(ch) || this.matches(JsonTokens.MINUS) || this.matches(JsonTokens.PLUS) ){
                    // parse number token
                    token = this.tokeniseNumber();
                }else if(TokenUtil.isLetter(ch)){
                    token = this.tokeniseBool();
                }else{
                    throw new TokenException(TokenConstants.UNEXPCTED_CHAR_EXCEPTION(ch),this.lineNumber,this.columnNumber);
                }
            }

            if(token != null) this.tokens.add(token);
            this.next();
        }

        token = new Token(TokenType.EOF,this.lineNumber,this.columnNumber);
        this.tokens.add(token);

        return;

    }
}
