package com.projectX.projectX.domain.cafe.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeBulkRepository;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.cafe.util.HttpUtil;
import com.projectX.projectX.global.common.CafeType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final CafeBulkRepository cafeBulkRepository;
    private final CafeRepository cafeRepository;

    @Value("${jeju-datahub.project-key}")
    private String project_key;
    @Value("${jeju-datahub.base-url}")
    private String base_url;

    @Value("${kakao.key}")
    private String kakao_key;
    @Value("${kakao.base-url}")
    private String kakao_base_url;

    public void createLibrary() {
        List<Map<String, String>> libraryList = getLibrary("create");
        if (!libraryList.isEmpty()) {
            cafeBulkRepository.saveCafe(libraryList);
        }
    }

    public void updateLibrary() {
        List<Map<String, String>> libraryList = getLibrary("update");
        if (!libraryList.isEmpty()) {
            cafeBulkRepository.updateCafe(libraryList);
        }
    }

    private List<Map<String, String>> getLibrary(String method) {
        String url = base_url + project_key + "?limit=50";
        String[] jsonForm = {"data"};
        String[] openAPIKeys = {"libraryName", "address"};
        String[] libraryKeys = {"name", "address", "cafeType"};

        JSONObject jsonObject = HttpUtil.connectHttp(url, "Content-type",
            "application/json");
        JSONArray jsonArray = parseJsonObject(jsonObject, jsonForm);
        List<Map<String, String>> libraryList = parseJsonArray(jsonArray, openAPIKeys, libraryKeys);
        List<Map<String, String>> midLibraryList = addCafeType(libraryList, 4);
        List<Map<String, String>> finalLibraryList = checkAlreadyExist(midLibraryList, method);

        return getLatitudeAndLongitude(finalLibraryList);
    }

    private List<Map<String, String>> getLatitudeAndLongitude(List<Map<String, String>> mapList) {
        String[] jsonForm = {"documents"};
        String[] openAPIKeys = {"address_name", "road_address_name"};
        String[] insertKeys = {"longitude", "latitude"};
        String[] insertAPIKeys = {"x", "y"};

        if (mapList.isEmpty()) {
            return mapList;
        }

        for (Map<String, String> map : mapList) {
            String target = map.get("name");
            String query = URLEncoder.encode(target, StandardCharsets.UTF_8);
            String url = kakao_base_url + "query=" + query;

            JSONObject jsonObject = HttpUtil.connectHttp(url, "Authorization", kakao_key);
            JSONArray jsonArray = parseJsonObject(jsonObject, jsonForm);

            String address = map.get("address");
            String target_address = address.replaceAll("\\s", "");
            log.info(address);

            JSONObject object = checkPlace(jsonArray, openAPIKeys, target_address);
            log.info(object.toJSONString());

            for (int i = 0; i < insertAPIKeys.length; ++i) {
                if (object.isEmpty()) {
                    map.put(insertKeys[i], "0");
                } else {
                    map.put(insertKeys[i], (String) object.get(insertAPIKeys[i]));
                }
            }
        }

        return mapList;
    }

    private JSONObject checkPlace(JSONArray jsonArray, String[] checkKeys, String target) {
        for (Object o : jsonArray) {
            JSONObject item = (JSONObject) o;

            for (String keys : checkKeys) {
                if (HttpUtil.isContainKey(item, keys)) {
                    String address = item.get(keys).toString();
                    String json_address = address.replaceAll("\\s", "");
                    if (json_address.contains(target) || target.contains(json_address)) {
                        return item;
                    }
                }
            }
        }

        return new JSONObject();
    }

    private List<Map<String, String>> checkAlreadyExist(List<Map<String, String>> mapList,
        String method) {
        List<Map<String, String>> createMapList = new ArrayList<>();
        List<Map<String, String>> updateMapList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            String name = map.get("name");
            String address = map.get("address");

            Optional<Cafe> optionalCafe = cafeRepository.findByNameAndAddress(name, address);
            if (optionalCafe.isEmpty()) {
                createMapList.add(map);
                continue;
            }
            Cafe cafe = optionalCafe.get();
            map.put("id", cafe.getId().toString());
            updateMapList.add(map);
        }

        if (Objects.equals("create", method)) {
            return createMapList;
        }
        return updateMapList;
    }

    private List<Map<String, String>> addCafeType(List<Map<String, String>> mapList, Integer type) {
        CafeType cafeType = CafeType.fromInt(type);
        for (Map<String, String> map : mapList) {
            map.put("cafeType", cafeType.toString());
        }

        return mapList;
    }

    private JSONArray parseJsonObject(JSONObject jsonObject, String[] jsonForm) {
        for (int i = 0; i < jsonForm.length - 1; ++i) {
            jsonObject = (JSONObject) jsonObject.get(jsonForm[i]);
        }

        return (JSONArray) jsonObject.get(jsonForm[jsonForm.length - 1]);
    }

    private List<Map<String, String>> parseJsonArray(JSONArray jsonArray, String[] OpenAPIKeys,
        String[] mapKeys) {
        List<Map<String, String>> mapList = new ArrayList<>();

        for (Object o : jsonArray) {
            Map<String, String> libraryMap = new HashMap<>();
            JSONObject finalItem = (JSONObject) o;

            for (int i = 0; i < OpenAPIKeys.length; ++i) {
                if (HttpUtil.isContainKey(finalItem, OpenAPIKeys[i])) {
                    libraryMap.put(mapKeys[i], (String) finalItem.get(OpenAPIKeys[i]));
                }
            }

            mapList.add(libraryMap);
        }

        return mapList;
    }


}
