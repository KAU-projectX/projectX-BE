package com.projectX.projectX.domain.tour.util;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.entity.TourImage;
import com.projectX.projectX.domain.tour.entity.TourImpairment;
import com.projectX.projectX.global.common.ContentType;
import com.projectX.projectX.global.common.JejuRegion;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourMapper {

    public static Tour toTour(Map<String, String> tourMap) {
        Long contentId = Long.valueOf(tourMap.get("contentId"));
        ContentType contentType = ContentType.fromInt(
            Integer.valueOf(tourMap.get("contentTypeId")));
        float mapX = Float.parseFloat(tourMap.get("mapX"));
        float mapY = Float.parseFloat(tourMap.get("mapY"));
        Long zipCode = Long.valueOf(tourMap.get("zipCode"));
        JejuRegion jejuRegion = JejuRegion.fromInt(Integer.valueOf(tourMap.get("jejuRegion")));
        return Tour.builder()
            .address(tourMap.get("address"))
            .spec_address(tourMap.get("specAddress"))
            .contentId(contentId)
            .contentType(contentType)
            .phone(tourMap.get("phone"))
            .title(tourMap.get("title"))
            .imageUrl(tourMap.get("imageUrl"))
            .mapX(mapX)
            .mapY(mapY)
            .jejuRegion(jejuRegion)
            .zipCode(zipCode)
            .imageUrl(tourMap.get("imageUrl"))
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

    public static TourImage toTourImage(Tour tour, String tourImage) {
        return TourImage.builder()
            .tour(tour)
            .imageUrl(tourImage)
            .build();
    }
}