package json.parsor.builder;

import json.parsor.annotation.JsonClass;
import json.parsor.exception.AnnotationException;
import json.parsor.tokeniser.Token;
import json.parsor.tokeniser.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Supplier;

public class ObjectBuilder {

    private ArrayList<Token> tokens;
    private Object object;
    private int tokenIdx;
    private int totalTokens;
    
    public ObjectBuilder(ArrayList<Token> tokens,Object object){
        this.tokens = tokens;
        this.object = object;
        this.tokenIdx = 0;
        this.totalTokens = tokens.size();
    }

    public Object build(){
        Class<?> clazz = object.getClass();
        HashMap<String,String> fieldToKey;
        TokenProcessor processor = new TokenProcessor(tokens);

        //check for annotation
        if(!clazz.isAnnotationPresent(JsonClass.class)){
            throw new AnnotationException(clazz.getSimpleName());
        }

        fieldToKey = PreProcessor.mapFieldToKey(object);

        for(Map.Entry<String,String> entry:fieldToKey.entrySet()){
            System.out.println("field: "+entry.getKey()+", key: "+entry.getValue());
        }

        // init
        processor.init();

        System.out.println("\n-------------------------\n");

        for(Map.Entry<String,Token> entry:processor.getObjectHashMap().entrySet()){
            System.out.println("key: "+entry.getKey()+" value: "+entry.getValue().getTokenValue().toString());
        }

        return object;
    }




}
