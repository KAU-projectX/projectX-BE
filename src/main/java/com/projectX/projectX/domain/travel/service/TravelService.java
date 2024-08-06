package com.projectX.projectX.domain.travel.service;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.entity.TourImage;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.travel.dto.response.TravelGetAllResponse;
import com.projectX.projectX.domain.travel.dto.response.TravelGetRecommdResponse;
import com.projectX.projectX.domain.travel.dto.response.TravelGetSpecificResponse;
import com.projectX.projectX.domain.travel.exception.TravelNotFoundException;
import com.projectX.projectX.domain.travel.util.TravelMapper;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.global.common.ContentType;
import com.projectX.projectX.global.common.JejuRegion;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {

    private final TourRepository tourRepository;
    private static final int RECOMMEND_WORK_SIZE = 3;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public TravelGetSpecificResponse getSpecificTravelInfo(Long travelId) {
        Tour tour = getTour(travelId);
        List<String> tourImageList = getTravelImageUrlList(tour.getTourImageList());
        return TravelMapper.toTravelGetSpecificResponse(tour, tourImageList);
    }

    private Tour getTour(long tourId) {
        return tourRepository.findById(tourId)
            .orElseThrow(() -> new TravelNotFoundException(ErrorCode.TRAVEL_NOT_FOUND));
    }

    private List<String> getTravelImageUrlList(List<TourImage> tourImageList) {
        if (tourImageList.isEmpty()) {
            return null;
        } else {
            return tourImageList.stream()
                .map(TourImage::getImageUrl)
                .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    public List<TravelGetRecommdResponse> getTravelRecommd(String jeju) {
        Random random = new Random();
        Set<Long> set = new HashSet<>();
        List<Tour> tours = new ArrayList<>();

        JejuRegion jejuRegion = JejuRegion.fromString(jeju);
        long maxPage = tourRepository.countByJejuRegion(jejuRegion);
        while (tours.size() < RECOMMEND_WORK_SIZE) {
            long randomPage = random.nextLong(maxPage);
            if (set.contains(randomPage)) {
                continue;
            }

            set.add(randomPage);
            Pageable pageable = PageRequest.of((int) randomPage, 1);
            Page<Tour> tour = tourRepository.findByJejuRegion(jejuRegion, pageable);
            tours.add(tour.getContent().get(0));
        }
        return TravelMapper.toTravelGetRecommendResponse(tours);
    }


}
