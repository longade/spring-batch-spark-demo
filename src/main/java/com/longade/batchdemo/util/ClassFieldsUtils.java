package com.longade.batchdemo.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClassFieldsUtils {

    public static <T> Map<String, String> getFieldsMapping(Class<T> aClass) {
        Map<String, String> fieldsMapping = new HashMap<>();

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String originalFieldName = field.getName();
            fieldsMapping.put(camelToSnake(originalFieldName), originalFieldName);
        }

        return fieldsMapping;
    }

    private static String camelToSnake(final String camelStr) {
        String ret = camelStr
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2");
        return ret.toLowerCase();
    }

}
