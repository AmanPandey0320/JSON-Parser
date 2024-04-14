package json.parsor.exception;

import json.parsor.tokeniser.Token;

public class ProcessorException extends Error{
    private Token token;
    private String msg;

    public ProcessorException(Token token, String msg) {
        this.token = token;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg + ", but found: "+token.getTokenValue().toString();
    }
}
