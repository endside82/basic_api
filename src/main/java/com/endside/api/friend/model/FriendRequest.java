package com.endside.api.friend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity(name = "friend_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiver;

    private Long sender;

    @CreationTimestamp
    private LocalDateTime createdAt; // 만든날짜

    @Builder
    public FriendRequest(long receiver, long sender){
        this.receiver = receiver;
        this.sender = sender;
    }
}
