package com.projectX.projectX.domain.tour.util;

import com.projectX.projectX.domain.tour.dto.request.TourSidoStoreRequest;
import com.projectX.projectX.domain.tour.dto.request.TourSigunguStoreRequest;
import com.projectX.projectX.domain.tour.dto.response.TourSidoStoreResponse;
import com.projectX.projectX.domain.tour.entity.Sido;
import com.projectX.projectX.domain.tour.entity.Sigungu;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.entity.TourImage;
import io.swagger.v3.core.util.Json;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourMapper {

    public static Sido toSido(TourSidoStoreRequest tourSidoStoreRequest){
        return Sido.builder()
            .sidoName(tourSidoStoreRequest.sidoName())
            .sidoCode(tourSidoStoreRequest.sidoCode())
            .build();
    }

    public static Sigungu toSigungu(TourSigunguStoreRequest tourSigunguStoreRequest){
        return Sigungu.builder()
            .sigunguName(tourSigunguStoreRequest.sigunguName())
            .sigunguCode(tourSigunguStoreRequest.sigunguCode())
            .sido(tourSigunguStoreRequest.sido())
            .build();
    }

    public static TourImage toTourImage(Tour tour, HashMap<String, String> TourImageMap){
        return TourImage.builder()
            .tour(tour)
            .imageUrl(TourImageMap.get("imageUrl"))
            .thumbnailImageUrl(TourImageMap.get("thumbnailImageUrl"))
            .build();
    }

}
