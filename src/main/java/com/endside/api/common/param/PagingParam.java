package com.endside.api.common.param;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public class PagingParam {
    private int page = 1;
    private int limit = 10;

    public PageRequest of() {
        return PageRequest.of(page - 1, limit);
    }
}

