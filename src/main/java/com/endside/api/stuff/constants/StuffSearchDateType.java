package com.endside.api.stuff.constants;

public enum StuffSearchDateType {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    MAKE_DATETIME("makeDatetime");
    private String typeName;

    StuffSearchDateType(String typeName) {
        this.typeName = typeName;
    }
}
