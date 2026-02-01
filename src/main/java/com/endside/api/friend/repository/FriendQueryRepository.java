package com.endside.api.friend.repository;

import com.endside.api.friend.param.FindFriendParam;
import com.endside.api.friend.param.FriendInfoParam;
import com.endside.api.friend.param.FriendListParam;
import com.endside.api.friend.vo.FindFriendVo;
import com.endside.api.friend.vo.FriendInfoVo;
import com.endside.api.friend.vo.FriendVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.endside.api.friend.model.QFriend.friend;
import static com.endside.api.friend.model.QFriendRequest.friendRequest;
import static com.endside.api.user.model.QProfile.profile;
import static com.endside.api.user.model.QUsers.users;

@Repository
@AllArgsConstructor
public class FriendQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<FriendVo> selectFriendByUserId(FriendListParam friendListParam , Pageable pageable){


        return jpaQueryFactory.select(Projections.bean(FriendVo.class,
                        users.userId,
                        users.status,
                        profile.nickname,
                        profile.image,
                        friend.friendId,
                        friend.createdAt,
                        friend.updatedAt
                ))
                .from(friend)
                .join(users).on(users.userId.eq(friend.friendId))
                .leftJoin(profile).on(profile.userId.eq(users.userId))
                .where(friend.userId.eq(friendListParam.getUserId()))
                .orderBy(friend.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    public List<FindFriendVo> selectFriendByNickname(FindFriendParam findFriendParam, Pageable pageable){

        return jpaQueryFactory.select(Projections.bean(FindFriendVo.class,
                        users.userId,
                        profile.nickname,
                        profile.image,
                        friend.friendId.isNotNull().as("isFriend"),
                        friendRequest.receiver.isNotNull().as("isRequest")
                ))
                .from(users)
                .leftJoin(profile).on(users.userId.eq(profile.userId))
                .leftJoin(friend).on(friend.userId.eq(findFriendParam.getUserId()).and(friend.friendId.eq(users.userId)))
                .leftJoin(friendRequest).on(friendRequest.receiver.eq(users.userId).and(friendRequest.sender.eq(findFriendParam.getUserId())))
                .where((profile.nickname.like(findFriendParam.getNickname()))
                        .and(users.userId.ne(findFriendParam.getUserId())))
                .groupBy(users.userId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    public FriendInfoVo selectFriendByFriendId(FriendInfoParam friendInfoParam){
        return jpaQueryFactory.select(Projections.bean(FriendInfoVo.class,
                        users.userId,
                        users.status,
                        profile.nickname,
                        profile.image,
                        friend.friendId,
                        friend.createdAt,
                        friend.updatedAt
                ))
                .from(friend)
                .join(users).on(users.userId.eq(friend.friendId))
                .leftJoin(profile).on(profile.userId.eq(users.userId))
                .where(friend.userId.eq(friendInfoParam.getUserId())
                        .and(friend.friendId.eq(friendInfoParam.getFriendId())))
                .fetchOne();
    }
    public FindFriendVo findFriendById(FindFriendParam findFriendParam){
        return jpaQueryFactory.select(Projections.bean(FindFriendVo.class,
                        users.userId,
                        profile.nickname,
                        profile.image,
                        friend.friendId.isNotNull().as("isFriend"),
                        friendRequest.receiver.isNotNull().as("isRequest")
                ))
                .from(users)
                .leftJoin(profile).on(users.userId.eq(profile.userId))
                .leftJoin(friend).on(friend.userId.eq(findFriendParam.getUserId()).and(friend.friendId.eq(users.userId)))
                .leftJoin(friendRequest).on(friendRequest.receiver.eq(users.userId).and(friendRequest.sender.eq(findFriendParam.getUserId())))
                .where(users.userId.eq(findFriendParam.getUserId()))
                .fetchOne();
    }
}
