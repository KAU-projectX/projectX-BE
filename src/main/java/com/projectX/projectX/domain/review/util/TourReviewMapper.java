package com.projectX.projectX.domain.review.util;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.entity.TourReview;
import com.projectX.projectX.domain.review.entity.TourReviewImage;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.global.common.RecommendationType;

public class TourReviewMapper {

    public static TourReview toTourReview(Member member, Tour tour, Integer score,
        String contents, RecommendationType recommendationType) {
        return TourReview.builder()
            .score(score)
            .contents(contents)
            .user(member)
            .tour(tour)
            .recommendationType(recommendationType)
            .build();
    }

    public static TourReviewImage toTourReviewImage(TourReview tourReview, String url) {
        return TourReviewImage.builder()
            .image(url)
            .tourReview(tourReview)
            .build();
    }
}
