package com.endside.api.stuff.param;

import com.endside.api.common.param.PagingParam;
import com.endside.api.stuff.constants.StuffSearchDateType;
import com.endside.api.stuff.constants.StuffSearchType;
import com.endside.api.stuff.constants.StuffStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class StuffListParam extends PagingParam {
    private List<StuffStatus> statuses; // 물건 상태
    private String name; // 물건 이름
    private String description; // 물건 설명
    private LocalDateTime makeDatetime; // 물건 생산 날짜

    private StuffSearchType searchType; // 검색어 타입
    private String searchValue; // 검색어

    private StuffSearchDateType searchDateType; // 날짜 검색 타입
    private LocalDate startDate; //
    private LocalDate endDate;
}
