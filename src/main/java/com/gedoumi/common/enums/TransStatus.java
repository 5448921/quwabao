package com.gedoumi.common.enums;


public enum TransStatus {

    Failed(0,"失败"),
    Success(1,"成功");

    private TransStatus(int value, String name){
        this.value = value;
        this.name = name;
    }

    private int value;

    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
