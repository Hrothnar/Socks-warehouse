package com.app.warehouse.sockswarehouse.models.enums;

public enum Color {
    BLACK("black"), WHITE("white"), YELLOW("yellow"), GREEN("green"), RED("red"), PURPLE("purple"), GRAY("gray");

    String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
