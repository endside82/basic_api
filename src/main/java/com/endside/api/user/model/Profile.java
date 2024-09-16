package com.endside.api.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name = "profile")
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String nickname;

    private String uniqueNickname;

    private String image;

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 변경일

    @CreationTimestamp
    private LocalDateTime createdAt; // 만든날짜

}
