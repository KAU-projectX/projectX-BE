package com.projectX.projectX.domain.review.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.dto.response.ReviewGetAllResponse;
import com.projectX.projectX.domain.review.entity.CafeReview;
import com.projectX.projectX.domain.review.entity.CafeReviewImage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

public class ReviewMapper {

    public static CafeReview toCafeReview(Member member, Cafe cafe, Integer score,
        String contents) {
        return CafeReview.builder()
            .score(score)
            .contents(contents)
            .user(member)
            .cafe(cafe)
            .build();
    }

    public static CafeReviewImage toCafeReviewImage(CafeReview cafeReview, String imageUrl) {
        return CafeReviewImage.builder()
            .image(imageUrl)
            .cafeReview(cafeReview)
            .build();
    }

    public static List<ReviewGetAllResponse> toCafeReviewGetAllResponse(Page<CafeReview> reviews) {
        List<ReviewGetAllResponse> cafeList = new ArrayList<>();
        for (CafeReview review : reviews) {
            ReviewGetAllResponse reviewGetAllResponse = ReviewMapper.toCafeReviewGetResponse(review);
            cafeList.add(reviewGetAllResponse);
        }

        return cafeList;
    }

    private static ReviewGetAllResponse toCafeReviewGetResponse(CafeReview review) {
        List<CafeReviewImage> reviewImages = review.getCafeReviewImages();
        if (reviewImages.isEmpty()) {
            return ReviewMapper.getReviewWithoutImage(review);
        }

        List<String> images = new ArrayList<>();
        for (CafeReviewImage image : reviewImages) {
            images.add(image.getImage());
        }

        return ReviewMapper.getReview(review, images);

    }

    private static ReviewGetAllResponse getReviewWithoutImage(CafeReview review) {
        return ReviewGetAllResponse.builder()
            .userNickname(review.getUser().getUserNickName())
            .contents(review.getContents())
            .recommendationType(review.getRecommendationType())
            .score(review.getScore())
            .build();
    }

    private static ReviewGetAllResponse getReview(CafeReview review, List<String> images) {
        return ReviewGetAllResponse.builder()
            .userNickname(review.getUser().getUserNickName())
            .contents(review.getContents())
            .recommendationType(review.getRecommendationType())
            .score(review.getScore())
            .images(images)
            .build();
    }

}
