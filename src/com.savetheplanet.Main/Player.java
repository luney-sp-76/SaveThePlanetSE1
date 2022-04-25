package com.savetheplanet.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player implements Serializable {

    String name;
    int funding;
    int totalValue;
    List<FundableSquare> ownedSquares = new ArrayList<>();

    public Player(String name) {
        setName(name);
        funding = 500;
        totalValue = 0;
    }

    public void calcTotalValue() {
        this.totalValue = getOwnedSquares().stream().mapToInt(FundableSquare::getValue).sum();

    }

    public int getTotalValue() {
        return totalValue;

    }

    public List<FundableSquare> getOwnedSquares() {
        return ownedSquares;
    }

    public FundableSquare getLowestValueSquare() {

        List<FundableSquare> ownedSquares = this.getOwnedSquares();
        ownedSquares.sort(Comparator.comparingInt(FundableSquare::getCost));
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

    public void setName(String name) throws IllegalArgumentException {

        name = name.trim();
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name format error");
        }
    }

    private boolean validateName(String name) throws IllegalArgumentException {

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
                ", totalValue=" + totalValue +
                '}';
    }
}
