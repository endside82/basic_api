package com.endside.api.config.redis;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("loginAttempt")
public class LoginAttempt {

    public LoginAttempt(String id){
        this.id = id;
        this.cnt = 1;
        this.isLock = false;
    }

    @Id
    private String id;
    private int cnt;
    private boolean isLock;

    @TimeToLive
    private long timeToLive;
}
