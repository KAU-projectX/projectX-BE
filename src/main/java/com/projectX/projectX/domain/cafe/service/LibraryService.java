package com.projectX.projectX.domain.cafe.service;

import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.cafe.util.CafeMapper;
import com.projectX.projectX.domain.tour.exception.InvalidRequestException;
import com.projectX.projectX.domain.tour.exception.InvalidURIException;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class LibraryService {

    private static JSONParser jsonParser;
    private final CafeRepository cafeRepository;

    @Value("${jeju-datahub.project-key}")
    private String project_key;
    @Value("${jeju-datahub.base-url}")
    private String base_url;

    public void createBarrierFree(Long contentId) {
        StringBuffer result = new StringBuffer();
        String uri = base_url + project_key + "?limit=50";

        try {
            URL requestUrl = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader br = new BufferedReader(
                new InputStreamReader(requestUrl.openStream(), "UTF-8"));
            result.append(br.readLine());

            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");

            String[] OpenAPIKeys = {"libraryName", "address"};
            String[] libraryKeys = {"name", "address", "cafeType"};

            Map<String, String> libraryMap = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject finalItem = (JSONObject) jsonArray.get(i);

                for (int j = 0; j < OpenAPIKeys.length; ++j) {
                    if (isContainKey(finalItem, OpenAPIKeys[j])) {
                        libraryMap.put(libraryKeys[i], (String) finalItem.get(OpenAPIKeys[i]));
                    }
                }
                libraryMap.put(libraryKeys[libraryKeys.length - 1], "4");

                cafeRepository.save(CafeMapper.toCafe(libraryMap));


            }
        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST_EXCEPTION);
        }
    }

    private static boolean isContainKey(JSONObject obj, String key) {
        if (obj != null & obj.containsKey(key)) {
            return true;
        }
        return false;
    }

}
