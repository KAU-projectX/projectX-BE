package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.global.common.ContentType;
import com.projectX.projectX.global.common.JejuRegion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Optional<Tour> findByContentId(Long contentId);

    Page<Tour> findByContentType(ContentType contentType, Pageable pageable);

    Page<Tour> findByContentTypeAndJejuRegion(ContentType contentType, JejuRegion jejuRegion,
        Pageable pageable);
}
