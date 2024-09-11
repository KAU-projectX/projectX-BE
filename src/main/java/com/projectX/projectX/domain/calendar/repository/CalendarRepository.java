package com.projectX.projectX.domain.calendar.repository;

import com.projectX.projectX.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {


}
