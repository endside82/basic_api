package com.endside.api.schedule.controller;

import com.endside.api.schedule.param.*;
import com.endside.api.schedule.service.ScheduleService;
import com.endside.api.schedule.vo.ScheduleVo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/schedule")
@AllArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;

    // 스케쥴 조회 - 단일건 조회
    @GetMapping("/{scheduleId}")
    @ResponseBody
    public ResponseEntity<?> getSchedule(@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleParam scheduleParam) {

        scheduleParam.setId(scheduleId);
        ScheduleVo scheduleVo = scheduleService.getSchedule(scheduleParam);
        return ResponseEntity.ok(scheduleVo);
    }

    // 스케쥴 추가
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleAddParam scheduleAddParam) {
        ScheduleVo scheduleVo = scheduleService.addSchedule(scheduleAddParam);
        return ResponseEntity.ok(scheduleVo);
    }

    // 스케쥴 변경
    @PutMapping("")
    @ResponseBody
    public ResponseEntity<?> modSchedule(@RequestBody ScheduleModParam scheduleModParam) {
        ScheduleVo scheduleVo = scheduleService.modSchedule(scheduleModParam);
        return ResponseEntity.ok(scheduleVo);
    }

    // 스케쥴 삭제
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<?> delSchedule(@RequestBody ScheduleDelParam scheduleDelParam) {
        scheduleService.deleteSchedule(scheduleDelParam);
        return ResponseEntity.noContent().build();
    }

    // 스케쥴 리스트
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> scheduleList(@RequestBody ScheduleListParam scheduleListParam) {
        return ResponseEntity.ok(scheduleService.getScheduleList(scheduleListParam));
    }

}
