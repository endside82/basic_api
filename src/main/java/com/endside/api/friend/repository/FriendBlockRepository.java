package com.endside.api.friend.repository;


import com.endside.api.friend.model.FriendBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendBlockRepository extends JpaRepository<FriendBlock, Long> {
    Optional<FriendBlock> findByUserIdAndFriendId(Long userId, Long friendId);
    boolean existsByUserIdAndFriendId(long userId, long friendId);
}
