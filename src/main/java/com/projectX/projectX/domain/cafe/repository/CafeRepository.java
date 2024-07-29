package com.projectX.projectX.domain.cafe.repository;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByNameAndAddress(String name, String Address);

    Optional<Cafe> findByCafePersonal(String cafePersonal);

    Page<Cafe> findByCafeType(CafeType cafeType, Pageable pageable);

    Page<Cafe> findByCafeTypeAndJejuRegion(CafeType cafeType, JejuRegion jejuRegion,
        Pageable pageable);

    Long countByJejuRegion(JejuRegion jejuRegion);

    Page<Cafe> findByJejuRegion(JejuRegion jejuRegion, Pageable pageable);

}
