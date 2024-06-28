package com.endside.api.schedule.util;

import com.endside.api.schedule.model.Schedule;
import com.endside.api.schedule.param.ScheduleAddParam;
import com.endside.api.schedule.param.ScheduleModParam;
import com.endside.api.schedule.vo.ScheduleVo;
import org.mapstruct.*;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateScheduleFromParam(ScheduleModParam scheduleModParam, @MappingTarget Schedule schedule);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ScheduleVo updateScheduleToVo(Schedule schedule);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Schedule mapToSchedule(ScheduleAddParam scheduleAddParam);
}
