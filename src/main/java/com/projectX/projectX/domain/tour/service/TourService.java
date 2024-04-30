package com.projectX.projectX.domain.tour.service;

import com.projectX.projectX.domain.tour.dto.request.TourSidoStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourSigunguStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourStoreRequest;
import com.projectX.projectX.domain.tour.entity.Sido;
import com.projectX.projectX.domain.tour.entity.Sigungu;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.exception.ContentIdNotFoundException;
import com.projectX.projectX.domain.tour.exception.InvalidAreaCodeException;
import com.projectX.projectX.domain.tour.exception.InvalidRequestException;
import com.projectX.projectX.domain.tour.exception.InvalidSigunguCodeException;
import com.projectX.projectX.domain.tour.exception.InvalidURIException;
import com.projectX.projectX.domain.tour.repository.ImpairmentRepository;
import com.projectX.projectX.domain.tour.repository.SidoRepository;
import com.projectX.projectX.domain.tour.repository.SigunguRepository;
import com.projectX.projectX.domain.tour.repository.TourImageRepository;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.tour.util.TourMapper;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourService {

    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;
    private final ImpairmentRepository impairmentRepository;
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;

    private static JSONParser jsonParser;
    private static StringBuffer result;
    private static URL requestUrl;

    @Value("${tour-api.service-key}")
    private String service_key;
    @Value("${tour-api.base-url}")
    private String base_url;

    private final String postfix = "&_type=json&MobileOS=ETC&MobileApp=AppTest";

    public String createSido() {
        StringBuffer result = new StringBuffer();
        String uri = base_url + "areaCode1?" +
            "pageNo=1&numOfRows=50&_type=json&MobileOS=ETC&MobileApp=AppTest&" +
            "serviceKey=" + service_key;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            BufferedReader bf = new BufferedReader(
                new InputStreamReader(url.openStream(), "UTF-8"));
            result.append(bf.readLine());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            //log.info(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("response");
            //log.info(jsonObject1.toJSONString());
            JSONObject jsonObject2 = (JSONObject) jsonObject1.get("body");
            //log.info(jsonObject2.toJSONString());
            JSONObject jsonObject3 = (JSONObject) jsonObject2.get("items");
            //log.info(jsonObject3.toJSONString());
            JSONArray jsonArray = (JSONArray) jsonObject3.get("item");
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String sidoName = (String) object.get("name");
                String tmp = (String) object.get("code");
                Integer sidoCode = Integer.parseInt(tmp);
                TourSidoStoreRequest sidoStoreRequest = new TourSidoStoreRequest(sidoName,
                    sidoCode);
                if (sidoRepository.findBySidoCode(sidoCode) == null) {
                    sidoRepository.save(TourMapper.toSido(sidoStoreRequest));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return "성공적으로 저장하였습니다.";
    }

    public String createSigungu(Integer areaCode) {
        StringBuffer result = new StringBuffer();
        String uri = base_url + "areaCode1?" +
            "pageNo=1&numOfRows=50&_type=json&MobileOS=ETC&MobileApp=AppTest&" +
            "serviceKey=" + service_key +
            "&areaCode=" + areaCode;
        Sido sido = sidoRepository.findBySidoCode(areaCode);
        if (sido == null) {
            throw new InvalidAreaCodeException(ErrorCode.INVALID_AREACODE_EXCEPTION);
        }
        //log.info(sido.getSidoName());

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
            //log.info(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("response");
            //log.info(jsonObject1.toJSONString());
            JSONObject jsonObject2 = (JSONObject) jsonObject1.get("body");
            //log.info(jsonObject2.toJSONString());
            JSONObject jsonObject3 = (JSONObject) jsonObject2.get("items");
            //log.info(jsonObject3.toJSONString());
            JSONArray jsonArray = (JSONArray) jsonObject3.get("item");
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String sigunguName = (String) object.get("name");
                String tmp = (String) object.get("code");
                Integer sigunguCode = Integer.parseInt(tmp);
                TourSigunguStoreRequest sigunguStoreRequest = new TourSigunguStoreRequest(
                    sigunguName, sigunguCode, sido);
                if (sigunguRepository.findBySigunguCodeAndSido(sigunguCode, sido) == null) {
                    sigunguRepository.save(TourMapper.toSigungu(sigunguStoreRequest));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return "성공적으로 저장하였습니다.";
    }

    public String createTour(Integer areaCode, Integer sigunguCode) {
        StringBuffer result = new StringBuffer();
        String uri = base_url + "areaBasedList1?" +
            "pageNo=1&numOfRows=100&_type=json&MobileOS=ETC&MobileApp=AppTest&" +
            "serviceKey=" + service_key;
        if (areaCode != 0) {
            uri += "&areaCode=" + areaCode;
        }
        if (sigunguCode != 0) {
            uri += "&sigunguCode=" + sigunguCode;
        }

        if (areaCode != 0) {
            Sido sido = sidoRepository.findBySidoCode(areaCode);
            if (sido == null) {
                throw new InvalidAreaCodeException(ErrorCode.INVALID_AREACODE_EXCEPTION);
            }
            if (sigunguCode != 0) {
                Sigungu sigungu = sigunguRepository.findBySigunguCodeAndSido(sigunguCode, sido);
                if (sigungu == null) {
                    throw new InvalidSigunguCodeException(ErrorCode.INVALID_SIGUNGU_CODE_EXCEPTION);
                }
            }
        }

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
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("response");
            JSONObject jsonObject2 = (JSONObject) jsonObject1.get("body");
            JSONObject jsonObject3 = (JSONObject) jsonObject2.get("items");
            JSONArray jsonArray = (JSONArray) jsonObject3.get("item");
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject object = (JSONObject) jsonArray.get(i);

                String address = (String) object.get("addr1");
                String specAddress = (String) object.get("addr2");
                String tmp = (String) object.get("areacode");
                Integer sidoCode = Integer.valueOf(tmp);
                Sido sido = sidoRepository.findBySidoCode(sidoCode);
                if (sido == null) {
                    throw new InvalidAreaCodeException(ErrorCode.INVALID_AREACODE_EXCEPTION);
                }
                tmp = (String) object.get("sigungucode");
                Integer sigunCode = Integer.valueOf(tmp);
                Sigungu sigungu = sigunguRepository.findBySigunguCodeAndSido(sigunCode, sido);
                if (sigungu == null) {
                    throw new InvalidSigunguCodeException(ErrorCode.INVALID_SIGUNGU_CODE_EXCEPTION);
                }
                tmp = (String) object.get("contentid");
                Long contentId = Long.valueOf(tmp);
                tmp = (String) object.get("contenttypeid");
                Integer contentTypeId = Integer.valueOf(tmp);
                tmp = (String) object.get("zipcode");
                Long zipCode = Long.valueOf(tmp);
                String imageUrl = (String) object.get("firstimage");
                String thumbnailImageUrl = (String) object.get("firstimage2");
                tmp = (String) object.get("mapx");
                Float mapX = Float.parseFloat(tmp);
                tmp = (String) object.get("mapy");
                Float mapY = Float.parseFloat(tmp);
                String title = (String) object.get("title");
                String phone = (String) object.get("tel");

                TourStoreRequest tourStoreRequest = new TourStoreRequest(address, specAddress, sido,
                    sigungu, contentId, contentTypeId, zipCode, imageUrl, thumbnailImageUrl, mapX,
                    mapY, title, phone);
                tourRepository.save(TourMapper.toTour(tourStoreRequest));

            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return "성공적으로 저장하였습니다.";
    }

    private List<Long> createContentIdList() {
        List<Tour> entireTourList = tourRepository.findAll();
        List<Long> contentIdList = new ArrayList<>();
        for (Tour tour : entireTourList) {
            contentIdList.add(tour.getContentId());
        }
        return contentIdList;
    }

    public void rotateForImpairment(){
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

    public void rotateForTourImage(){
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

    private void createTourImage(Long contentId){
        result = new StringBuffer();
        String uri =
            base_url + "detailImage1?serviceKey=" + service_key + "&contentId=" + contentId
                + postfix +"&imageYN=Y&subImageYN=Y";

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

            for(int i = 0; i<parsedItemArray.size(); i++){
                JSONObject parsedItem = (JSONObject) parsedItemArray.get(i);
                if(isContainKey(parsedItem, OpenAPIKeys)){
                    TourImageList.add(parsedItem.get(OpenAPIKeys).toString());
                }
            }

            Tour tour = tourRepository.findByContentId(contentId).orElseThrow(
                () -> new ContentIdNotFoundException(ErrorCode.CONTENT_ID_NOT_FOUND_EXCEPTION)
            );

            for(String tourImage : TourImageList){
                tourImageRepository.save(TourMapper.toTourImage(tour, tourImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        if (!item.isBlank()) SpecInfoArr[i] = extractHref(item);
                        else SpecInfoArr[i] = item;
                    } else SpecInfoArr[i] = item;
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