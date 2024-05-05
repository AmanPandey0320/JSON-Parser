package json.parsor.builder;

import json.parsor.exception.ProcessorException;
import json.parsor.tokeniser.Token;
import json.parsor.tokeniser.TokenType;

import java.util.*;

/*
*
* @author Aman Kr Pandey
* */
public class TokenProcessor {
    private HashMap<String,Token> objectHashMap;
    private HashMap<String, Integer> arrayLengthMap;
    private ArrayList<Token> tokens;
    private LinkedList<String> keyStack;
    private int currToken;
    private int numberOfTokens;


    public HashMap<String, Integer> getArrayLengthMap() {
        return arrayLengthMap;
    }

    public TokenProcessor(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.objectHashMap = new HashMap<>();
        this.arrayLengthMap = new HashMap<>();
        this.keyStack = new LinkedList<>();
        this.currToken = 0;
        this.numberOfTokens = tokens.size();
    }

    public HashMap<String, Token> getObjectHashMap() {
        return objectHashMap;
    }

    private boolean isFinish(){
        return this.currToken >= this.numberOfTokens;
    }

    private Token nextToken(){
        this.currToken = this.currToken+1;
        return this.getCurrToken();
    }

    private Token getCurrToken(){
        if(!this.isFinish()) return this.tokens.get(this.currToken);
        throw new RuntimeException("Unexpected EOF observed for tokens");
    }

    private boolean matchToken(Token token, TokenType type){
        return !this.isFinish() && type == token.getTokenType();
    }

    private boolean matchAndMoveToken(Token token, TokenType type){
        if(this.matchToken(token,type)){
            this.nextToken();
            return true;
        }
        return false;
    }

    private void processArray(String key) throws ProcessorException{
        Token token = this.nextToken();

        if(this.matchToken(token,TokenType.SQUARE_BRACKET_CLOSE)){
            return;
        }

        int idx = -1;

        while(true){
            idx++;
            token = this.getCurrToken();
            String currKey = !key.isEmpty() ? key+'.'+"@["+String.valueOf(idx)+']' : "@["+String.valueOf(idx)+']';

            this.processTokens(currKey);
            token = this.nextToken();
            if(!this.matchToken(token,TokenType.SQUARE_BRACKET_CLOSE)){
                if(!this.matchToken(token,TokenType.COMMA)){
                    throw new ProcessorException(token,"Expected a comma or closing square bracket");
                }else{
                    this.nextToken();
                }
            }else{
                break;
            }

        }

        System.out.println(key+"->"+idx+1);

        this.arrayLengthMap.put(key,idx+1);


    }

    private void processObject(String key) throws  ProcessorException{
        Token token = this.nextToken();

        if(this.matchToken(token,TokenType.CURLY_BRACKET_CLOSE)){
            return;
        }

        while(true){
            token = this.getCurrToken();

            if(!this.matchToken(token, TokenType.STRING)){
                throw new ProcessorException(token,"Expected a string");
            }
            String currKey = !key.isEmpty() ? key+'.'+token.getTokenValue().toString() : token.getTokenValue().toString();
            token = this.nextToken();

            if(!this.matchToken(token, TokenType.COLON)){
                throw new ProcessorException(token,"Expected a colon");
            }
            this.nextToken();
            this.processTokens(currKey);
            token = this.nextToken();

            if(!this.matchToken(token,TokenType.CURLY_BRACKET_CLOSE)){
                if(!this.matchToken(token,TokenType.COMMA)){
                    throw new ProcessorException(token,"Expected a comma or closing curly bracket");
                }else{
                    this.nextToken();
                }
            }else{
                return;
            }
        }
    }

    private void processTokens(String key) throws ProcessorException{
        Token token = this.getCurrToken();
        switch (token.getTokenType()){
            case CURLY_BRACKET_OPEN :
                this.processObject(key);
                break;
            case SQUARE_BRACKET_OPEN:
                this.processArray(key);
                break;
            case NUMBER, STRING, BOOL:
                this.objectHashMap.put(key,token);
                break;
            case NULL:
                break;
        }
    }



    public void init() throws ProcessorException{
        this.processTokens("");
    }

}
