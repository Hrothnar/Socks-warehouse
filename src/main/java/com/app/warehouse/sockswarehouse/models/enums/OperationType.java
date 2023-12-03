package com.app.warehouse.sockswarehouse.models.enums;

public enum OperationType {
    ADDITION("addition"), REALIZATION("realization"), REMOVE("remove");
    private final String type;

    OperationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
