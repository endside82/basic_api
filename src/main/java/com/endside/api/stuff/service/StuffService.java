package com.endside.api.stuff.service;

import com.endside.api.config.error.ErrorCode;
import com.endside.api.config.error.exception.RestException;
import com.endside.api.stuff.mapper.StuffMapper;
import com.endside.api.stuff.modle.Stuff;
import com.endside.api.stuff.param.StuffAddParam;
import com.endside.api.stuff.param.StuffListParam;
import com.endside.api.stuff.param.StuffModParam;
import com.endside.api.stuff.repository.StuffQueryRepository;
import com.endside.api.stuff.repository.StuffRepository;
import com.endside.api.stuff.vo.StuffVo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class StuffService {
    private final StuffQueryRepository stuffQueryRepository;
    private final StuffRepository stuffRepository;
    private final StuffMapper stuffMapper;

    @Transactional(readOnly = true)
    public StuffVo getStuff(Long stuffId) {
        return stuffMapper.updateStuffToVo(retrieveStuff(stuffId));
    }

    @Transactional(rollbackFor = Exception.class)
    public StuffVo addStuff(StuffAddParam stuffAddParam) {
         Stuff stuff = stuffRepository.save(Stuff.builder()
                .name(stuffAddParam.getName())
                .status(stuffAddParam.getStatus())
                .description(stuffAddParam.getDescription())
                .makeDatetime(stuffAddParam.getMakeDatetime()).build());
         return stuffMapper.updateStuffToVo(stuff);
    }

    @Transactional(rollbackFor = Exception.class)
    public StuffVo modStuff(StuffModParam stuffModParam) {
        Stuff stuff = retrieveStuff(stuffModParam.getId());
        stuffMapper.updateStuffFromParam(stuffModParam , stuff);
        return stuffMapper.updateStuffToVo(stuffRepository.save(stuff));
    }

    public List<StuffVo> getStuffList(StuffListParam stuffListParam) {
        return stuffQueryRepository.getStuffListByCondition(stuffListParam, stuffListParam.of());
    }

    // Stuff 가져오기
    protected Stuff retrieveStuff(long stuffId) {
        return stuffRepository.findById(stuffId).orElseThrow(() ->
                new RestException(ErrorCode.STUFF_NOT_EXIST)
        );
    }
}
