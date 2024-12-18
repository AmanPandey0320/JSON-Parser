package json.util;

import json.annotation.JsonProperty;
import json.builder.FieldType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    private static final Map<FieldType, Class<?>> FIELD_TYPE_MAP = new HashMap<>();

    static {
        // Initialize the mapping for FieldType to Class
        FIELD_TYPE_MAP.put(FieldType.OBJECT, Object.class);
        FIELD_TYPE_MAP.put(FieldType.STRING, String.class);
        FIELD_TYPE_MAP.put(FieldType.NUMBER, Integer.class);
        FIELD_TYPE_MAP.put(FieldType.BOOLEAN, Boolean.class);
        FIELD_TYPE_MAP.put(FieldType.ARRAY, ArrayList.class);
        // You can easily extend this by adding more mappings if needed
    }

    public static Object getObjectForField(String getterMethodName, Object object)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = object.getClass();

        try {
            // Get the method using reflection and invoke it
            Method method = clazz.getMethod(getterMethodName);
            method.setAccessible(true); // Allow access to private methods
            return method.invoke(object);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Log the exception with a meaningful message
            System.err.println("Error invoking method: " + getterMethodName + " on class " + clazz.getName());
            e.printStackTrace();
            return null; // Return null if method invocation fails
        }
    }
}
