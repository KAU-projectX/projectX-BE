package com.projectX.projectX.domain.calendar.entity;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    @Comment("일정 시작 일자")
    @Column(nullable = false)
    private LocalDate dateFrom;

    @Comment("일정 종료 일자")
    @Column(nullable = false)
    private LocalDate dateTo;

    @Comment("일정 시작 시간")
    private LocalTime timeFrom;

    @Comment("일정 종료 시간")
    private LocalTime timeTo;

    @Comment("일정명")
    @Column(nullable = false)
    private String title;

    @Comment("일정 장소")
    private String location;

    @Comment("일정 메모")
    private String memo;

    @Comment("스케줄 타입")
    @Column(nullable = false)
    private ScheduleType scheduleType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("유저 id")
    private Member user;

    @Builder
    public Calendar(Long id, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom,
        LocalTime timeTo,
        String title, String location, String memo, Member user, ScheduleType scheduleType) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.title = title;
        this.location = location;
        this.memo = memo;
        this.user = user;
        this.scheduleType = scheduleType;
    }
}
