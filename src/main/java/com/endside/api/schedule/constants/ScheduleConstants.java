package com.endside.api.schedule.constants;

public class ScheduleConstants {
    public static final int COMMON_MAINTENANCE_PERIOD_SEVEN = 7; // 일반 정비 기간 7일

    // 스케쥴 날짜 범위 관련 상수
    public static final boolean IS_NOT_OUT_OF_END_DATE = false;  // 마지막 날짜를 벗어나지 않았다
    public static final boolean IS_OUT_OF_END_DATE = true;       // 마지막 날짜를 벗어났다
    public static final int DATELESS = 0;                        // 날짜가 없다 (0일)
}
