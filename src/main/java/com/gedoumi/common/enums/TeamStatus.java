package com.gedoumi.common.enums;


public enum TeamStatus {

    All(-1,"全部"),
    Disable(0,"禁用"),
    Enable(1,"可用");

    private TeamStatus(int value, String name){
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
