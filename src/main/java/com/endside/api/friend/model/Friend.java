package com.endside.api.friend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long friendId;

    private Integer type;

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 변경일

    @CreationTimestamp
    private LocalDateTime createdAt; // 만든날짜

    @Builder
    public Friend(long userId, long friendId){
        this.userId = userId;
        this.friendId = friendId;
    }
}
