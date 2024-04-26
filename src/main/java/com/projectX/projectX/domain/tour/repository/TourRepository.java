package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Tour findByContentId(Long contentId);

}