package com.app.warehouse.sockswarehouse.models.enums;

public enum Size {
    SIZE_21(21), SIZE_23(23), SIZE_25(25), SIZE_27(27), SIZE_29(29), SIZE_31(31);

    private int size;

    Size(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static Size sizeAsEnum(String num) {
        int anInt = Integer.parseInt(num);
        for (Size one : Size.values()) {
            if (one.getSize() == anInt) {
                return one;
            }
        }
        throw new RuntimeException();
    }

}
