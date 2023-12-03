package com.app.warehouse.sockswarehouse.models;

import com.app.warehouse.sockswarehouse.models.enums.Color;
import com.app.warehouse.sockswarehouse.models.enums.Size;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(using = SockDeserializer.class)
public class Sock implements Cloneable {
    private Color color;
    private Size size;
    private int composition;

    public Sock(Color color, Size size, int composition) {
        this.color = color;
        this.size = size;
        this.composition = composition;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getComposition() {
        return composition;
    }

    public void setComposition(int composition) {
        this.composition = composition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return composition == sock.composition && color == sock.color && size == sock.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, composition);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
