package com.projectX.projectX.domain.review.repository;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.entity.CafeReview;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeReviewRepository extends JpaRepository<CafeReview, Long> {

    Boolean existsByUser(Member member);

    Optional<CafeReview> findByCafe(Cafe cafe);

    Page<CafeReview> findByCafe(Cafe cafe, Pageable pageable);
}
