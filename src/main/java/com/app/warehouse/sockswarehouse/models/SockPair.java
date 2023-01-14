package com.app.warehouse.sockswarehouse.models;

public class SockPair implements Cloneable {
    private Sock sock;
    private int quantity;

    public Sock getSock() {
        return sock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
