package com.projectX.projectX.domain.tour.repository;

import com.projectX.projectX.domain.tour.entity.Sido;
import com.projectX.projectX.domain.tour.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    public Sigungu findBySigunguCodeAndSido(Integer sigungu, Sido sido);

}
