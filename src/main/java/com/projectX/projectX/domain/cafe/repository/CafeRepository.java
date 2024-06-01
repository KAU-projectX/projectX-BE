package com.projectX.projectX.domain.cafe.repository;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByNameAndAddress(String name, String Address);
    Optional<Cafe> findByCafeId(String cafeId);
}
