package com.endside.api.friend.controller;

import com.endside.api.config.security.UserPrincipal;
import com.endside.api.friend.param.NoneFriendInfoParam;
import com.endside.api.friend.service.NoneFriendService;
import com.endside.api.friend.vo.NoneFriendInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class NoneFriendController {

    private final NoneFriendService noneFriendService;

    @RequestMapping(value = "/friends/none/{noneFriendId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<NoneFriendInfoVo> getNoneFriend(@PathVariable Long noneFriendId, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        return ResponseEntity.ok(noneFriendService.getNoneFriendInfo(NoneFriendInfoParam.builder()
                .userId(userPrincipal.getUserId())
                .noneFriendId(noneFriendId)
                .build()));
    }

}
