package com.projectX.projectX.domain.review.util;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.dto.response.ReviewGetAllResponse;
import com.projectX.projectX.domain.review.entity.TourReview;
import com.projectX.projectX.domain.review.entity.TourReviewImage;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.review.entity.RecommendationType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

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

    public static List<ReviewGetAllResponse> toTourReviewGetAllResponse(Page<TourReview> reviews) {
        List<ReviewGetAllResponse> tourList = new ArrayList<>();
        for (TourReview review : reviews) {
            ReviewGetAllResponse reviewGetAllResponse = TourReviewMapper.toTourReviewGetResponse(
                review);
            tourList.add(reviewGetAllResponse);
        }

        return tourList;
    }

    private static ReviewGetAllResponse toTourReviewGetResponse(TourReview review) {
        List<TourReviewImage> reviewImages = review.getTourReviewImages();
        if (reviewImages.isEmpty()) {
            return TourReviewMapper.getReviewWithoutImage(review);
        }

        List<String> images = new ArrayList<>();
        for (TourReviewImage image : reviewImages) {
            images.add(image.getImage());
        }

        return TourReviewMapper.getReview(review, images);
    }

    private static ReviewGetAllResponse getReviewWithoutImage(TourReview review) {
        return ReviewGetAllResponse.builder()
            .userNickname(review.getUser().getUserNickName())
            .contents(review.getContents())
            .recommendationType(review.getRecommendationType())
            .score(review.getScore())
            .build();
    }

    private static ReviewGetAllResponse getReview(TourReview review, List<String> images) {
        return ReviewGetAllResponse.builder()
            .userNickname(review.getUser().getUserNickName())
            .contents(review.getContents())
            .recommendationType(review.getRecommendationType())
            .score(review.getScore())
            .images(images)
            .build();
    }
}
