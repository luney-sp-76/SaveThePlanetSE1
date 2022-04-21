package com.savetheplanet.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Player implements Serializable {

    String name;
    int funding;
    List<FundableSquare> ownedSquares = new ArrayList<>();

    public Player() {
    }

    public Player(String name) {
        setName(name);
        funding = 500;
    }

    public List<FundableSquare> getOwnedSquares() {
        return ownedSquares;
    }

    public FundableSquare getLowestValueSquare(){

        List<FundableSquare> ownedSquares = this.getOwnedSquares();
        Collections.sort(ownedSquares, new Comparator<FundableSquare>(){
            public int compare(FundableSquare o1, FundableSquare o2){
                return o1.getCost() - o2.getCost();
            }
        });

        return ownedSquares.get(0);
    }

    public void addOwnedSquare(FundableSquare square) {
        this.ownedSquares.add(square);
    }

    public int getFunding() {
        return funding;
    }

    public void setFunding(int funding) {
        this.funding = funding;
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

        if (name.matches("^.*[^a-zA-Z\\d].*$"))
            throw new IllegalArgumentException("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.");
        if (name.length() < 2 || name.length() > 30)
            throw new IllegalArgumentException("Name format error. Length must be between 2 and 30 characters.");

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
