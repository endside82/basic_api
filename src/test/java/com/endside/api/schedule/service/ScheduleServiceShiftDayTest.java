package com.endside.api.schedule.service;

import com.endside.api.schedule.constants.ScheduleConstants;
import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.model.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleServiceTest")
class ScheduleServiceShiftDayTest {
    @InjectMocks
    ScheduleService scheduleService;

    /**
     * 연속 날짜 타입 , 변경불가능한 날짜 있음
     **/
    @Test
    void call_shiftGapDays_no_blank_type_schedule_then_return_shifted_day() {
        long stuffId = 1L;
        long receiptId = 1L;
        final int shiftGapDays = 7; // 뒤로 미룰 날짜
        // 기간 사이 날짜
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 18);
        List<Schedule> oldSchedules = generateSchedule(stuffId, receiptId, startDate, endDate);
        // 변경 불가능 날짜
        List<LocalDate> immovableDate = new ArrayList<>();
        immovableDate.add(LocalDate.of(2024, 3, 5));
        immovableDate.add(LocalDate.of(2024, 3, 10));
        immovableDate.add(LocalDate.of(2024, 3, 14));

        List<Schedule> immovableSchedules = generateScheduleByDateList(stuffId, receiptId, immovableDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        oldSchedules.sort(Comparator.comparing(Schedule::getDate)); // 정렬
        List<LocalDate> immovableDates = immovableSchedules.stream()
                .map(Schedule::getDate)
                .toList();
        scheduleService.shiftGapDaysWithImmovableSchedule(oldSchedules, shiftGapDays, immovableDates);
        oldSchedules.sort(Comparator.comparing(Schedule::getDate)); // 정렬
        oldSchedules.forEach(schedule -> {
            log.info(schedule.getDate().format(formatter));
        });
    }

    /**
     * 연속 날짜 타입 , 변경불가능한 날짜 있음
     **/
    @Test
    void call_shiftGapDays_blank_type_schedule1_then_return_shifted_day() {
        long stuffId = 1L;
        long receiptId = 1L;
        // 변경 불가능 날짜
        List<LocalDate> immovableDate = new ArrayList<>();
        immovableDate.add(LocalDate.of(2024, 3, 5));
        immovableDate.add(LocalDate.of(2024, 3, 10));
        immovableDate.add(LocalDate.of(2024, 3, 14));
        List<Schedule> immovableSchedules = generateScheduleByDateList(stuffId, receiptId, immovableDate);

        // 기간 사이 날짜
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 18);
        List<Schedule> oldSchedules = generateScheduleExceptDates(stuffId, receiptId, startDate, endDate, immovableDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<LocalDate> immovableDates = immovableSchedules.stream()
                .map(Schedule::getDate)
                .toList();
        scheduleService.shiftGapDaysWithImmovableSchedule(oldSchedules, 7, immovableDates);
        oldSchedules.sort(Comparator.comparing(Schedule::getDate)); // 정렬
        oldSchedules.forEach(schedule -> {
            log.info(schedule.getDate().format(formatter));
        });
    }

    @Test
    void call_shiftGapDays_blank_type_schedule_then_return_shifted_day() {
        long stuffId = 1L;
        long receiptId = 1L;

        List<LocalDate> immovableDate = new ArrayList<>();
        immovableDate.add(LocalDate.of(2024, 3, 10));
        immovableDate.add(LocalDate.of(2024, 3, 11));
        immovableDate.add(LocalDate.of(2024, 3, 12));
        immovableDate.add(LocalDate.of(2024, 3, 13));
        immovableDate.add(LocalDate.of(2024, 3, 14));
        List<Schedule> immovableSchedules = generateScheduleByDateList(stuffId, receiptId, immovableDate);

        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 20);
        List<Schedule> oldSchedules = generateScheduleExceptDates(stuffId, receiptId, startDate, endDate, immovableDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<LocalDate> immovableDates = immovableSchedules.stream()
                .map(Schedule::getDate)
                .toList();
        scheduleService.shiftGapDaysWithImmovableSchedule(oldSchedules, 7, immovableDates);
        oldSchedules.sort(Comparator.comparing(Schedule::getDate)); // 정렬
        oldSchedules.forEach(schedule -> {
            log.info(schedule.getDate().format(formatter));
        });
    }

