package com.endside.api.friend.controller;

import com.endside.api.config.security.UserPrincipal;
import com.endside.api.friend.param.*;
import com.endside.api.friend.service.FriendService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.endside.api.friend.vo.FriendVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class FriendController {

    private final FriendService friendService;

    // 친구 리스트
    @GetMapping("/friends")
    public ResponseEntity<List<FriendVo>> friendList(@RequestBody FriendListParam friendListParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendListParam.setUserId(userPrincipal.getUserId());
        return ResponseEntity.ok(friendService.getFriendList(friendListParam));
    }

    // 친구 정보 조회
    @GetMapping("/friends/{friendId}")
    public ResponseEntity<?> friendInfo(@PathVariable long friendId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(friendService.getFriendInfo(
                FriendInfoParam.builder()
                        .userId(userPrincipal.getUserId())
                        .friendId(friendId).build())
        );
    }

    // 친구요청 전송 리스트
    @GetMapping("/friends/requests/send")
    public ResponseEntity<?> friendRequestSendList(@RequestBody FriendListParam friendListParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendListParam.setUserId(userPrincipal.getUserId());
        return ResponseEntity.ok(friendService.friendRequestSendList(friendListParam));
    }


    // 친구요청 받은 리스트
    @GetMapping("/friends/requests/receive")
    public ResponseEntity<?> friendRequestList(@RequestBody FriendListParam friendListParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendListParam.setUserId(userPrincipal.getUserId());
        return ResponseEntity.ok(friendService.friendRequestList(friendListParam));
    }

    // 친구요청 등록
    @PostMapping("/friends/requests")
    public ResponseEntity<?> addFriendRequest(@RequestBody FriendRequestParam friendRequestParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendRequestParam.setSender(userPrincipal.getUserId());
        friendService.addFriendRequest(friendRequestParam);
        return ResponseEntity.ok().build();
    }

    // 친구 수락
    @PostMapping("/friends/requests/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestParam friendRequestParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendRequestParam.setReceiver(userPrincipal.getUserId());
        friendService.acceptFriendRequest(friendRequestParam);
        return ResponseEntity.ok().build();
    }

    // 친구요청 받음 삭제 (친구 거절)
    @DeleteMapping("/friends/requests/receive")
    public ResponseEntity<?> deleteFriendRequest(@RequestBody FriendRequestParam friendRequestParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendRequestParam.setReceiver(userPrincipal.getUserId());
        friendService.deleteFriendRequest(friendRequestParam);
        return ResponseEntity.ok().build();
    }

    // 친구요청 전송 삭제 (친구 요청 취소)
    @DeleteMapping("/friends/requests/send")
    public ResponseEntity<?> deleteFriendRequestSend(@RequestBody FriendRequestParam friendRequestParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendRequestParam.setSender(userPrincipal.getUserId());
        friendService.deleteFriendRequest(friendRequestParam);
        return ResponseEntity.ok().build();
    }

    // 친구삭제
    @DeleteMapping("/friends")
    public ResponseEntity<?> deleteFriend(@RequestBody FriendDeleteParam friendDeleteParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendDeleteParam.setUserId(userPrincipal.getUserId());
        friendService.deleteFriend(friendDeleteParam);
        return ResponseEntity.ok().build();
    }

    // 친구차단 리스트
    @GetMapping("/friends/blocks")
    public ResponseEntity<?> friendBlockList(@RequestBody FriendListParam friendListParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendListParam.setUserId(userPrincipal.getUserId());
        return ResponseEntity.ok(friendService.friendBlocKList(friendListParam));
    }

    // 친구차단 추가
    @PostMapping("/friends/blocks")
    public ResponseEntity<?> addFriendBlock(@RequestBody FriendBlockParam friendBlockParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendBlockParam.setUserId(userPrincipal.getUserId());
        friendService.addFriendBlock(friendBlockParam);
        return ResponseEntity.ok().build();
    }


    // 친구차단 삭제
    @DeleteMapping("/friends/blocks")
    public ResponseEntity<?> deleteFriendBlock(@RequestBody FriendBlockParam friendBlockParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        friendBlockParam.setUserId(userPrincipal.getUserId());
        friendService.deleteFriendBlock(friendBlockParam);
        return ResponseEntity.ok().build();
    }

    // 사람 찾기
    @PostMapping("/friends/search")
    public ResponseEntity<?> findFriend(@RequestBody FindFriendParam findFriendParam, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getUserId();
        findFriendParam.setUserId(userId);
        return ResponseEntity.ok(friendService.findFriend(findFriendParam));
    }
}