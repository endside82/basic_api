package com.endside.api.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Now {
    private long time;
    public Now(long time) {
        this.time = time;
    }
}
