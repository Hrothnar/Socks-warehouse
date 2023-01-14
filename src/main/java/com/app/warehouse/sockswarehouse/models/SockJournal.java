package com.app.warehouse.sockswarehouse.models;

import com.app.warehouse.sockswarehouse.models.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SockJournal implements Cloneable {
    private SockPair sockPair;
    private OperationType operationType;
    private LocalDateTime date;

    public SockJournal(SockPair sockPair, OperationType operationType, LocalDateTime date) {
        this.sockPair = sockPair;
        this.operationType = operationType;
        this.date = date;
    }

    public SockJournal() {

    }

    public SockPair getSockPair() {
        return sockPair;
    }

    public void setSockPair(SockPair sockPair) {
        this.sockPair = sockPair;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = OperationType.valueOf(operationType.toUpperCase());
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
