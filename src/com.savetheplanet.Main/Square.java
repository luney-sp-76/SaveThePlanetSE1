package com.savetheplanet.Main;

public class Square {
    private String name;
    private int field;

    public Square(String name, int field) {
        setName(name);
        setField(field);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getField() {
        return field;
    }

    public void setField(int group) {
        this.field = group;
    }

    @Override
    public String toString() {
        return "Square{" +
                "name='" + name + '\'' +
                ", field=" + field +
                '}';
    }
}