package json.parsor.builder;

import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.lang.reflect.Field;
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
    public static HashMap<String, String> mapFieldToKey(Object object){
        HashMap<String,String> hashMap = new HashMap<>();

        if(Objects.isNull(object)){
            throw new IllegalArgumentException("Expected an object, but found NULL");
        }

        Class<?> clazz = object.getClass();

        for(Field field:clazz.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(JsonProperty.class)){
                hashMap.put(field.getName(),getKey(field));
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
}
