package com.endside.api.schedule.service;

import com.endside.api.config.error.ErrorCode;
import com.endside.api.config.error.exception.RestException;
import com.endside.api.schedule.constants.ScheduleConstants;
import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.constants.StartEndType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleServiceTest")
class ScheduleServiceTest {
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    void call_calcScheduleType_then_return_schedule_type() {
        LocalDate startDate = LocalDate.of(2024, 3, 7);
        LocalDate theStartDate = LocalDate.of(2024, 3, 7);
        LocalDate secondsDate = theStartDate.plusDays(1);
        LocalDate dueDate = theStartDate.plusDays(ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN);
        LocalDate dayAfterDueDate = dueDate.plusDays(1);
        ScheduleType type1 = scheduleService.calcScheduleType(theStartDate, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN);
        ScheduleType type2 = scheduleService.calcScheduleType(secondsDate, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN);
        ScheduleType type3 = scheduleService.calcScheduleType(dueDate, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN);
        ScheduleType type4 = scheduleService.calcScheduleType(dayAfterDueDate, startDate, ScheduleConstants.COMMON_MAINTENANCE_PERIOD_SEVEN);
        assertThat(type1).isEqualTo(ScheduleType.WORK);
        assertThat(type2).isEqualTo(ScheduleType.WORK);
        assertThat(type3).isEqualTo(ScheduleType.WORK_MAINTENANCE);
        assertThat(type4).isEqualTo(ScheduleType.WORK);
    }

    @Test
    void call_calcStartEndType_then_return_start_end_type() {
        LocalDate startDate = LocalDate.of(2024, 3, 7);
        LocalDate endDate = LocalDate.of(2024, 3, 10);
        LocalDate date_01 = LocalDate.of(2024, 3, 7);
        LocalDate date_02 = date_01.plusDays(1);
        LocalDate date_03 = date_02.plusDays(1);
        LocalDate date_04 = date_03.plusDays(1);
        LocalDate date_05 = date_04.plusDays(1);
        LocalDate date_00 = date_01.minusDays(1);
        StartEndType t1 = scheduleService.calcStartEndType(date_01, startDate, endDate);
        StartEndType t2 = scheduleService.calcStartEndType(date_02, startDate, endDate);
        StartEndType t3 = scheduleService.calcStartEndType(date_03, startDate, endDate);
        StartEndType t4 = scheduleService.calcStartEndType(date_04, startDate, endDate);
        assertThat(t1).isEqualTo(StartEndType.START);
        assertThat(t2).isEqualTo(StartEndType.NORMAL);
        assertThat(t3).isEqualTo(StartEndType.NORMAL);
        assertThat(t4).isEqualTo(StartEndType.END);
        RestException restException_after_end = Assertions.assertThrows(RestException.class, () -> scheduleService.calcStartEndType(date_05, startDate, endDate));
        assertThat(restException_after_end.getErrorCode().getCode()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getCode());
        assertThat(restException_after_end.getMessage()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getMessage());

        RestException restException_before_start = Assertions.assertThrows(RestException.class, () -> scheduleService.calcStartEndType(date_00, startDate, endDate));
        assertThat(restException_before_start.getErrorCode().getCode()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getCode());
        assertThat(restException_before_start.getMessage()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getMessage());
    }

    @Test
    void call_calcOnlyEndType_then_return_schedule_type() {
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.of(2024, 3, 10);
        LocalDate date_01 = LocalDate.of(2024, 3, 8);
        LocalDate date_02 = date_01.plusDays(1); // 3/9
        LocalDate date_03 = date_02.plusDays(1); // 3/10
        LocalDate date_04 = date_03.plusDays(1); // 3/11
        StartEndType t1 = scheduleService.calcStartEndType(date_01, startDate, endDate);
        StartEndType t2 = scheduleService.calcStartEndType(date_02, startDate, endDate);
        StartEndType t3 = scheduleService.calcStartEndType(date_03, startDate, endDate);
        assertThat(t1).isEqualTo(StartEndType.NORMAL);
        assertThat(t2).isEqualTo(StartEndType.NORMAL);
        assertThat(t3).isEqualTo(StartEndType.END);
        RestException restException = Assertions.assertThrows(RestException.class, () -> scheduleService.calcStartEndType(date_04, startDate, endDate));
        assertThat(restException.getErrorCode().getCode()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getCode());
        assertThat(restException.getMessage()).isEqualTo(ErrorCode.DATE_OUT_OF_RANGE.getMessage());
    }
}