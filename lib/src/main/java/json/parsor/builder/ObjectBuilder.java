package json.parsor.builder;

import json.parsor.annotation.JsonArray;
import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;
import json.parsor.exception.AnnotationException;
import json.parsor.exception.BuilderException;
import json.parsor.tokeniser.Token;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectBuilder {

    private ArrayList<Token> tokens;
    private Object object;
    
    public ObjectBuilder(ArrayList<Token> tokens,Object object){
        this.tokens = tokens;
        this.object = object;
    }


    /*
    *
    *
    * @description get field type: normal, Array, object of a field
    * */

    private FieldType getFieldType(Field field){
        if(!field.isAnnotationPresent(JsonProperty.class)){
            throw new BuilderException("Unannotated field : "+field.getName()+" observed");
        }

        return switch (field.getAnnotation(JsonProperty.class).type()) {
            case ARRAY -> FieldType.Array;
            case OBJECT -> FieldType.Object;
            case BOOLEAN -> FieldType.Boolean;
            case NUMBER -> FieldType.Number;
            default ->  FieldType.String;
        };
    }

    private ArrayList<Object> buildArray(
            Field field,
            String fieldKey,
            HashMap<String,Token> keyTokenMap
    ) throws BuilderException {
        ArrayList<Object> arr = new ArrayList<>();
        if(!field.isAnnotationPresent(JsonArray.class)){
            throw new BuilderException("Required annotation of type JsonArray");
        }

        Class<?> clazz = field.getAnnotation(JsonArray.class).type();

        if (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Boolean.class)) {
            int idx = 0;
            while(true){
                String key = fieldKey+".@["+ idx +']';

                if(!keyTokenMap.containsKey(key)){
                    break;
                }

                arr.add(keyTokenMap.get(key).getTokenValue().toString());
                idx++;
            }
        }else{

        }

        return arr;

    }

    private Object buildObject(
            Class<?> clazz,
            HashMap<String,Token> keyTokenMap,
            HashMap<Field,String> fieldTokey,
            HashMap<String,String> fieldToSetter
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Constructor<?> constructor = clazz.getConstructor();
        Object obj = constructor.newInstance(new Object[]{});

        for(Map.Entry<Field,String> entry:fieldTokey.entrySet()){
            Field field = entry.getKey();
            String fieldKey = entry.getValue();
            Token fieldValue = keyTokenMap.get(fieldKey);



            FieldType fieldType = this.getFieldType(field);

            String setterMethodName = fieldToSetter.get(field.getName());
            Method method = null;

            if(fieldType == FieldType.Array){
                // destructure Array
                method = clazz.getMethod(setterMethodName,ArrayList.class);
                method.setAccessible(true);
                method.invoke(obj,this.buildArray(field,fieldKey,keyTokenMap));

            }else if(fieldType == FieldType.Object){
                //destructure object
//                method = clazz.getMethod(setterMethodName,ArrayList.class);
            }else if(fieldType == FieldType.Number){
                // destructure number
                method = clazz.getMethod(setterMethodName,Integer.class);
                method.setAccessible(true);
                method.invoke(obj,Integer.parseInt(fieldValue.getTokenValue().toString()));
            }else if(fieldType == FieldType.Boolean){
                // boolean
                method = clazz.getMethod(setterMethodName,Boolean.class);
                method.setAccessible(true);
                method.invoke(obj,Boolean.getBoolean(fieldValue.getTokenValue().toString()));
            }else{
                // string
                method = clazz.getMethod(setterMethodName,String.class);
                method.setAccessible(true);
                method.invoke(obj,fieldValue.getTokenValue().toString());
            }

        }

        return obj;


    }


    public Object build() throws
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException {
        Class<?> clazz = object.getClass();

        //check for annotation
        if(!clazz.isAnnotationPresent(JsonClass.class)){
            throw new AnnotationException(clazz.getSimpleName());
        }

        HashMap<String,Token> keyTokenMap;
        HashMap<Field,String> fieldToken;
        HashMap<String,String> fieldToSetter;
        TokenProcessor processor = new TokenProcessor(tokens);



        // init
        processor.init();
        keyTokenMap = processor.getObjectHashMap();
        fieldToken = PreProcessor.mapFieldToKey(object);
        fieldToSetter = PreProcessor.mapFieldToSetter(object);



        return this.buildObject(clazz,keyTokenMap,fieldToken,fieldToSetter);
    }




}
