package com.projectX.projectX.domain.review.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.entity.CafeReview;

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

}
