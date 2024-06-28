package com.endside.api.schedule.repository;

import com.endside.api.schedule.constants.ScheduleType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.endside.api.schedule.model.Schedule;
import com.endside.api.schedule.param.ScheduleConditionParam;
import com.endside.api.schedule.param.ScheduleListParam;
import com.endside.api.schedule.vo.ScheduleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.endside.api.schedule.model.QSchedule.schedule;


@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 스케쥴 리스트를 가져온다.
    public List<ScheduleVo> getScheduleList(ScheduleListParam scheduleListParam, Pageable pageable) {
        return jpaQueryFactory
                .select(Projections.fields(ScheduleVo.class,
                        schedule.id,            // 스케쥴 고유번호
                        schedule.stuffId,       // 물건 고유번호
                        schedule.receiptId,     // 접수 고유번호
                        schedule.date,          // 스케쥴 날짜
                        schedule.scheduleType,  // 스케쥴 종류
                        schedule.startEndType,  // 시작종료구분
                        schedule.createdAt,     // 생성시각
                        schedule.updatedAt      // 변경시각
                ))
                .from(schedule)
                .where(isReceiptId(scheduleListParam.getReceiptId()),           // 접수 고유번호
                            isStuffId(scheduleListParam.getStuffId()),          // 물건 고유번호
                        isScheduleType(scheduleListParam.getScheduleType()),    // 스케쥴 종류
                        inScheduleType(scheduleListParam.getScheduleTypes()),   // 스케쥴 종류 리스트
                        eqDate(scheduleListParam.getDate()),                    // 스케쥴 날짜
                        startAndEndDateCondition(scheduleListParam.getStartDate(), scheduleListParam.getEndDate()) // 스케쥴 날짜 검색 시작 마지막
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(schedule.createdAt.desc())
                .fetch();
    }

    // 해당 스케쥴 타입이 아닌 스케쥴을 접수 번호로 가져온다.
    public List<Schedule> getSchedulesByReceiptIdNotInScheduleType(long receiptId, List<ScheduleType> schduleTypeList) {
        return jpaQueryFactory
                .select(schedule)
                .from(schedule)
                .where(schedule.receiptId.eq(receiptId)
                        , notInScheduleType(schduleTypeList))
                .orderBy(schedule.createdAt.desc())
                .fetch();
    }

    // 해당 조건의 스케쥴이 있는지 없는지 확인
    public boolean existScheduleByCondition(ScheduleConditionParam param) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(schedule)
                .where(isReceiptId(param.getReceiptId()),                                   // 접수 고유번호
                        isStuffId(param.getStuffId()),                                      // 물건 고유번호
                        isScheduleType(param.getScheduleType()),                            // 스케쥴 종류
                        inScheduleType(param.getScheduleTypes()),                           // 스케쥴 종류 리스트
                        eqDate(param.getDate()),                                            // 스케쥴 날짜
                        startAndEndDateCondition(param.getStartDate(), param.getEndDate())  // 스케쥴 날짜 검색 시작 마지막
                )
                .fetchFirst();      // limit 1
        return fetchOne != null;    // 1개가 있는지 없는지 판단
    }

    // 조건에 해당하는 모든 스케쥴을 가져온다
    public List<Schedule> getAllSchedulesByCondition(ScheduleConditionParam param) {
        return jpaQueryFactory
                .select(schedule)
                .from(schedule)
                .where(isReceiptId(param.getReceiptId()),
                        isStuffId(param.getStuffId()),
                        isScheduleType(param.getScheduleType()),
                        eqDate(param.getDate()),
                        startAndEndDateCondition(param.getStartDate(), param.getEndDate())
                )
                .orderBy(schedule.date.asc())
                .fetch();
    }

    // 
    private BooleanExpression isStuffId(Long stuffId) {
        return stuffId == null ? null : schedule.stuffId.eq(stuffId);
    }

    private BooleanExpression isReceiptId(Long receiptId) {
        return receiptId == null ? null : schedule.receiptId.eq(receiptId);
    }

    private BooleanExpression notInScheduleType(List<ScheduleType> scheduleTypes) {
        if (scheduleTypes == null || scheduleTypes.isEmpty()) {
            return null;
        }
        return schedule.scheduleType.notIn(scheduleTypes);
    }

    private BooleanExpression isScheduleType(ScheduleType scheduleType) {
        return scheduleType == null ? null : schedule.scheduleType.eq(scheduleType);
    }

    private BooleanExpression inScheduleType(List<ScheduleType> scheduleTypes) {
        if (scheduleTypes == null || scheduleTypes.isEmpty()) {
            return null;
        }
        if (scheduleTypes.size() == 1 && scheduleTypes.getFirst() != null) {
            return schedule.scheduleType.eq(scheduleTypes.getFirst());
        }
        return schedule.scheduleType.in(scheduleTypes);
    }

    private BooleanExpression eqDate(LocalDate date) {
        return (date == null) ? null : schedule.date.eq(date);
    }

    private BooleanExpression startAndEndDateCondition(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        BooleanExpression startDateCondition = startDate != null ? schedule.date.goe(startDate) : null;
        BooleanExpression endDateCondition = endDate != null ? schedule.date.lt(endDate) : null;
        return Expressions.allOf(startDateCondition, endDateCondition);
    }


}
