package json.builder.object;

import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

/*
*
* @Author Aman Kr Pandey
* */
public class PreProcessor {
    /*
    *
    * @description
    * */
    public static HashMap<Field, String> mapFieldToKey(Object object){
        HashMap<Field,String> hashMap = new HashMap<>();

        if(Objects.isNull(object)){
            throw new IllegalArgumentException("Expected an object, but found NULL");
        }

        Class<?> clazz = object.getClass();

        for(Field field:clazz.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(JsonProperty.class)){
                hashMap.put(field,getKey(field));
            }
        }

        return hashMap;
    }

    /*
    *
    *
    * */

    private static String getKey(Field field){
        String key = field.getAnnotation(JsonProperty.class).name();
        if(key.isEmpty()){
            key = field.getName();
        }
        return key;
    }

    /*
    *
    *
    * */
    private static Type getType(Field field){
        Type type = field.getAnnotation(JsonProperty.class).type();
        return type;
    }

    /*
    *
    *
    *
    * */
    public static HashMap<String, String> mapFieldToSetter(Object object){
        HashMap<String,String> hashMap = new HashMap<>();

        if(Objects.isNull(object)){
            throw new IllegalArgumentException("Expected an object, but found NULL");
        }

        Class<?> clazz = object.getClass();

        for(Method method:clazz.getDeclaredMethods()){
            String methodName = method.getName();
            if(methodName.length() > 3){
                String methodType = methodName.substring(0,3);
                String methodField = (char)(methodName.charAt(3)+32) + methodName.substring(4);
                if(methodType.equals("set")){
                    hashMap.put(methodField,methodName);
                }
            }
        }

        return hashMap;
    }
}
