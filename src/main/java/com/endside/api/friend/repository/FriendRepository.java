package com.endside.api.friend.repository;

import com.endside.api.friend.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);
    boolean existsByUserIdAndFriendId(long userId, long friendId);
}