    /**
     * 스케쥴 더미생성 : 기간 사이
     **/
    private List<Schedule> generateSchedule(long stuffId, long receiptId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            schedules.add(Schedule.builder()
                    .stuffId(stuffId)     // 간병사 고유번호
                    .receiptId(receiptId)         // 접수 고유번호
                    .date(date)                   // 스케쥴 날짜
                    .scheduleType(scheduleService.calcScheduleType(date, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN))   // 스케쥴 종류
                    .startEndType(scheduleService.calcStartEndType(date, startDate, endDate))   // 시작종료구분
                    .build());
        });
        return schedules;
    }

    /**
     * 스케쥴 더미생성 : 기간 사이에 특정 날짜 제외 하고
     **/
    private List<Schedule> generateScheduleExceptDates(long stuffId, long receiptId, LocalDate startDate, LocalDate endDate, List<LocalDate> immovableDate) {
        List<Schedule> schedules = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            if (!immovableDate.contains(date)) {
                schedules.add(Schedule.builder()
                        .stuffId(stuffId)     // 간병사 고유번호
                        .receiptId(receiptId)         // 접수 고유번호
                        .date(date)                   // 스케쥴 날짜
                        .scheduleType(scheduleService.calcScheduleType(date, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN))   // 스케쥴 종류
                        .startEndType(scheduleService.calcStartEndType(date, startDate, endDate))   // 시작종료구분
                        .build());
            }
        });
        return schedules;
    }

    /**
     * 스케쥴 더미생성 : 지정 날짜 리스트
     **/
    private List<Schedule> generateScheduleByDateList(long stuffId, long receiptId, List<LocalDate> immovableDate) {

        List<Schedule> immovableSchedules = new ArrayList<>();
        immovableDate.forEach(date -> {
            immovableSchedules.add(
                    Schedule.builder()
                            .stuffId(stuffId) // 간병사 고유번호
                            .receiptId(receiptId) // 접수 고유번호
                            .date(date) // 스케쥴 날짜
                            .scheduleType(ScheduleType.UNPAID_REST) // 스케쥴 종류
                            .startEndType(null) // 시작종료구분
                            .build());
        });
        return immovableSchedules;
    }

    // 도래할 연속 날짜 갯수 계산
    @Test
    void continuouslyCheckContainTest() {
        // 날짜 리스트
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(LocalDate.of(2024, 3, 10));
        dateList.add(LocalDate.of(2024, 3, 11));
        // 불연속 12
        dateList.add(LocalDate.of(2024, 3, 13));
        dateList.add(LocalDate.of(2024, 3, 14));

        LocalDate date = LocalDate.of(2024, 3, 5);
        long shiftDays = scheduleService.getFutureShiftDays(date, dateList, 5);
        assertThat(shiftDays).isEqualTo(2);

        // 날짜 리스트
        List<LocalDate> dateList2 = new ArrayList<>();
        dateList2.add(LocalDate.of(2024, 3, 10));
        dateList2.add(LocalDate.of(2024, 3, 11));
        dateList2.add(LocalDate.of(2024, 3, 12));
        dateList2.add(LocalDate.of(2024, 3, 13));
        dateList2.add(LocalDate.of(2024, 3, 14));

        LocalDate date2 = LocalDate.of(2024, 3, 10);
        long shiftDays2 = scheduleService.getFutureShiftDays(date2, dateList2, 0);
        assertThat(shiftDays2).isEqualTo(5);

        LocalDate date3 = LocalDate.of(2024, 3, 8);
        long shiftDays3 = scheduleService.getFutureShiftDays(date3, dateList2, 0);
        assertThat(shiftDays3).isEqualTo(0);

    }

    @Test
    void call_splitScheduleListByDate_return_split_schedules(){
        long stuffId = 1L;
        long receiptId = 1L;
        // 기간 사이 날짜
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 5);
        List<Schedule> oldSchedules = generateSchedule(stuffId, receiptId, startDate, endDate);
        Map<Boolean, List<Schedule>> splitSchedule = scheduleService.splitScheduleListByDate(oldSchedules, LocalDate.of(2024, 3, 3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Schedule> inRangeSchedule = splitSchedule.get(ScheduleService.IS_NOT_OUT_OF_END_DATE);
        List<LocalDate> inRangeDates = inRangeSchedule.stream()
                .map(Schedule::getDate)
                .toList();
        inRangeDates.forEach(date -> {
            log.info(date.format(formatter));
        });
        assertThat(inRangeDates.contains(LocalDate.of(2024, 3, 1))).isTrue();
        assertThat(inRangeDates.contains(LocalDate.of(2024, 3, 2))).isTrue();
        assertThat(inRangeDates.contains(LocalDate.of(2024, 3, 3))).isTrue();
        assertThat(inRangeDates.contains(LocalDate.of(2024, 3, 4))).isFalse();
        assertThat(inRangeDates.contains(LocalDate.of(2024, 3, 5))).isFalse();
        log.info("----------------------------------");
        List<Schedule> outRangeSchedule = splitSchedule.get(ScheduleService.IS_OUT_OF_END_DATE);
        List<LocalDate> outRangeDates = outRangeSchedule.stream()
                .map(Schedule::getDate)
                .toList();
        outRangeDates.forEach(date -> {
            log.info(date.format(formatter));
        });
        assertThat(outRangeDates.contains(LocalDate.of(2024, 3, 1))).isFalse();
        assertThat(outRangeDates.contains(LocalDate.of(2024, 3, 2))).isFalse();
        assertThat(outRangeDates.contains(LocalDate.of(2024, 3, 3))).isFalse();
        assertThat(outRangeDates.contains(LocalDate.of(2024, 3, 4))).isTrue();
        assertThat(outRangeDates.contains(LocalDate.of(2024, 3, 5))).isTrue();
    }


}