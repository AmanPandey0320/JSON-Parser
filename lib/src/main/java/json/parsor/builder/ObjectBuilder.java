package json.parsor.builder;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;
import json.parsor.exception.AnnotationException;
import json.parsor.tokeniser.Token;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectBuilder {

    private ArrayList<Token> tokens;
    private Object object;
    HashMap<String, Field> keyToField;
    HashMap<String,String> fieldToSetter;
    
    public ObjectBuilder(ArrayList<Token> tokens,Object object){
        this.tokens = tokens;
        this.object = object;
        this.keyToField = PreProcessor.mapFieldToKey(object);
        this.fieldToSetter = PreProcessor.mapFieldToSetter(object);
    }

    public Object build() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = object.getClass();

        HashMap<String,Token> keyTokenMap;
        TokenProcessor processor = new TokenProcessor(tokens);

        //check for annotation
        if(!clazz.isAnnotationPresent(JsonClass.class)){
            throw new AnnotationException(clazz.getSimpleName());
        }

        // init
        processor.init();
        keyTokenMap = processor.getObjectHashMap();

        for(Map.Entry<String,Token> entry:keyTokenMap.entrySet()){
            String key = entry.getKey();
            if(this.keyToField.containsKey(key)){
                Field field = this.keyToField.get(key);
                if(this.fieldToSetter.containsKey(field.getName())){
                    String setterMethodName = this.fieldToSetter.get(field.getName());
                    Method method = null;
                    switch (field.getAnnotation(JsonProperty.class).type()){
                        case STRING:
                            method = clazz.getMethod(setterMethodName,String.class);
                            method.setAccessible(true);
                            method.invoke(object,entry.getValue().getTokenValue().toString());
                            break;
                        case NUMBER:
                            method = clazz.getMethod(setterMethodName,int.class);
                            method.setAccessible(true);
                            method.invoke(object,Integer.getInteger(entry.getValue().getTokenValue().toString()));
                            break;
                        case OBJECT:
                            break;
                        case ARRAY:
                            break;
                        case BOOLEAN:
                            break;
                        default:
                            break;

                    }
                }
            }
        }

        return object;
    }




}
