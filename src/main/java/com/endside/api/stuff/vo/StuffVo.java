package com.endside.api.stuff.vo;

import com.endside.api.stuff.constants.StuffStatus;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class StuffVo {
    private Long id; // 물건 고유번호
    private StuffStatus status;
    private String name;
    private String description;
    private LocalDateTime makeDatetime;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
