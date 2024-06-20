package com.projectX.projectX.domain.tour.service;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.exception.ContentIdNotFoundException;
import com.projectX.projectX.domain.tour.exception.InvalidRequestException;
import com.projectX.projectX.domain.tour.exception.InvalidURIException;
import com.projectX.projectX.domain.tour.repository.ImpairmentRepository;
import com.projectX.projectX.domain.tour.repository.TourImageRepository;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.tour.util.TourMapper;
import com.projectX.projectX.global.common.CommonService;
import com.projectX.projectX.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourService {

    private final ImpairmentRepository impairmentRepository;
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;
    private final CommonService commonService;

    private static JSONParser jsonParser;
    private static StringBuffer result;
    private static URL requestUrl;

    @Value("${tour-api.service-key}")
    private String service_key;
    @Value("${tour-api.base-url}")
    private String base_url;

    @Value("${kakao.key}")
    private String kakao_key;
    @Value("${kakao.base-url}")
    private String kakao_base_url;

    private final String postfix = "&_type=json&MobileOS=ETC&MobileApp=AppTest";

    private HttpEntity<String> httpEntity;

    @PostConstruct
    protected void init() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, kakao_key);
        httpEntity = new HttpEntity<>(headers);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTour() {
        String uri = base_url + "areaBasedList1?"
            + "serviceKey=" + service_key
            + "&areaCode=39"
            + postfix + "&pageNo=1&numOfRows=410";

        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader bf = new BufferedReader(
                new InputStreamReader(url.openStream(), "UTF-8"));
            result.append(bf.readLine());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject parsedResponse = getJSONObject(jsonObject, "response");
            JSONObject parsedBody = getJSONObject(parsedResponse, "body");
            JSONObject parsedItems = getJSONObject(parsedBody, "items");
            JSONArray jsonArray = (JSONArray) parsedItems.get("item");

            String[] OpenAPIKeys = {"addr1", "addr2", "contentid", "contenttypeid",
                "zipcode", "mapx", "mapy", "title", "tel", "firstimage"};
            String[] tourkeys = {"address", "specAddress", "contentId", "contentTypeId",
                "zipCode", "mapX", "mapY", "title", "phone", "imageUrl", "jejuRegion"};
            HashMap<String, String> tourMap = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject finalItem = (JSONObject) jsonArray.get(i);
                for (int j = 0; j < OpenAPIKeys.length; ++j) {
                    if (isContainKey(finalItem, OpenAPIKeys[j])) {
                        tourMap.put(tourkeys[j], (String) finalItem.get(OpenAPIKeys[j]));
                    }
                }

                String num = commonService.findJejuRegion(tourMap.get("address"));
                tourMap.put(tourkeys[tourkeys.length - 1], num);

                tourRepository.save(TourMapper.toTour(tourMap));


            }
        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST_EXCEPTION);
        }
    }


    private List<Long> createContentIdList() {
        List<Tour> entireTourList = tourRepository.findAll();
        List<Long> contentIdList = new ArrayList<>();
        for (Tour tour : entireTourList) {
            contentIdList.add(tour.getContentId());
        }
        return contentIdList;
    }

    public void rotateForImpairment() {
        List<Long> contentIdList = createContentIdList();
        for (Long contentId : contentIdList) {
            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );
            if (!impairmentRepository.findByTourId(tour.getId()).isPresent()) {
                createBarrierFree(contentId);
            }
        }
    }

    public void createBarrierFree(Long contentId) {
        StringBuffer result = new StringBuffer();
        String uri =
            base_url + "detailWithTour1?serviceKey=" + service_key + "&contentId=" + contentId
                + "&pageNo=1" + postfix;

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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject parsedResponse = getJSONObject(jsonObject, "response");
            JSONObject parsedBody = getJSONObject(parsedResponse, "body");
            JSONObject parsedItems = getJSONObject(parsedBody, "items");
            JSONArray parsedItemArray = (JSONArray) parsedItems.get("item");
            JSONObject finalItem = (JSONObject) parsedItemArray.get(0);

            String[] OpenAPIKeys = {"wheelchair", "braileblock", "audioguide", "videoguide",
                "stroller", "lactationroom"};
            String[] barrierFreekeys = {"wheelChair", "brailleBlock", "audioGuide", "videoGuide",
                "stroller", "lactationRoom"};
            HashMap<String, String> barrierFreeMap = new HashMap<>();
            for (int i = 0; i < OpenAPIKeys.length; i++) {
                if (isContainKey(finalItem, OpenAPIKeys[i])) {
                    barrierFreeMap.put(barrierFreekeys[i], (String) finalItem.get(OpenAPIKeys[i]));
                }
            }
            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );

            impairmentRepository.save(
                TourMapper.toTourImpairment(tour, convertToPossibleMap(barrierFreeMap)));

        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST_EXCEPTION);
        }
    }

    private static HashMap<String, Integer> convertToPossibleMap(
        Map<String, String> barrierFreeMap) {
        HashMap<String, Integer> barrierFreePossibleMap = new HashMap<>();
        for (String key : barrierFreeMap.keySet()) {
            if (isContainPossibleKeyword(barrierFreeMap.get(key))) {
                barrierFreePossibleMap.put(key, 1);
            } else {
                barrierFreePossibleMap.put(key, 0);
            }
        }
        return barrierFreePossibleMap;
    }

    private static boolean isContainPossibleKeyword(String value) {
        if (value.contains("가능") || value.contains("허용") || value.contains("있음") || value.contains(
            "존재")) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rotateForTourImage() {
        List<Long> contentIdList = createContentIdList();
        for (Long contentId : contentIdList) {
            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );
            if (!tourImageRepository.findByTourId(tour.getId()).isPresent()) {
                createTourImage(contentId);
            }
        }
    }

    private void createTourImage(Long contentId) {
        result = new StringBuffer();
        String uri =
            base_url + "detailImage1?serviceKey=" + service_key + "&contentId=" + contentId
                + postfix + "&imageYN=Y&subImageYN=Y";

        try {
            initConnection(uri);
            jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject parsedResponse = getJSONObject(jsonObject, "response");
            JSONObject parsedBody = getJSONObject(parsedResponse, "body");
            JSONObject parsedItems = getJSONObject(parsedBody, "items");
            JSONArray parsedItemArray = (JSONArray) parsedItems.get("item");

            String OpenAPIKeys = "originimgurl";
            List<String> TourImageList = new ArrayList<>();

            for (int i = 0; i < parsedItemArray.size(); i++) {
                JSONObject parsedItem = (JSONObject) parsedItemArray.get(i);
                if (isContainKey(parsedItem, OpenAPIKeys)) {
                    TourImageList.add(parsedItem.get(OpenAPIKeys).toString());
                }
            }

            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );

            for (String tourImage : TourImageList) {
                tourImageRepository.save(TourMapper.toTourImage(tour, tourImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rotateForEveryContentIdDetail() {
        List<Tour> entireTourList = tourRepository.findAll();
        List<Long> contentIdList = new ArrayList<>();
        for (Tour tour : entireTourList) {
            contentIdList.add(tour.getContentId());
        }
        for (Long contentId : contentIdList) {
            createDetailCommon(contentId);
        }
    }

    public void createDetailCommon(Long contentId) {
        StringBuffer result = new StringBuffer();
        String uri =
            base_url + "detailCommon1?serviceKey=" + service_key + "&contentId=" + contentId
                + postfix + "&defaultYN=Y&overviewYN=Y";

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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject parsedResponse = getJSONObject(jsonObject, "response");
            JSONObject parsedBody = getJSONObject(parsedResponse, "body");
            JSONObject parsedItems = getJSONObject(parsedBody, "items");
            JSONArray parsedItemArray = (JSONArray) parsedItems.get("item");
            JSONObject finalItem = (JSONObject) parsedItemArray.get(0);

            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );
            String[] OpenAPIKeys = {"homepage", "overview"};
            String[] SpecInfoArr = new String[2];
            for (int i = 0; i < OpenAPIKeys.length; i++) {
                if (isContainKey(finalItem, OpenAPIKeys[i])) {
                    String item = (String) finalItem.get(OpenAPIKeys[i]);
                    if (i == 0) { // homepage는 url 발췌 필요
                        if (!item.isBlank()) {
                            SpecInfoArr[i] = extractHref(item);
                        } else {
                            SpecInfoArr[i] = item;
                        }
                    } else {
                        SpecInfoArr[i] = item;
                    }
                }
            }
            tour.updateHomePageAndOverview(SpecInfoArr[0], SpecInfoArr[1]);
            tourRepository.save(tour);

        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST_EXCEPTION);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rotateForEmptyContentIdDetail() {
        List<Tour> entireTourList = tourRepository.findAll();
        List<Tour> emptyPhoneContentIdList = new ArrayList<>();
        for (Tour tour : entireTourList) {
            if (tour.getPhone() == null || tour.getPhone().isEmpty()) {
                emptyPhoneContentIdList.add(tour);
            }
            tour.updateKakaoMapUrl(getAttributeFromKakao(tour.getTitle(), "place_url"));
        }

        for (Tour emptyPhoneTour : emptyPhoneContentIdList) {
            emptyPhoneTour.updatePhone(getAttributeFromKakao(emptyPhoneTour.getTitle(), "phone"));
        }
    }

    private String getAttributeFromKakao(String name, String key) {
        URI tourURI = UriComponentsBuilder.fromHttpUrl(kakao_base_url)
            .queryParam("query", name)
            .queryParam("page", 1)
            .encode(StandardCharsets.UTF_8)
            .build().toUri();

        Assert.notNull(name, "query");
        ResponseEntity<String> tourResponse = new RestTemplate().exchange(tourURI, HttpMethod.GET,
            httpEntity, String.class);

        org.json.JSONObject jsonObject = new org.json.JSONObject(tourResponse.getBody().toString());
        org.json.JSONArray jsonArray = jsonObject.getJSONArray("documents");

        if (!jsonArray.isEmpty()) {
            org.json.JSONObject targetObject = jsonArray.getJSONObject(0);
            String attribute = targetObject.getString(key);
            return attribute;
        } else {
            return null;
        }
    }

    public static String extractHref(String input) {
        int hrefStartIndex = input.indexOf("href=\"");
        if (hrefStartIndex != -1) {
            int hrefEndIndex = input.indexOf("\"",
                hrefStartIndex + 6); // "href=\"" 다음의 인덱스부터 href 속성값이 시작됨
            if (hrefEndIndex != -1) {
                return input.substring(hrefStartIndex + 6, hrefEndIndex);
            }
        }
        return null; // href 속성을 찾을 수 없는 경우
    }


    private static JSONObject getJSONObject(JSONObject obj, String key) throws ClassCastException {
        if (obj != null) {
            return (JSONObject) obj.get(key);
        }
        return null;
    }

    private static boolean isContainKey(JSONObject obj, String key) {
        if (obj != null & obj.containsKey(key)) {
            return true;
        }
        return false;
    }

    private static void initConnection(String uri) throws IOException {
        requestUrl = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);
        urlConnection.setRequestProperty("Content-type", "application/json");

        BufferedReader br = new BufferedReader(
            new InputStreamReader(requestUrl.openStream(), "UTF-8"));
        result.append(br.readLine());
    }
}