package com.projectX.projectX.domain.calendar.service;

import com.projectX.projectX.domain.calendar.dto.request.CalendarScheduleRequest;
import com.projectX.projectX.domain.calendar.dto.response.AllCalendarScheduleResponse;
import com.projectX.projectX.domain.calendar.entity.Calendar;
import com.projectX.projectX.domain.calendar.repository.CalendarRepository;
import com.projectX.projectX.domain.calendar.util.CalendarMapper;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    private Member checkMemberExist(String email) {
        return memberRepository.findByUserEmail(email).orElseThrow(
            () -> new InvalidMemberException(ErrorCode.INVALID_MEMBER_EXCEPTION)
        );
    }

    @Transactional
    public void createSchedule(CalendarScheduleRequest calendarScheduleRequest,
        String email) {
        Member member = checkMemberExist(email);
        Calendar calendar = CalendarMapper.toCalendar(member, calendarScheduleRequest);
        calendarRepository.save(calendar);
    }


    @Transactional(readOnly = true)
    public List<AllCalendarScheduleResponse> getAllSchedule(int year, String email) {
        Member member = checkMemberExist(email);
        List<AllCalendarScheduleResponse> calendarScheduleList = calendarRepository.getScheduleFromYear(year, member.getId());
        return calendarScheduleList;

    }
}
