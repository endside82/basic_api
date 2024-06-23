package com.endside.api.stuff.controller;

import com.endside.api.stuff.param.StuffAddParam;
import com.endside.api.stuff.param.StuffListParam;
import com.endside.api.stuff.param.StuffModParam;
import com.endside.api.stuff.service.StuffService;
import com.endside.api.stuff.vo.StuffVo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stuff")
public class StuffController {

    private final StuffService stuffService;
    @GetMapping("/{stuffId}")
    public ResponseEntity<?> getStuff(@PathVariable("stuffId") Long stuffId) {
        return ResponseEntity.ok(stuffService.getStuff(stuffId));
    }

    // 추가
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<?> addStuff(@RequestBody StuffAddParam stuffAddParam) {
        StuffVo stuffVo = stuffService.addStuff(stuffAddParam);
        return ResponseEntity.ok(stuffVo);
    }

    // 변경
    @PutMapping("/{stuffId}")
    @ResponseBody
    public ResponseEntity<?> modStuff(@PathVariable("stuffId") Long stuffId,@RequestBody StuffModParam stuffModParam) {
        stuffModParam.setId(stuffId);
        return ResponseEntity.ok(stuffService.modStuff(stuffModParam));
    }

    // 리스트
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<?>> getStuffList( @RequestBody StuffListParam stuffListParam) {
        List<StuffVo> stuffVoList = stuffService.getStuffList(stuffListParam);
        return ResponseEntity.ok(stuffVoList);
    }
}
