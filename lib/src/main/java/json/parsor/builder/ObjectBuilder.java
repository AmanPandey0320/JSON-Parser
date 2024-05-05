package json.parsor.builder;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
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
            HashMap<String,Token> keyTokenMap,
            HashMap<String,Integer> arrayLengthMap
    ) throws BuilderException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        ArrayList<Object> arr = new ArrayList<>();
        if(!field.isAnnotationPresent(JsonProperty.class)){
            throw new BuilderException("Required annotation of type JsonProperty");
        }

        Class<?> clazz = field.getAnnotation(JsonProperty.class).nest();
        int idx = 0;
        int arrSize = arrayLengthMap.get(fieldKey);

        if (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Boolean.class)) {

            while(idx < arrSize){
                String key = fieldKey+".@["+ idx +']';

                if(!keyTokenMap.containsKey(key)){
                    break;
                }

                arr.add(keyTokenMap.get(key).getTokenValue().toString());
                idx++;
            }
        }else{
            //its object
            while(idx < arrSize){
                String key = fieldKey+".@["+ idx +']';
                Object arrElement = this.buildObject(key,clazz,keyTokenMap,arrayLengthMap);
                arr.add(arrElement);
                idx++;
            }
        }

        return arr;

    }

    private Object buildObject(
            String parKey,
            Class<?> clazz,
            HashMap<String,Token> keyTokenMap,
            HashMap<String,Integer> arrayLengthMap
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Constructor<?> constructor = clazz.getConstructor();
        Object obj = constructor.newInstance(new Object[]{});
        HashMap<String,String> fieldToSetter = PreProcessor.mapFieldToSetter(obj);
        HashMap<Field,String> fieldTokey = PreProcessor.mapFieldToKey(obj);

        for(Map.Entry<Field,String> entry:fieldTokey.entrySet()){
            Field field = entry.getKey();
            String fieldKey = entry.getValue();
            String realKey = parKey.isEmpty()?fieldKey:parKey+"."+fieldKey;
            Token fieldValue = keyTokenMap.get(realKey);
            FieldType fieldType = this.getFieldType(field);

            String setterMethodName = fieldToSetter.get(field.getName());
            Method method = null;

            if(fieldType == FieldType.Array){
                // destructure Array
                method = clazz.getMethod(setterMethodName,ArrayList.class);
                method.setAccessible(true);
                method.invoke(obj,this.buildArray(field,realKey,keyTokenMap,arrayLengthMap));

            }else if(fieldType == FieldType.Object){
                //destructure object
                Class<?> nestClazz = field.getAnnotation(JsonProperty.class).nest();
                method = clazz.getMethod(setterMethodName,nestClazz);
                method.setAccessible(true);
                Object nestObj = this.buildObject(
                        realKey,
                        nestClazz,
                        keyTokenMap,
                        arrayLengthMap
                );
                method.invoke(obj,nestObj);
            }else if(fieldType == FieldType.Number){
                // destructure number
                method = clazz.getMethod(setterMethodName,Integer.class);
                method.setAccessible(true);
                method.invoke(obj,Integer.parseInt(fieldValue.getTokenValue().toString()));
            }else if(fieldType == FieldType.Boolean){
                // boolean
                method = clazz.getMethod(setterMethodName,Boolean.class);
                method.setAccessible(true);
                method.invoke(obj,fieldValue.getTokenValue().toString().equals("true"));
            }else{
                // string
                System.out.println(fieldKey);
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
        HashMap<String,Integer> arrayLengthMap;
        TokenProcessor processor = new TokenProcessor(tokens);



        // init
        processor.init();
        keyTokenMap = processor.getObjectHashMap();
        arrayLengthMap = processor.getArrayLengthMap();



        return this.buildObject("",clazz,keyTokenMap,arrayLengthMap);
    }




}
