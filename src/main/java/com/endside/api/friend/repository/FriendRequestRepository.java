package com.endside.api.friend.repository;

import com.endside.api.friend.model.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findByReceiverAndSender(long receiver, long sender);
    boolean existsByReceiverAndSender(long receiver, long sender);
    void deleteByReceiverAndSender(long receiver, long sender);
}
