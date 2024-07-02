package com.endside.api.stuff.param;

import com.endside.api.stuff.constants.StuffStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StuffAddParam {
    private StuffStatus status; // 물건 상태
    private String name; // 물건 이름
    private String description; // 물건 설명
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime makeDatetime; // 물건 생산 날짜
}
