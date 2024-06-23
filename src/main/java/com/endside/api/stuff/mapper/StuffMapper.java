package com.endside.api.stuff.mapper;

import com.endside.api.stuff.modle.Stuff;
import com.endside.api.stuff.param.StuffModParam;
import com.endside.api.stuff.vo.StuffVo;
import org.mapstruct.*;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StuffMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStuffFromParam(StuffModParam param, @MappingTarget Stuff stuff);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StuffVo updateStuffToVo(Stuff stuff);
}
