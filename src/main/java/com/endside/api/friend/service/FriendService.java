package com.endside.api.friend.service;


import com.endside.api.config.error.ErrorCode;
import com.endside.api.config.error.exception.RestException;
import com.endside.api.event.constant.UserEventType;
import com.endside.api.event.service.UserEventService;
import com.endside.api.friend.model.FriendBlock;
import com.endside.api.friend.model.Friend;
import com.endside.api.friend.model.FriendRequest;
import com.endside.api.friend.param.*;
import com.endside.api.friend.repository.*;
import com.endside.api.friend.vo.*;
import com.endside.api.user.constants.UserStatus;
import com.endside.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FriendService {
    private static final String SOURCE_FRIEND_ACCEPT = "/api/friend/acceptFriendRequest";
    private static final String SOURCE_FRIEND_REQUEST = "/api/friend/addFriendRequest";
    private static final String SOURCE_FRIEND_DELETE = "/api/friend/deleteFriend";
    private final UserRepository userRepository;
    private final FriendQueryRepository friendQueryRepository;
    private final FriendRequestQueryRepository friendRequestQueryRepository;
    private final FriendBlockQueryRepository friendBlockQueryRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendBlockRepository friendBlockRepository;
    private final UserEventService userEventService;

    // 친구 리스트
    public List<FriendVo> getFriendList(FriendListParam friendListParam) {
        return friendQueryRepository.selectFriendByUserId(friendListParam, friendListParam.of());
    }

    // 친구 정보 조회
    public FriendInfoVo getFriendInfo(FriendInfoParam param) {

        // 자기 자신을 조회
        if (param.getUserId() == param.getFriendId()) {
            throw new RestException(ErrorCode.FOUND_USER_IS_ME);
        }

        FriendInfoVo friendInfoVo = friendQueryRepository.selectFriendByFriendId(param);
        if (friendInfoVo == null) {
            throw new RestException(ErrorCode.NOT_FOUND_FRIEND);
        }

        return friendInfoVo;
    }

    // 친구 요청 전송 리스트
    public List<FriendReqSendVo> friendRequestSendList(FriendListParam friendListParam) {
        return friendRequestQueryRepository.selectFriendRequestByFriendId(friendListParam, friendListParam.of());
    }

    // 친구요청 받은 리스트

    /**
     * 친구요청 등록
     * 주의 : 친구 요청을 보내면 받는 입장에서는 주체가 바뀐다.
     * request : sender = userId , receiver = friendId
     * request DB : userId = receiver = friendId(req) , friendId = sender = userId(req)
     *
     * @param friendRequestParam 친구요청 정보
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFriendRequest(FriendRequestParam friendRequestParam) {
        long sender = friendRequestParam.getSender();
        long receiver = friendRequestParam.getReceiver();

        // 나자신에게는 보낼수 없음
        if (sender == receiver) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_REQUEST_NOT_ALLOWED_SELF);
        }

        // 이미 친구 요청이 있는지 확인
        if (friendRequestRepository.existsByReceiverAndSender(receiver, sender)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_REQUEST_ALREADY_SENT);
        }

        // 상대방이 존재하는지 확인
        if (!userRepository.existsByUserIdAndStatusLessThanEqual(receiver, UserStatus.LOGOUT)) {
            throw new RestException(ErrorCode.FAIL_REQUEST_TARGET_NOT_EXIST); // FAIL_REQUEST_TARGET_NOT_EXIST
        }
        // 이미 친구 관계 인지 확인
        if (isFriend(sender, receiver)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_REQUEST_ALREADY_FRIEND);
        }

        // 상대방이 나를 block 했는지 확인
        if (isBlocked(receiver, sender)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_REQUEST_BLOCKED_USER_BY_FRIEND);
        }

        // 내가 상대방을 block 했는지 확인
        if (isBlocked(sender, receiver)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_REQUEST_BLOCKED_FRIEND_BY_USER);
        }

        // 친구가 나에게 이미 친구가 나를 추가했는지 확인 만약 있다면 친구관계로 변경
        boolean isAdded = deleteRequestAndAddFriends(sender, receiver);
        if (isAdded) {
            return;
        }

        // 친구 요청 정보 저장
        friendRequestRepository.save(FriendRequest.builder()
                .receiver(receiver)
                .sender(sender).build());

        // 친구 요청 알림
        String extData = userEventService.getSimpleExtData(sender);
        userEventService.setUserEvent(SOURCE_FRIEND_REQUEST, UserEventType.FRIEND_REQUEST, sender, receiver, extData);
    }

    // 친구 요청 정보 리스트
    public List<FriendReqRecvVo> friendRequestList(FriendListParam friendListParam) {
        return friendRequestQueryRepository.selectFriendRequestByUserId(friendListParam, friendListParam.of());
    }

    // 친구 요청 수락
    public void acceptFriendRequest(FriendRequestParam friendRequestParam) {
        long receiver = friendRequestParam.getReceiver();
        long sender = friendRequestParam.getSender();
        boolean isAdded = deleteRequestAndAddFriends(receiver, sender);
        if (!isAdded) {
            throw new RestException(ErrorCode.FAIL_ACCEPT_FRIEND_REQUEST);
        }
        // 친구 수락 알림
        String extData = userEventService.getSimpleExtData(receiver);
        userEventService.setUserEvent(SOURCE_FRIEND_ACCEPT, UserEventType.FRIEND_ACCEPT, receiver, sender, extData);
    }

    // 친구요청 삭제
    public void deleteFriendRequest(FriendRequestParam friendRequestParam) {
        FriendRequest friendRequest = retrieveFriendRequest(friendRequestParam.getReceiver(), friendRequestParam.getSender());
        friendRequestRepository.delete(friendRequest);
    }

    // 친구 요청 가져오기
    private FriendRequest retrieveFriendRequest(long receiver, long sender) {
        return friendRequestRepository.findByReceiverAndSender(receiver, sender).orElseThrow(() ->
                new RestException(ErrorCode.NOT_FOUND_FRIEND_REQUEST)
        );
    }

    // 친구차단 리스트
    public List<FriendBlockVo> friendBlocKList(FriendListParam friendListParam) {
        return friendBlockQueryRepository.selectFriendBlockByUserId(friendListParam, friendListParam.of());
    }

    // 친구차단 추가
    public void addFriendBlock(FriendBlockParam friendBlockParam) {
        long userId = friendBlockParam.getUserId();
        long friendId = friendBlockParam.getFriendId();
        // 상대방이 존재하는지 확인
        if (!userRepository.existsById(friendId)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_BLOCK_NOT_EXIST);
        }

        // 이미 친구 차단이 있는지 확인
        if (friendBlockRepository.existsByUserIdAndFriendId(userId, friendId)) {
            throw new RestException(ErrorCode.FAIL_ADD_FRIEND_BLOCK_ALREADY_BLOCK);
        }

        // 친구 관계가 있으면 삭제
        deleteFriend(userId, friendId, false);
        deleteFriend(friendId, userId, false);
        // 친구 요청이 있으면 삭제
        deleteFriendRequest(userId, friendId);
        deleteFriendRequest(friendId, userId);
        // 친구 관계 외에 맺어진 관계가 있으면 삭제한다.(TODO)

        // 친구 차단 정보 추가
        friendBlockRepository.save(FriendBlock.builder()
                .userId(userId)
                .friendId(friendId)
                .build());
    }

    // 친구차단 삭제
    public void deleteFriendBlock(FriendBlockParam friendBlockParam) {
        FriendBlock friendBlock = retrieveFriendBlock(friendBlockParam.getUserId(), friendBlockParam.getFriendId());
        friendBlockRepository.delete(friendBlock);
    }

    // 친구차단 가져오기
    private FriendBlock retrieveFriendBlock(long userId, long friendId) {
        return friendBlockRepository.findByUserIdAndFriendId(userId, friendId).orElseThrow(() ->
                new RestException(ErrorCode.NOT_FOUND_FRIEND_BLOCK)
        );
    }


    // 친구 요청 정보 삭제 및 친구 관계 설정
    private boolean deleteRequestAndAddFriends(long receiver, long sender) {
        return friendRequestRepository.findByReceiverAndSender(receiver, sender)
                .map(friendRequest -> {
                    friendRequestRepository.delete(friendRequest);
                    addFriend(sender, receiver); // Add friend relationship both ways
                    addFriend(receiver, sender);
                    return true;
                })
                .orElse(false);
    }

    // 친구 관계인지 확인
    public boolean isFriend(long userId, long friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    // BLOCK 당했는지 확인
    public boolean isBlocked(long userId, long friendId) {
        return friendBlockRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    // 친구 등록
    private void addFriend(long userId, long friendId) {
        friendRepository.save(Friend.builder()
                .userId(userId)
                .friendId(friendId).build());
    }

    // 친구 관계 삭제
    private void deleteFriend(long userId, long friendId, boolean isStrict) {
        Friend friend;
        try {
            friend = retrieveFriend(userId,friendId);
            friendRepository.delete(friend);
        } catch (RestException e){
            if (isStrict) {
                throw new RestException(ErrorCode.FAIL_DELETE_FRIEND);
            }
        }
    }

    // 친구차단 가져오기
    private Friend retrieveFriend(long userId, long friendId) {
        return friendRepository.findByUserIdAndFriendId(userId, friendId).orElseThrow(() ->
                new RestException(ErrorCode.NOT_FOUND_FRIEND)
        );
    }

    // 친구 요청 삭제
    private void deleteFriendRequest(long receiver, long sender) {
        friendRequestRepository.deleteByReceiverAndSender(receiver, sender);
    }

    // 친구 추가를 위한 유저 찾기
    public List<FindFriendVo> findFriend(FindFriendParam findFriendParam) {
        // to lowercase
        if (findFriendParam.getId() == null || findFriendParam.getId().isEmpty()) {
            findFriendParam.setNickname(findFriendParam.getNickname().toLowerCase());
            return friendQueryRepository.selectFriendByNickname(findFriendParam, findFriendParam.of());
        } else {
            findFriendParam.setId(findFriendParam.getId().toLowerCase());
            FindFriendVo findFriendVo = friendQueryRepository.findFriendById(findFriendParam);
            if (findFriendVo == null || findFriendVo.getUserId() == null) {
                throw new RestException(ErrorCode.NOT_FOUND_USER);
            }
            // 나자신 에게는 보낼수 없음
            if (findFriendVo.getUserId() == findFriendParam.getUserId()) {
                throw new RestException(ErrorCode.FOUND_USER_IS_ME);
            }
            return List.of(findFriendVo);
        }
    }

    // 친구 관계 삭제
    public void deleteFriend(FriendDeleteParam friendDeleteParam) {
        long userId = friendDeleteParam.getUserId();
        long friendId = friendDeleteParam.getFriendId();
        // 친구 관계가 있으면 삭제
        deleteFriend(userId, friendId, true);
        deleteFriend(friendId, userId, true);

        // 친구 삭제 알림
        String extData = userEventService.getSimpleExtData(userId);
        userEventService.setUserEvent(SOURCE_FRIEND_DELETE, UserEventType.FRIEND_DELETE, userId, friendId, extData);
    }

}

