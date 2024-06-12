package com.projectX.projectX.domain.cafe.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeBulkRepository;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.cafe.util.CSVReader;
import com.projectX.projectX.global.common.CafeType;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeBulkRepository cafeBulkRepository;
    private final CafeRepository cafeRepository;
    private final LibraryService libraryService;
    private final String[] header = {"cafeId", "name", "cafeType", "address", "latitude",
        "longitude"};

    @Value("${kakao.key}")
    private String kakao_key;
    @Value("${kakao.base-url}")
    private String kakao_base_url;
    @Value("${csv-root}")
    private String root;


    private HttpEntity<String> httpEntity;

    @PostConstruct
    protected void init() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, kakao_key);
        httpEntity = new HttpEntity<>(headers);
    }

    public void createCafeInfo() {
        String[] franchise = {"스타벅스", "투썸플레이스", "엔제리너스", "요거프레소", "이디야커피", "카페베네", "커피빈", "탐앤탐스",
            "파스쿠찌", "할리스커피", "드롭탑", "메가커피", "더리터", "봄봄", "더벤티", "커피베이", "디저트39"};
        String[] excepts = {"대형주점/호프/라이브카페/클럽", "설빙", "카페띠아모", "스무디킹", "망고식스"};
        String[] cafeId = {"ID"};
        String[] name = {"POI_NM", "BHF_NM"};
        String[] cafeType = {"CL_NM"};
        String[] addr = {"CTPRVN_NM", "SIGNGU_NM", "LEGALDONG_NM", "LI_NM", "LNBR_NO"};
        String[] la = {"LC_LA"};
        String[] lo = {"LC_LO"};
        String fileRoot = root + "전국 카페 정보.csv";

        List<Map<String, String>> cafeList = getFromCsv(fileRoot, "CTPRVN_NM", "제주특별자치도");
        List<Map<String, String>> midCafeList = getFinalMap(cafeList, cafeId, name, cafeType, addr,
            la, lo,
            excepts);
        List<Map<String, String>> finalCafeList = defineCafeType(midCafeList, franchise);

        List<Map<String, String>> finalCafeList1 = checkAlreadyExistByCafeId(finalCafeList,
            "create");
        log.info(finalCafeList1.toString());
        cafeBulkRepository.saveCafe(finalCafeList1);
    }

    public void createBookCafeInfo() {
        String[] excepts = {"만화책"};
        String[] cafeId = {"ESNTL_ID"};
        String[] name = {"FCLTY_NM"};
        String[] cafeType = {"MLSFC_NM"};
        String[] addr = {"FCLTY_ROAD_NM_ADDR"};
        String[] la = {"FCLTY_LA"};
        String[] lo = {"FCLTY_LO"};
        String fileRoot = root + "전국 북카페 정보.csv";

        List<Map<String, String>> cafeList = getFromCsv(fileRoot, "FCLTY_ROAD_NM_ADDR", "제주");
        List<Map<String, String>> midCafeList = getFinalMap(cafeList, cafeId, name, cafeType, addr,
            la, lo,
            excepts);
        List<Map<String, String>> finalCafeList = libraryService.addCafeType(midCafeList, 1);
        log.info(finalCafeList.toString());

        cafeBulkRepository.saveCafe(checkAlreadyExistByCafeId(finalCafeList, "create"));
    }

    private List<Map<String, String>> getFinalMap(List<Map<String, String>> mapList,
        String[] cafeId, String[] name,
        String[] cafeType, String[] addr, String[] la, String[] lo, String[] excepts) {
        List<String[]> convertHeader = new ArrayList<>();
        convertHeader.add(cafeId);
        convertHeader.add(name);
        convertHeader.add(cafeType);
        convertHeader.add(addr);
        convertHeader.add(la);
        convertHeader.add(lo);

        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            Map<String, String> tmpMap = new HashMap<>();

            String cafeName = combineString(convertHeader.get(1), map);

            if (isPlaceExist(cafeName)) {
                for (int i = 0; i < convertHeader.size(); ++i) {
                    String str = combineString(convertHeader.get(i), map);
                    tmpMap.put(header[i], str);
                }
                tmpMap.put("uri", getPlaceURI(cafeName));
                returnList.add(tmpMap);
            }
        }
        return exceptTargetInfo(returnList, excepts, "cafeType");
    }

    public boolean isPlaceExist(String name) {
        URI checkURI = UriComponentsBuilder.fromHttpUrl(kakao_base_url)
            .queryParam("query", name)
            .queryParam("page", 1)
            .queryParam("category_group_code", "CE7")
            .encode(StandardCharsets.UTF_8)
            .build().toUri();

        Assert.notNull(name, "query");
        ResponseEntity<String> cafeResponse = new RestTemplate().exchange(checkURI, HttpMethod.GET,
            httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(cafeResponse.getBody().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("documents");
        if (jsonArray.length() > 0) {
            return true;
        }
        return false;
    }

    public String getPlaceURI(String name) {
        URI cafeURI = UriComponentsBuilder.fromHttpUrl(kakao_base_url)
            .queryParam("query", name)
            .queryParam("page", 1)
            .encode(StandardCharsets.UTF_8)
            .build().toUri();

        Assert.notNull(name, "query");
        ResponseEntity<String> cafeResponse = new RestTemplate().exchange(cafeURI, HttpMethod.GET,
            httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(cafeResponse.getBody().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("documents");

        JSONObject targetObject = jsonArray.getJSONObject(0);
        String uri = targetObject.getString("place_url");
        return uri;
    }

    private String combineString(String[] targets, Map<String, String> map) {
        String str = "";
        for (String target : targets) {
            str = str + map.get(target) + " ";
        }

        return str;
    }


    private List<Map<String, String>> defineCafeType(List<Map<String, String>> mapList,
        String[] targetList) {
        for (Map<String, String> map : mapList) {
            String dest = map.get("cafeType");
            if (!Objects.isNull(dest)) {
                for (String target : targetList) {
                    if (dest.contains(target)) {
                        map.replace("cafeType", dest, CafeType.FRANCHISE.toString());
                        break;
                    }
                    map.replace("cafeType", dest, CafeType.PERSONAL.toString());
                }
            }
        }
        return mapList;
    }

    private List<Map<String, String>> getFromCsv(String str, String check, String checkTarget) {
        File csv = new File(str);
        List<Map<String, String>> tmpList = CSVReader.readCSV(csv);

        List<Map<String, String>> cafeList = new ArrayList<>();
        for (Map<String, String> map : tmpList) {
            if (checkCondition(map, check, checkTarget)) {
                cafeList.add(map);
            }
        }
        return cafeList;
    }

    private List<Map<String, String>> exceptTargetInfo(List<Map<String, String>> mapList,
        String[] targets, String targetString) {
        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            for (String target : targets) {
                if (!map.get(targetString).contains(target) && !target.contains(
                    map.get(targetString))) {
                    returnList.add(map);
                    break;
                }
            }
        }
        return returnList;
    }

    private boolean checkCondition(Map<String, String> map, String headerString, String target) {
        String dest = map.get(headerString);
        return dest.contains(target);
    }

    public List<Map<String, String>> checkAlreadyExistByCafeId(List<Map<String, String>> mapList,
        String method) {
        List<Map<String, String>> createMapList = new ArrayList<>();
        List<Map<String, String>> updateMapList = new ArrayList<>();

        for (Map<String, String> map : mapList) {
            String cafeId = map.get("cafeId");

            Optional<Cafe> optionalCafe = cafeRepository.findByCafeId(cafeId);
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
}
