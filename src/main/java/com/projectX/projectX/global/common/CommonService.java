package com.projectX.projectX.global.common;

import com.projectX.projectX.domain.tour.exception.NotFoundJejuRegionException;
import com.projectX.projectX.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    public static String findJejuRegion(String value) {
        if (value.contains("제주시")) {
            if (value.contains("구좌읍") || value.contains("조천읍")) {
                return "2";
            } else if (value.contains("애월읍") || value.contains("한림읍") || value.contains("한경읍")) {
                return "6";
            }
            return "1";
        } else if (value.contains("서귀포시")) {
            if (value.contains("대정읍") || value.contains("안덕면")) {
                return "5";
            } else if (value.contains("남원읍") || value.contains("표선면") || value.contains("성산읍")) {
                return "3";
            }
            return "4";
        }

        throw new NotFoundJejuRegionException(ErrorCode.JEJU_REGION_NOT_FOUND);
    }

}
