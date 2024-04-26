package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.TourImpairment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpairmentRepository extends JpaRepository<TourImpairment, Long> {

}
