package com.projectX.projectX.domain.tour.util;

import com.projectX.projectX.domain.tour.dto.request.TourSidoStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourSigunguStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourStoreRequest;
import com.projectX.projectX.domain.tour.entity.Sido;
import com.projectX.projectX.domain.tour.entity.Sigungu;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.entity.TourImpairment;
import com.projectX.projectX.global.common.ContentType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourMapper {

    public static Sido toSido(TourSidoStoreRequest tourSidoStoreRequest) {
        return Sido.builder()
            .sidoName(tourSidoStoreRequest.sidoName())
            .sidoCode(tourSidoStoreRequest.sidoCode())
            .build();
    }

    public static Sigungu toSigungu(TourSigunguStoreRequest tourSigunguStoreRequest) {
        return Sigungu.builder()
            .sigunguName(tourSigunguStoreRequest.sigunguName())
            .sigunguCode(tourSigunguStoreRequest.sigunguCode())
            .sido(tourSigunguStoreRequest.sido())
            .build();
    }

    public static Tour toTour(TourStoreRequest tourStoreRequest) {
        ContentType contentType = ContentType.fromInt(tourStoreRequest.contentTypeId());
        return Tour.builder()
            .address(tourStoreRequest.address())
            .spec_address(tourStoreRequest.specAddress())
            .contentId(tourStoreRequest.contentId())
            .contentType(contentType)
            .phone(tourStoreRequest.phone())
            .title(tourStoreRequest.title())
            .mapX(tourStoreRequest.mapX())
            .mapY(tourStoreRequest.mapY())
            .zipCode(tourStoreRequest.zipCode())
            .sigungu(tourStoreRequest.sigungu())
            .sido(tourStoreRequest.sido())
            .imageUrl(tourStoreRequest.imageUrl())
            .thumbnailImageUrl(tourStoreRequest.thumbnailImageUrl())
            .build();
    }

    public static TourImpairment toTourImpairment(Tour tour,
        Map<String, Integer> barrierFreePossibleMap) {
        return TourImpairment.builder()
            .tour(tour)
            .wheelChair(barrierFreePossibleMap.get("wheelChair"))
            .brailleBlock(barrierFreePossibleMap.get("brailleBlock"))
            .audioGuide(barrierFreePossibleMap.get("audioGuide"))
            .videoGuide(barrierFreePossibleMap.get("videoGuide"))
            .stroller(barrierFreePossibleMap.get("stroller"))
            .lactationRoom(barrierFreePossibleMap.get("lactationRoom"))
            .build();
    }
}