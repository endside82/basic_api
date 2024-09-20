package com.endside.api.event.service;

import com.endside.api.config.error.ErrorCode;
import com.endside.api.config.error.exception.RestException;
import com.endside.api.config.redis.RedisQueueRepositoryImpl;
import com.endside.api.event.constant.TargetType;
import com.endside.api.event.constant.UserEventType;
import com.endside.api.event.model.UserEvent;
import com.endside.api.event.param.EventQueueParam;
import com.endside.api.event.param.UserEventParam;
import com.endside.api.event.vo.SimpleExtDataVo;
import com.endside.api.event.vo.SocietyExtDataVo;
import com.endside.api.event.vo.UserEventVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventService {

    private final RedisQueueRepositoryImpl redisQueueRepositoryImpl;

    @Async
    public void setUserEvent(String source, UserEventType type, long senderId, long targetId, String extData) {
        setUserEvent(source, type, senderId, targetId, TargetType.SINGLE, new ArrayList<>(), extData);
    }

    @Async
    public void setUserEvent(String source, UserEventType type, long senderId, long targetId, TargetType targetType, List<Long> receivers, String extData) {
        EventQueueParam eventQueueParam = createEventQueueParam(source, type, senderId, targetId, targetType, receivers, extData);
        enqueueEvent(eventQueueParam);
    }

    private void enqueueEvent(EventQueueParam eventQueueParam) {
        String userEvent = serializeToString(eventQueueParam);
        redisQueueRepositoryImpl.enqueueEvent(userEvent);
    }

    private EventQueueParam createEventQueueParam(String source, UserEventType type, long senderId, long targetId, TargetType targetType, List<Long> receivers, String extData) {
        return EventQueueParam.builder()
                .source(source)
                .type(type.toString())
                .senderId(senderId)
                .targetId(targetId)
                .targetType(targetType.getTargetType())
                .receivers(receivers)
                .extData(extData)
                .timestamp(Instant.now().toEpochMilli()).build();
    }

    public String getSimpleExtData(long senderId) {
        SimpleExtDataVo simpleExtDataVo = SimpleExtDataVo.builder()
                .senderId(senderId).build();
        return serializeToString(simpleExtDataVo);
    }

    private String serializeToString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String toString = "";
        try {
            toString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // TODO: define error code
            log.error("enqueueEvent(EventParam eventParam) - JsonProcessingException: " + e);
        }
        return toString;
    }
}