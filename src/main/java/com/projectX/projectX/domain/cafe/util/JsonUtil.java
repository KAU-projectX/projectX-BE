package com.projectX.projectX.domain.cafe.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Slf4j
public class JsonUtil {

    public static String removeEmpty(String str) {
        return str.replaceAll("\\s", "");
    }

    public static boolean isContainKey(JSONObject obj, String key) {
        if (!Objects.isNull(obj)) {
            return obj.containsKey(key);
        }
        return false;
    }

    public static JSONArray parseJsonObject(JSONObject jsonObject, String[] jsonForm) {
        for (int i = 0; i < jsonForm.length - 1; ++i) {
            jsonObject = (JSONObject) jsonObject.get(jsonForm[i]);
        }

        return (JSONArray) jsonObject.get(jsonForm[jsonForm.length - 1]);
    }

    public static List<Map<String, String>> parseJsonArray(JSONArray jsonArray,
        String[] OpenAPIKeys,
        String[] mapKeys) {
        List<Map<String, String>> mapList = new ArrayList<>();

        for (Object o : jsonArray) {
            Map<String, String> libraryMap = new HashMap<>();
            JSONObject finalItem = (JSONObject) o;

            for (int i = 0; i < OpenAPIKeys.length; ++i) {
                if (isContainKey(finalItem, OpenAPIKeys[i])) {
                    libraryMap.put(mapKeys[i], (String) finalItem.get(OpenAPIKeys[i]));
                }
            }

            mapList.add(libraryMap);
        }

        return mapList;
    }

}
