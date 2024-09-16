package com.endside.api.friend.repository;


import com.endside.api.friend.param.FriendListParam;
import com.endside.api.friend.vo.FriendReqRecvVo;
import com.endside.api.friend.vo.FriendReqSendVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.endside.api.friend.model.QFriendBlock.friendBlock;
import static com.endside.api.friend.model.QFriendRequest.friendRequest;
import static com.endside.api.user.model.QProfile.profile;
import static com.endside.api.user.model.QUsers.users;

@Repository
@RequiredArgsConstructor
public class FriendRequestQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    public List<FriendReqSendVo> selectFriendRequestByFriendId(FriendListParam friendListParam, Pageable pageable) {
        return jpaQueryFactory.select(Projections.bean(FriendReqSendVo.class,
                        friendRequest.id,
                        friendRequest.receiver,
                        friendRequest.sender,
                        friendRequest.createdAt,
                        users.status,
                        users.updatedAt.as("accountUpdateDate"),
                        profile.nickname,
                        profile.image,
                        profile.updatedAt.as("profileUpdateDate")
                ))
                .from(friendRequest)
                .join(users).on(friendRequest.receiver.eq(users.userId))
                .leftJoin(profile).on(users.userId.eq(profile.userId))
                .where(friendRequest.sender.eq(friendListParam.getUserId()))
                .orderBy(friendRequest.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<FriendReqRecvVo> selectFriendRequestByUserId(FriendListParam friendListParam, Pageable pageable) {
        return jpaQueryFactory.select(Projections.bean(FriendReqRecvVo.class,
                        friendRequest.id,
                        friendRequest.receiver,
                        friendRequest.sender,
                        friendRequest.createdAt,
                        users.status,
                        users.updatedAt.as("accountUpdateDate"),
                        profile.nickname,
                        profile.image,
                        profile.updatedAt.as("profileUpdateDate")
                ))
                .from(friendRequest)
                .join(users).on(friendRequest.sender.eq(users.userId))
                .leftJoin(profile).on(users.userId.eq(profile.userId))
                .where(friendRequest.receiver.eq(friendListParam.getUserId()))
                .orderBy(friendRequest.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
