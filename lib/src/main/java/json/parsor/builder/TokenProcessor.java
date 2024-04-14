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
    private ArrayList<Token> tokens;
    private Stack<Token> bracketStack;
    private LinkedList<String> keyStack;
    private int currToken;
    private int numberOfTokens;

    public TokenProcessor(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.objectHashMap = new HashMap<>();
        this.bracketStack = new Stack<>();
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

    private void nextToken(){
        this.currToken = this.currToken+1;
        return;
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

    private String generateConsolidateKey(){
        int n = this.keyStack.size();
        String key = "";
        String curr;
        while(n > 0){
            curr = this.keyStack.getFirst();
            key = key+curr;
            this.keyStack.removeFirst();
            this.keyStack.addLast(curr);
            n--;
        }
        return key;
    }

    private void parseObject() throws ProcessorException {
        // move pointer from {
        this.nextToken();

        Token token;

        while(true){
            token = this.getCurrToken();

            if(this.matchToken(token,TokenType.CURLY_BRACKET_CLOSE)){
                return;
            }

            if(this.matchToken(token,TokenType.STRING)){
                System.out.println("here------------------");
                this.parseKeyValue();
            }else{
                throw new ProcessorException(token,"Expected string");
            }
        }
    }

    private void parseArray(){
        // skip the first [

    }

    private void parseKeyValue(){
        Token token = this.getCurrToken();
        // key comes
        String key = token.getTokenValue().toString();
        this.keyStack.add(key);

        // colon comes next
        this.nextToken();
        token = this.getCurrToken();
        if(this.matchToken(token,TokenType.COLON)){

            // value comes
            this.nextToken();
            token = this.getCurrToken();
            objectHashMap.put(key,token);

            // comma comes next
            this.nextToken();
            token = this.getCurrToken();

            if(this.matchToken(token,TokenType.CURLY_BRACKET_CLOSE)){
                return;
            }

            if(this.matchToken(token,TokenType.COMMA)){
                this.nextToken();
            }else{
                throw new ProcessorException(token,"Expected ,");
            }
        }else{
            throw new ProcessorException(token,"Expected :");
        }
    }

    public void init() throws ProcessorException{
        Token token;

        while(true){
            token = this.getCurrToken();

            if(token == null){
                throw new ProcessorException(token,"Expected a valid token");
            }

            if(this.matchToken(token,TokenType.EOF)){
                break;
            }

            if(this.matchToken(token,TokenType.CURLY_BRACKET_OPEN)){
                this.bracketStack.push(token);
                this.parseObject();
            } else if (this.matchToken(token,TokenType.STRING)) {
                this.parseKeyValue();
            }else{
                throw new ProcessorException(token,"Expected a valid token");
            }

            this.nextToken();
        }
    }

}
