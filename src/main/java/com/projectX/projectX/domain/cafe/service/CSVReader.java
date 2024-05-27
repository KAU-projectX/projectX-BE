package com.projectX.projectX.domain.cafe.service;

import com.projectX.projectX.domain.cafe.repository.CafeBulkRepository;
import com.projectX.projectX.global.common.CafeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CSVReader {

    private final CafeBulkRepository cafeBulkRepository;

    public void saveCafeInfo(List<Map<String, String>> csvList) {
        cafeBulkRepository.saveAll(csvList);
    }

    public List<Map<String, String>> readCSV() {
        List<Map<String, String>> csvList = new ArrayList<>();
        File csv = new File("C://home/root/전국 카페 정보.csv");
        log.info(csv.getName());
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));

            List<String> firstLine = new ArrayList<String>();
            if ((line = br.readLine()) != null) { // 첫번째 행
                String[] lineArr = line.split(",");
                firstLine = Arrays.asList(lineArr);
            }

            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(",");
                Map<String, String> cafeMap = createCafeInfo(lineArr, firstLine);
                if (!cafeMap.isEmpty()) {
                    csvList.add(cafeMap);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return csvList;
    }

    private Map<String, String> createCafeInfo(String[] line, List<String> header) {
        String[] franchise = {"스타벅스", "투썸플레이스", "엔제리너스", "요거프레소", "이디야커피", "카페베네", "커피빈", "탐앤탐스",
            "파스쿠찌", "할리스커피", "드롭탑", "메가커피", "더리터", "봄봄", "더벤티", "커피베이", "디저트39"};
        String[] excepts = {"대형주점/호프/라이브카페/클럽", "설빙", "카페띠아모", "스무디킹", "망고식스"};

        int idxSido = getIdx(header, "CTPRVN_NM");
        int idxCafeName = getIdx(header, "POI_NM");
        int idxCafeType = getIdx(header, "CL_NM");

        if (checkSido(line, idxSido, "제주특별자치도")) {
            if (checkCafeType(line, idxCafeType, excepts)) {
                return new HashMap<>();
            }

            Map<String, String> cafeInfoMap = new HashMap<>();

            //name
            int idxCafeBranch = getIdx(header, "BHF_NM");

            String name = line[idxCafeName] + line[idxCafeBranch];
            cafeInfoMap.put("name", name);

            //address
            int idxSigungu = getIdx(header, "SIGNGU_NM");
            int idxDong = getIdx(header, "LEGALDONG_NM");
            int idxLi = getIdx(header, "LI_NM");
            int idxNo = getIdx(header, "LNBR_NO");

            String addr = "제주특별자치도" + line[idxSigungu] + line[idxDong] + line[idxLi] + line[idxNo];
            cafeInfoMap.put("address", addr);

            //latitude
            int idxLA = getIdx(header, "LC_LA");
            cafeInfoMap.put("latitude", line[idxLA]);

            //longitude
            int idxLO = getIdx(header, "LC_LO");
            cafeInfoMap.put("longitude", line[idxLO]);

            //cafeType
            String cafe;
            if (checkCafeType(line, idxCafeName, franchise)) {
                cafe = CafeType.fromInt(3).toString();
            } else {
                cafe = CafeType.fromInt(2).toString();
            }
            cafeInfoMap.put("cafeType", cafe);

            return cafeInfoMap;
        }
        return new HashMap<>();
    }

    private int getIdx(List<String> header, String targetHeader) {
        return header.indexOf(targetHeader);
    }

    private boolean checkCafeType(String[] line, int idx, String[] excepts) {
        for (String except : excepts) {
            if (line[idx].equals(except)) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkSido(String[] line, int idx, String targetString) {
        String Sido = line[idx];
        if (Sido.equals(targetString)) {
            return true;
        }
        return false;
    }


}
