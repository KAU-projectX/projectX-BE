package com.projectX.projectX.domain.cafe.util;

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
public class CSVReader {
    public static List<Map<String, String>> readCSV(File csv) {
        List<Map<String, String>> csvList = new ArrayList<>();
        log.info(csv.getName());
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));

            List<String> firstLine = new ArrayList<>();
            if ((line = br.readLine()) != null) { // 첫번째 행
                String[] lineArr = line.split(",");
                firstLine = Arrays.asList(lineArr);
            }

            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(",");
                Map<String, String> cafeMap = new HashMap<>();
                for(int i = 0; i < firstLine.size(); ++i){
                    cafeMap.put(firstLine.get(i), lineArr[i]);
                }

                if (!cafeMap.isEmpty()) {
                    csvList.add(cafeMap);
                }
            }
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
}
