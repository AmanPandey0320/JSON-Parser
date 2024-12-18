package json.builder.string;

import json.annotation.JsonProperty;
import json.builder.FieldType;
import json.util.PreProcessor;
import json.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringBuilder {

    public static String build(Object obj) {
        try{
            String jsonString = "";

            getKeyToValueMap(obj);

            return jsonString;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String,Object> getKeyToValueMap(Object object) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        HashMap<Field,String> fieldToKeyMap = PreProcessor.mapFieldToKey(object);
        HashMap<String,String> fieldToGetterMap = PreProcessor.mapFieldToFunction(object,"get");
        HashMap<String,Object> keyValueMap = new HashMap<>();

        String getterMethodName = null;

        Class<?> clazz = object.getClass();

        for(Map.Entry<Field,String> entry:fieldToKeyMap.entrySet()){
            getterMethodName = fieldToGetterMap.get(entry.getKey().getName());
            keyValueMap.put(entry.getValue(), ReflectionUtils.getObjectForField(getterMethodName, object));
        }

        for(Map.Entry<String,Object> entry:keyValueMap.entrySet()){
            System.out.println("KEY -> "+entry.getKey());
            System.out.println("VALUE -> "+entry.getValue().toString());
        }

        return keyValueMap;
    }



}
