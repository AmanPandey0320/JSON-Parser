package json.parsor.tokeniser;

import java.util.ArrayList;

import json.parsor.exception.JsonException;
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
     * @return Token
     */

    public Token parseString(){
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
                    throw new TokenException("Unexpected end of file, null pointer observed", this.lineNumber, this.columnNumber);
                }else{
                    stringBuilder.append(c);
                }
            }else if(this.matches(JsonTokens.NEWLINE)){
                throw new TokenException("Unexpected end of file, invalid token NEWLINE found", this.lineNumber, this.columnNumber);
            }else{
                if(0x20 <= c && c <= 0x10FFFF){
                    stringBuilder.append(c);
                }else{
                    throw new TokenException("Invalid token found", this.lineNumber, this.columnNumber);
                }
            }

            this.next();
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
                token = new Token(TokenType.CURLY_BRACKET_OPEN, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.CURLY_BRACKETS_CLOSE)){
                token = new Token(TokenType.CURLY_BRACKET_CLOSE, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.SQUARE_BRACKET_OPEN)){
                token = new Token(TokenType.SQUARE_BRACKET_OPEN, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.SQUARE_BRACKET_CLOSE)){
                token = new Token(TokenType.SQUARE_BRACKET_CLOSE, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.COMMA)){
                token = new Token(TokenType.COMMA, this.lineNumber, this.columnNumber);
            }else if(this.matches(JsonTokens.COLON)){
                token = new Token(TokenType.COLON, this.lineNumber, this.columnNumber);
            }else{
                /*
                * here it is:
                * string
                * +ve integer
                * -ve integer
                * exp
                *
                */

                char ch = this.front();
                if(ch == JsonTokens.NULL_CHAR){
                    throw new TokenException("Unexcepted end of file",this.lineNumber, this.columnNumber);
                }

                if(TokenUtil.isWhiteSpace(ch)){
                    // move to next token seq
                    this.next();
                    continue;
                }else if(ch == JsonTokens.DOUBLE_QUOTE){
                    //parse string token
                    this.next();
                    token = this.parseString();
                }else if(TokenUtil.isDigit(ch) || ch == JsonTokens.MINUS){
                    // parse number token
                    this.next();
                }else if(TokenUtil.isLetter(ch)){
                    throw new TokenException("Invalid string found", this.lineNumber, this.columnNumber);
                }else{
                    throw new TokenException("Unexpected token found\"" + ch + "\"",this.lineNumber,this.columnNumber);
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
