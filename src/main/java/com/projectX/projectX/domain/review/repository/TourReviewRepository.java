package com.projectX.projectX.domain.review.repository;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.entity.TourReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourReviewRepository extends JpaRepository<TourReview, Long> {

    Boolean existsByUser(Member member);
}
