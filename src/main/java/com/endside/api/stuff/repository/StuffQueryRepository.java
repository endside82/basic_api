package com.endside.api.stuff.repository;

import com.endside.api.stuff.constants.StuffSearchDateType;
import com.endside.api.stuff.constants.StuffSearchType;
import com.endside.api.stuff.constants.StuffStatus;
import com.endside.api.stuff.param.StuffListParam;
import com.endside.api.stuff.vo.StuffVo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.endside.api.stuff.modle.QStuff.stuff;
import static org.flywaydb.core.internal.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class StuffQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<StuffVo> getStuffListByCondition(StuffListParam stuffListParam, Pageable pageable) {
        return jpaQueryFactory.select(Projections.fields(StuffVo.class,
                        stuff.id,
                        stuff.name,
                        stuff.status,
                        stuff.description,
                        stuff.makeDatetime,
                        stuff.createdAt,
                        stuff.updatedAt
                )).from(stuff)
                .where(
                        inStatus(stuffListParam.getStatuses()),
                        keywordMatch(stuffListParam.getSearchType(),
                                stuffListParam.getSearchValue()),
                        dateBetween(stuffListParam.getSearchDateType(),
                                stuffListParam.getStartDate(),
                                stuffListParam.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(stuff.createdAt.desc())
                .fetch();
    }

    private BooleanExpression inStatus(List<StuffStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return null;
        }
        if (statuses.size() == 1 && statuses.getFirst() != null) {
            return stuff.status.eq(statuses.getFirst());
        }
        return stuff.status.in(statuses);
    }

    private BooleanExpression keywordMatch(StuffSearchType stuffSearchType, String value) {
        if (stuffSearchType == null || !hasText(value)) {
            return null;
        }
        return switch (stuffSearchType) {
            case NAME -> stuff.name.containsIgnoreCase(value);
            case DESCRIPTION -> stuff.description.containsIgnoreCase(value);
            case ID -> stuff.id.eq(Long.parseLong(value));
        };
    }

    private BooleanExpression dateBetween(StuffSearchDateType searchDateType, LocalDate startDate, LocalDate endDate) {
        if (searchDateType == null || startDate == null || endDate == null) {
            return null;
        }
        return switch (searchDateType) {
            case CREATED_AT -> stuff.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
            case UPDATED_AT ->  stuff.updatedAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
            case MAKE_DATETIME -> stuff.makeDatetime.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        };
    }
}
