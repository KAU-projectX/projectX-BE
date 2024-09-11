package com.projectX.projectX.domain.calendar.repository;

import com.projectX.projectX.domain.calendar.dto.response.AllCalendarScheduleResponse;
import com.projectX.projectX.domain.calendar.entity.Calendar;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("""
    SELECT c.dateFrom, c.dateTo, c.title, c.scheduleType
    FROM Calendar c
    WHERE c.user.id = :memberId AND FUNCTION('YEAR', c.dateFrom) = :year
""")
    List<AllCalendarScheduleResponse> getScheduleFromYear(int year, long memberId);


}
