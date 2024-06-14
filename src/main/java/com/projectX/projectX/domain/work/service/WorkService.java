package com.projectX.projectX.domain.work.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.domain.work.exception.WorkNotFoundException;
import com.projectX.projectX.domain.work.util.WorkUtil;
import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkService {

    private final CafeRepository cafeRepository;

    public List<WorkGetAllResponse> getWorkAll(Integer page, Integer cafeType, Integer jejuRegion,
        String franchiseName) {
        Page<Cafe> workPage;
        Pageable pageable = PageRequest.of(page, 20);

        if (jejuRegion == null && franchiseName == null) {
            workPage = cafeRepository.findByCafeType(CafeType.fromInt(cafeType), pageable);
        } else if (jejuRegion != null && franchiseName == null) {
            workPage = cafeRepository.findByCafeTypeAndJejuRegion(CafeType.fromInt(cafeType),
                JejuRegion.fromInt(jejuRegion), pageable);
        } else if (jejuRegion == null && franchiseName != null) {
            workPage = cafeRepository.findByCafeType(CafeType.FRANCHISE, pageable);
        } else {
            workPage = cafeRepository.findByCafeTypeAndJejuRegion(CafeType.FRANCHISE,
                JejuRegion.fromInt(jejuRegion), pageable);
        }

        if (workPage.isEmpty()) {
            if (page >= 0) {
                throw new NoMorePageException(ErrorCode.NO_MORE_PAGE);
            }
            throw new InvalidPageException(ErrorCode.INVALID_PAGE);
        }

        List<WorkGetAllResponse> cafeList = new ArrayList<>();
        for (Cafe cafe : workPage.toList()) {
            WorkGetAllResponse workGetAllResponse = WorkUtil.toWorkGetAllResponse(cafe);
            cafeList.add(workGetAllResponse);
        }

        if (cafeList.isEmpty()) {
            throw new WorkNotFoundException(ErrorCode.WORK_NOT_FOUND);
        }

        return cafeList;
    }

}
