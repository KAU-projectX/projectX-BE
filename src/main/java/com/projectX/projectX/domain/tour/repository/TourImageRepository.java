package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.TourImage;
import com.projectX.projectX.domain.tour.entity.TourImpairment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourImageRepository extends JpaRepository<TourImage, Long> {

    Optional<TourImage> findByTourId(Long tourId);
}
