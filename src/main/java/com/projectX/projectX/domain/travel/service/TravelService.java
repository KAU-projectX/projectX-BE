package com.projectX.projectX.domain.travel.service;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.travel.dto.response.TravelGetAllResponse;
import com.projectX.projectX.domain.travel.exception.TravelNotFoundException;
import com.projectX.projectX.domain.travel.util.TravelMapper;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.global.common.ContentType;
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
public class TravelService {

    private final TourRepository tourRepository;

    public List<TravelGetAllResponse> getAllTravelInfo(Integer page, Integer contentType,
        Integer jejuRegion) {
        Page<Tour> tourPage;
        Pageable pageable = PageRequest.of(page, 20);

        if (jejuRegion == null) {
            tourPage = tourRepository.findByContentType(ContentType.fromInt(contentType), pageable);
        } else {
            tourPage = tourRepository.findByContentTypeAndJejuRegion(
                ContentType.fromInt(contentType),
                JejuRegion.fromInt(jejuRegion), pageable);
        }

        if (tourPage.isEmpty()) {
            if (page >= 0) {
                throw new NoMorePageException(ErrorCode.NO_MORE_PAGE);
            }
            throw new InvalidPageException(ErrorCode.INVALID_PAGE);
        }

        List<TravelGetAllResponse> tourList = new ArrayList<>();
        for (Tour tour : tourPage.toList()) {
            TravelGetAllResponse travelGetAllResponse = TravelMapper.toTravelGetAllResponse(tour);
            tourList.add(travelGetAllResponse);
        }

        if (tourList.isEmpty()) {
            throw new TravelNotFoundException(ErrorCode.TRAVEL_NOT_FOUND);
        }

        return tourList;

    }

}
