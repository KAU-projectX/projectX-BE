package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoRepository extends JpaRepository<Sido, Long> {
    public Sido findBySidoCode(Integer areaCode);

}
