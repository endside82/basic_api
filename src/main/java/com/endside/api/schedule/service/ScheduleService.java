package com.endside.api.schedule.service;

import com.endside.api.config.error.ErrorCode;
import com.endside.api.config.error.exception.RestException;
import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.constants.StartEndType;
import com.endside.api.schedule.model.Schedule;
import com.endside.api.schedule.param.*;
import com.endside.api.schedule.repository.ScheduleQueryRepository;
import com.endside.api.schedule.repository.ScheduleRepository;
import com.endside.api.schedule.util.ScheduleMapper;
import com.endside.api.schedule.vo.ScheduleVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleQueryRepository scheduleQueryRepository;
    protected static final boolean IS_NOT_OUT_OF_END_DATE = false;  // 마지막 날짜를 벗어 나지 않았다.
    protected static final boolean IS_OUT_OF_END_DATE = true;       // 마지막 날짜를 벗어 났다.

    protected static final int DATELESS = 0; // 날짜가 없다. (0 일)

    @Transactional(readOnly = true)
    public ScheduleVo getSchedule(ScheduleParam scheduleParam) {
        Schedule schedule = retrieveSchedule(scheduleParam.getId());
        return scheduleMapper.updateScheduleToVo(schedule);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ScheduleVo addSchedule(ScheduleAddParam scheduleAddParam) {
        Schedule schedule = scheduleMapper.mapToSchedule(scheduleAddParam);
        scheduleRepository.save(schedule);
        return scheduleMapper.updateScheduleToVo(schedule);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ScheduleVo modSchedule(ScheduleModParam scheduleModParam) {
        Schedule schedule = retrieveSchedule(scheduleModParam.getId());
        scheduleMapper.updateScheduleFromParam(scheduleModParam, schedule);
        scheduleRepository.save(schedule);
        return scheduleMapper.updateScheduleToVo(schedule);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteSchedule(ScheduleDelParam scheduleDelParam) {
        Schedule schedule = retrieveSchedule(scheduleDelParam.getScheduleId());
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleVo> getScheduleList(ScheduleListParam scheduleListParam) {
        return scheduleQueryRepository.getScheduleList(scheduleListParam, scheduleListParam.of());
    }

    private Schedule retrieveSchedule(long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new RestException(ErrorCode.SCHEDULE_NOT_EXIST)
        );
    }

    //
    protected ScheduleType calcScheduleType(LocalDate date, LocalDate startDate, long leavePeriod) {
        if (leavePeriod == DATELESS) {
            return ScheduleType.WORK; // 워크
        }
        long days = ChronoUnit.DAYS.between(startDate, date);
        return (days != DATELESS // 첫날
                && Math.floorMod(days, leavePeriod) == 0)   // 정해진 단위 가간 당일이면 (기간으로 나누어 떨어진다)
                ? ScheduleType.WORK_MAINTENANCE             // 워크 + 정비
                : ScheduleType.WORK;                        // 워크
    }

    // 기간 안에 물건의 스케쥴이 있는지 확인한다.
    public boolean checkExistStuffScheduleInPeriod(long stuffId, ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        return scheduleQueryRepository.existScheduleByCondition(
                ScheduleConditionParam.builder()
                        .stuffId(stuffId)           // 물건 고유번호
                        .scheduleType(scheduleType) // 스케쥴 종류
                        .startDate(startDate)       // 스케쥴 검색용 시작 날짜
                        .endDate(endDate)           // 스케쥴 검색용 마지막 날짜
                        .build());
    }



    public void modStuffPeriodSchedule(Long receiptId, Long stuffId, int leavePeriod,
                                           LocalDate originStartDate, LocalDate originEndDate,
                                           LocalDate changeStartDate, LocalDate changeEndDate) {

        long oldDays = ChronoUnit.DAYS.between(originStartDate, originEndDate);         // 기존 할당 일 수
        long newDays = ChronoUnit.DAYS.between(changeStartDate, changeEndDate);         // 변경 할당 일 수
        long shiftGapDays = ChronoUnit.DAYS.between(originStartDate, changeStartDate);  // 변경 될 일 수 (시작일 기준)
        // 부동 스케쥴 (날짜만)
        List<LocalDate> immovableDates = getImmovableDates(receiptId);
        // 접수에 속한 기존 스케쥴
        List<Schedule> oldSchedules = getSchedulesByReceiptId(receiptId);
        // 기간 동일 or 기간 단축
        List<Schedule> changedSchedules = shiftScheduleDate(oldSchedules, immovableDates, shiftGapDays);
        // lastDay -> StartEndType.NORMAL
        Schedule lastDay = changedSchedules.getLast(); // 변경될 마지막 스케쥴
        LocalDate changeLastDate = lastDay.getDate();  // 마지막 날짜
        if (oldDays >= newDays) {
            if (changeEndDate.equals(changeLastDate)) {
                scheduleRepository.saveAll(changedSchedules);
            } else {
                // 같은 날짜 만큼 기간만 변경 되었어도 부동 스케쥴 여부에 따라서 마지막 날짜 기간이 달라질 수 있다.
                Map<Boolean, List<Schedule>> splitSchedule = splitScheduleListByDate(changedSchedules, changeEndDate);
                scheduleRepository.saveAll(splitSchedule.get(IS_NOT_OUT_OF_END_DATE));
                scheduleRepository.deleteAll(splitSchedule.get(IS_OUT_OF_END_DATE));
            }
        }
        // 기간 연장
        if (oldDays < newDays) {
            lastDay.changeStartEndType(StartEndType.NORMAL);
            scheduleRepository.saveAll(changedSchedules);
            // 추가 될 날짜 첫날(기존 추가된 스케쥴 날짜 다음날)
            LocalDate remainFirstDate = changeLastDate.plusDays(1);
            // 새로 추가될 스케쥴
            List<Schedule> newSchedules = remainFirstDate.datesUntil(changeEndDate)
                    .filter(date -> isScheduleChangeable(date, immovableDates))
                    .map(date -> createSchedule(stuffId, receiptId, date, changeStartDate, changeEndDate, leavePeriod, false))
                    .collect(Collectors.toList());
            scheduleRepository.saveAll(newSchedules);
        }
    }

    // Method to check if the schedule is changeable
    private boolean isScheduleChangeable(LocalDate date, List<LocalDate> immovableDates) {
        return !immovableDates.contains(date);
    }

    // 연속된 스케쥴을 넣는다.
    public void addStuffSchedule(Long stuffId, Long receiptId, int leavePeriod, LocalDate startDate, LocalDate endDate) {
        startDate.datesUntil(endDate.plusDays(1))
                .forEach(date -> createSchedule(stuffId, receiptId, date, startDate, endDate, leavePeriod, true));
    }

    // 단순 스케쥴 변경
    public void modScheduleOfStuff(long receiptId, long stuffId, final LocalDate startDate, int leavePeriod) {
        List<Schedule> schedules = scheduleQueryRepository.getAllSchedulesByCondition(
                ScheduleConditionParam.builder()
                        .receiptId(receiptId)
                        .startDate(startDate)
                        .build());
        scheduleRepository.deleteAll(schedules);
        Schedule firstSchedule = schedules.getFirst();
        // 시작일 타입이 필요한지 여부
        boolean isNeedStartType = firstSchedule.getStartEndType() != StartEndType.START;
        // 마지막 스케쥴
        Schedule lastSchedule = schedules.getLast();
        final LocalDate lastDate = lastSchedule.getDate();
        // 추가 될 날짜 첫날(기존 추가된 스케쥴 날짜 다음날)
        // 부동 스케쥴 (날짜만)
        List<LocalDate> immovableDates = getImmovableDates(receiptId);
        // 다시 추가될 스케쥴
        List<Schedule> renewSchedules = startDate.datesUntil(lastDate.plusDays(1))
                .filter(date -> isScheduleChangeable(date, immovableDates))
                .map(date -> createSchedule(stuffId, receiptId, date, startDate, lastDate, leavePeriod, isNeedStartType))
                .collect(Collectors.toList());
        scheduleRepository.saveAll(renewSchedules);
    }

    // Method to create a new schedule, 마지막 날 타입만 확인해서 넣는다.
    // isNeedStartType 중간부터 스케쥴을 넣기 때문에 시작 날짜 타입을 계산하지 않아도 되는 케이스가 있음
    private Schedule createSchedule(Long stuffId, Long receiptId, LocalDate date, LocalDate startDate, LocalDate endDate, long leavePeriod, boolean isNeedStartType) {
        LocalDate startEndTypeStartDate = isNeedStartType ? startDate : null;
        return Schedule.builder()
                .stuffId(stuffId)
                .receiptId(receiptId)
                .date(date)
                .scheduleType(calcScheduleType(date, startDate, leavePeriod))
                .startEndType(calcStartEndType(date, startEndTypeStartDate, endDate))
                .build();
    }

    public Map<Boolean, List<Schedule>> splitScheduleListByDate(List<Schedule> list, LocalDate date) {
        return list.stream().collect(Collectors.partitioningBy(schedule -> schedule.getDate().isAfter(date)));
    }

    private List<LocalDate> getImmovableDates(long receiptId) {
        // 변경 불가능 한 스케쥴 조회 : 해당 접수에 속한 스케쥴 뿐만 아니라 물건 고유의 이동 불가한 스케쥴도 고려하여 스케쥴을 가져온다.
        List<Schedule> immovableSchedules = getImmovableSchedule(receiptId);
        return immovableSchedules.stream()
                .map(Schedule::getDate)
                .toList();
    }

    protected List<Schedule> shiftScheduleDate(List<Schedule> oldSchedules, List<LocalDate> immovableDates, long shiftGapDays) {
        // shift gapDays AND Check Available
        if (immovableDates.isEmpty()) {
            for (Schedule schedule : oldSchedules) {
                schedule.shiftDay(shiftGapDays);
            }
        } else {
            shiftGapDaysWithImmovableSchedule(oldSchedules, shiftGapDays, immovableDates); // 기간 만큼 변경
        }
        return oldSchedules;
    }

    /**
     * 날짜를 변경 한다.
     * old schedule 은 기존에 기간안의 모든 스케쥴을 가져온다.
     */
    protected void shiftGapDaysWithImmovableSchedule(List<Schedule> oldSchedules, long shiftGapDays, List<LocalDate> immovableDates) {
        // 기존 스케쥴을 날짜에 맞게 정렬한다.
        oldSchedules.sort(Comparator.comparing(Schedule::getDate));
        LocalDate previousDate = null;

        long adjustShiftDays = 0;  // 마지막 최종 변경 날짜 차이
        for (Schedule schedule : oldSchedules) {
            LocalDate currentDate = schedule.getDate(); // 변경할 기존 스케쥴 날짜
            if (immovableDates.contains(currentDate)) {
                continue; // Skip shifting for immovable dates
            }
            if (previousDate != null) {
                long daysBetween = ChronoUnit.DAYS.between(previousDate, currentDate);
                adjustShiftDays -= Math.max(0, daysBetween - 1);
            }
            adjustShiftDays += getFutureShiftDays(currentDate, immovableDates, shiftGapDays + adjustShiftDays);
            previousDate = currentDate;

            long finalShiftDay = shiftGapDays + adjustShiftDays;
            if (finalShiftDay != 0) {
                schedule.shiftDay(finalShiftDay);
                schedule.updateNow();
            }
        }
    }

    //
    protected int getFutureShiftDays(LocalDate date, List<LocalDate> immovableDates, long shiftGapDays) {
        int daysToShift = 0;
        LocalDate futureDate = date.plusDays(shiftGapDays);
        while (immovableDates.contains(futureDate)) {
            daysToShift++;
            futureDate = futureDate.plusDays(1);
        }
        return daysToShift;
    }

    // 접수에 속한 모든 스케쥴
    private List<Schedule> getSchedulesByReceiptId(long receiptId) {
        return scheduleQueryRepository.getAllSchedulesByCondition(
                ScheduleConditionParam.builder()
                        .receiptId(receiptId).
                        build());
    }

    // 기간 안에 일정 중 변경 불가능한 일정을 가져온다. (유급휴가, 무급휴가)
    protected List<Schedule> getImmovableSchedule(long receiptId) {
        List<ScheduleType> scheduleTypes = Arrays.asList(ScheduleType.MAINTENANCE, ScheduleType.UNPAID_REST);
        return scheduleQueryRepository.getSchedulesByReceiptIdNotInScheduleType(receiptId, scheduleTypes);
    }

    // 시작일인경우 시작일 타입 마지막 날에는 마지막날 타입 , 그외는 그냥 NORMAL)
    protected StartEndType calcStartEndType(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if ((startDate != null && date.isBefore(startDate))
                || (endDate != null && date.isAfter(endDate))) {
            throw new RestException(ErrorCode.DATE_OUT_OF_RANGE);
        }
        if (startDate != null && startDate.equals(date)) {
            return StartEndType.START;
        }
        if (endDate != null && endDate.equals(date)) {
            return StartEndType.END;
        }
        return StartEndType.NORMAL;
    }

    public void deleteScheduleOfStuff(long receiptId, long stuffId, LocalDate startDate) {
        List<Schedule> schedules = scheduleQueryRepository.getAllSchedulesByCondition(
                ScheduleConditionParam.builder()
                        .receiptId(receiptId)
                        .stuffId(stuffId)
                        .startDate(startDate)
                        .build());
    }
}
