package com.projectX.projectX.domain.cafe.service;

import com.projectX.projectX.domain.cafe.repository.CafeBulkRepository;
import com.projectX.projectX.domain.cafe.util.CSVReader;
import com.projectX.projectX.global.common.CafeType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeBulkRepository cafeBulkRepository;
    private final LibraryService libraryService;
    private final String[] header = {"name", "cafeType", "address", "latitude", "longitude"};

    public void createCafeInfo() {
        String[] franchise = {"스타벅스", "투썸플레이스", "엔제리너스", "요거프레소", "이디야커피", "카페베네", "커피빈", "탐앤탐스",
            "파스쿠찌", "할리스커피", "드롭탑", "메가커피", "더리터", "봄봄", "더벤티", "커피베이", "디저트39"};
        String[] excepts = {"대형주점/호프/라이브카페/클럽", "설빙", "카페띠아모", "스무디킹", "망고식스"};
        String[] name = {"POI_NM", "BHF_NM"};
        String[] cafeType = {"CL_NM"};
        String[] addr = {"CTPRVN_NM", "SIGNGU_NM", "LEGALDONG_NM", "LI_NM", "LNBR_NO"};
        String[] la = {"LC_LA"};
        String[] lo = {"LC_LO"};
        String fileRoot = "C://Users/dlwns/Downloads/전국 카페 정보.csv";

        List<Map<String, String>> cafeList = getFromCsv(fileRoot, "CTPRVN_NM", "제주특별자치도");
        List<Map<String, String>> midCafeList = getFinalMap(cafeList, name, cafeType, addr, la, lo,
            excepts);
        List<Map<String, String>> finalCafeList = defineCafeType(midCafeList, franchise);

        cafeBulkRepository.saveCafe(finalCafeList);
    }

    public void createBookCafeInfo() {
        String[] excepts = {"만화책"};
        String[] name = {"FCLTY_NM"};
        String[] cafeType = {"MLSFC_NM"};
        String[] addr = {"FCLTY_ROAD_NM_ADDR"};
        String[] la = {"FCLTY_LA"};
        String[] lo = {"FCLTY_LO"};
        String fileRoot = "C://Users/dlwns/Downloads/전국 북카페 정보.csv";

        List<Map<String, String>> cafeList = getFromCsv(fileRoot, "FCLTY_ROAD_NM_ADDR", "제주");
        List<Map<String, String>> midCafeList = getFinalMap(cafeList, name, cafeType, addr, la, lo,
            excepts);
        List<Map<String, String>> finalCafeList = libraryService.addCafeType(midCafeList, 1);

        cafeBulkRepository.saveCafe(finalCafeList);
    }

    public List<Map<String, String>> getFinalMap(List<Map<String, String>> mapList, String[] name,
        String[] cafeType, String[] addr, String[] la, String[] lo, String[] excepts) {
        List<String[]> convertHeader = new ArrayList<>();
        convertHeader.add(name);
        convertHeader.add(cafeType);
        convertHeader.add(addr);
        convertHeader.add(la);
        convertHeader.add(lo);

        List<Map<String, String>> returnList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            Map<String, String> tmpMap = new HashMap<>();

            for (int i = 0; i < convertHeader.size(); ++i) {
                String str = combineString(convertHeader.get(i), map);
                tmpMap.put(header[i], str);
            }
            returnList.add(tmpMap);
        }
        List<Map<String, String>> mapList1 = exceptTargetInfo(returnList, excepts, "cafeType");

        return mapList1;
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
                if (!Objects.equals(target, map.get(targetString))) {
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
}
