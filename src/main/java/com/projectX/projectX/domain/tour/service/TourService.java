package com.projectX.projectX.domain.tour.service;

import com.projectX.projectX.domain.tour.dto.request.TourSidoStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourSigunguStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourStoreRequest;
import com.projectX.projectX.domain.tour.entity.Sido;
import com.projectX.projectX.domain.tour.entity.Sigungu;
import com.projectX.projectX.domain.tour.exception.InvalidAreaCodeException;
import com.projectX.projectX.domain.tour.exception.InvalidSigunguCodeException;
import com.projectX.projectX.domain.tour.repository.SidoRepository;
import com.projectX.projectX.domain.tour.repository.SigunguRepository;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.tour.util.TourMapper;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private final TourRepository tourRepository;

    @Value("${tour-api.service-key}")
    private String service_key;

    @Value("${tour-api.base-url}")
    private String base_url;

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
            "pageNo=1&numOfRows=7908&_type=json&MobileOS=ETC&MobileApp=AppTest&" +
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
                if (tourRepository.findByContentId(contentId) == null) {
                    tourRepository.save(TourMapper.toTour(tourStoreRequest));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return "성공적으로 저장하였습니다.";
    }


}
