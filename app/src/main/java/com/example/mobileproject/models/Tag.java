package com.example.mobileproject.models;

public class Tag {

    private final String value;
    private boolean selected;

    public Tag(String value, boolean selected){
        this.value = value;
        this.selected = selected;
    }
    public String getValue() {
        return value;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
