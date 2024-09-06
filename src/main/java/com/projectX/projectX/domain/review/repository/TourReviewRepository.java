package com.projectX.projectX.domain.review.repository;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.entity.TourReview;
import com.projectX.projectX.domain.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourReviewRepository extends JpaRepository<TourReview, Long> {

    Boolean existsByUserAndTour(Member member, Tour tour);

    Page<TourReview> findByTour(Tour tour, Pageable pageable);
}
