package com.savetheplanet.Main;

public class Player {

    String name;
    int funding;

    public Player() {
    }
    public Player(String name) {
        setName(name);
        funding = 300;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        name = name.trim();

        if (validateName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name format error");

        }
    }

    private boolean validateName(String name) {
        if (name.length() < 2 || name.length() > 30)
            throw new IllegalArgumentException("Name format error. Length must be between 2 and 30 characters.");

        if (name.matches("^.*[^a-zA-Z0-9].*$"))
            throw new IllegalArgumentException("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.");

        return true;
    }
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", funding=" + funding +
                '}';
    }
}
